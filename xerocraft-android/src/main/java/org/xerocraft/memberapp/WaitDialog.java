package org.xerocraft.memberapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class WaitDialog extends DialogFragment {
	private static final String KEY_MESSAGE = "message";

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setCancelable(false);
		final ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setIndeterminate(true);
		dialog.setMessage(getArguments().getString(KEY_MESSAGE));
		return dialog;
	}

	public static void show(final FragmentActivity activity, final String message) {
		/* To keep dismiss() simple, only one wait dialog can be shown at a time. */
		dismiss(activity);
		final Bundle args = new Bundle();
		args.putString(KEY_MESSAGE, message);
		FragmentUtil.show(activity, WaitDialog.class, args);
	}

	public static void dismiss(final FragmentActivity activity) {
		FragmentUtil.dismiss(activity, WaitDialog.class);
	}

}
