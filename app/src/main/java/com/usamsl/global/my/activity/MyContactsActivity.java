package com.usamsl.global.my.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.step4.entity.AllContacts;
import com.usamsl.global.index.step.step4.entity.Contacts;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.index.util.CnSpell;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.login.activity.RegisterActivity;
import com.usamsl.global.login.entity.Login;
import com.usamsl.global.login.entity.User;
import com.usamsl.global.main.MainActivity;
import com.usamsl.global.my.adapter.MyContactsAdapter;
import com.usamsl.global.my.adapter.MyContactsSearchAdapter;
import com.usamsl.global.my.adapter.MyGroupAdapter;
import com.usamsl.global.my.data.MyContactsData;
import com.usamsl.global.my.entity.MyContacts;
import com.usamsl.global.my.entity.MyGroup;
import com.usamsl.global.my.util.ModifyDialog;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.SharedPreferencesManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.view.MyExpandableListView;
import com.usamsl.global.view.MyListView;


import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 * 时间：2016/12/29
 * 描述：我的：我的联系人
 */
public class MyContactsActivity extends AppCompatActivity {
    private ModifyDialog dialog;
    private EditText edit_modify;
    //点击的分组或者联系人
    private int currentGroup, currentChild;
    //添加按钮
    private TextView tv_add;
    //返回按钮
    private ImageView img_back;
    //搜索
    private EditText et_search;
    //搜索提示
    private TextView tv_search;
    private ListView lv;
    //按资料分
    private MyExpandableListView myExpandableListViewTop;
    //按分组分
    private MyExpandableListView myExpandableListViewBody;
    //分组联系人
    private Map<String, List<MyContacts>> mData;
    //资料联系人
    private List<List<MyContacts>> completeData;
    //联系人
    private List<MyContacts> myContacts;
    //分组
    private List<MyGroup.ResultBean> groups;
    //资料
    private List<String> completeGroup;
    //按分组分的Adapter
    private static MyContactsAdapter myContactsAdapter;
    //按资料完整分的Adapter
    private static MyGroupAdapter myGroupAdapter;
    //最外层
    private RelativeLayout rl;
    //判断是否联网
    private boolean connection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);
        initView();
        initData();
        toListener();
    }

    /**
     * 监听
     */
    private void toListener() {
        //设置长按点击事件
        MyOnLongClickListener myLongListener = new MyOnLongClickListener();
        myExpandableListViewBody.setOnItemLongClickListener(myLongListener);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("") || charSequence == null) {
                    tv_search.setVisibility(View.VISIBLE);
                } else {
                    tv_search.setVisibility(View.GONE);
                }
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /**
         * 点击下个时让别的收缩
         */
        myExpandableListViewBody.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                myContactsAdapter.setPointRotate(groupPosition);
                for (int i = 0; i < myContactsAdapter.getGroupCount(); i++) {
                    if (groupPosition != i && myExpandableListViewBody.isGroupExpanded(groupPosition)) {
                        myExpandableListViewBody.collapseGroup(i);
                    }
                }

            }
        });
        myExpandableListViewTop.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                myGroupAdapter.setPointRotate(groupPosition);
                for (int i = 0; i < myGroupAdapter.getGroupCount(); i++) {
                    if (groupPosition != i && myExpandableListViewTop.isGroupExpanded(groupPosition)) {
                        myExpandableListViewTop.collapseGroup(i);
                    }
                }

            }
        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroup();
            }
        });
        //点击除EditText外地方使键盘消失
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) MyContactsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connection = true;
        } else {
            connection = false;
            ConstantsMethod.showTip(MyContactsActivity.this, "无网络连接");
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<MyContacts> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            lv.setVisibility(View.GONE);
            myExpandableListViewTop.setVisibility(View.VISIBLE);
            myExpandableListViewBody.setVisibility(View.VISIBLE);
        } else {
            myExpandableListViewTop.setVisibility(View.GONE);
            myExpandableListViewBody.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            mSortList.clear();
            for (MyContacts c : myContacts) {
                String name = c.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 ||
                        CnSpell.getPinYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(c);
                }
            }
            MyContactsSearchAdapter m = new MyContactsSearchAdapter(this, mSortList);
            lv.setAdapter(m);
        }
    }

    /**
     * 添加分组
     */
    private void addGroup() {
        alertAddDialog(MyContactsActivity.this);
    }

    /**
     * 数据加载
     */
    private void initData() {
        connectWork();
        mData = new HashMap<>();
        groups = new ArrayList<>();
        myContacts = new ArrayList<>();
        getGroup();
        completeData = new ArrayList<>();
        completeGroup = new ArrayList<>();
        completeGroup.add("资料完整");
        completeGroup.add("资料不完整");
        myContactsAdapter = new MyContactsAdapter(this, groups, mData);
        myGroupAdapter = new MyGroupAdapter(this, completeGroup, completeData);

    }


    OkHttpClient client = OkHttpManager.myClient();

    /**
     * 请求好友分组
     */
    private void getGroup() {
        connectWork();
        if (connection) {
            new ProgressBarLoadUtils(this).startProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // get请求上传
                    String url = UrlSet.groupList + Constants.TOKEN;
                    getG(url);
                }
            }).start();
        }
    }

    /**
     * 请求好友分组
     */
    private void getG(String url) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstantsMethod.showTip(MyContactsActivity.this, "网络异常！");
                        new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                if (str.substring(0, 1).equals("{")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            MyGroup group = gson.fromJson(str, MyGroup.class);
                            if (group.getError_code() == 0) {
                                groups.addAll(group.getResult());
                                MyContactsData.initMyGroups(groups);
                                //加载联系人
                                getContacts(UrlSet.selectContactAll + Constants.TOKEN);
                            } else if (group.getError_code() == 2) {
                                Intent intent = new Intent(MyContactsActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });
    }

    /**
     * 加载联系人
     */
    private void getContacts(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request.Builder requestBuilder = new Request.Builder().url(url);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(MyContactsActivity.this, "网络异常！");
                                new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                        final String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    AllContacts all = gson.fromJson(str, AllContacts.class);
                                    switch (all.getError_code()) {
                                        case 0:
                                            List<MyContacts> l = new ArrayList<>();
                                            for (int j = 0; j < all.getResult().size(); j++) {
                                                MyContacts c = new MyContacts();
                                                AllContacts.ResultBean bean = all.getResult().get(j);
                                                if (bean.getContact_number() != null
                                                        && bean.getPassport_no() != null
                                                        && !bean.getContact_number().equals(" ")
                                                        && !bean.getPassport_no().equals(" ")) {
                                                    c.setComplete(true);
                                                } else {
                                                    c.setComplete(false);
                                                }
                                                if (bean.getContact_number() != null
                                                        && !bean.getContact_number().equals(" ")) {
                                                    c.setRealName(true);
                                                } else {
                                                    c.setRealName(false);
                                                }
                                                c.setName(bean.getContact_name());
                                                c.setGroupId(bean.getGroup_id());
                                                c.setPhoto(bean.getHead_url());
                                                c.setTel(bean.getContact_phone());
                                                c.setEmail(bean.getE_mail());
                                                c.setContactId(bean.getContact_id());
                                                l.add(c);
                                            }
                                            myContacts.addAll(l);
                                            MyContactsData.initMyContacts(myContacts);
                                            resetContactsData();
                                            myExpandableListViewBody.setAdapter(myContactsAdapter);
                                            //按资料完整分
                                            resetComplete();
                                            myExpandableListViewTop.setAdapter(myGroupAdapter);
                                            break;

                                        case 1:
                                            myExpandableListViewBody.setAdapter(myContactsAdapter);
                                            myExpandableListViewTop.setAdapter(myGroupAdapter);
                                            break;

                                        case 2:
                                            Intent intent = new Intent(MyContactsActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }


    /**
     * 完整数据加载
     */
    private void resetComplete() {
        completeData.clear();
        List<MyContacts> complete = new ArrayList<>();
        List<MyContacts> unComplete = new ArrayList<>();
        for (int j = 0; j < myContacts.size(); j++) {
            MyContacts contacts = myContacts.get(j);
            if (myContacts.get(j).isComplete()) {
                complete.add(contacts);
            } else {
                unComplete.add(contacts);
            }
        }
        completeData.add(complete);
        completeData.add(unComplete);
    }

    /**
     * 分组数据加载
     */
    private void resetContactsData() {
        List<MyContacts> list;
        for (MyGroup.ResultBean group : groups) {
            list = new ArrayList<>();
            for (MyContacts contacts : myContacts) {
                if (contacts.getGroupId() == group.getId()) {
                    list.add(contacts);
                }
            }
            mData.put(group.getGroup_name(), list);
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_add = (TextView) findViewById(R.id.tv_add);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        myExpandableListViewTop = (MyExpandableListView) findViewById(R.id.elv_top);
        myExpandableListViewBody = (MyExpandableListView) findViewById(R.id.elv_body);
        lv = (MyListView) findViewById(R.id.lv);
        rl = (RelativeLayout) findViewById(R.id.rl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            MyGroup.ResultBean group = data.getParcelableExtra("group");
            if (group != null) {
                //修改数据库移动分组
                updateGroup(group);
            }
        }
    }

    //修改数据库移动分组
    private void updateGroup(final MyGroup.ResultBean group) {
        new ProgressBarLoadUtils(this).startProgressDialog();
        String groupName = groups.get(currentGroup).getGroup_name();
        final List<MyContacts> list = mData.get(groupName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateContact;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("contact_id", list.get(currentChild).getContactId() + "")
                        .add("group_id", group.getId() + "")
                        .add("use_status", 1 + "")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(MyContactsActivity.this, "网络异常！");
                                new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                        String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            Gson gson = new Gson();
                            final Result r = gson.fromJson(str, Result.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (r.getError_code()) {
                                        case 0:
                                            String groupName = groups.get(currentGroup).getGroup_name();
                                            MyContacts c = mData.get(groupName).get(currentChild);
                                            mData.get(groupName).remove(c);
                                            c.setGroup(group.getGroup_name());
                                            mData.get(group.getGroup_name()).add(c);
                                            myContactsAdapter.notifyDataSetChanged();
                                            break;
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    //自定义长按监听事件
    public class MyOnLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            //长按子项
            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
                final int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                final int childPosition = ExpandableListView.getPackedPositionChild(packedPos);

                currentGroup = groupPosition;
                currentChild = childPosition;
                String[] str = {"移动到"};/*"删除",*/
                Dialog alertDialog = new AlertDialog.Builder(MyContactsActivity.this)
                        .setItems(str, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    /*case 0:
                                       *//* Dialog alertDialog =
                                                new AlertDialog.Builder(MyContactsActivity.this)
                                                        .setTitle("确定删除？").
                                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                deleteChild(groupPosition, childPosition);
                                                            }
                                                        })
                                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                            }
                                                        }).create();
                                        alertDialog.show();
*//*
                                        final CustomUserInfoDialog_Profession customIsFormSaveDialog = new CustomUserInfoDialog_Profession(MyContactsActivity.this,
                                                "是否删除此联系人?","提示");
                                        customIsFormSaveDialog.setDialogClickListener(new CustomUserInfoDialog_Profession.DialogClickListener() {
                                            @Override
                                            public void doCancel() {
                                                customIsFormSaveDialog.dismiss();
                                            }

                                            @Override
                                            public void doConfirm() {
                                                deleteChild(groupPosition, childPosition);
                                                customIsFormSaveDialog.dismiss();
                                            }
                                        });
                                        customIsFormSaveDialog.show();
                                        break;*/
                                    case 0:
                                        Intent intent = new Intent(MyContactsActivity.this, GroupActivity.class);
                                        startActivityForResult(intent, 1);
                                        break;
                                }
                            }
                        }).create();
                alertDialog.show();

                return true;
                //长按组
            } else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
                final int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPos);

                currentGroup = groupPosition;
                currentChild = childPosition;
                String[] str = {"删除", "重命名"};
                final String groupName = groups.get(groupPosition).getGroup_name();
                if (!groupName.equals("我的好友")) {
                    Dialog alertDialog = new AlertDialog.Builder(MyContactsActivity.this)
                            .setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            List<MyContacts> list = mData.get(groupName);
                                            if (list == null || list.size() == 0) {
                                               /* Dialog alertDialog =
                                                        new AlertDialog.Builder(MyContactsActivity.this)
                                                                .setTitle("确定删除？").
                                                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        deleteGroup();
                                                                    }
                                                                })
                                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                    }
                                                                }).create();
                                                alertDialog.show();*/
                                                final CustomIsFormSaveDialog customIsFormSaveDialog =
                                                        new CustomIsFormSaveDialog(MyContactsActivity.this,"是否删除此分组?","提示");
                                                customIsFormSaveDialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
                                                    @Override
                                                    public void doCancel() {
                                                        customIsFormSaveDialog.dismiss();
                                                    }

                                                    @Override
                                                    public void doConfirm() {
                                                        deleteGroup();
                                                        customIsFormSaveDialog.dismiss();
                                                    }
                                                });
                                                customIsFormSaveDialog.show();
                                            } else {
                                                Toast.makeText(MyContactsActivity.this, "不能删除有联系人的分组", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case 1:
                                            alertModifyDialog(groups.get(groupPosition).getGroup_name());
                                            break;
                                    }
                                }
                            }).create();
                    alertDialog.show();
                } else {
                    Toast.makeText(MyContactsActivity.this, "不能对默认分组进行操作", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }
    }

    //新增组
    public void addGroup(String newGroupName) {
        int i;
        for (i = 0; i < groups.size(); i++) {
            if (groups.get(i).getGroup_name().equals(newGroupName)) {
                ConstantsMethod.showTip(MyContactsActivity.this, "分组不能重名");
                break;
            }
        }
        if (i == groups.size()) {
            saveAddGroup(newGroupName);
            MyGroup.ResultBean g = new MyGroup.ResultBean();
            g.setGroup_name(newGroupName);
            groups.add(g);
            List<MyContacts> list = new ArrayList<>();
            mData.put(newGroupName, list);
            myContactsAdapter.notifyDataSetChanged();
        }
        MyContactsData.addMyGroup(newGroupName);
    }

    /**
     * 上传新增分组
     */
    private void saveAddGroup(final String newGroupName) {
        new ProgressBarLoadUtils(this).startProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // post请求上传
                String url = UrlSet.addGroup;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("token", Constants.TOKEN)
                        .add("group_name", newGroupName)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(MyContactsActivity.this, "网络异常！");
                                new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                        if (str.substring(0, 1).equals("{")) {
                            Gson gson = new Gson();
                            final Result result = gson.fromJson(str, Result.class);
                        }
                    }
                });
            }
        }).start();
    }

    //删除指定组
    public void deleteGroup() {
        new ProgressBarLoadUtils(this).startProgressDialog();
        final int id = groups.get(currentGroup).getId();
        String groupName = groups.get(currentGroup).getGroup_name();
        mData.remove(groupName);
        groups.remove(currentGroup);
        myContactsAdapter.notifyDataSetChanged();
        myGroupAdapter.notifyDataSetChanged();
        MyContactsData.deleteMyGroup(groupName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.deleteGroup + id);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                                ConstantsMethod.showTip(MyContactsActivity.this, "网络异常！");
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        final String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                                    Gson gson = new Gson();
                                    AllContacts all = gson.fromJson(str, AllContacts.class);
                                    switch (all.getError_code()) {
                                        case 1:
                                            ConstantsMethod.showTip(MyContactsActivity.this, all.getReason());
                                            break;
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    //删除指定子项
    public void deleteChild(final int groupPos, final int childPos) {
        new ProgressBarLoadUtils(this).startProgressDialog();
        String groupName = groups.get(groupPos).getGroup_name();
        final List<MyContacts> list = mData.get(groupName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateContact;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("contact_id", list.get(childPos).getContactId() + "")
                        .add("use_status", 0 + "")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                                ConstantsMethod.showTip(MyContactsActivity.this, "网络异常！");
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                            Gson gson = new Gson();
                            final Result r = gson.fromJson(str, Result.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (r.getError_code()) {
                                        case 0:
                                            //完整数据中也删除
                                            MyContactsData.deleteMyContacts(list.get(childPos));
                                            myContacts.remove(list.get(childPos));
                                            resetComplete();
                                            myGroupAdapter.notifyDataSetChanged();
                                            //分组数据中删除
                                            list.remove(childPos);
                                            myContactsAdapter.notifyDataSetChanged();
                                            break;
                                        case 1:
                                            ConstantsMethod.showTip(MyContactsActivity.this, r.getReason());
                                            break;
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    //修改该项名称
    public void modifyName(final String modifyName) {
        new ProgressBarLoadUtils(this).startProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateGroup;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("token", Constants.TOKEN)
                        .add("group_name", modifyName)
                        .add("id", groups.get(currentGroup).getId() + "")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                                ConstantsMethod.showTip(MyContactsActivity.this, "网络异常！");
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            new ProgressBarLoadUtils(MyContactsActivity.this).stopProgressDialog();
                            Gson gson = new Gson();
                            final Result r = gson.fromJson(str, Result.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (r.getError_code()) {
                                        case 0:
                                            //修改组名称
                                            String groupName = groups.get(currentGroup).getGroup_name();
                                            if (!groupName.equals(modifyName)) {
                                                mData.put(modifyName, mData.get(groupName));
                                                mData.remove(groupName);
                                                groups.get(currentGroup).setGroup_name(modifyName);
                                                //修改后台数据
                                                MyContactsData.deleteMyGroup(groupName);
                                                MyContactsData.addMyGroup(modifyName);
                                            }
                                            myContactsAdapter.notifyDataSetChanged();
                                            break;
                                        case 1:
                                            ConstantsMethod.showTip(MyContactsActivity.this, r.getReason());
                                            break;
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    //弹修改对话框
    public void alertModifyDialog(String name) {
        dialog = new ModifyDialog(MyContactsActivity.this, name);
        edit_modify = dialog.getEditText();
        dialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentChild < 0) {
                    int i;
                    for (i = 0; i < groups.size(); i++) {
                        if (groups.get(i).getGroup_name().equals(edit_modify.getText().toString())) {
                            break;
                        }
                    }
                    if (i == groups.size()) {
                        modifyName(edit_modify.getText().toString());
                    } else if (currentGroup == i) {
                        ConstantsMethod.showTip(MyContactsActivity.this, "无修改");
                    } else {
                        ConstantsMethod.showTip(MyContactsActivity.this, "分组名字不能相同");
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //弹新增组对话框
    public void alertAddDialog(Context context) {
        dialog = new ModifyDialog(context, null);
        edit_modify = dialog.getEditText();
        dialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newGroupName = edit_modify.getText().toString();
                if(newGroupName != null && !newGroupName.trim().equals("")){
                    connectWork();
                    if (connection) {
                        addGroup(edit_modify.getText().toString());
                    }
                    dialog.dismiss();
                }else{
                    ConstantsMethod.showTip(MyContactsActivity.this, "分组名字不能为空");
                }

            }
        });
        dialog.show();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
