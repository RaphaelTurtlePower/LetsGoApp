package com.app.letsgo.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LocalEventParcel implements Parcelable {
	LocalEvent ev;
	
	public LocalEventParcel(LocalEvent e){
		ev = e;
	}
	
	public LocalEvent getEvent(){
		return this.ev;
	}
	
	public LocalEventParcel(Parcel in){
		String objectId = in.readString();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("LocalEvent");
		query.include("location");
		query.include("createdBy");
		try {
			ev = (LocalEvent) query.get(objectId);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ev.getObjectId());
		
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public LocalEventParcel createFromParcel(Parcel in){
			return new LocalEventParcel(in);
		}
		public LocalEvent[] newArray(int size){
			return new LocalEvent[size];
		}
	};

	
	
	public static ArrayList<LocalEventParcel> search(android.location.Location location, Activity activity, String query){
		final ArrayList<LocalEventParcel> events = new ArrayList<LocalEventParcel>();
		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		SharedPreferences mSettings = activity.getSharedPreferences("LetsGoSettings", 0);
		
		//pull data from SharedPreferences and default values to the right

		Double maxDistance = Double.valueOf(mSettings.getInt("max_distance", 500));

		Double cost  = Double.valueOf(mSettings.getInt("cost", 1000000000));
		
		//Search criteria looking at the name
		ParseQuery<ParseObject> name = ParseQuery.getQuery("LocalEvent");
		ParseQuery<ParseObject> description = ParseQuery.getQuery("LocalEvent");
		
		//filter by name
		if(query != null){
			name.whereContains("eventName", query);
			description.whereContains("description", query);
		}
		
		//filter by cost
		if(cost != null){
			name.whereLessThanOrEqualTo("cost", cost);
			description.whereLessThanOrEqualTo("cost", cost);
		}
		
		if(location != null){
			ParseQuery<ParseObject> locationQuery = ParseQuery.getQuery("Location");
			ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
			locationQuery.whereWithinMiles("geoPoint",  point, maxDistance);
		
			name.whereMatchesQuery("location", locationQuery);
			description.whereMatchesQuery("location", locationQuery);
		}
			
		queries.add(name);
		queries.add(description);
		ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
		mainQuery.include("location");
		mainQuery.include("createdBy");
		List<ParseObject> objects;
		HashSet<String> ids = new HashSet<String>();
		try {
			objects = mainQuery.find();
			for(int i=0; i<objects.size(); i++){
				LocalEvent ev = (LocalEvent) objects.get(i);
				//remove duplicates
	         	if(!ids.contains(ev.getObjectId())){
	         		events.add(new LocalEventParcel(ev));
	         		ids.add(ev.getObjectId());
	         	}
	         }
		} catch (com.parse.ParseException e) {
			e.printStackTrace();
		}
		 
		 return events;
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
	**/
	
}
