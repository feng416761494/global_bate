package com.usamsl.global.order.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.my.entity.AllOrder;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.order.activity.OrderDetailsActivity;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/21.
 * 描述：未完成订单显示
 */
public class UnFinishOrderAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    //显示的签证国家集合
    private List<AllOrder.ResultBean> mData;

    public UnFinishOrderAdapter(Context mContext, List<AllOrder.ResultBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        final AllOrder.ResultBean order = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.unfinish_order_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.tv_paid = (TextView) view.findViewById(R.id.tv_paid);
            viewHolder.tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
            viewHolder.tv_continue = (TextView) view.findViewById(R.id.tv_continue);
            viewHolder.tv_1 = (TextView) view.findViewById(R.id.tv1);
            viewHolder.tv_2 = (TextView) view.findViewById(R.id.tv2);
            viewHolder.tv_3 = (TextView) view.findViewById(R.id.tv3);
            viewHolder.tv_4 = (TextView) view.findViewById(R.id.tv4);
            viewHolder.tv_5 = (TextView) view.findViewById(R.id.tv5);
            viewHolder.tv_details = (TextView) view.findViewById(R.id.tv_details);
            viewHolder.rl_last = (RelativeLayout) view.findViewById(R.id.rl_last);
            viewHolder.tv_hint3 = (TextView) view.findViewById(R.id.tv_hint3);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_name.setText("姓名：" + order.getContact_name());
        StringBuilder sb = new StringBuilder();
        sb.append(order.getVisa_name());
        if (order.getVisa_area_name() != null && !order.getVisa_area_name().equals("") &&
                !order.getVisa_area_name().equals(" ")) {
            sb.append("-");
            sb.append(order.getVisa_area_name());
        }
        viewHolder.tv_type.setText(sb.toString());
        viewHolder.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Dialog alertDialog =
                        new AlertDialog.Builder(mContext)
                                .setTitle("确定取消订单？").
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cancelOrderListener.cancelOrder(i);
                                        cancel(order.getOrder_id());
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create();
                alertDialog.show();*/
                final CustomIsFormSaveDialog dialog = new CustomIsFormSaveDialog(mContext, "是否取消此订单?", "提示");
                dialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doConfirm() {
                        cancelOrderListener.cancelOrder(i);
                        cancel(order.getOrder_id());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

       /* viewHolder.tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOrder addOrder = new AddOrder();
                addOrder.setCust_type(order.getCust_type());
                addOrder.setVisa_id(order.getVisa_id());
                addOrder.setContact_name(order.getContact_name());
                addOrder.setContact_id(order.getContact_id());
                Constants.SPEC_KEY = order.getPhoto_format();
                Constants.COUNTRY_ID = order.getCountry_id();
                try {
                    Constants.travelDate = ConstantsMethod.longToDate(order.getDepart_time());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Constants.IS_PAY = order.getIs_pay();
                UrlSet.countrySurface = order.getForm_url();
                Constants.COUNTRY = order.getCountry_name();
                //Is_pay == 1是线下支付
                if (order.getIs_pay() < 1) {
                    if (order.getVisa_id() > 1) {
                        switch (order.getOrder_status()) {
                            case 0:
                                Intent intent = new Intent(mContext, DocumentScanningActivity.class);
                                intent.putExtra("addOrder", addOrder);
                                intent.putExtra("order_id", order.getOrder_id());
                                mContext.startActivity(intent);
                                break;
                            case 1:
                                //跳转支付界面
                                intent = new Intent(mContext, PaymentActivity.class);
                                intent.putExtra("addOrder", addOrder);
                                intent.putExtra("order_id", order.getOrder_id());
                                mContext.startActivity(intent);
                                break;
                            case 2:
                                //跳转提交照片材料界面
                                Intent intent1 = new Intent(mContext, PhotoSubmissionActivity.class);
                                intent1.putExtra("addOrder", addOrder);
                                intent1.putExtra("order_id", order.getOrder_id());
                                mContext.startActivity(intent1);
                                break;
                            case 3:
                                //跳转预约时间界面
                                Intent intent2 = new Intent(mContext, InterviewTimeActivity.class);
                                intent2.putExtra("order_id", order.getOrder_id());
                                mContext.startActivity(intent2);
                                break;
                        }
                    } else {
                        switch (order.getOrder_status()) {
                            case 0:
                                Intent intent = new Intent(mContext, EvUsActivity.class);
                                intent.putExtra("addOrder", addOrder);
                                intent.putExtra("order_id", order.getOrder_id());
                                intent.putExtra("evus", "1");
                                mContext.startActivity(intent);
                                break;
                            case 1:
                                //跳转支付界面
                                intent = new Intent(mContext, PaymentActivity.class);
                                intent.putExtra("addOrder", addOrder);
                                intent.putExtra("order_id", order.getOrder_id());
                                intent.putExtra("evus", "1");
                                mContext.startActivity(intent);
                                break;
                        }
                    }
                } else {
                    if (order.getVisa_id() > 1) {
                        switch (order.getOrder_status()) {
                            case 0:
                                Intent intent = new Intent(mContext, DocumentScanningActivity.class);
                                intent.putExtra("addOrder", addOrder);
                                intent.putExtra("order_id", order.getOrder_id());
                                mContext.startActivity(intent);
                                break;
                            case 1:
                            case 2:
                                //跳转提交照片材料界面
                                Intent intent1 = new Intent(mContext, PhotoSubmissionActivity.class);
                                intent1.putExtra("addOrder", addOrder);
                                intent1.putExtra("order_id", order.getOrder_id());
                                mContext.startActivity(intent1);
                                break;
                            case 3:
                                //跳转预约时间界面
                                Intent intent2 = new Intent(mContext, InterviewTimeActivity.class);
                                intent2.putExtra("order_id", order.getOrder_id());
                                mContext.startActivity(intent2);
                                break;
                        }
                    } else {
                        switch (order.getOrder_status()) {
                            case 0:
                                Intent intent = new Intent(mContext, EvUsActivity.class);
                                intent.putExtra("addOrder", addOrder);
                                intent.putExtra("order_id", order.getOrder_id());
                                intent.putExtra("evus", "1");
                                mContext.startActivity(intent);
                                break;
                        }
                    }
                }
            }
        });*/
        if (order.getIs_pay() == 1) {
            //后台修改的支付状态
            viewHolder.tv_paid.setText("￥" + order.getPayment() + "（已付款）");
            viewHolder.tv_cancel.setVisibility(View.GONE);
        } else {
            if (order.getOrder_status() >= 2) {
                viewHolder.tv_paid.setText("￥" + order.getPayment() + "（已付款）");
                viewHolder.tv_cancel.setVisibility(View.GONE);
            } else {
                viewHolder.tv_paid.setText("￥" + order.getPayment() + "（未付款）");
                viewHolder.tv_cancel.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.tv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOrder addOrder = new AddOrder();
                addOrder.setCust_type(order.getCust_type());
                addOrder.setVisa_id(order.getVisa_id());
                addOrder.setContact_name(order.getContact_name());
                addOrder.setContact_id(order.getContact_id());
                Constants.SPEC_KEY = order.getPhoto_format();
                Constants.COUNTRY_ID = order.getCountry_id();
                Constants.IS_PAY = order.getIs_pay();
                UrlSet.countrySurface = order.getForm_url();
                Constants.COUNTRY = order.getCountry_name();
                //获取订单的状态
                Constants.STATUS = order.getOrder_status();
                try {
                    Constants.travelDate = ConstantsMethod.longToDate(order.getDepart_time());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //跳转至详情界面
                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                if(ObjectIsNullUtils.objectIsNull(order) && ObjectIsNullUtils.objectIsNull(addOrder)){
                    intent.putExtra("finish", 0);
                    intent.putExtra("order", order);
                    intent.putExtra("addOrder", addOrder);
                    mContext.startActivity(intent);
                }
            }
        });
        if (order.getVisa_id() > 1) {
            viewHolder.rl_last.setVisibility(View.VISIBLE);
            viewHolder.tv_hint3.setText("上传资料");
            viewHolder.tv_details.setVisibility(View.VISIBLE);
            if (order.getIs_pay() == 1) {
                switch (order.getOrder_status()) {
                    case 0:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 1:
                    case 2:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 3:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 4:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 5:
                    case 6:
                    case 7:
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    default:
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#78b200"));
                }
            } else {
                switch (order.getOrder_status()) {
                    case 0:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 1:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 2:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 3:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 4:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 5:
                    case 6:
                    case 7:
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    default:
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_4.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_5.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_4.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_5.setTextColor(Color.parseColor("#78b200"));
                }
            }
        } else if (order.getVisa_id() == 1) {
            //最后两项消失
            viewHolder.rl_last.setVisibility(View.GONE);
            viewHolder.tv_hint3.setText("审核中");
            //evus没有详情界面
            viewHolder.tv_details.setVisibility(View.VISIBLE);
            if (order.getIs_pay() == 1) {
                switch (order.getOrder_status()) {
                    case 0:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    default:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                }
            } else {
                switch (order.getOrder_status()) {
                    case 0:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    case 1:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg3);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#868c7a"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                        break;
                    default:
                        viewHolder.tv_1.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_2.setBackgroundResource(R.drawable.unfinish_step_bg1);
                        viewHolder.tv_3.setBackgroundResource(R.drawable.unfinish_step_bg2);
                        viewHolder.tv_1.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_2.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_3.setTextColor(Color.parseColor("#ffffff"));
                        viewHolder.tv_continue.setVisibility(View.GONE);
                }
            }
        }
        return view;
    }

    /**
     * 取消订单时，删除订单
     */
    public interface CancelOrderListener {
        void cancelOrder(int position);
    }

    private CancelOrderListener cancelOrderListener;

    public void setCancelOrderListener(CancelOrderListener cancelOrderListener) {
        this.cancelOrderListener = cancelOrderListener;
    }

    /**
     * 取消订单
     *
     * @param order_id
     */
    private void cancel(final int order_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.orderUpdateStop;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_id", order_id + "")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(mContext, "网络异常！");
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            Gson gson = new Gson();
                            final Result r = gson.fromJson(str, Result.class);
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (r.getError_code()) {
                                        case 1:
                                            ConstantsMethod.showTip(mContext, r.getReason());
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

    class ViewHolder {
        //姓名
        TextView tv_name;
        //类型
        TextView tv_type;
        //是否付款
        TextView tv_paid;
        //取消订单文本
        TextView tv_cancel;
        //继续文本
        TextView tv_continue;
        //基本信息
        TextView tv_1;
        //上传资料
        TextView tv_2;
        //付款
        TextView tv_3;
        //预约面试
        TextView tv_4;
        //领取签证
        TextView tv_5;
        //详情
        TextView tv_details;
        //evus区分
        RelativeLayout rl_last;
        //evus第三步
        TextView tv_hint3;
    }
}
