package com.app.letsgo.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Location")
public class Location extends ParseObject {

	public Location() {
		super();
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
	
}
