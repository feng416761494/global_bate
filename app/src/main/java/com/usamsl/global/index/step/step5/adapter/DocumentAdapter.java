package com.usamsl.global.index.step.step5.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.index.step.step5.activity.DocumentScanningActivity;
import com.usamsl.global.index.step.step7.activity.PassportActivity;
import com.usamsl.global.index.step.step7.activity.PhotoActivity;
import com.usamsl.global.index.step.step7.activity.PhotoSubmissionActivity;
import com.usamsl.global.index.step.step7.activity.TakeIdCardActivity;
import com.usamsl.global.index.step.step7.activity.TakeIdCardActivity_new;
import com.usamsl.global.index.step.step7.activity.TakePhotoActivity;
import com.usamsl.global.index.step.step7.adapter.PhotoSubmissionRecyclerViewAdapter;
import com.usamsl.global.index.step.step7.entity.PhotoSubmission;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.view.MyListView;

import java.util.List;

/**
 * Created by MSL Vacation Inc on 2017/3/7.
 * 描述：证件扫描照片提交
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {
    //上下文
    private Context mContext;
    //显示的Country集合
    private List<PhotoSubmission> mData;

    public DocumentAdapter(Context mContext, List<PhotoSubmission> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.photo_submission_adapter, null);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final PhotoSubmission photoSubmission = mData.get(position);
        viewHolder.tv_material.setText((position + 1) + "." + photoSubmission.getTypeMaterial());
        if (photoSubmission.isNecessary()) {
            viewHolder.tv_necessary.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_necessary.setVisibility(View.GONE);
        }
        viewHolder.adapter = new PhotoSubmissionRecyclerViewAdapter(photoSubmission.getTypeMaterialDemand(), mContext);
        viewHolder.recyclerView.setAdapter(viewHolder.adapter);
        if (photoSubmission.getPhotoUrl() != null) {
            //是否为下载图片（1：是，0：否）
            int photo = 0;
            if ((photoSubmission.getPhotoUrl().substring(0, 1).equals("h"))) {
                photo = 1;
            }
            switch (photo) {
                case 0:
                    if (photoSubmission.getVisa_datum_type().equals("1")) {
                        String[] photoUrl = photoSubmission.getPhotoUrl().split("#");
                        viewHolder.img_photo.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap(photoUrl[0],
                                viewHolder.img_photo.getWidth(), viewHolder.img_photo.getHeight()));

                    } else {
                        viewHolder.img_photo.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap(photoSubmission.getPhotoUrl(),
                                viewHolder.img_photo.getWidth(), viewHolder.img_photo.getHeight()));
                    }
                    break;
                case 1:
                    if (photoSubmission.getVisa_datum_type().equals("1")) {
                        String[] photoUrl = photoSubmission.getPhotoUrl().split("#");
                        ImageLoadUtils.loadBitmap(photoUrl[0],viewHolder.img_photo,mContext);
//                        Picasso.with(mContext)
//                                .load(photoUrl[0])
//                                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                                .error(R.drawable.camera)
//                                .into(viewHolder.img_photo);
                    } else {
                        ImageLoadUtils.loadBitmap(photoSubmission.getPhotoUrl(),viewHolder.img_photo,mContext);
//                        Picasso.with(mContext)
//                                .load(photoSubmission.getPhotoUrl())
//                                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                                .error(R.drawable.camera)
//                                .into(viewHolder.img_photo);
                    }
                    break;
            }
        }

        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoSubmission.getVisa_datum_type().equals("1")) {
                    Intent intent = new Intent(mContext, TakeIdCardActivity_new.class);
                    intent.putExtra("photoSubmission", photoSubmission);
                    ((DocumentScanningActivity) mContext).startActivityForResult(intent, ConstantsCode.ID_CARD_PHOTO);
                } else if (photoSubmission.getVisa_datum_type().equals("2")) {
                    Intent intent = new Intent(mContext, PassportActivity.class);
                    intent.putExtra("photoSubmission", photoSubmission);
                    ((DocumentScanningActivity) mContext).startActivityForResult(intent, ConstantsCode.PASSPORT_PHOTO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //材料
        TextView tv_material;
        //要求
        MyListView recyclerView;
        //是否为必要文件
        TextView tv_necessary;
        PhotoSubmissionRecyclerViewAdapter adapter;
        ImageView img_photo;
        RelativeLayout rl;

        public ViewHolder(View itemView) {
            super(itemView);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            tv_material = (TextView) itemView.findViewById(R.id.tv_material);
            tv_necessary = (TextView) itemView.findViewById(R.id.tv_necessary);
            recyclerView = (MyListView) itemView.findViewById(R.id.recycle);
            img_photo = (ImageView) itemView.findViewById(R.id.img_photo);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

