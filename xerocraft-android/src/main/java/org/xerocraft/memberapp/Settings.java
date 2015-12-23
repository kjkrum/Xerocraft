package org.xerocraft.memberapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Persistent app settings.
 *
 * @author Kevin Krumwiede
 */
public class Settings {
	private static final String FILE_NAME = "settings";
	private static final String PREF_USER_ID = "userId";
	private static final String PREF_CHECKED_IN = "checkedIn";
	private final SharedPreferences mPrefs;

	public Settings(final Context context) {
		mPrefs = context.getApplicationContext()
				.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * Tests if the user ID has been set.
	 *
	 * @return true if the user ID is set; otherwise false
	 */
	boolean isUserConfigured() {
		return mPrefs.contains(PREF_USER_ID);
	}

	/**
	 * Gets the user's member ID.
	 *
	 * @return the user ID, or null if it has not been set
	 * @see #isUserConfigured()
	 */
	String getUserId() {
		return mPrefs.getString(PREF_USER_ID, null);
	}

	/**
	 * Sets the user ID.
	 *
	 * @param userId the user ID
	 * @throws IllegalArgumentException if userId is invalid
	 * @see Member#isValidId(String)
	 */
	void setUserId(final String userId) {
		if(!Member.isValidId(userId)) {
			throw new IllegalArgumentException("bogus user ID: " + userId);
		}
		mPrefs.edit().putString(PREF_USER_ID, userId).commit();
	}

	/**
	 * Tests if the user is checked in.  There's no way to query the server
	 * for this information, so we initially assume false and maintain the
	 * state in persistent settings.
	 *
	 * @return true if the user is checked in; otherwise false
	 */
	boolean isCheckedIn() {
		return mPrefs.getBoolean(PREF_CHECKED_IN, false);
	}

	/**
	 * Sets whether the user is checked in.
	 *
	 * @param checkedIn true if the user is checked in; otherwise false
	 */
	void setCheckedIn(boolean checkedIn) {
		mPrefs.edit().putBoolean(PREF_CHECKED_IN, checkedIn).commit();
	}

}
