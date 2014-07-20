package com.app.letsgo.models;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.app.letsgo.R;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("LocalEvent")
public class LocalEvent extends ParseObject implements Parcelable {
	public static final int SHORT_DESCRIPTION_LENGTH = 140;

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
	SimpleDateFormat sdf_time = new SimpleDateFormat("h:mm a");
	
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
	
	
	public LocalEvent(Parcel in){
		String[] data = new String[8];
		in.readStringArray(data);
		setEventName(data[0]);
		setEventType(data[1]);
		setStartDate(data[2]);
		setEndDate(data[3]);
		setStartTime(data[4]);
		setEndTime(data[5]);
		setCost(Double.parseDouble(data[6]));
		setDescription(data[7]);
		Location location = (Location) Location.CREATOR.createFromParcel(in);
		setLocation(location);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {getEventName(),
				getEventType(),
				getStartDate(),
				getEndDate(),
				getStartTime(),
				getEndTime(),
				getCost().toString(),
				getDescription()});
		getLocation().writeToParcel(dest, flags);
		
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public LocalEvent createFromParcel(Parcel in){
			return new LocalEvent(in);
		}
		public LocalEvent[] newArray(int size){
			return new LocalEvent[size];
		}
	};
	
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
		return R.drawable.ic_map_marker;
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
	
	public static ArrayList<LocalEvent> getLocalEvents(){
		final ArrayList<LocalEvent> events = new ArrayList<LocalEvent>();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("LocalEvent");
		query.include("location");
		List<ParseObject> objects;
		try {
			objects = query.find();
			for(int i=0; i<objects.size(); i++){
	         	LocalEvent ev = (LocalEvent) objects.get(i);
	         	
	         	events.add(ev);
	         }
		} catch (com.parse.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return events;
	}
	
	public String getItemShortDescription(){
		String descriptionText = getDescription();
		if(descriptionText != null && descriptionText.length() > SHORT_DESCRIPTION_LENGTH){
			descriptionText = descriptionText.substring(0, SHORT_DESCRIPTION_LENGTH) + "...";
		}
		return descriptionText;
	}
	
	public static ArrayList<LocalEvent> search(String query){
		final ArrayList<LocalEvent> events = new ArrayList<LocalEvent>();
		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		
		ParseQuery<ParseObject> name = ParseQuery.getQuery("LocalEvent");
		name.whereContains("eventName", query);
		
		ParseQuery<ParseObject> description = ParseQuery.getQuery("LocalEvent");
		description.whereContains("description", query);
		queries.add(name);
		queries.add(description);
		 
		ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
		mainQuery.include("location");
		List<ParseObject> objects;
		HashSet<String> ids = new HashSet<String>();
		try {
			objects = mainQuery.find();
			for(int i=0; i<objects.size(); i++){
				LocalEvent ev = (LocalEvent) objects.get(i);
				//remove duplicates
	         	if(!ids.contains(ev.getObjectId())){
	         		events.add(ev);
	         		ids.add(ev.getObjectId());
	         	}
	         }
		} catch (com.parse.ParseException e) {
			e.printStackTrace();
		}
		 
		 return events;
	}
	
}

