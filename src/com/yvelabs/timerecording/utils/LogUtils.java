package com.yvelabs.timerecording.utils;

import android.util.Log;

public class LogUtils {
	
	public static void d (Class clazz, String msg) {
		Log.d("com.yvelabs.timerecording", clazz.getName() + ", " + msg);
	}

}
