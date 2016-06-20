package org.xerocraft.memberapp.dagger;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.chalcodes.event.EventBus;
import com.chalcodes.event.SimpleEventBus;
import com.chalcodes.event.StickyEventBus;
import dagger.Module;
import dagger.Provides;
import org.xerocraft.memberapp.Member;
import org.xerocraft.memberapp.Settings;
import org.xerocraft.memberapp.http.HttpClient;
import org.xerocraft.memberapp.http.VolleyHttpClient;

import javax.inject.Singleton;
import java.util.concurrent.Executor;

/**
 * Provides application-scope dependencies.
 *
 * @author Kevin Krumwiede
 */
@Module
public class AppModule {
	private final Context mAppContext;

	public AppModule(final Context context) {
		if(context == null) {
			throw new NullPointerException();
		}
		mAppContext = context.getApplicationContext();
	}

	@Provides @Singleton
	Context provideContext() {
		return mAppContext;
	}

	@Provides @Singleton
	Executor provideExecutor() {
		return new Executor() {
			private final Handler mHandler = new Handler(Looper.getMainLooper());
			@Override
			public void execute(@NonNull final Runnable command) {
				mHandler.post(command);
			}
		};
	}

	@Provides @Singleton
	EventBus<Exception> provideExceptionBus(final Executor executor) {
		return new SimpleEventBus<>(executor, null);
	}

	@Provides @Singleton
	StickyEventBus<Member> provideStickyUserBus(final Executor executor, final EventBus<Exception> exceptionBus) {
		return new StickyEventBus<>(executor, exceptionBus);
	}

	/* Dagger won't automatically inject a StickyEventBus<T> into an
	 * EventBus<T> field, even if StickyEventBus<T> is the only kind of
	 * EventBus<T> it knows how to provide. */
	@Provides @Singleton
	EventBus<Member> provideUserBus(final StickyEventBus<Member> bus) {
		return bus;
	}

	@Provides @Singleton
	Settings provideSettings(final Context context) {
		return new Settings(context);
	}

	@Provides @Singleton
	HttpClient provideHttpClient(final Context context) {
		return new VolleyHttpClient(context);
	}

}
