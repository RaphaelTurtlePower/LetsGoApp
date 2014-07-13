package com.app.letsgo;

import android.app.Application;

import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.Location;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LetsGoApplication extends Application {
	public static final String TAG = "LETSGO";

	@Override
	public void onCreate() {
		super.onCreate();
	
		// initialize parse
		ParseObject.registerSubclass(LocalEvent.class);
		ParseObject.registerSubclass(Location.class);
		Parse.initialize(this, "3mZcH7C0jA10fF0esmFovEZh4ZoiGD3stEKhlsLJ", 
				"MyJD5Xqx4jaKzdVzD0DrDt6ZaBqkwTKZcd6mDcqH");
	
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		
		 // the Facebook App Id 
        ParseFacebookUtils.initialize("629002607207382");

	}

}
