package com.app.letsgo.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("LocalEvent")
public class LocalEvent extends ParseObject {
	
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
	
	public Date getStartDate() {
		return getDate("startDate");
	}
	
	public void setStartDate(String startDate) {
		put("startDate", startDate);
	}
	
	public Date getStartTime() {
		return getDate("startTime");
	}
	
	public void setStartTime(String startTime)  {
		put("startTime", startTime);
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
	
	public String getDescription() {
		return getString("description");
	}

	public void setDescription(String desc) {
		put("description", desc);
	}
	
}

