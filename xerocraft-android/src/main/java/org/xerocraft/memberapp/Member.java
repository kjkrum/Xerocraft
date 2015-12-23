package org.xerocraft.memberapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Member info.  Implements <tt>Serializable</tt> instead of
 * <tt>Parcelable</tt> because it's much simpler and the performance
 * difference is insignificant for a small number of instances.
 *
 * @author Kevin Krumwiede
 */
public class Member implements Serializable {
	private static final long serialVersionUID = -8044116327968768718L;
	private static final String ID_REGEX = "[-_a-zA-Z0-9]{32}";
	private static final Matcher gIdMatcher = Pattern.compile(ID_REGEX).matcher("");

	private String mFirstName;
	private String mLastName;
	private String mUserName;
	private String mEmail;
	private int mPk;
	private boolean mActive;
	private final List<String> mTags = new ArrayList<>();

	public String getFirstName() {
		return mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public String getUserName() {
		return mUserName;
	}

	public String getEmail() {
		return mEmail;
	}

	public int getPk() {
		return mPk;
	}

	public boolean isActive() {
		return mActive;
	}

	public boolean isStaff() {
		return mTags.contains("Staff");
	}

	public List<String> getTags() {
		return new ArrayList<String>(mTags);
	}

	private Member() {}

	public static Member fromJson(final String json) throws JSONException {
		return fromJson(new JSONObject(json));
	}

	public static Member fromJson(final JSONObject obj) throws JSONException {
		final Member member = new Member();
		member.mPk = obj.getInt("pk");
		member.mFirstName = obj.optString("first_name", "");
		member.mLastName = obj.optString("last_name", "");
		member.mUserName = obj.optString("username", "");
		member.mEmail = obj.optString("email", "");
		member.mActive = obj.optBoolean("is_active", false);
		final JSONArray tags = obj.optJSONArray("tags");
		if(tags != null) {
			for(int i = 0, limit = tags.length(); i < limit; ++i) {
				member.mTags.add(tags.getString(i));
			}
		}
		return member;
	}

	/**
	 * Tests if a member ID is valid.  A valid member ID is exactly 32
	 * characters including dashes, underscores, a-z, A-Z, and 0-9.
	 *
	 * @param memberId the member ID
	 * @return true if memberId is valid; otherwise false
	 */
	static boolean isValidId(final String memberId) {
		return memberId != null && gIdMatcher.reset(memberId).matches();
	}
}
