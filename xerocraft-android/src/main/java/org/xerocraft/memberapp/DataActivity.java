package org.xerocraft.memberapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Base class for activities that display data.
 *
 * @author Kevin Krumwiede
 */
abstract public class DataActivity extends AppCompatActivity {
	private LayoutInflater mInflater;
	private ViewGroup mViewContainer;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mViewContainer = (ViewGroup) findViewById(R.id.dataContainer);
	}

	protected void addDataItem(final String label, final String data) {
		final View view = mInflater.inflate(R.layout.view_data, mViewContainer, false);
		((TextView) view.findViewById(R.id.labelText)).setText(label);
		((TextView) view.findViewById(R.id.dataText)).setText(data);
		mViewContainer.addView(view);
	}

	protected void addDataItem(final int labelResId, final String data) {
		addDataItem(getString(labelResId), data);
	}

	protected void addDataItem(final int labelResId, final int dataResId) {
		addDataItem(getString(labelResId), getString(dataResId));
	}
}
