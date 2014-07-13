package com.app.letsgo.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.letsgo.R;
import com.app.letsgo.activities.EventDetailActivity;
import com.app.letsgo.dialogs.MapItemDialog;
import com.app.letsgo.models.LocalEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class BaseMapFragment extends MapFragment implements OnMarkerClickListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	private HashMap<Marker, LocalEvent> markerMap = new HashMap<Marker, LocalEvent>();
	private LocationClient mLocationClient;
	
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	public static BaseMapFragment newInstance(){
		BaseMapFragment mapFragment = new BaseMapFragment();
		return mapFragment;
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mLocationClient = new LocationClient(getActivity(), this, this);
		
	}
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	public void initialize(){
		// loadEvents(LocalEvent.getLocalEvents());
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		loadMap();
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
	
	public void loadMap(){
		if (getMap() != null) {
			Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
			getMap().setMyLocationEnabled(true);
		} else {
			Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
		}
	}
	
	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(getActivity(),
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(),
					"Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
		}
	}


	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Location location = mLocationClient.getLastLocation();
		if (location != null) {
			Toast.makeText(getActivity(), "GPS location was found!", Toast.LENGTH_SHORT).show();
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
			if(getMap()!=null){
				getMap().animateCamera(cameraUpdate);
			}
		} else {
			Toast.makeText(getActivity(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
		}
	}


	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}

	
	public void connect(){
		mLocationClient.connect();
	}
	
	public void disconnect(){
		mLocationClient.disconnect();
	}
}
