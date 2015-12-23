package org.xerocraft.memberapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 * A generic error dialog with an "OK" button.
 *
 * @author Kevin Krumwiede
 */
public class ErrorDialog extends DialogFragment {
	private static final String KEY_ERROR = "error";
	private static final String KEY_DISMISS_ON_PAUSE = "dismissOnPause";

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setCancelable(false);
		final String message = getArguments().getString(KEY_ERROR);
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.title_error)
		.setMessage(message)
		.setNeutralButton(android.R.string.ok, null)
		.create();
	}

	@Override
	public void onPause() {
		super.onPause();
		if(getArguments().getBoolean(KEY_DISMISS_ON_PAUSE)) {
			dismiss();
		}
	}

	/**
	 * Shows an error dialog.  Dialogs should be dismissed on pause if they
	 * are shown by a condition that will be tested again on resume.
	 *
	 * @param activity the hosting activity
	 * @param message the error message
	 * @param dismissOnPause true if the dialog should be dismissed on pause;
	 *                       otherwise false
	 */
	static void show(final FragmentActivity activity, final String message, final boolean dismissOnPause) {
		final Bundle args = new Bundle();
		args.putString(KEY_ERROR, message);
		args.putBoolean(KEY_DISMISS_ON_PAUSE, dismissOnPause);
		FragmentUtil.show(activity, ErrorDialog.class, args);
	}
}
