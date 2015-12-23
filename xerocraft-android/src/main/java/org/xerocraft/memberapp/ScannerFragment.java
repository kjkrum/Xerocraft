package org.xerocraft.memberapp;

import android.content.Intent;

/**
 * Base class for fragments that scan QR codes.
 *
 * @author Kevin Krumwiede
 */
public class ScannerFragment extends BaseFragment {
	// request codes are defined here to help avoid collisions
	/**
	 * Request code for scanning the user's own membership card to configure
	 * the app.
	 */
	protected static final int REQ_SCAN_USER = 1;
	/**
	 * Request code for scanning a membership card to request member info.
	 */
	protected static final int REQ_SCAN_MEMBER = 2;
	/**
	 * Request code for scanning a parking permit to request permit info.
	 */
	protected static final int REQ_SCAN_PERMIT = 3;
	/**
	 * Request code for scanning locations and parking permits to update the
	 * permit database.
	 */
	protected static final int REQ_SCAN_INVENTORY = 4;
	/**
	 * <tt>String</tt> extra containing the scan result.
	 */
	protected static final String EXTRA_SCAN_RESULT = "SCAN_RESULT";

	/**
	 * Starts an activity for result to scan a QR code.  If successful, the
	 * data from the QR code will be returned in {@link #EXTRA_SCAN_RESULT}.
	 * If no compatible scanner app is available, this method shows a dialog
	 * offering to take the user to the Play Store to download one.
	 *
	 * @param requestCode the request code for the scanner activity
	 */
	protected void scanQrCode(final int requestCode) {
		final Intent scanIntent = new Intent("com.google.zxing.client.android.SCAN");
	    scanIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		if(scanIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivityForResult(scanIntent, requestCode);
		}
		else {
			NoScannerDialog.show(getActivity());
		}
	}
}
