package com.usamsl.global.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.usamsl.global.R;


public class CustomProgressDialog extends Dialog {
	private Context context;
	private LayoutInflater inflater;
	private ProgressBar bar;
	public CustomProgressDialog(Context context2) {
		super(context2, R.style.CustomDialogLoading);
		this.context = context2;
		inflater = LayoutInflater.from(context2);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.layout_loading, null);
		setContentView(view);
		bar = (ProgressBar) view.findViewById(R.id.custom_progressDialog);
	}
	
}
