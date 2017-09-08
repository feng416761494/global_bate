package com.usamsl.global.index.step.step7.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.index.step.step7.entity.AccountBook;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.index.util.BitmpUtils;
import com.usamsl.global.index.util.XCRoundRectImageView;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.view.RecyclerImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 * 描述：身份证
 */
public class TakeIdAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    //显示的已有联系人集合
    private List<AccountBook> mData;

    public TakeIdAdapter(Context mContext, List<AccountBook> mData) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final AccountBook accountBook = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.take_id_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.img_photo = (XCRoundRectImageView) view.findViewById(R.id.img_photo);
            viewHolder.tv=(TextView)view.findViewById(R.id.tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i==0){
            viewHolder.tv.setText("身份证正面");
        }else {
            viewHolder.tv.setText("身份证反面");
        }
        if (accountBook.getPhotoUrl() != null && !accountBook.getPhotoUrl().equals("null")) {
            if (accountBook.getPhotoUrl().substring(0, 1).equals("h")) {
                ImageLoadUtils.loadBitmap(accountBook.getPhotoUrl(),viewHolder.img_photo,mContext);
//                Picasso.with(mContext)
//                        .load(accountBook.getPhotoUrl())
//                        .memoryPolicy(MemoryPolicy.NO_CACHE)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.camera)
//                        .into(viewHolder.img_photo);
            } else {
//                viewHolder.img_photo.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap(accountBook.getPhotoUrl(),
//                        viewHolder.img_photo.getWidth(), viewHolder.img_photo.getHeight()));
                /**2017/5/15圆角图片修改**/
                viewHolder.img_photo.setImageBitmap(BitmpUtils.getSmallBitmap(accountBook.getPhotoUrl(),450,450));
            }
        }
        return view;
    }

    class ViewHolder {
        //图片
        XCRoundRectImageView img_photo;
        TextView tv;
    }
}
