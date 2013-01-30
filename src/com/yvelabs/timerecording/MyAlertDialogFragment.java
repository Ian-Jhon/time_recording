package com.yvelabs.timerecording;

import com.yvelabs.timerecording.utils.TypefaceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAlertDialogFragment extends DialogFragment {
	
	private OKOnClickListener okOnClickListener;
	private CancelOnClickListener cancelOnClickListener;

	private ImageView titleIcon;
	private TextView titleText;
	private TextView message;
	private ImageView okBut;
	private ImageView cancelBut;

	public MyAlertDialogFragment(OKOnClickListener okListener,
			CancelOnClickListener cancelListener) {
		this.okOnClickListener = okListener;
		this.cancelOnClickListener = cancelListener;
	}

	public static MyAlertDialogFragment newInstance(int title, int icon, String message,
			OKOnClickListener okListener,
			CancelOnClickListener cancelListener) {
		MyAlertDialogFragment fragment = new MyAlertDialogFragment(okListener, cancelListener);
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("icon", icon);
		args.putString("message", message);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_alert_dialog, container, false);
		titleIcon = (ImageView) view.findViewById(R.id.my_alert_dialog_title_icon);
		titleText = (TextView) view.findViewById(R.id.my_alert_dialog_title_text);
		message = (TextView) view.findViewById(R.id.my_alert_dialog_message);
		okBut = (ImageView) view.findViewById(R.id.my_alert_dialog_ok);
		cancelBut = (ImageView) view.findViewById(R.id.my_alert_dialog_cancel);
		
		new TypefaceUtils().setTypeface(titleText, TypefaceUtils.MOBY_MONOSPACE);
		
		if (getArguments().getInt("icon") > 0) {
			titleIcon.setImageResource(getArguments().getInt("icon"));
		}
		
		if (getArguments().getInt("title") > 0) {
			titleText.setText(getString(getArguments().getInt("title")));
		}
		
		if (getArguments().getString("message") != null && getArguments().getString("message").length() > 0) {
			message.setText(getArguments().getString("message"));
		}
		
		okBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (okOnClickListener != null)
					okOnClickListener.onClick(view);
				getDialog().dismiss();
			}
		});
		cancelBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (cancelOnClickListener != null) 
					cancelOnClickListener.onClick(view);
				getDialog().dismiss();
			}
		});
		
		return view;
	}
	
	interface OKOnClickListener {
		public void onClick (View view); 
	}
	
	interface CancelOnClickListener {
		public void onClick (View view); 
	}

}
