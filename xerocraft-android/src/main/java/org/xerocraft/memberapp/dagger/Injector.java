package org.xerocraft.memberapp.dagger;

import dagger.Component;
import org.xerocraft.memberapp.CheckInOutFragment;
import org.xerocraft.memberapp.InventoryFragment;
import org.xerocraft.memberapp.MainActivity;
import org.xerocraft.memberapp.MemberInfoFragment;
import org.xerocraft.memberapp.PermitInfoFragment;
import org.xerocraft.memberapp.UserFragment;

import javax.inject.Singleton;

/**
 * Dagger dependency injector.
 *
 * @author Kevin Krumwiede
 */
@Singleton @Component(modules = AppModule.class)
public interface Injector {
	void inject(MainActivity activity);
	void inject(UserFragment fragment);
	void inject(InventoryFragment fragment);
	void inject(CheckInOutFragment fragment);
	void inject(MemberInfoFragment fragment);
	void inject(PermitInfoFragment fragment);
}
