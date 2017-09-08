package com.usamsl.global.my.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.usamsl.global.R;

/**
 * Created by Administrator on 2017/8/4.
 */
public class CustomUserInfoDialog_Photo extends Dialog implements View.OnClickListener{
    private Context context;
    private LayoutInflater inflater;
    private PhotoClickListener listener;
    private View view;
    private TextView tvAlbum,tvOpenCamera;

    public CustomUserInfoDialog_Photo(Context context2) {
        super(context2, R.style.CustomDialog);
        this.context = context2;
        inflater = LayoutInflater.from(context2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.custom_dialog_user_photo,null);
        setContentView(view);
        tvAlbum = (TextView) view.findViewById(R.id.tvAlbum);
        tvOpenCamera = (TextView) view.findViewById(R.id.tvOpenCamera);
        tvAlbum.setOnClickListener(this);
        tvOpenCamera.setOnClickListener(this);
    }

    public interface PhotoClickListener{
        void album();
        void openCamera();
    }

    public void setPhotoClickListener(PhotoClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tvAlbum:
                listener.album();
                break;

            case R.id.tvOpenCamera:
                listener.openCamera();
                break;
        }
    }
}
