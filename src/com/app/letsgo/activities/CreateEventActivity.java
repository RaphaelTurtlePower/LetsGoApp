package com.app.letsgo.activities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filterable;

import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.Location;
import com.example.letsgoapp.R;
import com.parse.ParseUser;

public class CreateEventActivity extends Activity {
	EditText etEventName;
	EditText etEventType;
	EditText etLocation;
	EditText etStartDate;
	EditText etStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }
    
    public void onCreateEvent(View v) {
    	etEventName = (EditText) findViewById(R.id.etEventName);
    	etEventType = (EditText) findViewById(R.id.etEventType);
    	etStartDate = (EditText) findViewById(R.id.etStartDate);
    	etStartTime = (EditText) findViewById(R.id.etStartTime);
    	etLocation = (EditText) findViewById(R.id.etLocation);
    	
    	LocalEvent event = new LocalEvent();
    	event.setEventName(etEventName.getText().toString());
    	event.setEventType(etEventType.getText().toString());
    	event.setStartDate(etStartDate.getText().toString());
    	event.setStartTime(etStartTime.getText().toString());
    	
    	Location loc = new Location();
    	String[] address = etLocation.getText().toString().split("\\,");
    	
    	loc.setAddressLine1(address[0]);
    	// loc.setAddressLine2(address[1]);
    	loc.setCity(address[1]);
    	loc.setState(address[2]);
    	//loc.setZipCode(address[3]);
    	event.setLocation(loc);
    	
    	ParseUser currentUser = ParseUser.getCurrentUser();
    	event.setCreatedBy(currentUser);

    	/*event.put("eventName", "firstEvent");
    	event.put("eventType", "music");
    	event.put("cost",  10);
    	event.put("public", true);*/
    	event.saveInBackground();
    	
    }
    
    private static final String LOG_TAG = "letsgoapp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBGpIbaW1ryQEPa-dSNV7RERqc8Dbj3BVo";

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
 
    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        /*@Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        } */
    } 
    
    public void onSaveForLater(View v) {
    	
    }
}
