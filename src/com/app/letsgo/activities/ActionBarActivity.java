package com.app.letsgo.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SearchView;

import com.app.letsgo.R;
import com.app.letsgo.fragments.BaseMapFragment;
import com.app.letsgo.helpers.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class ActionBarActivity extends FragmentActivity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener {

    // Global variable to hold the current location
    Location mCurrentLocation;
	LocationClient mLocationClient;    
    String nearByArea;
    
	public static final int CREATE_EVENT_REQUEST_CODE = 5;
	public static final int SETTINGS_REQUEST_CODE = 10;
	
	/*
	 * Define a request code to send to Google Play services
	 * This code is returned in Activity.onActivityResult
	 */
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("debug", "ActionBarActivity.onCreate(): ....");
		
		/*
		 * Create a new location client, using callbacks.
		 */
		Log.d("debug", "ActionBarActivity.onCreate(): get locationClient");
		
		mLocationClient = new LocationClient(this, this, this);		
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        super.onOptionsItemSelected(item); 
        Intent i;
        switch (item.getItemId()) {        
            case R.id.action_create:
            	Log.d("debug", "selected Create...");
        		i = new Intent(this, CreateEventActivity.class);
        		startActivityForResult(i, CREATE_EVENT_REQUEST_CODE);
                break; 
            case R.id.action_settings:
            	Log.d("debug", "selected Preference...");
            	i = new Intent(this, SettingsActivity.class);
            	startActivityForResult(i, SETTINGS_REQUEST_CODE);
                break;
            case R.id.action_search:
            	Log.d("debug", "selected Search...");            	
            	break;
        }
        return true;
    } 
	
	/*
	 * Handle results returned to the FragmentActivity
	 * by Google Play services
	 */
	@Override
	protected void onActivityResult(
			int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		
		case CONNECTION_FAILURE_RESOLUTION_REQUEST :
			/*
			 * If the result code is Activity.RESULT_OK, try
			 * to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK :
				/*
				 * Try the request again
				 */				
				break;
			}			
		}
	}

    /**
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client
        if (isGooglePlayServicesAvailable()) {
        	mLocationClient.connect();
            Log.d("debug", "ActionBarActivity.onStart(): connecting...");
        }
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
    	mCurrentLocation = mLocationClient.getLastLocation();  
        Log.d("debug", "ActionBarActivity.onConnected(): Connected, location = " + mCurrentLocation);   
    	if (mCurrentLocation != null) {    		
    		AsyncTask<Location, Void, String> nearByTask = new GetAddressTask(getContext());
    		Log.d("debug", "onConnected(): calling nearByTask...");
    		nearByTask.execute(mCurrentLocation);
    		Log.d("debug", "onConnected(): calling nearByTask...end");
    	}
    }
    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Log.e(Utils.LOG_TAG, "Disconnected. Please re-connect.");
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
             // If no resolution is available, display a dialog to the user with the error.
        	Log.e(Utils.LOG_TAG, "ActionBarActivity.onConnectionFailed..." + 
        			connectionResult.getErrorCode());
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

       
    /**
    * A subclass of AsyncTask that calls getFromLocation() in the
    * background. The class definition has these generic types:
    * Location - A Location object containing the current location.
    * Void     - indicates that progress units are not used
    * String   - An address passed to onPostExecute()
    */
    private class GetAddressTask extends AsyncTask<Location, Void, String> {
    	
        Context mContext;
        
        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }
        
        /**
         * Get a Geocoder instance, get the latitude and longitude,
         * look up the address, and return it
         *
         * @params params One or more Location objects
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         */
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
            	// return 1 address
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
	            Log.e("LocationService",
	                    "IO Exception in getFromLocation()");
	            e1.printStackTrace();
	            return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
	            // Error message to post in the log
	            String errorString = "Illegal arguments " +
	                    Double.toString(loc.getLatitude()) +
	                    " , " +
	                    Double.toString(loc.getLongitude()) +
	                    " passed to address service";
	            Log.e("LocationService", errorString);
	            e2.printStackTrace();
	            return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                StringBuilder nearByArea = new StringBuilder(address.getLocality())
                	.append(", ").append(address.getAdminArea());
                Log.d("debug", "GetAddressTask.doInBackground(): area = " + nearByArea.toString());
                // Return the text
                return nearByArea.toString();
            } else {
                return "No address found";
            }
        }
        /**
         * A method that's called once doInBackground() completes. Turn
         * off the indeterminate activity indicator and set
         * the text of the UI element that shows the address. If the
         * lookup failed, display the error message.
         */
        @Override
        protected void onPostExecute(String address) {
            // Set activity indicator visibility to "gone"
            // mActivityIndicator.setVisibility(View.GONE);
            // Display the results of the lookup.
            nearByArea = address;
        }
    }

    public Context getContext() {
    	return (Context)this;
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actions, menu);
		// searchEvents(menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Search local events based on the {@code query} string
	 * @param query user entered query string
	 */
	public void searchEvents(Menu menu) {
		Log.d("debug", "MapActivity do search...");
		//this.menu = menu;
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) searchItem.getActionView();
		setOnSearchClickListener(searchView);
		searchView.setQueryHint("Search events");
	}
		
    
	public void setOnSearchClickListener(SearchView searchView) {
		searchView.setOnSearchClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("debug", "ActionBarActivity.showNearByLocation().onClick()");				
				setLocationText();
			}
			
			public void setLocationText() {
				Log.d("debug", "Location Text: " + nearByArea);
			}
		});		
	}
}
