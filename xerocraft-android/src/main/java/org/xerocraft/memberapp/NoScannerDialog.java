package org.xerocraft.memberapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class NoScannerDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.title_no_scanner)
		.setMessage(R.string.message_no_scanner)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
			    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
			    startActivity(marketIntent);
			}
		})
		.setNegativeButton(android.R.string.no, null)
		.create();
	}

//	@Override
//	public void onPause() {
//		super.onPause();
//		/* Dialog is shown by a user action. */
//		dismiss();
//	}

	public static void show(final FragmentActivity activity) {
		FragmentUtil.show(activity, NoScannerDialog.class, null);
	}

}
