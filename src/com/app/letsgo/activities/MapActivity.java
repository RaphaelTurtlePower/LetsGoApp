package com.app.letsgo.activities;

import android.app.Activity;
import android.app.Dialog;

import android.app.FragmentManager.OnBackStackChangedListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.app.letsgo.R;
import com.app.letsgo.fragments.BaseMapFragment;
import com.app.letsgo.fragments.ListFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends FragmentActivity implements
		OnBackStackChangedListener{

	private BaseMapFragment mapFragment;
	private com.app.letsgo.fragments.ListFragment listFragment;
	private Handler mHandler = new Handler();
	private boolean mShowingBack = false;
	private Button toggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		listFragment = ListFragment.newInstance();
		mapFragment = BaseMapFragment.newInstance();
		if (savedInstanceState == null) {
			getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mapFragment)
                    .commit();
			getFragmentManager().executePendingTransactions();
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }
	    getFragmentManager().addOnBackStackChangedListener(this);
        toggle = (Button) findViewById(R.id.toggle);
        toggle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//flips the card view
				flipCard();
			}
        	
        });
        
        toggle.setText("List");       
	}
	
	
	 private void flipCard() {
	        if (mShowingBack) {
	        	toggle.setText("List");
	            getFragmentManager().popBackStack();
	            return;
	        }

	        mShowingBack = true;
	        
	        getFragmentManager()
	                .beginTransaction()
	                .setCustomAnimations(
	                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
	                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
	                .replace(R.id.container, listFragment)
	                .addToBackStack(null)
	                .commit();
	        toggle.setText("Map");
	        mHandler.post(new Runnable() {
	            @Override
	            public void run() {
	                invalidateOptionsMenu();
	            }
	        });
	    }
	
	@Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actions, menu);
		searchEvents(menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Search local events based on the {@code query} string
	 * @param query user entered query string
	 */
	public void searchEvents(Menu menu) {
		Log.d("debug", "MapActivity do search...");
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) searchItem.getActionView();
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
            	Log.d("debug", "onCreateOptionsMenu(): query = " + query);
            	// TODO: handle search here
            	return true;
			}
			@Override
			public boolean onQueryTextChange(String text) {
				return false;
			}
		});
	}
	
	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		if (isGooglePlayServicesAvailable()) {
			mapFragment.connect();
		}

	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mapFragment.disconnect();
		super.onStop();
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case BaseMapFragment.CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				mapFragment.connect();
				break;
			}

		}
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					BaseMapFragment.CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
			}

			return false;
		}
	}
	
	
	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
}