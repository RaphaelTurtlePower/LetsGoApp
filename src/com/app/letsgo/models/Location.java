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

	public String getAddress() {
		return getString("address");
	}
	
	public void setAddress(String addr) {
		put("address", addr);
	}
	
	public String getAddress(){
		return getString("address");
	}
	
	public ParseGeoPoint getGeoPoint(){
		return getParseGeoPoint("geoPoint");
	}
	
	public void setGeoPoint(ParseGeoPoint geoPoint) {
		put("geoPoint", geoPoint);
	}
	
	public Double getLatitude(){
		return getDouble("latitude");
	}
	
	public void setLatitude(Double lat) {
		put("latitude", lat);
	}
	
	public Double getLongitude(){
		return getDouble("longitude");
	}
	
	public void setLongitude(Double lng) {
		put("longitude", lng);
	}
	
	@Override
	public String toString() {
		return "Location = " + getAddress() + getLatitude() + getLongitude();
	}
	
	public Location(Parcel in){
		String[] data = new String[3];
		in.readStringArray(data);
		setAddress(data[0]);
		Double lat = new Double(data[1]);
		Double lon = new Double(data[2]);
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
		dest.writeStringArray(new String[] {getAddress(),
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
	
	
}
