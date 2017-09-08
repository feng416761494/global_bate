package com.usamsl.global.index.step.step5.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.usamsl.global.R;


public class MyAlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private LinearLayout dialog_Group;
	private Button btn_pos;
	private Display display;
	private boolean showLayout = false;
	private boolean showPosBtn = false;

	public MyAlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public MyAlertDialog builder() {
		// ��ȡDialog����
		View view = LayoutInflater.from(context).inflate(
				R.layout.toast_view_alertdialog, null);

		// ��ȡ�Զ���Dialog�����еĿؼ�
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		dialog_Group = (LinearLayout) view.findViewById(R.id.dialog_Group);
		dialog_Group.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);

		// ����Dialog���ֺͲ���
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// ����dialog������С
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}



	public MyAlertDialog setView(View view) {
		showLayout = true;
		if (view == null) {
			showLayout = false;
		} else
			dialog_Group.addView(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		return this;
	}
	public void removeAllView(){
		dialog_Group.removeAllViews();
	}
	public MyAlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public MyAlertDialog setPositiveButton(
			final OnClickListener listener) {
		showPosBtn = true;
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}


	private void setLayout() {
		if (!showPosBtn ) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}

		if (showPosBtn) {
			btn_pos.setVisibility(View.VISIBLE);
		}

		if (showPosBtn) {
			btn_pos.setVisibility(View.VISIBLE);
		}
		if (showLayout) {
			dialog_Group.setVisibility(View.VISIBLE);
		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}
}
