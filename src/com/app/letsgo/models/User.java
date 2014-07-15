// determined to be inadvisable to use with Parse, Jul 13 2014
// Parse does not cope well with child classes of ParseUser.
// so use ParseUser everywhere, not User.  PvdL.
// this class just helps you match a Facebook userId to a Parse uid.

package com.app.letsgo.models;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.util.Log;


public class User {

    static public ArrayList<FriendRecord> friendsOfCurrUser;

	static public void addToFriendsList(String uid, String name, String parseId) {
		if (friendsOfCurrUser==null) {
			friendsOfCurrUser = new ArrayList<FriendRecord>(10);
		}
		FriendRecord fr = new FriendRecord();
		fr.name = name;
		fr.facebookUid = Long.valueOf(uid);
		fr.parseUid = Long.valueOf(parseId);
		friendsOfCurrUser.add(fr);
		// Log.d("letsgo", "added "+name +", "+uid + " to friendOfCurrUser");
	}                  	
}
