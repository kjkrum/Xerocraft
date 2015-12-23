package org.xerocraft.memberapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class NoUserDialog extends DialogFragment {

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setCancelable(false);
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.title_no_user)
		.setMessage(R.string.message_no_user)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				FragmentUtil.getFragment(getActivity(), UserFragment.class)
				.scanQrCode(ScannerFragment.REQ_SCAN_USER);
			}
		})
		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getActivity().finish();
			}
		})
		.create();
	}

	@Override
	public void onPause() {
		super.onPause();
		/* Condition that shows this dialog will be tested again on resume. */
		dismiss();
	}

	public static void show(final FragmentActivity activity) {
		FragmentUtil.show(activity, NoUserDialog.class, null);
	}
}
