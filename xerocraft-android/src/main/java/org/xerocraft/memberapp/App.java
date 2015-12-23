package org.xerocraft.memberapp;

import android.app.Application;
import org.xerocraft.memberapp.dagger.AppModule;
import org.xerocraft.memberapp.dagger.DaggerInjector;
import org.xerocraft.memberapp.dagger.Injector;

/**
 * Custom application class.
 *
 * @author Kevin Krumwiede
 */
public class App extends Application {
	private Injector mInjector;

	public Injector getInjector() {
		if(mInjector == null) {
			mInjector = DaggerInjector.builder()
					.appModule(new AppModule(this))
					.build();
		}
		return mInjector;
	}

	public void resetInjector() {
		mInjector = null;
	}
}
