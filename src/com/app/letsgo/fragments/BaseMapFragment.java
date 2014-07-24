package com.app.letsgo.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.app.letsgo.models.LocalEventParcel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BaseMapFragment extends MapFragment implements OnMarkerClickListener {
	private ArrayList<LocalEventParcel> events;
	private HashMap<Marker, LocalEventParcel> markerMap = new HashMap<Marker, LocalEventParcel>();
	
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	public static BaseMapFragment newInstance(ArrayList<LocalEventParcel> events){
		BaseMapFragment mapFragment = new BaseMapFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList("events", events);
		mapFragment.setArguments(args);
		// Log.d("debug", "MapFragment.newInstance(): events size = " + events.size());;
		return mapFragment;
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		events = args.getParcelableArrayList("events");
		Log.d("debug", "BaseMapFragment.onCreate(): events.size = " + events.size());
		
	}
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return super.onCreateView(inflater, container, savedInstanceState);
	}
		
	public void initialize(){
		loadEvents(events);
	}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		loadMap();
		initialize();
	}
	
	public void addEvent(LocalEvent event, Boolean updateCamera){
		
		Marker mapMarker = getMap().addMarker( 
				new MarkerOptions().position(event.getMapPosition()).
					icon(BitmapDescriptorFactory.fromResource(event.getMarkerType())));

		markerMap.put(mapMarker, new LocalEventParcel(event));
		
		getMap().setInfoWindowAdapter(new InfoWindowAdapter(){
			@Override
			public View getInfoContents(Marker marker) {
				LocalEvent event = markerMap.get(marker).getEvent();
				LayoutInflater mInflater = getActivity().getLayoutInflater();
				View v = mInflater.inflate(R.layout.map_item, null);
				TextView title = (TextView) v.findViewById(R.id.map_item_title);
				title.setText(event.getEventName());
				TextView description = (TextView) v.findViewById(R.id.map_item_description);
				description.setText(event.getItemShortDescription());
				
				TextView street_address = (TextView) v.findViewById(R.id.map_item_street);
				street_address.setText(event.getLocation().getAddress());
		
				TextView startDate = (TextView) v.findViewById(R.id.map_item_startDate);
				startDate.setText(event.getStartDate());
				TextView recommend = (TextView) v.findViewById(R.id.map_item_recommend);
				String percentage = "Be the first to recommend!";
		        int total = event.getUpCount().intValue() + event.getDownCount().intValue();
		        if(total > 0){
		        	Integer p = event.getUpCount().intValue() * 100 / total;
		        	percentage = p.toString() + "% Recommended";
		        }
		        recommend.setText(percentage);
				TextView cost = (TextView) v.findViewById(R.id.map_item_cost);
		        cost.setText("$" + event.getCost());
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
				LocalEventParcel event = markerMap.get(marker);
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtra("event", event);
				startActivity(i);
			}
			
		});
		if(updateCamera){
			updateCamera(event.getLocation().getLatitude(), event.getLocation().getLongitude());
		}
	}
	
	public void loadEvents(ArrayList<LocalEventParcel> events){
		this.events = events;
		if(getMap() != null){
			getMap().clear();
		}
		for(int i=0; i<events.size(); i++){
			addEvent(events.get(i).getEvent(), false);
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		LocalEvent eventDetails = markerMap.get(marker).getEvent();
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
	
	public void loadMap(){
		if (getMap() != null) {
		//	Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
			getMap().setMyLocationEnabled(true);
			getMap().setPadding(10, 10, 10, 100);
		} else {
		//	Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
		}
	}

	public void setCurrentLocation(Location location) {
		// Display the connection status
		if (location != null) {
		//	Toast.makeText(getActivity(), "GPS location was found!", Toast.LENGTH_SHORT).show();
			updateCamera(location.getLatitude(), location.getLongitude());
		} else {
		//	Toast.makeText(getActivity(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
		}
	}

	public void updateCamera(Double latitude, Double longitude){
		LatLng latLng = new LatLng(latitude, longitude);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
		if(getMap()!=null){
			getMap().animateCamera(cameraUpdate);
		}
	}
	
}
