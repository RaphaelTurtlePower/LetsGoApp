package com.app.letsgo.activities;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import com.app.letsgo.R;
import com.app.letsgo.R.layout;
import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.Location;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
	private Number upCount;
	private Number downCount;

	private LocalEvent e;
	
    private TextView tvName;
    private TextView tvType;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvCreatedBy;
    private TextView tvLocation;
    private TextView tvDowncount;
    private TextView tvCost;
    private TextView tvDescription;
    private TextView tvCounts;
    private Button bInvite;

	private void setUpViews() {
		tvName = (TextView) findViewById(R.id.tvName);
		tvType = (TextView) findViewById(R.id.tvType);
		tvStart = (TextView) findViewById(R.id.tvStart);
		tvEnd = (TextView) findViewById(R.id.tvEnd);
		tvCreatedBy = (TextView) findViewById(R.id.tvCreatedBy);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvCost = (TextView) findViewById(R.id.tvCost);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvCounts = (TextView) findViewById(R.id.tvCounts);
		bInvite = (Button) findViewById(R.id.bInvite);
	}
	
	static Format df = new DecimalFormat("0.00");

	private void loadFieldsIntoView() {
		tvName.setText(e.getEventName());
		tvType.setText(e.getEventType());
		tvStart.setText(e.getStartDate()+ " " + e.getStartTime());
		tvEnd.setText(e.getEndDate()+ " " + e.getEndTime());
		ParseUser u = e.getCreatedBy();
		tvCreatedBy.setText("put parse user");
		String locn = e.getLocation().getAddress();
		tvLocation.setText(locn);
		Number n = e.getCost();
		String cost =  df.format(e.getCost().floatValue());
		tvCost.setText("$"+cost);
		tvDescription.setText(e.getDescription());
		n = e.getUpCount();
		int up = (n==null? 0 : n.intValue());
		n = e.getDownCount();
		int down = (n==null? 0 : n.intValue());		
		tvCounts.setText("     Up votes: " + up + ", down votes: "+down);
	}	

	public void onClick(View v) {
		// "invite friends" button handler.  Format email to invite them
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.EXTRA_SUBJECT));
		String mailBody = getString(R.string.eventInvitation) 
				+ getString(R.string.eventName) +" "+ e.getEventName() + "\n"
				+ getString(R.string.eventDescription) +" "+ e.getDescription() + "\n"
				+ getString(R.string.eventAddress) +" "+ e.getLocation().getAddress() + "\n"
				+ getString(R.string.eventStart) +" "+e.getStartDate() + ", " + e.getStartTime() +"\n" ;    		        
		emailIntent.putExtra(Intent.EXTRA_TEXT, mailBody); 
		startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		e = (LocalEvent) getIntent().getExtras().getParcelable("event");
		setUpViews();
		loadFieldsIntoView();		

	}

}
