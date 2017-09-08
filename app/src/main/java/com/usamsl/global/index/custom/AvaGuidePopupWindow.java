package com.usamsl.global.index.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step6.util.PayRadioGroup;

/**
 * Created by Administrator on 2017/6/26.
 */
public class AvaGuidePopupWindow extends PopupWindow {
    public View menuView;
    public AvaGuidePopupWindow(Activity context) {
        // TODO Auto-generated constructor stub
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.guide_ava_layout, null);
        //设置MyPopuWindow的View
        this.setContentView(menuView);
        //设置弹窗的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置弹窗的高
        this.setHeight(PayRadioGroup.LayoutParams.WRAP_CONTENT);
        //设置弹窗客点击
//        this.setFocusable(true);
        //动画效果
//        this.setAnimationStyle(R.style.popwin_anim_style);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable draw = new ColorDrawable(context.getResources().getColor(R.color.banTouMing));
        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(draw);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        menuView.setFocusable(true);
//        menuView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
//                    AvaGuidePopupWindow.this.dismiss();
//                }
//                return false;
//            }
//        });
    }
}