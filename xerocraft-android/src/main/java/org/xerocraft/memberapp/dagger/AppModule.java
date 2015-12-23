package org.xerocraft.memberapp.dagger;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.chalcodes.event.EventBus;
import com.chalcodes.event.SimpleEventBus;
import com.chalcodes.event.StickyEventBus;
import com.chalcodes.event.exec.AndroidExecutor;
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
		return new AndroidExecutor();
	}

	@Provides @Singleton
	EventBus<Exception> provideExceptionBus(final Executor executor) {
		return new SimpleEventBus<>(executor, null, false);
	}

	@Provides @Singleton
	EventBus<Member> provideUserBus(final Executor executor, final EventBus<Exception> exceptionBus) {
		return new StickyEventBus<>(executor, exceptionBus, false);
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
