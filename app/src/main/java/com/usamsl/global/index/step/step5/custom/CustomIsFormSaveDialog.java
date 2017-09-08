package com.usamsl.global.index.step.step5.custom;

import java.util.regex.Pattern;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usamsl.global.R;
import com.usamsl.global.util.update.ObjectIsNullUtils;

public class CustomIsFormSaveDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String confirm;
    private String title, hint,tvCancelText,tvConfirmText;
    private LayoutInflater inflater;
    private EditText edPrice;
    private DialogClickListener listener;
    private View view;

    public CustomIsFormSaveDialog(Context context2, String title2, String hint2) {
        super(context2, R.style.CustomDialog);
        this.context = context2;
        this.title = title2;
        this.hint = hint2;
        inflater = LayoutInflater.from(context2);
    }

    public CustomIsFormSaveDialog(Context context2, String title2, String hint2,String tvCancel,String tvConfirm) {
        super(context2, R.style.CustomDialog);
        this.context = context2;
        this.title = title2;
        this.hint = hint2;
        this.tvCancelText = tvCancel;
        this.tvConfirmText = tvConfirm;
        inflater = LayoutInflater.from(context2);
    }

    public interface DialogClickListener {
        public void doCancel();

        public void doConfirm();
    }

    public void setDialogClickListener(DialogClickListener listener1) {
        this.listener = listener1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViews();
    }

    private void setViews() {
        view = inflater.inflate(R.layout.isformsave, null);
        setContentView(view);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tvConfirm);
        TextView tv_title = (TextView) view.findViewById(R.id.tvHint);
        TextView tv_hint = (TextView) view.findViewById(R.id.tvTitle);
        tv_title.setText(title);
        tv_hint.setText(hint);
        if(ObjectIsNullUtils.TextIsNull(tvCancelText) && ObjectIsNullUtils.TextIsNull(tvConfirmText)){
            tvCancel.setText(tvCancelText);
            tvConfirm.setText(tvConfirmText);
        }
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                listener.doCancel();
                break;
            case R.id.tvConfirm:
                listener.doConfirm();
                break;
        }
    }
}
