package com.yvelabs.timerecording;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MyAlertDialogFragment extends DialogFragment {

	private DialogInterface.OnClickListener OkListener;
	private DialogInterface.OnClickListener cancelListener;

	public MyAlertDialogFragment(DialogInterface.OnClickListener OkListener,
			DialogInterface.OnClickListener cancelListener) {
		this.OkListener = OkListener;
		this.cancelListener = cancelListener;
	}

	public static MyAlertDialogFragment newInstance(int title, int icon, String message,
			DialogInterface.OnClickListener OkListener,
			DialogInterface.OnClickListener cancelListener) {
		MyAlertDialogFragment fragment = new MyAlertDialogFragment(OkListener, cancelListener);
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("icon", icon);
		args.putString("message", message);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Dialog);

		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity()).setPositiveButton(R.string.ok, OkListener)
				.setNegativeButton(R.string.cancel, cancelListener);

		if (getArguments().getInt("title") > 0) {
			builder.setTitle(getArguments().getInt("title"));
		}
		
		if (getArguments().getInt("icon") > 0) {
			builder.setIcon(getArguments().getInt("icon"));
		}
		
		if (getArguments().getString("message") != null && getArguments().getString("message").length() > 0) {
			builder.setMessage(getArguments().getString("message"));
		}
		

		return builder.create();
	}
}
