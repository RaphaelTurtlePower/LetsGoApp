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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.letsgo.R;
import com.app.letsgo.adapters.PlacesAdapter;
import com.app.letsgo.helpers.Utils;
import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.Location;
import com.parse.ParseUser;

public class CreateEventActivity extends FragmentActivity {
	EditText etEventName;
	EditText etEventType;
	EditText etDescription;
	EditText etCost;
	
	static EditText etStartDate;
	static EditText etStartTime;
	static EditText etEndDate;
	static EditText etEndTime;
	static GregorianCalendar startDate;
	static GregorianCalendar endDate;
	AutoCompleteTextView etLocation;
	String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        
    	etEventName = (EditText) findViewById(R.id.etEventName);
    	etEventType = (EditText) findViewById(R.id.etEventType);
    	etDescription = (EditText) findViewById(R.id.etDescription);
    	etCost = (EditText) findViewById(R.id.etCost);
    	
    	etStartDate = (EditText) findViewById(R.id.etStartDate);
    	etStartTime = (EditText) findViewById(R.id.etStartTime);
    	etEndDate = (EditText) findViewById(R.id.etEndDate);
    	etEndTime = (EditText) findViewById(R.id.etEndTime);
    	etLocation = (AutoCompleteTextView) findViewById(R.id.etLocation);
        
	   	startDate = new GregorianCalendar();
	   	endDate = new GregorianCalendar();
	   	
	   	setLocation();
    }

    private void setLocation() {
    	etLocation.setAdapter(new PlacesAdapter(this, R.layout.place_list));
        etLocation.setOnItemClickListener(new OnItemClickListener() {

        	@Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String str = (String) adapterView.getItemAtPosition(position);
                etLocation.setText(str);
                Toast.makeText(CreateEventActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });

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
		
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			showDate(etStartDate, year, month, day);
			startDate.set(GregorianCalendar.YEAR, year);
			startDate.set(GregorianCalendar.MONTH, month);
			startDate.set(GregorianCalendar.DAY_OF_MONTH, day);
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
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			showTime(etStartTime, hourOfDay, minute);
			startDate.set(GregorianCalendar.HOUR, hourOfDay);
			startDate.set(GregorianCalendar.MINUTE, minute);
		}
    }
    
    public static void showDate(EditText date, int year, int month, int day) {
        date.setText(new StringBuilder().append(month + 1)
        		.append("/").append(day).append("/").append(year));
    }
    
    public static void showTime(EditText time, int hour, int minute) {
    	time.setText(new StringBuilder().append(hour).append(":").append(minute));
    }
    
    public void onCreateEvent(View v) {
    	
    	LocalEvent event = new LocalEvent();
    	event.setEventName(etEventName.getText().toString());
    	event.setEventType(etEventType.getText().toString());
    	event.setStartDate(etStartDate.getText().toString());
    	event.setStartTime(etStartTime.getText().toString());
    	if (!Utils.isNull(etCost)) {
    		try {
    			Double cost = Double.parseDouble(etCost.getText().toString());
    			event.setCost(cost);
    		} catch (NumberFormatException e) {
    			e.printStackTrace();
    			event.setCost(0);
    		}
    	} else event.setCost(0);
    	
    	// only set description if it's not entered
    	if (!Utils.isNull(etDescription)) {
    		event.setDescription(etDescription.getText().toString());
    	}
    	
    	if (!Utils.isNull(etLocation)) {
        	address = etLocation.getText().toString();
        	Location loc = new Location();
	    	loc.setAddress(address);
	    	// TODO: set latitude and longitude as well
	    	event.setLocation(loc);
	    }
    	
    	ParseUser currentUser = ParseUser.getCurrentUser();
    	event.setCreatedBy(currentUser);

    	event.setCost(0);  // default to free
    	event.put("public", true); // default to public
    	event.saveInBackground();
    	
    	addToCalendar();
    }
    
    /**
     *  Add the newly created event into calendar
     */
    public void addToCalendar() {
    	Intent intent = new Intent(Intent.ACTION_INSERT);
    	intent.setData(CalendarContract.Events.CONTENT_URI);
    	intent.setType("vnd.android.cursor.item/event");
    	intent.putExtra(Events.TITLE, etEventName.getText().toString());
    	intent.putExtra(Events.EVENT_LOCATION, address);
    	if (!Utils.isNull(etDescription)) {
    		intent.putExtra(Events.DESCRIPTION, etDescription.getText().toString());
    	}

    	// Setting dates    	
    	intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate);
    	intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate);
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
