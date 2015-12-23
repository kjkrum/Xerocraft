package org.xerocraft.memberapp;

import android.animation.LayoutTransition;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

/* The bogus lint error that sometimes pops up is caused by a bug in the
 * Android SDK.  Cleaning the project should clear it.
 *
 * https://code.google.com/p/android/issues/detail?id=188677
 */

public class MainActivity extends AppCompatActivity {
//	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentUtil.addFragment(this, UserFragment.class, null);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			final LayoutTransition transition = new LayoutTransition();
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				transition.enableTransitionType(LayoutTransition.CHANGING);
			}
			((ViewGroup) findViewById(R.id.root_layout)).setLayoutTransition(transition);
		}
	}

	@Override
	public void onBackPressed() {
		((App) getApplication()).resetInjector();
		super.onBackPressed();
	}
}
