package org.xerocraft.memberapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.chalcodes.event.EventBus;
import com.chalcodes.event.EventReceiver;
import org.json.JSONException;
import org.xerocraft.memberapp.http.Callback;
import org.xerocraft.memberapp.http.HttpClient;

import javax.inject.Inject;

/**
 * Provides the member info button and related behavior.
 *
 * @author Kevin Krumwiede
 */
public class MemberInfoFragment extends ScannerFragment {
	private Member mUser;
	private Button mButton;

	@Inject EventBus<Member> mUserBus;
	@Inject Settings mSettings;
	@Inject	HttpClient mHttpClient;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getApp().getInjector().inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_button, container, false);
		mButton = (Button) view.findViewById(R.id.button);
		mButton.setText(R.string.member_info);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scanQrCode(REQ_SCAN_MEMBER);
			}
		});
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_SCAN_MEMBER && resultCode == Activity.RESULT_OK) {
			final String memberId = data.getStringExtra(EXTRA_SCAN_RESULT);
			if(Member.isValidId(memberId)) {
				getMemberInfo(memberId);
			}
			else {
				ErrorDialog.show(getActivity(), getString(R.string.message_not_member_qr), true);
			}
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void getMemberInfo(final String memberId) {
		final String url = mUser.isStaff() ?
				Urls.privateMemberInfo(memberId, mSettings.getUserId()) :
				Urls.publicMemberInfo(memberId);
		WaitDialog.show(getActivity(), getString(R.string.message_getting_member_info));

		mHttpClient.get(url, getActivity(), new Callback() {
			@Override
			public void onComplete() {
				WaitDialog.dismiss(getActivity());
			}

			@Override
			public void onSuccess(String body) {
				try {
					final Member member = Member.fromJson(body);
					final Intent intent = new Intent(getActivity(), MemberInfoActivity.class);
					intent.putExtra(MemberInfoActivity.EXTRA_MEMBER, member);
					startActivity(intent);
				}
				catch (JSONException e) {
					ErrorDialog.show(getActivity(), e.getLocalizedMessage(), true);
				}
			}

			@Override
			public void onError(Throwable e) {
				ErrorDialog.show(getActivity(), e.getLocalizedMessage(), true);
			}

			@Override
			public void onError(int responseCode, String body) {
				ErrorDialog.show(getActivity(), body, true);
			}
		});
	}

	/* Member info controls visibility. */
	private final EventReceiver<Member> mUserReceiver = new EventReceiver<Member>() {
		@Override
		public void onEvent(EventBus<Member> bus, Member user) {
			mButton.setVisibility(user.isActive() ? View.VISIBLE : View.GONE);
			mUser = user;
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		mUserBus.register(mUserReceiver);
	}

	@Override
	public void onPause() {
		super.onPause();
		mUserBus.unregister(mUserReceiver);
	}

}
