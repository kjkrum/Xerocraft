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
import org.json.JSONObject;
import org.xerocraft.memberapp.http.Callback;
import org.xerocraft.memberapp.http.HttpClient;

import javax.inject.Inject;

public class PermitInfoFragment extends ScannerFragment {
	private Button mButton;

	@Inject EventBus<Member> mUserBus;
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
		mButton.setText(R.string.permit_info);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scanQrCode(REQ_SCAN_PERMIT);
			}
		});
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_SCAN_PERMIT && resultCode == Activity.RESULT_OK) {
			final String scan = data.getStringExtra(EXTRA_SCAN_RESULT);
			try {
				final JSONObject json = new JSONObject(scan);
				if(JsonUtil.isPermit(json)) {
					getPermitInfo(JsonUtil.getPermitId(json));
				}
				else {
					ErrorDialog.show(getActivity(), getString(R.string.message_not_permit_qr), false);
				}
			}
			catch(JSONException e) {
				ErrorDialog.show(getActivity(), getString(R.string.message_not_permit_qr), false);
			}
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void getPermitInfo(int permitId) {
		final String url = Urls.permitInfo(permitId);
		WaitDialog.show(getActivity(), getString(R.string.message_getting_permit_info));
		mHttpClient.get(url, getActivity(), new Callback() {
			@Override
			public void onComplete() {
				WaitDialog.dismiss(getActivity());
			}

			@Override
			public void onSuccess(String body) {
				try {
					final JSONObject json = new JSONObject(body);
					if(JsonUtil.isPermit(json)) {
						final Intent intent = new Intent(getActivity(), PermitInfoActivity.class);
						intent.putExtra(PermitInfoActivity.EXTRA_PERMIT, body);
						startActivity(intent);
					}
					else if(JsonUtil.isError(json)) {
						ErrorDialog.show(getActivity(), JsonUtil.getErrorMessage(json), false);
					}
				}
				catch(JSONException e) {
					ErrorDialog.show(getActivity(), e.getLocalizedMessage(), false);
				}
			}

			@Override
			public void onError(Throwable error) {
				ErrorDialog.show(getActivity(), error.getLocalizedMessage(), false);
			}

			@Override
			public void onError(int responseCode, String body) {
				ErrorDialog.show(getActivity(), body, false);
			}
		});
	}

	/* Member info controls visibility. */
	private final EventReceiver<Member> mUserReceiver = new EventReceiver<Member>() {
		@Override
		public void onEvent(EventBus<Member> bus, Member user) {
		mButton.setVisibility(user.isActive() && user.isStaff() ? View.VISIBLE : View.GONE);
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
