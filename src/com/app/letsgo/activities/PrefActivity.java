package com.app.letsgo.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

import com.app.letsgo.fragments.SettingsFragment;

public class PrefActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
	}
	
	public void onSave(View v) {
		
	}
}
