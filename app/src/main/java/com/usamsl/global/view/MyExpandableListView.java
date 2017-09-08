package com.usamsl.global.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2016/12/28.
 * 描述：重写ExpandableListView以解决ScrollView嵌套ExpandableListView
 * 导致ExpandableListView显示不正常的问题
 */
public class MyExpandableListView  extends ExpandableListView {
    public MyExpandableListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyExpandableListView(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
