package com.app.letsgo.activities;

import java.text.DecimalFormat;
import java.text.Format;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.app.letsgo.R;
import com.app.letsgo.fragments.MiniMapFragment;
import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.LocalEventParcel;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
	private MiniMapFragment miniMapFragment;
	private LocalEvent e;
	private LocalEventParcel lp;
	
	private ShareActionProvider mShareActionProvider;

    private TextView tvName;
    private TextView tvType;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvCreatedBy;
    private TextView tvLocation;
    private TextView tvCost;
    private TextView tvDescription;
    private ImageButton ibimageUp;
    private ImageButton ibimageDown;
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
		ibimageUp = (ImageButton) findViewById(R.id.imageUp);
		ibimageDown = (ImageButton) findViewById(R.id.imageDown);
		// bInvite = (Button) findViewById(R.id.bInvite);
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
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		lp = (LocalEventParcel) getIntent().getExtras().getParcelable("event");
		e = lp.getEvent();
		setUpViews();
		loadFieldsIntoView();
		
		final TextView tvUpCount = (TextView) findViewById(R.id.txtUpCount);
		final TextView tvDownCount = (TextView) findViewById(R.id.txtDownCount);
		Number n = (e.getUpCount() == null) ? 0 : e.getUpCount();
		tvUpCount.setText(n.toString());
		n = (e.getDownCount() == null) ? 0 : e.getDownCount();
		tvDownCount.setText(n.toString());
		
		final String objectId = e.getObjectId();
		Log.d("DEBUG", "ObjectID:" + e.getObjectId());
		ibimageUp.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("LocalEvent");
				query.getInBackground(objectId, new GetCallback<ParseObject>(){
				@Override
					public void done(ParseObject localEvent, ParseException e) {
						final Number up = (localEvent.getNumber("upCount") == null)? 1 : localEvent.getNumber("upCount").intValue() + 1;
						localEvent.put("upCount", up);
						localEvent.saveInBackground(new SaveCallback(){

							@Override
							public void done(ParseException e) {
								tvUpCount.setText(up.toString());
							}
							
						});
					
					}
					
				});
			}
			
		});
		
		ibimageDown.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("LocalEvent");
				query.getInBackground(objectId, new GetCallback<ParseObject>(){
				@Override
					public void done(ParseObject localEvent, ParseException e) {
						final Number down = (localEvent.getNumber("downCount") == null)? 1 : localEvent.getNumber("downCount").intValue() + 1;
						localEvent.put("downCount", down);
						localEvent.saveInBackground(new SaveCallback(){

							@Override
							public void done(ParseException e) {
								tvDownCount.setText(down.toString());
							}
							
						});
					
					}
					
				});
			}
			
		});
		
		miniMapFragment = MiniMapFragment.newInstance(lp);
		if (savedInstanceState == null) {
			getFragmentManager()
                    .beginTransaction()
                    .add(R.id.miniMap, miniMapFragment)
                    .commit();
			getFragmentManager().executePendingTransactions();
		}
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.detail_activity_actions, menu);
	    MenuItem item = menu.findItem(R.id.action_share);

	    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
	    Intent myIntent = new Intent();
	    myIntent.setAction(Intent.ACTION_SEND);
	    myIntent.putExtra(Intent.EXTRA_TEXT, "I would like to invite you at " + 
	    				e.getLocation().getAddress() + " for " +
	    				e.getEventName() + " on " + e.getStartDate() + " at " + 
	    				e.getStartTime() + ". See you there!");
	    myIntent.setType("text/plain");
	    mShareActionProvider.setShareIntent(myIntent);
	    return super.onCreateOptionsMenu(menu);
	}
}
