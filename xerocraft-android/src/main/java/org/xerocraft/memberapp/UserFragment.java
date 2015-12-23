package org.xerocraft.memberapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.chalcodes.event.EventBus;
import org.json.JSONException;
import org.xerocraft.memberapp.http.Callback;
import org.xerocraft.memberapp.http.HttpClient;

import javax.inject.Inject;

/**
 * Obtains the user's member info and broadcasts it to other fragments.  This
 * fragment has no UI except for its menu item.
 *
 * @author Kevin Krumwiede
 */
public class UserFragment extends ScannerFragment {
//	private static final String TAG = UserFragment.class.getSimpleName();
	private static final int SHORT_DELAY = 1000;
	private final Handler mHandler = new Handler(Looper.getMainLooper());

	@Inject EventBus<Member> mUserBus;
	@Inject	HttpClient mHttpClient;
	@Inject Settings mSettings;

	private Member mUser;
	private static final String KEY_USER = "user";

	private final Runnable mShowDialogTask = new Runnable() {
		@Override
		public void run() {
			WaitDialog.show(getActivity(), getString(R.string.message_updating_user));
		}
	};

	private final Runnable mUpdateUserTask = new Runnable() {
		@Override
		public void run() {
			updateUser();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null) {
			mUser = (Member) savedInstanceState.getSerializable(KEY_USER);
		}
		setHasOptionsMenu(true);
		getApp().getInjector().inject(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(KEY_USER, mUser);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!mSettings.isUserConfigured()) {
			NoUserDialog.show(getActivity());
		}
		else if(mUser == null) {
			mHandler.postDelayed(mUpdateUserTask, SHORT_DELAY);
		}
		else {
			mUserBus.broadcast(mUser);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mHandler.removeCallbacks(mShowDialogTask);
		mHandler.removeCallbacks(mUpdateUserTask);
		mHttpClient.cancel(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if(item.getItemId() == R.id.item_set_user_id) {
			scanQrCode(REQ_SCAN_USER);
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_SCAN_USER && resultCode == Activity.RESULT_OK) {
			final String memberId = data.getStringExtra(EXTRA_SCAN_RESULT);
			if(Member.isValidId(memberId)) {
				mSettings.setUserId(memberId);
				updateUser();
			}
			else {
				ErrorDialog.show(getActivity(), getString(R.string.message_not_member_qr), true);
			}
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Downloads the user's member info.
	 */
	private void updateUser() {
		// don't show the wait dialog unless update is taking a long time
		mHandler.postDelayed(mShowDialogTask, SHORT_DELAY);
//		WaitDialog.show(getActivity(), getString(R.string.message_updating_user));
		final String url = Urls.publicMemberInfo(mSettings.getUserId());
		mHttpClient.get(url, getActivity(), new Callback() {
			@Override
			public void onComplete() {
				mHandler.removeCallbacks(mShowDialogTask);
				WaitDialog.dismiss(getActivity());
			}

			@Override
			public void onSuccess(String body) {
				try {
					mUser = Member.fromJson(body);
					mUserBus.broadcast(mUser);
				}
				catch (JSONException e) {
					ErrorDialog.show(getActivity(), e.getLocalizedMessage(), true);
				}
			}

			@Override
			public void onError(Throwable error) {
				ErrorDialog.show(getActivity(), error.getLocalizedMessage(), true);
			}

			@Override
			public void onError(int responseCode, String body) {
				ErrorDialog.show(getActivity(), body, true);
			}
		});
	}
}
