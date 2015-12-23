package org.xerocraft.memberapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.chalcodes.event.EventBus;
import com.chalcodes.event.EventReceiver;

import javax.inject.Inject;

/**
 * The check in/out button.
 */
public class CheckInOutFragment extends BaseFragment {
	private Button mButton;

	@Inject Settings mSettings;
	@Inject EventBus<Member> mUserBus;

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
		setButtonText();
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO check in or out
			}
		});
		return view;
	}

	private void setButtonText() {
		mButton.setText(mSettings.isCheckedIn() ? R.string.check_out : R.string.check_in);
	}

	/* User info controls visibility. */
	private final EventReceiver<Member> mUserReceiver = new EventReceiver<Member>() {
		@Override
		public void onEvent(EventBus<Member> bus, Member user) {
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
}
