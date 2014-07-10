package com.app.letsgo.helpers;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;


public class LetsGoApplication extends Application {
    public static final String TAG = "LETSGO";

    @Override
    public void onCreate() {
            super.onCreate();

            // from: https://www.parse.com/account/keys
            Parse.initialize(this, "3mZcH7C0jA10fF0esmFovEZh4ZoiGD3stEKhlsLJ",
                            "MyJD5Xqx4jaKzdVzD0DrDt6ZaBqkwTKZcd6mDcqH");

            // the Facebook App Id 
            ParseFacebookUtils.initialize("629002607207382");

    }

}
