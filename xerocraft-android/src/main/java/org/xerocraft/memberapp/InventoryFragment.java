package org.xerocraft.memberapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class InventoryFragment extends ScannerFragment {
//	private static final String TAG = InventoryFragment.class.getSimpleName();
	private Button mButton;
	private int mLocation;
	private static final String KEY_LOCATION = "location";

	@Inject HttpClient mHttpClient;
	@Inject EventBus<Member> mUserBus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null) {
			mLocation = savedInstanceState.getInt(KEY_LOCATION, 0);
		}
		getApp().getInjector().inject(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_LOCATION, mLocation);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_button, container, false);
		mButton = (Button) view.findViewById(R.id.button);
		mButton.setText(R.string.inventory);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent ttsService = new Intent(getActivity(), TtsService.class);
				getActivity().startService(ttsService);
				scanQrCode(REQ_SCAN_INVENTORY);
			}
		});
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_SCAN_INVENTORY) {
			if(resultCode == Activity.RESULT_OK) {
				final String scan = data.getStringExtra(EXTRA_SCAN_RESULT);
				try {
					final JSONObject obj = new JSONObject(scan);
					if(JsonUtil.isLocation(obj)) {
						final int loc = JsonUtil.getLocationId(obj);
						mLocation = loc;
						TtsService.speak(getString(R.string.utterance_location).replace("{LOCATION}",
								Integer.toString(loc)));
						scanQrCode(REQ_SCAN_INVENTORY);
					}
					else if(JsonUtil.isPermit(obj)) {
						if(mLocation > 0) {
							final int permitId = JsonUtil.getPermitId(obj);
							final String url = Urls.updateInventory(permitId, mLocation);
							WaitDialog.show(getActivity(), getString(R.string.message_updating_inventory));
							mHttpClient.get(url, getActivity(), new Callback() {
								@Override
								public void onComplete() {
									WaitDialog.dismiss(getActivity());
								}

								@Override
								public void onSuccess(String body) {
									// TODO indicate success with a beep or something?
									scanQrCode(REQ_SCAN_INVENTORY);
								}

								@Override
								public void onError(Throwable error) {
									// TODO audible error?
									scanQrCode(REQ_SCAN_INVENTORY);
								}

								@Override
								public void onError(int responseCode, String body) {
									// TODO audible error?
									scanQrCode(REQ_SCAN_INVENTORY);
								}
							});
						}
						else {
							speak(R.string.utterance_scan_location);
							scanQrCode(REQ_SCAN_INVENTORY);
						}
					}
					else {
						speak(R.string.utterance_not_inventory_qr);
						scanQrCode(REQ_SCAN_INVENTORY);
					}
				} catch (JSONException e) {
					speak(R.string.utterance_not_inventory_qr);
					scanQrCode(REQ_SCAN_INVENTORY);
				}
			}
			else {
				/* User canceled inventory mode by canceling a scan. */
				mLocation = 0;
				final Intent ttsService = new Intent(getActivity(), TtsService.class);
				getActivity().stopService(ttsService);
			}
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void speak(int resId) {
		TtsService.speak(getString(resId));
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
