package org.xerocraft.memberapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.chalcodes.event.EventBus;
import org.xerocraft.memberapp.http.HttpClient;

import javax.inject.Inject;

/**
 * Base class for other fragments.
 *
 * @author Kevin Krumwiede
 */
public class BaseFragment extends Fragment {
	protected App getApp() {
		return (App) getActivity().getApplication();
	}
}
