package com.usamsl.global.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.usamsl.global.R;

/**
 * 冯井起
 */

public class ImageLoadUtils {
	public static void loadBitmap(String url,ImageView img,Context context){
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				// 正在加载
				.showImageOnLoading(R.drawable.fail)
				// 空图片
				.showImageForEmptyUri(R.drawable.camera)
				// 错误图片
				.showImageOnFail(R.drawable.camera)
				.cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context.getApplicationContext())
//				.defaultDisplayImageOptions(options)
//				.discCacheSize(50 * 1024 * 1024)//
//				.discCacheFileCount(100)// 缓存一百张图片
//				.writeDebugLogs().build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.displayImage(url, img, options);
	}

	public static void loadCameraImage(String url,ImageView img,Context context){
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.camera)
				// 正在加载
				.showImageForEmptyUri(R.drawable.camera)
				// 空图片
				.showImageOnFail(R.drawable.camera)
				// 错误图片
				.cacheInMemory(true).cacheOnDisk(false).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.displayImage(url, img, options);
	}


	public static void loadUserPhoto(String url,ImageView img,Context context){
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_info_user1)
				// 正在加载
				.showImageForEmptyUri(R.drawable.user_info_user1)
				// 空图片
				.showImageOnFail(R.drawable.user_info_user1)
				// 错误图片
				.cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.displayImage(url, img, options);
	}



	public static void loadAllImage(String url,ImageView img,Context context){
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.fail)
				// 正在加载
				.showImageForEmptyUri(R.drawable.fail)
				// 空图片
				.showImageOnFail(R.drawable.fail)
				// 错误图片
				.cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.displayImage(url, img, options);
	}

	/**
	 * 首页banner1
	 * @param url
	 * @param img
	 * @param context
     */
	public static void loadBanner1Image(String url,ImageView img,Context context){
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.fail1)
				// 正在加载
				.showImageForEmptyUri(R.drawable.fail1)
				// 空图片
				.showImageOnFail(R.drawable.fail1)
				// 错误图片
				.cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.displayImage(url, img, options);
	}

	/**
	 * 首页Bannner2
	 * @param url
	 * @param img
	 * @param context
     */
	public static void loadBanner2Image(String url,ImageView img,Context context){
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.fail2)
				// 正在加载
				.showImageForEmptyUri(R.drawable.fail2)
				// 空图片
				.showImageOnFail(R.drawable.fail2)
				// 错误图片
				.cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.displayImage(url, img, options);
	}
}
