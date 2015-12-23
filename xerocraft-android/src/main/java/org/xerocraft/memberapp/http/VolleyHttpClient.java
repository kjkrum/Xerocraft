package org.xerocraft.memberapp.http;

import android.content.Context;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * An HTTP client that uses Volley.
 *
 * @author Kevin Krumwiede
 */
public class VolleyHttpClient implements HttpClient {
	private static final String TAG = VolleyHttpClient.class.getSimpleName();
	/* Need a longish timeout because Django can take a while to wake up. */
	private static final int SOCKET_TIMEOUT_MS = 30000;
	final RequestQueue mRequestQueue;

	public VolleyHttpClient(final Context context) {
		mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
	}

	@Override
	public Request get(final String url, final Object tag, final Callback callback) {
		final com.android.volley.Request<String> volleyRequest =
				new StringRequest(url,
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								Log.d(TAG, response);
								callback.onComplete();
								callback.onSuccess(response);
							}
						},
						new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								if(error.networkResponse != null) {
									final String body = new String(error.networkResponse.data);
									Log.w(TAG, body);
									callback.onComplete();
									callback.onError(error.networkResponse.statusCode, body);
								}
								else {
									callback.onComplete();
									callback.onError(error);
									Log.w(TAG, error);
								}
							}
						});
		volleyRequest.setTag(tag);
		volleyRequest.setRetryPolicy(new DefaultRetryPolicy(
				SOCKET_TIMEOUT_MS,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mRequestQueue.add(volleyRequest);

		return new Request() {
			@Override
			public void cancel() {
				volleyRequest.cancel();
			}
		};
	}

	@Override
	public void cancel(Object tag) {
		mRequestQueue.cancelAll(tag);
	}

	@Override
	public void cancelAll() {
		mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
			@Override
			public boolean apply(com.android.volley.Request<?> request) {
				return true;
			}
		});
	}
}
