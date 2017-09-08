package com.usamsl.global.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/11.
 * 预防内存溢出
 */
public class RecyclerImageView extends ImageView {
    public RecyclerImageView(Context context) {
        super(context);
    }

    public RecyclerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
