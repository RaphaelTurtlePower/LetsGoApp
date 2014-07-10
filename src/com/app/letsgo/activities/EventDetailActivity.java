package com.app.letsgo.activities;

import java.text.SimpleDateFormat;

import com.app.letsgo.R;
import com.app.letsgo.R.layout;
import com.app.letsgo.models.LocalEvent;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class EventDetailActivity extends Activity {

	LocalEvent event;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_item);
		LocalEvent event = (LocalEvent) getIntent().getExtras().getParcelable("event");
		
		TextView title = (TextView) findViewById(R.id.map_item_title);
		title.setText(event.getEventName());
		TextView description = (TextView) findViewById(R.id.map_item_description);
		description.setText(event.getDescription());
		
		TextView street_address = (TextView) findViewById(R.id.map_item_street);
		street_address.setText(event.getLocation().getAddressLine1());
		TextView city_state = (TextView) findViewById(R.id.map_item_city_state);
		city_state.setText(event.getLocation().getCityAndState());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");      


		TextView startDate = (TextView) findViewById(R.id.map_item_startDate);
		startDate.setText(sdf.format(event.getStartDate()).toString());
		
	}
}
