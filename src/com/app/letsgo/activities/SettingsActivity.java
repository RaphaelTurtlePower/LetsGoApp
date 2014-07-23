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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.letsgo.R;
import com.app.letsgo.fragments.DatePickerFragment;
import com.app.letsgo.fragments.TimePickerFragment;
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
			int cost = mSettings.getInt("cost",  0);
			sbCost.setProgress(cost);
			tvCostValue.setText(String.valueOf(cost));
			etStartDate.setText(mSettings.getString("startDate", "8/18/2014"));
			etStartTime.setText(mSettings.getString("startTime", "4:00 PM"));
			etEndDate.setText(mSettings.getString("endDate", "8/28/2014"));
			etEndTime.setText(mSettings.getString("endTime", "10:00 PM"));
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
