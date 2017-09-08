package com.usamsl.global.order.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.order.adapter.ViewPagerAdapter;
import com.usamsl.global.order.fragment.FinishOrderFragment;
import com.usamsl.global.order.fragment.UnFinishOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间：2016/12/20
 * 描述：菜单栏订单页
 */
public class OrderFragment extends Fragment {

    //视图View
    private View v;
    private ViewPager vp;
    //已完成和未完成界面列表
    private List<Fragment> mData;
    //已完成
    private FinishOrderFragment finishOrderFragment;
    //未完成
    private UnFinishOrderFragment unFinishOrderFragment;
    //fragment显示的adapter
    private ViewPagerAdapter adapter;
    private RadioGroup rg;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_order, container, false);

            initView();
            initFragment();
            toListener();
            initData();
        }
        return v;
    }


    /**
     * 视图界面初始化
     */
    private void initView() {
        vp = (ViewPager) v.findViewById(R.id.vp);
        rg = (RadioGroup) v.findViewById(R.id.rg);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Rect frame = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int contentTop = getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl_top);
        rl.setPadding(0, titleBarHeight, 0, 0);
        //已完成默认被选中
        rg.check(R.id.rb_finish);
        vp.setCurrentItem(0);
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        mData = new ArrayList<>();
        finishOrderFragment = new FinishOrderFragment();
        unFinishOrderFragment = new UnFinishOrderFragment();
        mData.add(unFinishOrderFragment);
        mData.add(finishOrderFragment);
        //在Fragment里面嵌套Fragment 的话，会在ViewPager中出现。有些Fragment 不会加载的情况。。。既ViewPager 加载 Fragment 空白页的情况。。。。
        //所以 Fragment里面嵌套Fragment 的话：一定要用getChildFragmentManager();
        adapter = new ViewPagerAdapter(getChildFragmentManager(), mData);
        vp.setAdapter(adapter);
    }


    /**
     * 监听
     */
    private void toListener() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rg.check(R.id.rb_finish);
                        break;
                    case 1:
                        rg.check(R.id.rb_unFinish);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_finish:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.rb_unFinish:
                        vp.setCurrentItem(1);
                        break;
                }
            }
        });
    }
}
