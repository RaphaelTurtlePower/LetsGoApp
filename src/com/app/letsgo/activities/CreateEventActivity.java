package com.app.letsgo.activities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.Location;
import com.example.letsgoapp.R;
import com.parse.ParseUser;

public class CreateEventActivity extends FragmentActivity {
	EditText etEventName;
	EditText etEventType;
	EditText etLocation;
	EditText etStartDate;
	EditText etStartTime;
	EditText etDescription;
	EditText etCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }
 
    public static class DatePickerFragment extends DialogFragment
    	implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
		}
	}
    
    public static class TimePickerFragment extends DialogFragment
    	implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			
			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
			DateFormat.is24HourFormat(getActivity()));
		}
		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
		}
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
    	// event.setCost(etCost.getText().toString());
    	// event.setDescription(etDescription.getText().toString());
    	
    	String[] address = etLocation.getText().toString().split("\\,");
    	if (address != null && address.length > 0) {
        	Location loc = new Location();
	    	loc.setAddressLine1(address[0]);
	    	// loc.setAddressLine2(address[1]);
	    	// loc.setCity(address[1]);
	    	// loc.setState(address[2]);
	    	//loc.setZipCode(address[3]);
	    	event.setLocation(loc);
	    }
    	
    	ParseUser currentUser = ParseUser.getCurrentUser();
    	event.setCreatedBy(currentUser);

    	event.setCost(0);  // default to free
    	event.put("public", true); // default to public
    	event.saveInBackground();
    	
    	// addToCalendar();
    }
    
    /**
     *  Add the newly created event into calendar
     */
    public void addToCalendar() {
    	Intent intent = new Intent(Intent.ACTION_INSERT);
    	intent.setData(CalendarContract.Events.CONTENT_URI);
    	intent.setType("com.android.calendar/events");
    	intent.putExtra(Events.TITLE, etEventName.getText().toString());
    	intent.putExtra(Events.EVENT_LOCATION, "my place");
    	intent.putExtra(Events.DESCRIPTION, etDescription.getText().toString());

    	// Setting dates
    	GregorianCalendar calDate = new GregorianCalendar(2012, 10, 02);
    	intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
    	  calDate.getTimeInMillis());
    	intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
    	  calDate.getTimeInMillis());
    	startActivity(intent);     	
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");	
    }
    
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    
    public void onSaveForLater(View v) {
    	
    }
}
