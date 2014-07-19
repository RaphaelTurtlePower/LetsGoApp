package com.app.letsgo.models;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.app.letsgo.R;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("LocalEvent")
public class LocalEvent extends ParseObject {
	public static final int SHORT_DESCRIPTION_LENGTH = 140;

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
	SimpleDateFormat sdf_time = new SimpleDateFormat("h:mm a");
	
	public String localEventId; //check this if objectId is null
	
	public LocalEvent() {
		super();
	}
	
	public String getEventName() {
		return getString("eventName");
	}

	public void setEventName(String name) {
		put("eventName", name);
	}
	
	public String getEventType() {
		return getString("eventType");
	}
	
	public void setEventType(String type) {
		put("eventType", type);
	}

	public String getStartDate() {
		return getString("startDate");
	}
	
	public void setStartDate(String startDate) {
		put("startDate", startDate);
	}
	
	public String getStartTime() {
		return getString("startTime");
	}
	
	public void setStartTime(String startTime)  {
		put("startTime", startTime);
	}

	public String getEndDate() {
		return getString("endDate");
	}
	
	public void setEndDate(String endDate) {
		put("endDate", endDate);
	}
	
	public String getEndTime() {
		return getString("endTime");
	}
	
	public void setEndTime(String endTime)  {
		put("endTime", endTime);
	}

	public ParseUser getCreatedBy() {
		return getParseUser("createdBy");
	}
	
	public void setCreatedBy(ParseUser createdBy) {
		put("createdBy", createdBy);
	}
	
	public Location getLocation() {
		return (Location) getParseObject("location");
	}
	
	public void setLocation(Location loc) {
		put("location", loc);
	}
	
	public Number getCost() {
		return getNumber("cost");		
	}
	
	public void setCost(Number cost) {
		put("cost", cost);
	}
	
	public Number getUpCount() {
		return getNumber("upCount");		
	}
	
	public void setUpCount(Number up) {
		put("upCount", up);
	}
	public Number getDownCount() {
		return getNumber("downCount");		
	}
	
	public void setDownCount(Number down) {
		put("downCount", down);
	}

	public String getDescription() {
		return getString("description");
	}

	public void setDescription(String desc) {
		put("description", desc);
	}
	
	public static Date parseDate(String date, String format)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try{
			return formatter.parse(date);
		}catch(ParseException e){
	    	System.out.println("Error parsing Date:" + date);
	    	return null;
		}
	}
	
	public int getMarkerType(){
		return R.drawable.ic_pink_flag;
	}
	
	public LatLng getMapPosition(){
		if(getLocation() ==null){
			return null;
		}
		LatLng mapPosition = new LatLng(getLocation().getGeoPoint().getLatitude(), getLocation().getGeoPoint().getLongitude());
		return mapPosition;
	}
	
	
	public static Address convertAddress(Context context, String address){
		Geocoder coder = new Geocoder(context);
		try {
			List<Address> placeList = coder.getFromLocationName(address, 1);
			if(address == null){
				Log.d("Address from coder returned null", "Check your address");
				return null;
			}
			return placeList.get(0);
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("IOException occurred: Event.getAddress", e.getMessage());
			return null;
		}
	}
	
	public String getItemShortDescription(){
		String descriptionText = getDescription();
		if(descriptionText != null && descriptionText.length() > SHORT_DESCRIPTION_LENGTH){
			descriptionText = descriptionText.substring(0, SHORT_DESCRIPTION_LENGTH) + "...";
		}
		return descriptionText;
	}
	/**
	mSettings = getSharedPreferences("LetsGoSettings", 0);	
	spEventType = (Spinner) findViewById(R.id.spEventType);

	sbCost = (SeekBar) findViewById(R.id.sbCost);
	tvCostValue = (TextView) findViewById(R.id.tvCostValue);		
	
	if (mSettings != null) {
		String type = mSettings.getString("eventType", "missing");			
		int cost = mSettings.getInt("cost",  0);
		sbCost.setProgress(cost);
		tvCostValue.setText(String.valueOf(cost));
		etStartDate.setText(mSettings.getString("startDate", "7/18/2014"));
		etStartTime.setText(mSettings.getString("startTime", "16:00"));
		etEndDate.setText(mSettings.getString("endDate", "7/28/2014"));
		etEndTime.setText(mSettings.getString("endTime", "20:00"));
	}			
	*/
	

}

