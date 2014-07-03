package com.app.letsgo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.letsgoapp.R;
import com.parse.Parse;
import com.parse.ParseObject;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Parse.initialize(this, "3mZcH7C0jA10fF0esmFovEZh4ZoiGD3stEKhlsLJ", "MyJD5Xqx4jaKzdVzD0DrDt6ZaBqkwTKZcd6mDcqH");
	
		/**
		 * Test Code to see if Parse communicates with the remote server 
		 */
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
		Toast.makeText(this, "Parse Test Complete", Toast.LENGTH_SHORT).show();
	}
}
