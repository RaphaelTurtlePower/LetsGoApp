package com.app.letsgo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.Location;
import com.example.letsgoapp.R;
import com.parse.ParseUser;

public class CreateEventActivity extends Activity {
	EditText etEventName;
	EditText etEventType;
	EditText etLocation;
	EditText etStartDate;
	EditText etStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }
    
    public void onCreateEvent(View v) {
    	etEventName = (EditText) findViewById(R.id.etEventName);
    	etEventType = (EditText) findViewById(R.id.etEventType);
    	etStartDate = (EditText) findViewById(R.id.etStartDate);
    	etStartTime = (EditText) findViewById(R.id.etStartTime);
    	etLocation = (EditText) findViewById(R.id.etLocation);
    	
    	LocalEvent event = new LocalEvent();
    	event.setEventName(etEventName.getText().toString());
    	event.setEventType(etEventType.getText().toString());
    	event.setStartDate(etStartDate.getText().toString());
    	event.setStartTime(etStartTime.getText().toString());
    	
    	Location loc = new Location();
    	String[] address = etLocation.getText().toString().split("\\,");
    	
    	loc.setAddressLine1(address[0]);
    	// loc.setAddressLine2(address[1]);
    	loc.setCity(address[1]);
    	loc.setState(address[2]);
    	//loc.setZipCode(address[3]);
    	event.setLocation(loc);
    	
    	ParseUser currentUser = ParseUser.getCurrentUser();
    	event.setCreatedBy(currentUser);

    	/*event.put("eventName", "firstEvent");
    	event.put("eventType", "music");
    	event.put("cost",  10);
    	event.put("public", true);*/
    	event.saveInBackground();
    	
    }
    
    public void onSaveForLater(View v) {
    	
    }
}
