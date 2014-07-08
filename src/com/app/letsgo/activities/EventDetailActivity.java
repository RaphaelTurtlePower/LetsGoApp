package com.app.letsgo.activities;

import java.text.SimpleDateFormat;

import com.app.letsgo.R;
import com.app.letsgo.R.layout;
import com.app.letsgo.models.Event;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class EventDetailActivity extends Activity {

	Event event;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_item);
		Event event = (Event) getIntent().getExtras().getParcelable("event");
		
		TextView title = (TextView) findViewById(R.id.map_item_title);
		title.setText(event.getName());
		TextView description = (TextView) findViewById(R.id.map_item_description);
		description.setText(event.getDescription());
		
		TextView street_address = (TextView) findViewById(R.id.map_item_street);
		street_address.setText(event.getAddress().getAddressLine(0));
		TextView city_state = (TextView) findViewById(R.id.map_item_city_state);
		city_state.setText(event.getAddress().getPostalCode());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");      


		TextView startDate = (TextView) findViewById(R.id.map_item_startDate);
		startDate.setText(sdf.format(event.getStartDate()).toString());
		
	}
}
