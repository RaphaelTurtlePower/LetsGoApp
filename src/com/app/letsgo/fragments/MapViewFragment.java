package com.app.letsgo.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.letsgo.R;
import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.LocalEventParcel;

public class MapViewFragment extends Fragment implements OnBackStackChangedListener{

	private BaseMapFragment mapFragment;
	private ListFragment listFragment;
	private Handler mHandler = new Handler();
	private boolean mShowingBack = false;
	private Button toggle;
	Menu menu;
	ArrayList<LocalEventParcel> events;	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Defines the xml file for the fragment
      View view = inflater.inflate(R.layout.fragment_map, container, false);
      
      events = LocalEventParcel.getLocalEvents();
      
      listFragment = ListFragment.newInstance(events);
      mapFragment = BaseMapFragment.newInstance(events);
      
      if (savedInstanceState == null) {
    	  getChildFragmentManager()
    	  .beginTransaction()
    	  .add(R.id.container, mapFragment)
    	  .commit();
    	  getChildFragmentManager().executePendingTransactions();
      } else {
    	  mShowingBack = (getChildFragmentManager().getBackStackEntryCount() > 0);
      }
      getChildFragmentManager().addOnBackStackChangedListener(this);
      toggle = (Button) view.findViewById(R.id.toggle);
      
      toggle.setOnClickListener(new OnClickListener(){

    	  @Override
    	  public void onClick(View v) {
    		  //flips the card view
    		  flipCard();
    	  }        	
      });
      toggle.setText("List");  
      Log.d("debug", "MapViewFragment.onCreateView(): events = " + events.size());
      return view;
    }
	
	private void flipCard() {
		if (mShowingBack) {
			toggle.setText("List");
			getChildFragmentManager().popBackStack();
			mapFragment.loadEvents(events);
			return;
		}
		mShowingBack = true;
		listFragment = ListFragment.newInstance(events);
		getChildFragmentManager()
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
				// TODO: add this back later
				// invalidateOptionsMenu();
			}
		});
	}
	
	@Override
    public void onBackStackChanged() {
        mShowingBack = (getChildFragmentManager().getBackStackEntryCount() > 0);
	}	
	
	public BaseMapFragment getBaseMapFragment() {
		return mapFragment;
	}
	
	public ListFragment getListFragment() {
		return listFragment;
	}

}
