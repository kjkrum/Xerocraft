package org.xerocraft.memberapp;

/**
 * Static methods to generate URLs with parameters.
 *
 * @author Kevin Krumwiede
 */
public class Urls {
	private static final String BASE_URL = "http://xerocraft-django.herokuapp.com";
	private static final String PUBLIC_MEMBER_INFO = "/members/api/member-details-pub/";
	private static final String PRIVATE_MEMBER_INFO = "/members/api/member-details/";
	private static final String PERMIT_INFO = "/inventory/get-permit-details/";
	private static final String UPDATE_INVENTORY = "/inventory/note-permit-scan/";
	private static final String CHECK_IN_OUT = "/members/api/visit-event/";


	public static String publicMemberInfo(final String memberId) {
		if(!Member.isValidId(memberId)) {
			throw new IllegalArgumentException();
		}
		return BASE_URL + PUBLIC_MEMBER_INFO + memberId;
	}

	public static String privateMemberInfo(final String memberId, final String userId) {
		if(!Member.isValidId(memberId) || !Member.isValidId(userId)) {
			throw new IllegalArgumentException();
		}
		return BASE_URL + PRIVATE_MEMBER_INFO + memberId + '_' + userId;
	}

	public static String permitInfo(final int permitId) {
		if(permitId <= 0) {
			throw new IllegalArgumentException();
		}
		return BASE_URL + PERMIT_INFO + permitId;
	}

	public static String updateInventory(final int permitId, final int locationId) {
		if(permitId <= 0 || locationId <= 0) {
			throw new IllegalArgumentException();
		}
		return BASE_URL + UPDATE_INVENTORY + permitId + '_' + locationId;
	}

	public static String checkInOut(final String userId, final boolean in) {
		if(!Member.isValidId(userId)) {
			throw new IllegalArgumentException();
		}
		/* 'A' stands for arrival and 'D' for departure.  The iOS app also
		 * defines, but never uses, a visit type 'P' meaning present. */
		return BASE_URL + CHECK_IN_OUT + userId + '_' + (in ? "A" : "D");
	}

	private Urls() {}
}
