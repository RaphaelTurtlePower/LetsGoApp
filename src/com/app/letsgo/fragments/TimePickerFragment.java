package com.app.letsgo.fragments;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
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

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		showTime(etTime, hourOfDay, minute);
		date.set(GregorianCalendar.HOUR, hourOfDay);
		date.set(GregorianCalendar.MINUTE, minute);
	}
	
	public void showTime(EditText time, int hour, int minute) {
		time.setText(new StringBuilder().append(hour).append(":").append(minute));
	}


}

