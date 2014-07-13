package com.app.letsgo.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.letsgo.R;
import com.app.letsgo.activities.EventDetailActivity;
import com.app.letsgo.dialogs.MapItemDialog;
import com.app.letsgo.models.LocalEvent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class BaseMapFragment extends MapFragment implements OnMarkerClickListener {
	private HashMap<Marker, LocalEvent> markerMap = new HashMap<Marker, LocalEvent>();
	
	public static BaseMapFragment newInstance(){
		BaseMapFragment mapFragment = new BaseMapFragment();
		return mapFragment;
	}
	
	public void initialize(){
		loadEvents(LocalEvent.getLocalEvents());
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		initialize();
	}
	
	public void addEvent(LocalEvent event){
		Marker mapMarker = getMap().addMarker( new MarkerOptions()
	    .position(event.getMapPosition())					 								    
	   .icon(BitmapDescriptorFactory.fromResource(event.getMarkerType())));
		markerMap.put(mapMarker, event);
		getMap().setInfoWindowAdapter(new InfoWindowAdapter(){

			@Override
			public View getInfoContents(Marker marker) {
				LocalEvent event = markerMap.get(marker);
				LayoutInflater mInflater = getActivity().getLayoutInflater();
				View v = mInflater.inflate(R.layout.map_item, null);
				TextView title = (TextView) v.findViewById(R.id.map_item_title);
				title.setText(event.getEventName());
				TextView description = (TextView) v.findViewById(R.id.map_item_description);
				description.setText(event.getDescription());
				
				TextView street_address = (TextView) v.findViewById(R.id.map_item_street);
				street_address.setText(event.getLocation().getAddress());
				TextView city_state = (TextView) v.findViewById(R.id.map_item_city_state);
			

				TextView startDate = (TextView) v.findViewById(R.id.map_item_startDate);
				startDate.setText(event.getStartDate());
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
				LocalEvent event = markerMap.get(marker);
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtra("event", event);
				startActivity(i);
			}
			
		});
	}
	
	public void loadEvents(ArrayList<LocalEvent> events){
		for(int i=0; i<events.size(); i++){
			addEvent(events.get(i));
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		LocalEvent eventDetails = markerMap.get(marker);
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
