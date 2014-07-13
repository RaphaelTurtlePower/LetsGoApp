package com.app.letsgo.helpers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.EditText;

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


}
