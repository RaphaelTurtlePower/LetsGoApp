package com.app.letsgo.models;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {
	private String u_objectId;
	private String u_name;
	private String u_password;
	private long   u_authData;
	private boolean u_email_verified;
	private String u_email;
	private String u_phone;
	private String u_profile;  /// TODO - what data is this?
	private Date   u_createdAt;
	private Date   u_updatedAt;
	private String u_acl;        /// TODO - what data is this?
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.v("LETSGO", "writeToParcel of User " + this.u_name);
		dest.writeString(this.u_objectId);
		dest.writeString(this.u_name);
		dest.writeString(this.u_password);
		dest.writeLong(this.u_authData);
		if (u_email_verified) {
			dest.writeByte( (byte) 1);
		} else {
			dest.writeByte( (byte) 0);
		}
		dest.writeString(u_email);
		dest.writeString(u_phone);
		dest.writeString(u_profile);
		long writableDate = this.u_createdAt.getTime();
		dest.writeLong(writableDate);
		writableDate = this.u_updatedAt.getTime();
		dest.writeLong(writableDate);
		dest.writeString(this.u_acl);
	}
	
	

}
