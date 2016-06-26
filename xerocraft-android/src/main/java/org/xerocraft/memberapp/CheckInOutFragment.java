package org.xerocraft.memberapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

/**
 * The check in/out button.
 */
public class CheckInOutFragment extends BaseFragment {
	private Button mButton;

	@Inject Settings mSettings;
	@Inject EventBus<Member> mUserBus;
	@Inject HttpClient mHttpClient;

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
		setButtonText(mSettings.isCheckedIn());
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkInOrOut();
			}
		});
		return view;
	}

	private void setButtonText(final boolean in) {
		mButton.setText(in ? R.string.check_out : R.string.check_in);
	}

	/* User info controls visibility. */
	private final EventReceiver<Member> mUserReceiver = new EventReceiver<Member>() {
		@Override
		public void onEvent(@NonNull EventBus<Member> bus, @NonNull Member user) {
			mButton.setVisibility(user.isActive() ? View.VISIBLE : View.GONE);
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

	private void checkInOrOut() {
		final boolean in = !mSettings.isCheckedIn();
		final String url = Urls.checkInOut(mSettings.getUserId(), in);
		mHttpClient.get(url, getActivity(), new Callback() {
			@Override
			public void onComplete() {
				// do nothing
			}

			@Override
			public void onSuccess(final String body) {
				try {
					final JSONObject json = new JSONObject(body);
					if(json.has("success")) {
						mSettings.setCheckedIn(in);
						setButtonText(in);
					}
					else if(json.has("error")) {
						ErrorDialog.show(getActivity(), json.getString("error"), false);
					}
					// else ???
				}
				catch(JSONException e) {
					ErrorDialog.show(getActivity(), e.getLocalizedMessage(), false);
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
