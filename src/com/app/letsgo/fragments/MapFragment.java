package com.app.letsgo.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.letsgo.R;
import com.app.letsgo.activities.EventDetailActivity;
import com.app.letsgo.dialogs.MapItemDialog;
import com.app.letsgo.models.Event;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends SupportMapFragment implements OnMarkerClickListener {
	private HashMap<Marker, Event> markerMap = new HashMap<Marker, Event>();
	
	public void initialize(){
		//getMap().setOnMarkerClickListener(this);
		ArrayList<Event> ev = new ArrayList<Event>();
		ev.add( new Event(getActivity(), "3081 Tulare Dr, San Jose, CA 95132", "Raphael's 1st Birthday", "It's his birthday!", new Date()));
		ev.add(new Event(getActivity(), "1 Washington Square, San Jose, CA 95132", "Graduation!", "Yay!", new Date()));
		ev.add(new Event(getActivity(), "San Francisco, CA", "Fourth of July Bash!", "Bashing", new Date()));
		loadEvents(ev);
		
	}
	
	public void addEvent(Event event){
		Marker mapMarker = getMap().addMarker( new MarkerOptions()
	    .position(event.getMapPosition())					 								    
	  //  .title(event.getName())
	 //   .snippet(event.getDescription())
	   .icon(BitmapDescriptorFactory.fromResource(event.getMarkerType())));
		markerMap.put(mapMarker, event);
		getMap().setInfoWindowAdapter(new InfoWindowAdapter(){

			@Override
			public View getInfoContents(Marker marker) {
				Event event = markerMap.get(marker);
				LayoutInflater mInflater = getActivity().getLayoutInflater();
				View v = mInflater.inflate(R.layout.map_item, null);
				TextView title = (TextView) v.findViewById(R.id.map_item_title);
				title.setText(event.getName());
				TextView description = (TextView) v.findViewById(R.id.map_item_description);
				description.setText(event.getDescription());
				
				TextView street_address = (TextView) v.findViewById(R.id.map_item_street);
				street_address.setText(event.getAddress().getAddressLine(0));
				TextView city_state = (TextView) v.findViewById(R.id.map_item_city_state);
				city_state.setText(event.getAddress().getPostalCode());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");      
	

				TextView startDate = (TextView) v.findViewById(R.id.map_item_startDate);
				startDate.setText(sdf.format(event.getStartDate()).toString());
				
				return v;
			}

			@Override
			public View getInfoWindow(Marker marker) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		
		getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

			@Override
			public void onInfoWindowClick(Marker marker) {
				Event event = markerMap.get(marker);
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtra("event", event);
				startActivity(i);
			}
			
		});
	}
	
	public void loadEvents(ArrayList<Event> events){
		for(int i=0; i<events.size(); i++){
			addEvent(events.get(i));
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Event eventDetails = markerMap.get(marker);
		MapItemDialog dialog = new MapItemDialog(eventDetails);
		Window window = getActivity().getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.TOP | Gravity.LEFT;
		lp.x = 50;
		lp.y = 95;
		window.setAttributes(lp);
		window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
				window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			
		dialog.show(getFragmentManager(),  "eventDetails");
		return false;
	}
}
