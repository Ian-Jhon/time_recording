package com.yvelabs.timerecording.utils;

import android.graphics.Typeface;
import android.widget.TextView;

public class TypefaceUtils {
	
	public static final String RBNO2_LIGHT_A = "fonts/RBNo2Light_a.otf";
	public static final String MOBY_MONOSPACE ="fonts/Moby_Monospace.ttf";
	
	public static void setTypeface (TextView view, String typefaceName) {

		Typeface typeFace = Typeface.createFromAsset(view.getContext().getAssets(), typefaceName);  
		view.setTypeface(typeFace);
	}

}
 