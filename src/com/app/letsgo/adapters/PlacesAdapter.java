package com.app.letsgo.adapters;

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

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.app.letsgo.helpers.Utils;
import com.app.letsgo.models.Place;

public class PlacesAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<Place> resultList;

	public PlacesAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index).getAddress();
	}

	public Place getPlace(int index) {
		return resultList.get(index);
	}
	
	@Override
	public android.widget.Filter getFilter() {
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
	}
	
    private ArrayList<Place> autocomplete(String input) {
        ArrayList<Place> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(Utils.PLACES_API_BASE + 
            		Utils.TYPE_AUTOCOMPLETE + Utils.OUT_JSON);
            sb.append("?key=" + Utils.API_KEY);
            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            Log.e(Utils.LOG_TAG, "Auto complete URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Log.e(Utils.LOG_TAG, "Auto complete result: " + jsonResults);
        } catch (MalformedURLException e) {
            Log.e(Utils.LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(Utils.LOG_TAG, "Error connecting to Places API", e);
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
            resultList = new ArrayList<Place>(predsJsonArray.length());
            Place place = new Place();
            for (int i = 0; i < predsJsonArray.length(); i++) {
            	place.setAddress(predsJsonArray.getJSONObject(i).getString("description"));
            	place.setPlaceId(predsJsonArray.getJSONObject(i).getString("place_id"));
                resultList.add(place);
            }
            
        } catch (JSONException e) {
            Log.e(Utils.LOG_TAG, "Cannot process JSON results", e);
        }
        return resultList;
    }
    
}

