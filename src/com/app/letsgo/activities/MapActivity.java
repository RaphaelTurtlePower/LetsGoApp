package com.app.letsgo.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.app.letsgo.R;
import com.app.letsgo.fragments.BaseMapFragment;
import com.app.letsgo.fragments.ListFragment;
import com.app.letsgo.models.LocalEvent;

public class MapActivity extends ActionBarActivity implements
		OnBackStackChangedListener{

	private BaseMapFragment mapFragment;
	private com.app.letsgo.fragments.ListFragment listFragment;
	private Handler mHandler = new Handler();
	private boolean mShowingBack = false;
	private Button toggle;
	ArrayList<LocalEvent> events;
	Menu menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		events = LocalEvent.getLocalEvents();
		listFragment = ListFragment.newInstance(events);
		mapFragment = BaseMapFragment.newInstance(events);
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
	            mapFragment.loadEvents(events);
	            return;
	        }
	        mShowingBack = true;
	        listFragment = ListFragment.newInstance(events);
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
		boolean value = super.onCreateOptionsMenu(menu);
		setQueryTextListener(menu);
		return value;
	}

	/**
	 * Search local events based on the {@code query} string
	 * @param query user entered query string
	 */
	public void setQueryTextListener(Menu menu) {
		Log.d("debug", "MapActivity do search...");
		this.menu = menu;
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) searchItem.getActionView();
		searchView.setQueryHint("Search events");
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
            	Log.d("debug", "searchEvents(): query = " + query);
            	events = LocalEvent.search(query);
            	if (mShowingBack) {
            		listFragment.setList(events);
            	} else {            		
            		mapFragment.loadEvents(events);
            	}
            	return true;
			}
			@Override
			public boolean onQueryTextChange(String text) {
				return false;
			}
		});
	}
		
	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case BaseMapFragment.CONNECTION_FAILURE_RESOLUTION_REQUEST:
			if(resultCode == Activity.RESULT_OK){
				// mapFragment.connect();
			}
			break;
		case ActionBarActivity.CREATE_EVENT_REQUEST_CODE:
			if(resultCode == Activity.RESULT_OK){
				LocalEvent event = data.getParcelableExtra("event");
				events.add(event);	//add local event
				if (mShowingBack) {
            		listFragment.addEvent(event);
            	} else {            		
            		mapFragment.addEvent(event, false);
            	}
			}
			break;
		case ActionBarActivity.SETTINGS_REQUEST_CODE:
			if(resultCode == Activity.RESULT_OK){
				//re-run the query
				MenuItem searchItem = menu.findItem(R.id.action_search);
				SearchView sView = (SearchView) searchItem.getActionView();
				String query = (String) sView.getQuery().toString();
				events = LocalEvent.search(query);
            	if (mShowingBack) {
            		listFragment.setList(events);
            	} else {            		
            		mapFragment.loadEvents(events);
            	}
			}
			break;
		}
	}
	
	@Override
	public void onConnected(Bundle bundle) {
		super.onConnected(bundle);
		mapFragment.setCurrentLocation(mCurrentLocation);
	}
 
}