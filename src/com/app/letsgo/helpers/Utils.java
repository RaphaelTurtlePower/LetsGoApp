package com.app.letsgo.helpers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.letsgo.R;
import com.app.letsgo.fragments.DatePickerFragment;
import com.app.letsgo.fragments.TimePickerFragment;
import com.app.letsgo.models.Location;
import com.app.letsgo.models.Place;
import com.parse.ParseGeoPoint;

public class Utils {
    public static final String LOG_TAG = "LetsGoApp";

    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String TYPE_DETAIL = "/details";
    public static final String OUT_JSON = "/json";

    public static final String API_KEY = "AIzaSyDEDV5kZcnb2FkfeXhYq028WU_Y5gYckw0";
    // private static final String API_KEY = "AIzaSyCH4lqj6EY7zSVf0cLRsiKs947zuS0XrV8";
	
	public static boolean isNull(EditText et) {
		if (et != null) {
			if (et.getText() != null)  {
				String s = et.getText().toString();
				if (s != null && !s.isEmpty()) return false;
			} else return true;
		}
		return true;
	}
				
    public static Location getGeocode(Place place) {
    	
        HttpURLConnection conn = null;
        Location loc = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAIL + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&placeid=" + URLEncoder.encode(place.getPlaceId(), "utf8"));

            URL url = new URL(sb.toString());
            Log.e(LOG_TAG, "Place Detail URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Log.e(LOG_TAG, "Got Places detail result ");
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return loc;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return loc;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject result = new JSONObject(jsonResults.toString());

            // Extract the Place descriptions from the results
            loc = new Location();
            JSONObject jsonObj = result.getJSONObject("result");
            loc.setAddress(jsonObj.getString("formatted_address"));
            Double lat = jsonObj.getJSONObject("geometry").getJSONObject("location").getDouble("lat");           		
            Double lng = jsonObj.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            ParseGeoPoint geoPoint = new ParseGeoPoint(lat, lng);
            loc.setGeoPoint(geoPoint);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
        return loc;
    } 

	/**
	 *  Add the newly created event into calendar
	 */
	public static void addToCalendar(Activity activity, String eventName, String location, String description, 
			String startDate, String endDate) {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setData(CalendarContract.Events.CONTENT_URI);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, eventName);
		intent.putExtra(Events.EVENT_LOCATION, location);
		if (description != null) {
			intent.putExtra(Events.DESCRIPTION, description);
		}

		// Setting dates    	
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate);
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate);
		activity.startActivity(intent);     	
	}
	
	public static void setupEventType(Activity activity, Spinner spEventType) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
				R.array.event_types, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spEventType.setAdapter(adapter);

	}

	public static void showDatePickerDialog(FragmentActivity activity, EditText etDate, Calendar date) {
		DialogFragment newFragment = new DatePickerFragment(etDate, date);
		newFragment.show(activity.getSupportFragmentManager(), "datePicker");		
	}
	
	public static void showTimePickerDialog(FragmentActivity activity, EditText etTime, Calendar date) {
		DialogFragment newFragment = new TimePickerFragment(etTime, date);
		newFragment.show(activity.getSupportFragmentManager(), "timePicker");	
	}

}
