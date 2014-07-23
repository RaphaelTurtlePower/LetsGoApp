package com.app.letsgo.activities;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.letsgo.R;
import com.app.letsgo.adapters.PlacesAdapter;
import com.app.letsgo.helpers.Utils;
import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.LocalEventParcel;
import com.app.letsgo.models.Location;
import com.app.letsgo.models.Place;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateEventActivity extends FragmentActivity {
	EditText etEventName;
	Spinner spEventType;
	EditText etDescription;
	SeekBar sbCost;
	TextView tvCostValue;
	EditText etStartDate;
	EditText etStartTime;
	EditText etEndDate;
	EditText etEndTime;
	GregorianCalendar startDate;
	GregorianCalendar endDate;

	AutoCompleteTextView etLocation;
	GeoCodeAsyncTask geoAsyncTask;
	LocalEvent event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_create_event);

		// make sure to call super after setting the content view
		super.onCreate(savedInstanceState);

		etEventName = (EditText) findViewById(R.id.etEventName);
		spEventType = (Spinner) findViewById(R.id.spEventType);
		etDescription = (EditText) findViewById(R.id.etDescription);
		sbCost = (SeekBar) findViewById(R.id.sbCost);
		tvCostValue = (TextView) findViewById(R.id.tvCostValue);
		etStartDate = (EditText) findViewById(R.id.etStartDate);
		etStartTime = (EditText) findViewById(R.id.etStartTime);
		etEndDate = (EditText) findViewById(R.id.etEndDate);
		etEndTime = (EditText) findViewById(R.id.etEndTime);

		startDate = new GregorianCalendar();
		endDate = new GregorianCalendar();	   	

		etLocation = (AutoCompleteTextView) findViewById(R.id.etLocation);
		Utils.setupEventType(this, spEventType);

		setLocation();
		setupCostListener();
		// setupDatePickerListener(etStartDate);
	}

	/** 
	 * call back to get location info from places api
	 */
	private void setLocation() {
		final PlacesAdapter adapter = new PlacesAdapter(this, R.layout.place_list);
		etLocation.setAdapter(adapter);

		etLocation.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Place place = adapter.getPlace(position);
				geoAsyncTask = new GeoCodeAsyncTask();
				geoAsyncTask.execute(place);
				Log.d(Utils.LOG_TAG,  "CreateEvent.setLocation(): " + place.getAddress());
			}
		}); 
	}

	public static class GeoCodeAsyncTask extends AsyncTask<Place, Integer, Location> {
		@Override
		protected Location doInBackground(Place... params) {
			Place place = params[0];
			return Utils.getGeocode(place);
		}

		@Override
		protected void onPostExecute(Location location) {
			Log.d(Utils.LOG_TAG, "Location: " + location.toString());
		}
	}

	public Activity getContext() {
		return this;
	}
	
	public void setupDatePickerListener(EditText etDate) {
		etDate.setOnTouchListener(new OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            // show calendar date picker
	            showStartDatePickerDialog(v);
	            Log.d(Utils.LOG_TAG, "Inside On touch");
	            return false;
	        }
	    });
	}
	
	public void onCreateEvent(View v) {

		// validation
		/*if (!passedValidation()) {
			return;
		}*/
		if (Utils.isNull(etLocation)) {
			Utils.showValidationWarning(this, "Please enter event venue.");
			return;
		}
		
		event = new LocalEvent();
		
		// for now all fields are set default values if the field is not entered
		event.setEventName("party");
		if (!Utils.isNull(etEventName)) {
			event.setEventName(etEventName.getText().toString());
		}
		event.setEventType("music");
		if (spEventType != null) {
			event.setEventType(spEventType.getSelectedItem().toString());
		}

		if (sbCost != null) {
			try {
				Integer cost = sbCost.getProgress();
				Log.d("debug", "saveEvent(): cost = " + cost);
				event.setCost(cost);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				event.setCost(0);
			}
		} else event.setCost(0); 

		// set description if it's entered
		event.setDescription("More details...");
		if (!Utils.isNull(etDescription)) {
			event.setDescription(etDescription.getText().toString());
		}

		ParseUser currentUser = ParseUser.getCurrentUser();
		event.setCreatedBy(currentUser);
		saveDates();

		if (geoAsyncTask != null) {
			try {
				event.setLocation(geoAsyncTask.get());
				Log.d(Utils.LOG_TAG,  "Location:" + event.getLocation().toString());
			} catch (Exception e) {
				Log.d(Utils.LOG_TAG,  "Failed to get location");
			}
		}

		// default values		
		event.setUpCount(0);
		event.setDownCount(0);
		event.put("public", true); // default to public
		
		event.saveInBackground(new SaveCallback(){
			@Override
			public void done(ParseException e) {
				if(e == null){
					Log.d("OBJECT_SAVE", "Event successfully saved.");

					Intent data = new Intent(getContext(), EventDetailActivity.class);
					LocalEventParcel parcel = new LocalEventParcel(event);
					data.putExtra("event", parcel);
					startActivity(data);
				} else {
					Log.e("OBJECT NOT SAVED", "Event not successfully saved");
				}				
			}			
		});		
	}

	/**
	 *  Returns t/o Map view when click on Cancel button
	 * @param v
	 */
	public void onCancel(View v) {
		finish();  	
	}
	
	public void showStartDatePickerDialog(View v) {
		Utils.showDatePickerDialog(this, etStartDate, startDate);
	}

	public void showStartTimePickerDialog(View v) {
		Utils.showTimePickerDialog(this, etStartTime, startDate);
	}

	// show datepicker for end date
	public void showEndDatePickerDialog(View v) {
		Utils.showDatePickerDialog(this, etEndDate, endDate);
	}

	public void showEndTimePickerDialog(View v) {
		Utils.showTimePickerDialog(this, etEndTime, endDate);
	} 

	public void setupCostListener() {
		sbCost.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvCostValue.setText(String.valueOf(progress));	
			}
		});
	}
	public void saveDates() {
		event.setStartDate("8/12/14");
		if (!Utils.isNull(etStartDate)) {
			event.setStartDate(etStartDate.getText().toString());
		}
		event.setStartTime("4:00 PM");
		if (!Utils.isNull(etStartTime)) {
			event.setStartTime(etStartTime.getText().toString());
		}
		event.setEndDate("8/18/14");
		if (!Utils.isNull(etEndDate)) {
			event.setEndDate(etEndDate.getText().toString());
		}
		event.setEndTime("10:00 PM");
		if (!Utils.isNull(etEndTime)) {
			event.setEndTime(etEndTime.getText().toString());
		}
	}

	boolean passedValidation() {
		StringBuilder toastMsg = new StringBuilder("Please enter event");
		int validationBits = 0;
		// eventType X10000, eventName X01000, startDate X00100, startTime X00010, locaiton X00001
		if (Utils.isNull(etEventName)) {
			validationBits |= 16; // 0b10000
		}
		if (Utils.isNull(etStartDate)) {
			validationBits |= 4; //0b00100;
		}
		if (Utils.isNull(etLocation)) {
			validationBits |= 1; //0b00100;
		}
		
		if (validationBits > 0) {
			switch (validationBits) {
			case 1: // 0x00001:
				toastMsg.append(" venue").toString();
			case 4: // 0x00100
				toastMsg.append(" start date").toString();
			case 16: // 0x10000
				toastMsg.append(" name").toString();
			}
			Utils.showValidationWarning(this, toastMsg.append(".").toString());
			return false;
		}
		return true;	
	}

}
