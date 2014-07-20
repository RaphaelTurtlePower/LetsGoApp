package com.app.letsgo.fragments;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
///////minecraft is awesome
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	EditText etDate;
	Calendar date;

	public DatePickerFragment(EditText etDate, Calendar date) {
		this.etDate = etDate;
		this.date = date;
	}
	
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
		showDate(etDate, year, month, day);
		date.set(GregorianCalendar.YEAR, year);
		date.set(GregorianCalendar.MONTH, month);
		date.set(GregorianCalendar.DAY_OF_MONTH, day);
	}	
	
	public void showDate(EditText dateText, int year, int month, int day) {
		dateText.setText(new StringBuilder().append(month + 1)
				.append("/").append(day).append("/").append(year));
	}

}
//minecraft is awesome