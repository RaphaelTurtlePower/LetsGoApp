package com.app.letsgo.activities;

import java.util.GregorianCalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.letsgo.R;
import com.app.letsgo.helpers.Utils;
import com.app.letsgo.models.LocalEvent;

public class SettingsActivity extends FragmentActivity {

	SharedPreferences mSettings;
	Spinner spEventType;
	
	TextView tvDistanceValue;
	SeekBar sbDistance;
	TextView tvRadius;
	EditText etCost;	
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

		etCost = (EditText) findViewById(R.id.etCost);
		// sbCost = (SeekBar) findViewById(R.id.sbCost);
		// tvCostValue = (TextView) findViewById(R.id.tvCostValue);		
		
		sbDistance = (SeekBar) findViewById(R.id.sbDistance);
		tvRadius = (TextView) findViewById(R.id.tvRadius);
		
		if (mSettings != null) {
			int distance = mSettings.getInt("max_distance",  30);
			sbDistance.setProgress(distance);
			tvRadius.setText(String.valueOf(distance));
			etStartDate.setText(mSettings.getString("startDate", "8/18/2014"));
			etStartTime.setText(mSettings.getString("startTime", "4:00 PM"));
			etEndDate.setText(mSettings.getString("endDate", "8/28/2014"));
			etEndTime.setText(mSettings.getString("endTime", "10:00 PM"));
			etCost.setText(String.valueOf(mSettings.getInt("cost", 60)));
		}				
		// setupCostListener();
		setupDistanceListener();
		setupEventType();
	}

	public void setupDistanceListener() {
		sbDistance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvRadius.setText(String.valueOf(progress));	
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
		
		editor.putInt("max_distance", sbDistance.getProgress());
		editor.putInt("eventTypePos", spEventType.getSelectedItemPosition());
		editor.putInt("cost", Integer.valueOf(etCost.getText().toString()));
		Log.d("debug", "SettingsActivity.onSave(): distance = " + sbDistance.getProgress());
		editor.commit();
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}
	
	public void cancelSettings(View v) {
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

	public void saveDates(LocalEvent event) {
		event.setStartDate("8/18/14");
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

}
