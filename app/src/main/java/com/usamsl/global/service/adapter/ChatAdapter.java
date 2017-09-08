package com.usamsl.global.service.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.service.entity.ChatModel;
import com.usamsl.global.service.entity.ItemModel;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2017/2/6
 * 聊天对话框
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseAdapter> {
    private List<ItemModel> dataList = new ArrayList<>();

//    public ChatAdapter(List<ItemModel> mDataList){
//        this.dataList = mDataList;
//    }

    public void replaceAll(List<ItemModel> list) {
        //数据源先清空
        dataList.clear();
        //将新数据添加到源数据中
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<ItemModel> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
//            notifyItemRangeChanged(dataList.size(), list.size());
            notifyItemInserted(dataList.size()-1);
        }
    }


    public void Refresh(ArrayList<ItemModel> itemModelList) {
        if (dataList != null && itemModelList != null) {
            dataList.addAll(0,itemModelList);
            notifyItemRangeChanged(0, itemModelList.size());
//            notifyItemInserted(0);
        }
    }

    public void setData(List<ItemModel> chatContentList) {
        this.dataList = chatContentList;
    }


    @Override
    public BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.CHAT_A:
                return new ChatAViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_a, parent, false));
            case ItemModel.CHAT_B:
                return new ChatBViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_b, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseAdapter holder, int position) {
        holder.setData(dataList.get(position).object, position);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }



    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(Object object, int position) {

        }
    }

    private class ChatAViewHolder extends BaseAdapter {
        private TextView tv;

        public ChatAViewHolder(View view) {
            super(view);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Object object, final int position) {
            super.setData(object, position);
            final ChatModel model = (ChatModel) object;
            tv.setText(model.getContent());
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClickListener.onItemLongClickListener(view, position,model.getContent());
                    return false;
                }
            });
        }
    }

    private class ChatBViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        public ChatBViewHolder(View view) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Object object, int position) {
            super.setData(object, position);
            ChatModel model = (ChatModel) object;
            if (Constants.USER_LOGIN) {
                ic_user.setImageResource(R.drawable.user_up);
            } else {
                ic_user.setImageResource(R.drawable.user_down);
            }
            tv.setText(model.getContent());
        }
    }

    //文本长按事件
    public interface RecyclerViewOnItemLongClickListener {
        boolean onItemLongClickListener(View view, int position,String content);
    }

    private RecyclerViewOnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(RecyclerViewOnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
