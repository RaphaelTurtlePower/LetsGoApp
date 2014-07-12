package com.app.letsgo.activities;

import java.text.SimpleDateFormat;

import com.app.letsgo.R;
import com.app.letsgo.R.layout;
import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.Location;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class EventDetailActivity extends Activity {
	
	private String eventName;
	private String eventType;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String createdBy;
	private String location;
	private Number cost;
	private String description;

	private LocalEvent e;
	
    private TextView tvName;
    private TextView tvType;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvCreatedBy;
    private TextView tvLocation;
    private TextView tvCost;
    private TextView tvDescription;

	private void setUpViews() {
		tvName = (TextView) findViewById(R.id.tvName);
		tvType = (TextView) findViewById(R.id.tvType);
		tvStart = (TextView) findViewById(R.id.tvStart);
		tvEnd = (TextView) findViewById(R.id.tvEnd);
		tvCreatedBy = (TextView) findViewById(R.id.tvCreatedBy);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvCost = (TextView) findViewById(R.id.tvCost);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
	}
	
	private void loadFieldsIntoView() {
		tvName.setText(e.getEventName());
		tvType.setText(e.getEventType());
		tvStart.setText(e.getStartDate()+ " " + e.getStartTime());
		tvEnd.setText(e.getEndDate()+ " " + e.getEndTime());
		ParseUser u = e.getCreatedBy();
		tvCreatedBy.setText("put parse user");
		Location l = e.getLocation();
		tvLocation.setText("put location here");
		Number n = e.getCost();
		tvCost.setText("put cost here");
		tvDescription.setText(e.getDescription());
	}	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		LocalEvent event = (LocalEvent) getIntent().getExtras().getParcelable("event");
		setUpViews();
		loadFieldsIntoView();
		
//		TextView title = (TextView) findViewById(R.id.map_item_title);
//		title.setText(event.getEventName());
//		TextView description = (TextView) findViewById(R.id.map_item_description);
//		description.setText(event.getDescription());
//		
//		TextView street_address = (TextView) findViewById(R.id.map_item_street);
//		street_address.setText(event.getLocation().getAddressLine1());
//		TextView city_state = (TextView) findViewById(R.id.map_item_city_state);
//		city_state.setText(event.getLocation().getCityAndState());
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");      
//
//
//		TextView startDate = (TextView) findViewById(R.id.map_item_startDate);
//		startDate.setText(event.getStartDate());
		
	}

}


//TextView title = (TextView) findViewById(R.id.map_item_title);
//title.setText(event.getEventName());
//TextView description = (TextView) findViewById(R.id.map_item_description);
//description.setText(event.getDescription());
//
//TextView street_address = (TextView) findViewById(R.id.map_item_street);
//street_address.setText(event.getLocation().getAddressLine1());
//TextView city_state = (TextView) findViewById(R.id.map_item_city_state);
//city_state.setText(event.getLocation().getCityAndState());
//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");      
//
//
//TextView startDate = (TextView) findViewById(R.id.map_item_startDate);
//startDate.setText(event.getStartDate());