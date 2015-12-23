package org.xerocraft.memberapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Displays a {@link JSONObject} representing a parking permit.
 *
 * @author Kevin Krumwiede
 */
public class PermitInfoActivity extends DataActivity {
	/**
	 * String extra containing the permit JSON.
	 */
	public static final String EXTRA_PERMIT = "permit";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String str = getIntent().getStringExtra(EXTRA_PERMIT);
		try {
			final JSONObject permit = new JSONObject(str);
			display(permit);
		}
		catch(JSONException e) {
			// shouldn't happen; already parsed in PermitInfoFragment
			throw new RuntimeException(e);
		}
	}

	private void display(final JSONObject permit) {
		addDataItem(R.string.permit_no, Integer.toString(permit.optInt("permit", 0)));
		// TODO parse and reformat date
		addDataItem(R.string.created, permit.optString("created", ""));
		addDataItem(R.string.desc, permit.optString("short_desc", ""));
		addDataItem(R.string.owner, permit.optString("owner_name", ""));
		addDataItem(R.string.in_inventoried_space,
				permit.optBoolean("is_in_inventoried_space") ?
						R.string.yes : R.string.no);
		addDataItem(R.string.ok_to_move,
				permit.optBoolean("ok_to_move") ?
						R.string.yes : R.string.no);
	}
}
