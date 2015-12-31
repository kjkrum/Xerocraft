package org.xerocraft.memberapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Displays a {@link Member}.
 *
 * @author Kevin Krumwiede
 */
public class MemberInfoActivity extends DataActivity {
	/**
	 * Serializable extra containing the {@link Member} to display.
	 */
	public static final String EXTRA_MEMBER = "member";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Member member = (Member) getIntent().getSerializableExtra(EXTRA_MEMBER);
		display(member);
	}

	private void display(final Member member) {
		addDataItem(R.string.name, member.getFirstName() + ' ' + member.getLastName());
		addDataItem(R.string.user_name, member.getUserName());
		addDataItem(R.string.email, member.getEmail());
		addDataItem(R.string.status, member.isActive() ? R.string.active : R.string.inactive);
		addDataItem(R.string.tags, TextUtils.join(", ", member.getTags()));
	}
}
