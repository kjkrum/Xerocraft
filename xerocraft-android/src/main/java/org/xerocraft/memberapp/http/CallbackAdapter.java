package org.xerocraft.memberapp.http;

/**
 * Abstract adapter for {@link Callback}.
 *
 * @author Kevin Krumwiede
 */
abstract public class CallbackAdapter implements Callback {
	@Override
	public void onComplete() {}

	@Override
	public void onSuccess(String body) {}

	@Override
	public void onError(Throwable error) {}

	@Override
	public void onError(int responseCode, String body) {}
}
