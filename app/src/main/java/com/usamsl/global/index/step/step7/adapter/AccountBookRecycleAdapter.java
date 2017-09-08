package com.usamsl.global.index.step.step7.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.index.step.step7.entity.AccountBook;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.index.util.XCRoundRectImageView;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.view.RecyclerImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class AccountBookRecycleAdapter extends RecyclerView.Adapter<AccountBookRecycleAdapter.AccountBookViewHolder> {

    private Context context;
    private List<AccountBook> mData;
    private LayoutInflater inflater;
    private AccountItemClickListener clickListener;


    public AccountBookRecycleAdapter(Context context2, List<AccountBook> mData2) {
        this.context = context2;
        this.mData = mData2;
        inflater = LayoutInflater.from(context2);
    }

    //给控件添加点击监听和长按监听
    public interface AccountItemClickListener {
        void onItemClick(View view, int position);

        void onItemDeleteClick(View view, int position, int showId);

        void onItemLongClick(View view, int position);
    }

    public void setAccountItemClickListener(AccountItemClickListener clickListener2) {
        this.clickListener = clickListener2;
    }

    @Override
    public AccountBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AccountBookViewHolder holder = new AccountBookViewHolder(inflater.inflate(R.layout.account_book_adapter, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final AccountBookViewHolder holder, final int position) {
        final AccountBook accountBook = mData.get(position);
        //是否存在图片链接
        if (accountBook.getPhotoUrl() != null && !accountBook.getPhotoUrl().equals("null")) {
            if (accountBook.getPhotoUrl().substring(0, 1).equals("h")) {
                if (position != mData.size() - 1) {
                    ImageLoadUtils.loadBitmap(accountBook.getPhotoUrl(), holder.img_photo, context);
                } else {
                    Picasso.with(context)
                            .load(accountBook.getPhotoUrl())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .config(Bitmap.Config.RGB_565)
                            .placeholder(R.drawable.fail)
                            .error(R.drawable.add)
                            .into(holder.img_photo);
                }
            } else {
                holder.img_photo.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap(accountBook.getPhotoUrl(),
                        holder.img_photo.getWidth(), holder.img_photo.getHeight()));
            }
            /*if (accountBook.getPhotoUrl().equals("h")) {
                holder.img_photo.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                holder.img_photo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }*/
        }
        //设置删除logo的显示或者不显示
        if (Constants.UPDATE && mData.size() - 1 != position) {
            holder.img_delete.setVisibility(View.VISIBLE);
            //删除logo出现动画
            Animation translateAnimation = new TranslateAnimation(0.1f, -5.0f, 0.1f, 0.1f);
            translateAnimation.setDuration(10);
            translateAnimation.setRepeatMode(Animation.REVERSE);
            translateAnimation.setRepeatCount(6);
            holder.img_delete.setAnimation(translateAnimation);
        } else {
            holder.img_delete.setVisibility(View.GONE);
        }


        //监听设置
        if (clickListener != null) {
            //照片点击事件
            holder.img_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickPosition = holder.getLayoutPosition();
                    clickListener.onItemClick(holder.img_photo, position);
                }
            });

            //照片长按事件
            holder.img_photo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //int clickPosition = holder.getLayoutPosition();
                    clickListener.onItemLongClick(holder.img_photo, position);
                    return true;
                }
            });

            //删除logo点击事件
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickPosition = holder.getLayoutPosition();
                    clickListener.onItemDeleteClick(holder.img_delete, position, accountBook.getId());
                }
            });


        }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class AccountBookViewHolder extends RecyclerView.ViewHolder {

        XCRoundRectImageView img_photo;
        ImageView img_delete;

        public AccountBookViewHolder(View itemView) {
            super(itemView);
            //照片和删除logo
            img_photo = (XCRoundRectImageView) itemView.findViewById(R.id.img_photo);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
        }
    }
}
