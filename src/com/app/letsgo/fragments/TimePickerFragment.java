package com.app.letsgo.fragments;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
	implements TimePickerDialog.OnTimeSetListener {
	EditText etTime;
	Calendar date;

	public TimePickerFragment(EditText etTime, Calendar date) {
		this.etTime = etTime;
		this.date = date;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		Log.d("debug", "TimePickerDialog.onCreateDialog(): is24HourFormat = " + 
				DateFormat.is24HourFormat(getActivity()));

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, false);
				// DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		showTime(etTime, hourOfDay, minute);
		date.set(GregorianCalendar.HOUR, hourOfDay);
		date.set(GregorianCalendar.MINUTE, minute);
				
	}
	
	public void showTime(EditText etTime, int hourOfDay, int minute) {
        int hours = hourOfDay;
        int minutes = minute;
        //int hours = hourOfDay;
        
        String timeSet = "";
        if (hours > 12) {
        	hours -= 12;
        	timeSet = "PM";
        } else if (hours == 0) {
        	hours += 12;
        	timeSet = "AM";
        } else if (hours == 12)
        	timeSet = "PM";
        else
        	timeSet = "AM";

        String min = "";
        if (minutes < 10)
        	min = "0" + minutes ;
        else
        	min = String.valueOf(minutes);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
        		.append(min ).append(" ").append(timeSet).toString();
        etTime.setText(aTime);
        // time.setText(new StringBuilder().append(hour).append(":").append(minute));
	}


}

