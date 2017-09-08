package com.usamsl.global.service.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.usamsl.global.R;
import com.usamsl.global.service.activity.CommonProblemActivity;
import com.usamsl.global.service.adapter.ProblemAdapter;
import com.usamsl.global.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签栏服务菜单问题部分
 * 时间：2016/12/15
 */
public class VisaQuestionsFragment extends Fragment {

    //整体视图View
    private View v;
    private MyListView lv;
    private List<String> mData;
    private ProblemAdapter adapter;

    public VisaQuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_visa_questions, container, false);
            initView();
            initData();
            toListener();
        }
        return v;
    }

    private void initView() {
        lv = (MyListView) v.findViewById(R.id.lv);
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add("请问你们的客服邮箱是多少？");
        mData.add("在职证明准备中文的可以吗？");
        mData.add("为什么我要准备全家福照片？");
        mData.add("美国签证面谈，我不会英语怎么办？");
        adapter = new ProblemAdapter(getActivity(), mData);
        lv.setAdapter(adapter);
    }

    private void toListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), CommonProblemActivity.class);
                intent.putExtra("problem", mData.get(i));
                if (i == 0) {
                    intent.putExtra("email", true);
                }
                startActivity(intent);
            }
        });
    }

}
