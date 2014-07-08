package com.app.letsgo.models;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.app.letsgo.R;
import com.google.android.gms.maps.model.LatLng;

public class Event implements Parcelable {
	private String name;
	private Address address;
	private Date startDate;
	private String description;
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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
	
	public Event(Context context, String addr, String name, String description, Date startDate){
		this.address = Event.convertAddress(context, addr);
		this.name = name;
		this.description = description;
		this.startDate = startDate;
	}
	
	public Event(Address addr, String name, String description, Date startDate){
		this.address = addr;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LatLng getMapPosition(){
		if(address ==null){
			return null;
		}
		LatLng mapPosition = new LatLng(getAddress().getLatitude(), getAddress().getLongitude());
		return mapPosition;
	}
	
	public int getMarkerType(){
		return R.drawable.ic_map_marker;
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
	
	public Event(Parcel in){
		String[] data = new String[3];
		in.readStringArray(data);
		this.name = data[0];
		this.startDate = parseDate(data[1], "dd-MMM-yy");
		this.description = data[2];
		this.address = Address.CREATOR.createFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		dest.writeStringArray(new String[] {this.name,
				sdf.format(this.startDate),
				this.description});
		this.address.writeToParcel(dest, flags);
		
		
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Event createFromParcel(Parcel in){
			return new Event(in);
		}
		public Event[] newArray(int size){
			return new Event[size];
		}
	};
}
