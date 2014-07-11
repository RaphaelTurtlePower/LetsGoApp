package com.app.letsgo.models;


import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {
	private String u_objectId;
	private String u_name;
	private String u_password;
	private String u_authData;
	private boolean u_email_verified;
	private String u_email;
	private String u_phone;
	private String u_profile;  /// TODO - what data is this?
	private String u_createdAt;
	private String u_updatedAt;
	private String u_acl;        /// TODO - what data is this?

	public String get_u_objectId() { return this.u_objectId; }
	public String get_u_name() { return this.u_name; }
	public String get_u_password() { return this.u_password; }
	public String get_u_authData() { return this.u_authData; }
	public boolean get_u_email_verified() { return this.u_email_verified; }
	public boolean is_u_email_verified() { return this.u_email_verified; }
	private String get_u_email() { return this.u_email; }
	private String get_u_phone() { return this.u_phone; }
	private String get_u_profile() { return this.u_profile; } 
	private String get_u_createdAt() { return this.u_createdAt; }
	private String get_u_updatedAt() { return this.u_updatedAt; }
	private String get_u_acl() { return this.u_acl; }   

	public void set_u_objectId(String s) { this.u_objectId = s; }
	public void set_u_name(String s) { this.u_name = s; }
	public void set_u_password(String s) { this.u_password = s; }
	public void set_u_authData(String s) { this.u_authData = s; }
	public void set_u_email_verified(boolean b) { this.u_email_verified = b; }
	public void set_u_email(String s) { this.u_email = s; }
	public void set_u_phone(String s) { this.u_phone = s; }
	public void set_u_profile(String s) { this.u_profile = s; } 
	public void set_u_createdAt(String s) { this.u_createdAt = s; }
	public void set_u_updatedAt(String s) { this.u_updatedAt = s; }
	public void set_u_acl(String s) { this.u_acl = s; }   
	
	@Override
	public int describeContents() {
		// Auto-generated method stub
		return 0;
	}
	
	public User(Parcel src) {
		Log.v("LETSGO", "writeToParcel of User " + this.u_name);
		this.u_objectId = src.readString();
		this.u_name = src.readString();
		this.u_password = src.readString();
		this.u_authData = src.readString();
		byte b = src.readByte();
		if ( b==1 ) {
			this.u_email_verified = true;
		} else {
			this.u_email_verified = false;
		}
		this.u_email = src.readString();
		this.u_phone = src.readString();
		this.u_profile = src.readString();
		this.u_createdAt = src.readString();
		this.u_updatedAt = src.readString();
		this.u_acl = src.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.v("LETSGO", "writeToParcel of User " + this.u_name);
		dest.writeString(this.u_objectId);
		dest.writeString(this.u_name);
		dest.writeString(this.u_password);
		dest.writeString(this.u_authData);
		if (u_email_verified) {
			dest.writeByte( (byte) 1);
		} else {
			dest.writeByte( (byte) 0);
		}
		dest.writeString(this.u_email);
		dest.writeString(this.u_phone);
		dest.writeString(this.u_profile);
		dest.writeString(this.u_createdAt);
		dest.writeString(this.u_updatedAt);
		dest.writeString(this.u_acl);
	}
	
    public static Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User createFromParcel(Parcel src) {
			return new User(src);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
    	
    };

}