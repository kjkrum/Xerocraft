package org.xerocraft.memberapp.http;

/**
 * A very simple HTTP client.
 *
 * @author Kevin Krumwiede
 */
public interface HttpClient {
	Request get(String url, Object tag, Callback callback);
	void cancel(Object tag);
	void cancelAll();
}
