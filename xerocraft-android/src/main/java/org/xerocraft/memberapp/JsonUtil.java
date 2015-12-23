package org.xerocraft.memberapp;

import org.json.JSONObject;

/**
 * Static utility methods for working with JSON objects from QR codes and
 * server endpoints.
 *
 * @author Kevin Krumwiede
 */
public class JsonUtil {
	private static final String PROP_PERMIT = "permit";
	private static final String PROP_LOCATION = "loc";
	private static final String PROP_ERROR = "error";
	private static final String PROP_SUCCESS = "success";
	private static final int BOGUS_VALUE = -1;
	
	/**
	 * Tests whether a JSON object represents an error.
	 * 	
	 * @param obj the JSON object
	 * @return true if obj represents an error; otherwise false
	 */
	public static boolean isError(final JSONObject obj) {
		return obj.has(PROP_ERROR);
	}
	
	/**
	 * Gets the error message from a JSON object.
	 * 
	 * @param obj the JSON object
	 * @return the error message, or an empty string if obj does not represent
	 * an error
	 * @see #isError(JSONObject)
	 */
	public static String getErrorMessage(final JSONObject obj) {
		return obj.optString(PROP_ERROR);
	}
	
	/**
	 * Tests whether a JSON object represents success.
	 * 
	 * @param obj the JSON object
	 * @return true if obj represents success; otherwise false
	 */
	public static boolean isSuccess(final JSONObject obj) {
		return obj.has(PROP_SUCCESS);
	}
	
	/**
	 * Tests if a JSON object represents a parking permit.  This will be true
	 * for both permit tags and permit details.
	 * 
	 * @param obj the JSON object 
	 * @return true if the object represents a permit; otherwise false
	 */
	public static boolean isPermit(final JSONObject obj) {
		/* Adrian says: "I'm using the PKs that are assigned by Django's
		 * object persistence and they always start at one and inc up from
		 * there.  So zero won't be a permit or location number." */
		return obj.optInt(PROP_PERMIT, BOGUS_VALUE) > 0;
	}
	
	/**
	 * Gets the parking permit number from a JSON object.
	 * 
	 * @param obj the JSON object
	 * @return the permit number
	 * @throws IllegalArgumentException if obj does not represent a parking
	 * permit
	 * @see #isPermit(JSONObject)
	 */
	public static int getPermitId(final JSONObject obj) {
		final int num = obj.optInt(PROP_PERMIT, BOGUS_VALUE);
		if(num == BOGUS_VALUE) {
			throw new IllegalArgumentException();
		}
		return num;
	}
	
	/**
	 * Tests if a JSON object represents a location.
	 * 
	 * @param obj the JSON object 
	 * @return true if the object represents a location; otherwise false
	 */
	public static boolean isLocation(final JSONObject obj) {
		return obj.optInt(PROP_LOCATION, BOGUS_VALUE) > 0;
	}
	
	/**
	 * Gets the location number from a JSON object.
	 * 
	 * @param obj the JSON object
	 * @return the location number
	 * @throws IllegalArgumentException if obj does not represent a location
	 * @see #isLocation(JSONObject)
	 */
	public static int getLocationId(final JSONObject obj) {
		final int num = obj.optInt(PROP_LOCATION, BOGUS_VALUE);
		if(num == BOGUS_VALUE) {
			throw new IllegalArgumentException();
		}
		return num;
	}
	
	private JsonUtil() {}
}
