package com.app.letsgo.models;

import java.text.SimpleDateFormat;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Location")
public class Location extends ParseObject implements Parcelable{

	public Location() {
		super();
	}

	public void setAddress(String addr) {
		put("address", addr);
	}
	
	public String getAddressLine1(){
		return getString("addressLine1");
	}
	
	public String getAddressLine2(){
		return getString("addressLine2");
	}
	
	public String getCity(){
		return getString("city");
	}
	
	public String getState(){
		return getString("state");
	}
	
	public String getZipCode(){
		return getString("zipCode");
	}
	
	public ParseGeoPoint getGeoPoint(){
		return getParseGeoPoint("geoPoint");
	}
	
	public void setAddressLine1(String line1) {
		put("addressLine1", line1);
	}
	
	public void setAddressLine2(String line2) {
		put("addressLine2", line2);
	}
	
	public void setCity(String city) {
		put("city", city);
	}
	
	public void setState(String state) {
		put("state", state);
	}
	
	public void setZipCode(String zip) {
		put("zipCode", zip);
	}
	
	public void setGeoPoint(ParseGeoPoint geoPoint) {
		put("geoPoint", geoPoint);
	}
	
	
	
	public Location(Parcel in){
		String[] data = new String[6];
		in.readStringArray(data);
		setAddressLine1(data[0]);
	//	setAddressLine2(data[1]);
		setCity(data[1]);
		setState(data[2]);
		setZipCode(data[3]);
		Double lat = new Double(data[4]);
		Double lon = new Double(data[5]);
		setGeoPoint(new ParseGeoPoint(lat, lon));
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		dest.writeStringArray(new String[] {getAddressLine1(),
		//		getAddressLine2(),
				getCity(),
				getState(),
				getZipCode(),
				new Double(getGeoPoint().getLatitude()).toString(),
				new Double(getGeoPoint().getLongitude()).toString()});
				
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Location createFromParcel(Parcel in){
			return new Location(in);
		}
		public Location[] newArray(int size){
			return new Location[size];
		}
	};
	
	public String getCityAndState(){
		return getCity() + "," + getState();
	}
	
}
