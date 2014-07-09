package com.app.letsgo.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.letsgo.R;
import com.app.letsgo.models.Event;

	public class ListEventsAdapter extends ArrayAdapter<Event> {
		
		public ListEventsAdapter(Context context, List<Event> items){
			super(context, 0, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent ){
			Event event = getItem(position);
			View v;
			if(convertView == null){
				LayoutInflater inflator = LayoutInflater.from(getContext());
				v = inflator.inflate(R.layout.map_item, parent, false);
			}else{
				v = convertView;
			}
			
			TextView title = (TextView) v.findViewById(R.id.map_item_title);
			title.setText(event.getName());
			TextView description = (TextView) v.findViewById(R.id.map_item_description);
			description.setText(event.getDescription());
			
			TextView street_address = (TextView) v.findViewById(R.id.map_item_street);
			street_address.setText(event.getAddress().getAddressLine(0));
			TextView city_state = (TextView) v.findViewById(R.id.map_item_city_state);
			city_state.setText(event.getAddress().getPostalCode());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");      


			TextView startDate = (TextView) v.findViewById(R.id.map_item_startDate);
			startDate.setText(sdf.format(event.getStartDate()).toString());
			
			return v;
		}
		
	}

