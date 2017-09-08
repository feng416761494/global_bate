package com.usamsl.global.index.step.step7.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2017/2/8.
 * 防止照片过大出现ooMB问题
 */
public class PhotoBitmapManager {
    public static Bitmap getFitSampleBitmap(String file_path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file_path, options);
        options.inSampleSize = getFitInSampleSize(width, height, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file_path, options);
    }

    public static int getFitInSampleSize(int reqWidth, int reqHeight, BitmapFactory.Options options) {
        int inSampleSize = 1;
        if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
            int widthRatio = Math.round((float) options.outWidth / (float) reqWidth);
            int heightRatio = Math.round((float) options.outHeight / (float) reqHeight);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        return inSampleSize;
    }
    /**
     * 根据需要的宽高压缩一张图片
     *
     * @param data 包含图片信息的byte数组
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data,int reqWidth,int reqHeight)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        opts.inSampleSize = calculateInSampleSize(opts, reqHeight, reqWidth);
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        return bitmap;
    }
    /**
     * 计算采样比
     */
    private static int calculateInSampleSize(BitmapFactory.Options opts,int reqHeight,int reqWidth)
    {
        if(opts == null)
            return -1;
        int width = opts.outWidth;
        int height = opts.outHeight;

        int sampleSize = 1;

        if(width > reqWidth || height > reqHeight)
        {
            int heightRatio = (int) (height/(float)reqHeight);
            int widthRatio = (int) (width/(float)reqWidth);
            sampleSize = (heightRatio > widthRatio) ? widthRatio : heightRatio;
        }
        return sampleSize;
    }
}
