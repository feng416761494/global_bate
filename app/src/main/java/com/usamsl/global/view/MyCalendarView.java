package com.usamsl.global.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.squareup.timessquare.CalendarPickerView;

/**
 * Created by Administrator on 2017/1/22.
 * 描述：重写CalendarPickerView的onMeasure方法，解决与ScrollView的滑动冲突
 */
public class MyCalendarView extends CalendarPickerView {
    public MyCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
