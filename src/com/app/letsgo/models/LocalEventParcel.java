package com.app.letsgo.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.GetCallback;
import com.parse.ParseException;
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
	
	
	
	public static ArrayList<LocalEventParcel> getLocalEvents(){
		final ArrayList<LocalEventParcel> events = new ArrayList<LocalEventParcel>();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("LocalEvent");
		query.include("location");
		List<ParseObject> objects;
		try {
			objects = query.find();
			for(int i=0; i<objects.size(); i++){
	         	LocalEvent ev = (LocalEvent) objects.get(i);
	         	events.add(new LocalEventParcel(ev));
	         }
		} catch (com.parse.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return events;
	}

	
	public static ArrayList<LocalEventParcel> search(Activity activity, String query){
		final ArrayList<LocalEventParcel> events = new ArrayList<LocalEventParcel>();
		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		SharedPreferences mSettings = activity.getSharedPreferences("LetsGoSettings", 0);
		
		ParseQuery<ParseObject> name = ParseQuery.getQuery("LocalEvent");
		name.whereContains("eventName", query);
		
		ParseQuery<ParseObject> description = ParseQuery.getQuery("LocalEvent");
		description.whereContains("description", query);

		if(mSettings!=null){
			String date = mSettings.getString("startDate", "7/18/2014");
			
		}
		
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
	         		events.add(new LocalEventParcel(ev));
	         		ids.add(ev.getObjectId());
	         	}
	         }
		} catch (com.parse.ParseException e) {
			e.printStackTrace();
		}
		 
		 return events;
	}
	
	
	
}
