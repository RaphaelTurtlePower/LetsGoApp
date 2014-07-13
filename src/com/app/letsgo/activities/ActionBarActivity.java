package com.app.letsgo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

import com.app.letsgo.R;

public class ActionBarActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        super.onOptionsItemSelected(item); 
        Intent i;
        switch (item.getItemId()) {
        
            case R.id.action_create:
            	Log.d("debug", "selected Create...");
        		i = new Intent(this, CreateEventActivity.class);
        		startActivity(i);
                break; 
            case R.id.action_settings:
            	Log.d("debug", "selected Preference...");
            	i = new Intent(this, PrefActivity.class);
            	startActivity(i);
                break;
            case R.id.action_search:
            	Log.d("debug", "selected Search...");            	
            	break;
        }
        return true;
 
    }
}
