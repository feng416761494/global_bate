package com.usamsl.global.my.custom;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.usamsl.global.R;

public class CustomUserInfoDialog_Sex extends Dialog implements View.OnClickListener{

    private Context context;
    private String confirm;
    private LayoutInflater inflater;
    private DialogClickListener listener;
    private View view;
    private String [] professions = {"男","女"};

    public CustomUserInfoDialog_Sex(Context context2) {
        super(context2, R.style.CustomDialog);
        this.context = context2;
        inflater = LayoutInflater.from(context2);
    }


    @Override
    public void onClick(View v) {
        listener.selectProfessions(v);
    }


    public interface DialogClickListener {
       void selectProfessions(View v);
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
        view = inflater.inflate(R.layout.custom_dialog_user_info_sex, null);
        setContentView(view);
        ListView lsvDialogUserInfo = (ListView) view.findViewById(R.id.lsvDialogUserInfo);
        lsvDialogUserInfo.setAdapter(new DialogUserInfoAdapter());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lsvDialogUserInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("USER_SELECT_SEX");
                intent.putExtra("selectSex",professions[position]);
                context.sendBroadcast(intent);
                CustomUserInfoDialog_Sex.this.dismiss();
            }
        });
    }



    class DialogUserInfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return professions.length;
        }

        @Override
        public Object getItem(int position) {
            return professions[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DialogUserInfoViewHolder holder = null;
            if(convertView == null){
                holder = new DialogUserInfoViewHolder();
                convertView = inflater.inflate(R.layout.custom_dialog_user_info_item,null);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
                convertView.setTag(holder);
            }else{
                holder = (DialogUserInfoViewHolder) convertView.getTag();
            }
            String itemContent = professions[position];
            holder.tvContent.setText(itemContent);
            return convertView;
        }

        class DialogUserInfoViewHolder{
            TextView tvContent;
        }
    }
}
