package com.app.letsgo.activities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.letsgo.R;
import com.app.letsgo.helpers.Utils;
import com.app.letsgo.models.LocalEvent;

public class SettingsActivity extends FragmentActivity {

	SharedPreferences mSettings;
	Spinner spEventType;
	SeekBar sbCost;
	TextView tvCostValue;
	EditText etStartDate;
	EditText etStartTime;
	EditText etEndDate;
	EditText etEndTime;
	GregorianCalendar startDate;
	GregorianCalendar endDate;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
    	etStartDate = (EditText) findViewById(R.id.etStartDate);
    	etStartTime = (EditText) findViewById(R.id.etStartTime);
    	etEndDate = (EditText) findViewById(R.id.etEndDate);
    	etEndTime = (EditText) findViewById(R.id.etEndTime);
        
	   	startDate = new GregorianCalendar();
	   	endDate = new GregorianCalendar();	   	

		mSettings = getSharedPreferences("LetsGoSettings", 0);	
		spEventType = (Spinner) findViewById(R.id.spEventType);

		sbCost = (SeekBar) findViewById(R.id.sbCost);
		tvCostValue = (TextView) findViewById(R.id.tvCostValue);		
		
		if (mSettings != null) {
			String type = mSettings.getString("eventType", "missing");			
			int cost = mSettings.getInt("cost",  0);
			sbCost.setProgress(cost);
			tvCostValue.setText(String.valueOf(cost));
			etStartDate.setText(mSettings.getString("startDate", "7/18/2014"));
			etStartTime.setText(mSettings.getString("startTime", "16:00"));
			etEndDate.setText(mSettings.getString("endDate", "7/28/2014"));
			etEndTime.setText(mSettings.getString("endTime", "20:00"));
		}				
		setupCostListener();
		setupEventType();
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
	
	public void setupEventType() {
		Utils.setupEventType(this,  spEventType);

		int pos = mSettings.getInt("eventTypePos", 0);
		spEventType.setSelection(pos);
	}

	public void saveSettings(View v) {
		
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putString("startDate", etStartDate.getText().toString());
		editor.putString("startTime", etStartTime.getText().toString());
		editor.putString("startDate", etStartDate.getText().toString());
		editor.putString("startTime", etStartTime.getText().toString());
		editor.putInt("cost", sbCost.getProgress());
		editor.putInt("eventTypePos", spEventType.getSelectedItemPosition());
		Log.d("debug", "SettingsActivity.onSave(): cost = " + sbCost.getProgress());
		editor.commit();
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}
	
	public void cancelSettings(View v) {
		finish();
	}

	public class DatePickerFragment extends DialogFragment
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

	public class TimePickerFragment extends DialogFragment
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

	// TODO: need to refactor this!
	public class EndDatePickerFragment extends DialogFragment
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
			showDate(etEndDate, year, month, day);
			endDate.set(GregorianCalendar.YEAR, year);
			endDate.set(GregorianCalendar.MONTH, month);
			endDate.set(GregorianCalendar.DAY_OF_MONTH, day);
		}
	}

	// TODO: need to refactor this!
	public class EndTimePickerFragment extends DialogFragment
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
			showTime(etEndTime, hourOfDay, minute);
			endDate.set(GregorianCalendar.HOUR, hourOfDay);
			endDate.set(GregorianCalendar.MINUTE, minute);
		}
	}

	public void showDate(EditText date, int year, int month, int day) {
		date.setText(new StringBuilder().append(month + 1)
				.append("/").append(day).append("/").append(year));
	}

	public void showTime(EditText time, int hour, int minute) {
		time.setText(new StringBuilder().append(hour).append(":").append(minute));
	}

	public void showStartDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");	
	}

	public void showStartTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	// TODO: refactor this
	public void showEndDatePickerDialog(View v) {
		DialogFragment newFragment = new EndDatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");	
	}

	public void showEndTimePickerDialog(View v) {
		DialogFragment newFragment = new EndTimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	} 

	public void saveDates(LocalEvent event) {
		event.setStartDate("7/18/14");
		if (!Utils.isNull(etStartDate)) {
			event.setStartDate(etStartDate.getText().toString());
		}
		event.setStartTime("16:00");
		if (!Utils.isNull(etStartTime)) {
			event.setStartTime(etStartTime.getText().toString());
		}
		event.setEndDate("7/18/14");
		if (!Utils.isNull(etEndDate)) {
			event.setEndDate(etEndDate.getText().toString());
		}
		event.setEndTime("22:00");
		if (!Utils.isNull(etEndTime)) {
			event.setEndTime(etEndTime.getText().toString());
		}
	}

}
