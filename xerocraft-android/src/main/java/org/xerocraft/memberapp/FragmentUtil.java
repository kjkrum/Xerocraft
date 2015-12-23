package org.xerocraft.memberapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Static utility methods for working with fragments.  All methods derive
 * fragment tags from the fragment class name.
 *
 * @author Kevin Krumwiede
 */
public class FragmentUtil {
	/**
	 * Shows a dialog fragment.
	 *
	 * @param activity the activity
	 * @param klass the dialog fragment class
	 * @param args the arguments bundle; may be null
	 */
	public static void show(final FragmentActivity activity,
			final Class<? extends DialogFragment> klass, final Bundle args) {
		final FragmentManager fm = activity.getSupportFragmentManager();
		try {
			final DialogFragment dialog = klass.newInstance();
			if(args != null) {
				dialog.setArguments(args);
			}
			dialog.show(fm, tag(klass));
		}
		catch (Exception e) {
			/* Fragments must have a public no-arg constructor. */
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dismiss a dialog fragment.
	 *
	 * @param activity the activity
	 */
	public static void dismiss(final FragmentActivity activity,
			final Class<? extends DialogFragment> klass) {
		DialogFragment dialog =
				(DialogFragment) activity.getSupportFragmentManager()
				.findFragmentByTag(tag(klass));
		if(dialog != null) {
			dialog.dismiss();
		}
	}

	/**
	 * Creates and adds a headless fragment.  When this method returns, the
	 * fragment will not yet be attached to the activity.  If this method is
	 * called in an activity's {@code onCreate()}, the fragment will be
	 * attached before {@code onResume()}.
	 *
	 * @param activity the activity
	 * @param klass the fragment class
	 * @param args the arguments bundle; may be null
	 */
	public static <T extends Fragment> T addFragment(final FragmentActivity activity,
													 final Class<T> klass, final Bundle args) {
		final String tag = tag(klass);
		final Fragment frag;
		try {
			frag = klass.newInstance();
			if(args != null) {
				frag.setArguments(args);
			}
			activity.getSupportFragmentManager().beginTransaction().add(frag, tag).commit();
		} catch (Exception e) {
			/* Fragments must have a public no-arg constructor. */
			throw new RuntimeException(e);
		}
		/* This cast should work unless something else is creating fragments
		 * of different types using the same tags. */
		return klass.cast(frag);
	}

	/**
	 * Gets an activity's instance of a fragment.
	 *
	 * @param activity the activity
	 * @param klass the fragment class
	 * @return the fragment instance, or null if it does not exist
	 */
	public static <T extends Fragment> T getFragment(final FragmentActivity activity,
			final Class<T> klass) {
		final String tag = tag(klass);
		Fragment frag = activity.getSupportFragmentManager().findFragmentByTag(tag);
		/* This cast should work unless something else is creating fragments
		 * of different types using the same tags. */
		return klass.cast(frag);
	}

	private static String tag(final Class<? extends Fragment> klass) {
		return klass.getName();
	}

	private FragmentUtil() { /* Non-instantiable. */ }
}
