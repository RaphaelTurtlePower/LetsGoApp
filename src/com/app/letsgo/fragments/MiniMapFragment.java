package com.app.letsgo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.letsgo.R;
import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.LocalEventParcel;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MiniMapFragment extends MapFragment{
	private LocalEventParcel eventParcel;
	private Marker marker;
	
	public static MiniMapFragment newInstance(LocalEventParcel e){
		MiniMapFragment mapFragment = new MiniMapFragment();
		Bundle args = new Bundle();
		args.putParcelable("event", e);
		mapFragment.setArguments(args);
		return mapFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		eventParcel = args.getParcelable("event");
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return super.onCreateView(inflater, container, savedInstanceState);
	}
			
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Marker myMarker = getMap().addMarker( new MarkerOptions()
	    .position(eventParcel.getEvent().getMapPosition())					 								    
	   .icon(BitmapDescriptorFactory.fromResource(eventParcel.getEvent().getMarkerType())));
		
		getMap().setInfoWindowAdapter(new InfoWindowAdapter(){

			@Override
			public View getInfoContents(Marker marker) {
				LocalEvent event = eventParcel.getEvent();
				LayoutInflater mInflater = getActivity().getLayoutInflater();
				View v = mInflater.inflate(R.layout.mini_map_item, null);
			

				TextView street_address = (TextView) v.findViewById(R.id.mini_map_item_street);
				street_address.setText(event.getLocation().getAddress());
	
				/*	TextView title = (TextView) v.findViewById(R.id.mini_map_item_title);
				title.setText(event.getEventName());
					
				TextView startDate = (TextView) v.findViewById(R.id.mini_map_item_startDate);
				startDate.setText(event.getStartDate()); */
				return v;
			}

			@Override
			public View getInfoWindow(Marker marker) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		updateCamera(eventParcel.getEvent().getMapPosition());
		myMarker.showInfoWindow();
	}
	

	public void updateCamera(LatLng position){
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 14);
		if(getMap()!=null){
			getMap().animateCamera(cameraUpdate);
		}
	}
	
}
