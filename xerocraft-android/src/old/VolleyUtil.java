package org.xerocraft.memberapp;

import android.app.Activity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

/**
 * Static utility methods for interacting with Xerocraft's endpoints using
 * Volley.  This class is designed around the assumption that all network
 * requests are completed within the lifetime of the activity that initiates
 * them.  If that assumption turns out to be incorrect, the request queue may
 * need to be owned by a service.
 *
 * @author Kevin Krumwiede
 */
public class VolleyUtil {
	/* Need a longish timeout because Django can take a while to wake up. */
	private static final int SOCKET_TIMEOUT_MS = 30000;

	private static RequestQueue gRequestQueue;

	/**
	 * Gets a JSON object.
	 *
	 * @param activity the activity
	 * @param url the URL
	 * @param responseListener the response listener
	 * @param errorListener the error listener; may be null
	 * @return the request
	 */
	public static Request<JSONObject> get(
			final Activity activity,
			final String url,
			final Response.Listener<JSONObject> responseListener,
			final Response.ErrorListener errorListener) {

		final JsonObjectRequest request = new JsonObjectRequest(url, null,
				responseListener, errorListener);
		request.setTag(activity);
		request.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		if(gRequestQueue == null) {
			gRequestQueue = Volley.newRequestQueue(activity.getApplicationContext());
		}
		return gRequestQueue.add(request);
	}

	public static void cancelRequests(final Activity activity) {
		if(gRequestQueue != null) {
			gRequestQueue.cancelAll(activity);
		}
	}

	private VolleyUtil() {}
}
