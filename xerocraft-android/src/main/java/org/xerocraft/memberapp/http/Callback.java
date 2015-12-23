package org.xerocraft.memberapp.http;

/**
 * HTTP request callback.
 *
 * @author Kevin Krumwiede
 */
public interface Callback {
	void onComplete();
	void onSuccess(String body);
	void onError(Throwable error);
	void onError(int responseCode, String body);
}
