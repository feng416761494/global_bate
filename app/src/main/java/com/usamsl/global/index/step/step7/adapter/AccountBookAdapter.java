package com.usamsl.global.index.step.step7.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.index.step.step7.entity.AccountBook;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.view.RecyclerImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 * 描述：户口本
 */
public class AccountBookAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    //显示的已有联系人集合
    private List<AccountBook> mData;

    public AccountBookAdapter(Context mContext, List<AccountBook> mData) {
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
        final ViewHolder viewHolder;
        final AccountBook accountBook = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.account_book_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.img_photo = (RecyclerImageView) view.findViewById(R.id.img_photo);
            viewHolder.img_delete = (ImageView) view.findViewById(R.id.img_delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (accountBook.getPhotoUrl() != null && !accountBook.getPhotoUrl().equals("null")) {
            if (accountBook.getPhotoUrl().substring(0, 1).equals("h")) {
                if (i != mData.size() - 1) {
                   /* Picasso.with(mContext)
                            .load(accountBook.getPhotoUrl())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.camera)
                            .into(viewHolder.img_photo);*/
                    ImageLoadUtils.loadBitmap(accountBook.getPhotoUrl(), viewHolder.img_photo, mContext);
                }else {
                    Picasso.with(mContext)
                            .load(accountBook.getPhotoUrl())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.add)
                            .into(viewHolder.img_photo);
                }
            } else {
                viewHolder.img_photo.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap(accountBook.getPhotoUrl(),
                        viewHolder.img_photo.getWidth(), viewHolder.img_photo.getHeight()));
            }
            if (accountBook.getPhotoUrl().equals("h")) {
                viewHolder.img_photo.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                viewHolder.img_photo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        }
        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhotoListener.update(accountBook.getId(), i);
            }
        });
        //设置删除logo的显示或者不显示
        if (Constants.UPDATE && mData.size() - 1 != i) {
            viewHolder.img_delete.setVisibility(View.VISIBLE);
            //删除logo出现动画
            Animation translateAnimation = new TranslateAnimation(0.1f, -5.0f, 0.1f, 0.1f);
            translateAnimation.setDuration(10);
            translateAnimation.setRepeatMode(Animation.REVERSE);
            translateAnimation.setRepeatCount(6);
            viewHolder.img_delete.setAnimation(translateAnimation);
        } else {
            viewHolder.img_delete.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder {
        //图片
        RecyclerImageView img_photo;
        ImageView img_delete;
    }

    /**
     * 点击编辑的时候，提供activity接口，把show_order传递过去
     */
    public interface UpdatePhotoListener {
        void update(int id, int i);
    }

    private UpdatePhotoListener updatePhotoListener;

    public void setUpdatePhotoListener(UpdatePhotoListener updatePhotoListener) {
        this.updatePhotoListener = updatePhotoListener;
    }
}
