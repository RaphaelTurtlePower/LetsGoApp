package com.app.letsgo.models;

public class Place {
	String address;
	String placeId;
	
	public Place() {}
	
	public Place(String addr, String id) {
		this.address = addr;
		this.placeId = id;
	}
	
	public String getAddress() {
		return address;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	
	

}
