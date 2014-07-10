package com.app.letsgo.helpers;

import android.widget.EditText;

public class Utils {
	
	public static boolean isNull(EditText et) {
		if (et != null) {
			if (et.getText() != null)  {
				String s = et.getText().toString();
				if (s != null && !s.isEmpty()) return false;
			} else return true;
		}
		return true;
	}
				

}
