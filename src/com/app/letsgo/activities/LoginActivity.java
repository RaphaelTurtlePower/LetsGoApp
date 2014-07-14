package com.app.letsgo.activities;


import com.app.letsgo.R;
import com.app.letsgo.LetsGoApplication;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class LoginActivity extends Activity {

	private Button loginButton;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});

		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		int i =0;
		if (currentUser!=null) i=1;
		
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			//showUserDetailsActivity();
			showMapViewActivity();
		}
		i=2;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void onLoginButtonClicked() {
		LoginActivity.this.progressDialog = ProgressDialog.show(
				LoginActivity.this, "", "Logging in...", true);
		// List<String> permissions = Arrays.asList("basic_info", "user_about_me",
		// TODO check against https://developers.facebook.com/docs/facebook-login/permissions/v2.0
		List<String> permissions = Arrays.asList("public_profile", "user_friends",
				"user_relationships", "user_birthday", "user_location");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(LetsGoApplication.TAG,
							"The user field came back as null from Facebook login.");
				} else if (user.isNew()) {
					Log.d(LetsGoApplication.TAG,
							"User signed up and logged in through Facebook!");
					showMapViewActivity();
				} else {
					Log.d(LetsGoApplication.TAG,
							"User logged in through Facebook!");
					showMapViewActivity();
				}
			}
		});
	}
/*
	private void showUserDetailsActivity() {
		Intent intent = new Intent(this, UserDetailsActivity.class);
		startActivity(intent);
	}
*/
	private void showMapViewActivity(){
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}
}
