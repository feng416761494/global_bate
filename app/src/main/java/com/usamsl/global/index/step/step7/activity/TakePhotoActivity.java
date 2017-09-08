package com.usamsl.global.index.step.step7.activity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.hardware.Camera.Size;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.view.CustomProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 各种证件的拍摄
 * 时间：2017/2/6
 */
public class TakePhotoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    //拍照
    private ImageView img_take;
    //从本地获取
    private ImageView img_import;
    private Camera mCamera;
    //照片名字
    private String mFileName;
    //照片路径
    private String photoUrl;
    private OrientationEventListener mOrEventListener; // 设备方向监听器
    private boolean mCurrentOrientation = true; // 当前设备方向 横屏false,竖屏true
    private Camera.Parameters parameters;
    private boolean flash = false;//闪光灯
    //闪光灯
    private ImageView img_flash;
    //返回键
    private ImageView img_back;
    //确定按钮
    private ImageView img_ok;
    //取消按钮
    private ImageView img_cancel;
    private int width, height;//surfaceView预览最接近宽高
    //从本地获取照片显示
    private ImageView img;

    //此字段是用于区分拍照的需要是从哪里来的，身份证或者护照
    private String whereFrom = "";
    //罩层，防止用户拍照操作过快
    private RelativeLayout relativeLoad;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        ActivityManager.getInstance().addActivity(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            whereFrom = bundle.getString("FROM_WHERE");
        }
        initView();
        initData();
        toListener();
    }


    private void initData() {
        startOrientationChangeListener(); // 启动设备方向监听器
        SurfaceHolder holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this); // 回调接口
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getBaseContext(), "请插入存储卡", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    /**
     * 判断横竖屏
     */
    private void startOrientationChangeListener() {
        mOrEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)
                        || ((rotation >= 135) && (rotation <= 225))) {// portrait
                    mCurrentOrientation = true;
                    // Log.i(TAG, "竖屏");
                } else if (((rotation > 45) && (rotation < 135))
                        || ((rotation > 225) && (rotation < 315))) {// landscape
                    mCurrentOrientation = false;
                    // Log.i(TAG, "横屏");
                }
            }
        };
        mOrEventListener.enable();
    }

    private void toListener() {
        img_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ProgressBarLoadUtils(TakePhotoActivity.this).startProgressDialog_takePhoto();
                mCamera.takePicture(mShutter, null, mJpeg);
                take();
                //这里设置1.5秒的延迟效果，是为了防止用户操作过快，在onActivityForResult中无法获取photoUrl
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new ProgressBarLoadUtils(TakePhotoActivity.this).stopProgressDialog_takePhoto();
                    }
                }, 1500);

            }
        });
        img_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ConstantsCode.IMPORT_PHOTO);
            }
        });
        img_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flash) {
                    flash = false;
                    img_flash.setImageResource(R.drawable.flash1);
                } else {
                    flash = true;
                    img_flash.setImageResource(R.drawable.flash);
                }
                initCamera();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.setPhoto = photoUrl;
                Intent data = new Intent();
                data.putExtra("photoUrl", photoUrl);
                data.putExtra("order_datum_id", getIntent().getIntExtra("order_datum_id", 1));
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre();
                initCamera();
            }
        });
    }

    //拍照之前
    private void pre() {
        img_flash.setVisibility(View.VISIBLE);
        img_take.setVisibility(View.VISIBLE);
        img_import.setVisibility(View.VISIBLE);
        img_back.setVisibility(View.VISIBLE);
        img_ok.setVisibility(View.GONE);
        img_cancel.setVisibility(View.GONE);
        img.setVisibility(View.GONE);
        surfaceView.setVisibility(View.VISIBLE);
    }

    //拍照之后
    private void take() {
        img_flash.setVisibility(View.GONE);
        img_take.setVisibility(View.GONE);
        img_import.setVisibility(View.GONE);
        img_back.setVisibility(View.GONE);
        img_ok.setVisibility(View.VISIBLE);
        img_cancel.setVisibility(View.VISIBLE);
    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        img_import = (ImageView) findViewById(R.id.img_import);
        img_take = (ImageView) findViewById(R.id.img_take);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_flash = (ImageView) findViewById(R.id.img_flash);
        img_ok = (ImageView) findViewById(R.id.img_ok);
        img_cancel = (ImageView) findViewById(R.id.img_cancel);
        img = (ImageView) findViewById(R.id.img);
        relativeLoad = (RelativeLayout)findViewById(R.id.relativeLoad);
        if(whereFrom != null && !whereFrom.equals("")){
            if(whereFrom.equals("IdCard_front")){
                surfaceView.setBackgroundDrawable(getResources().getDrawable(R.drawable.front));
            }else if(whereFrom.equals("IdCard_behind")){
                surfaceView.setBackgroundDrawable(getResources().getDrawable(R.drawable.behind));
            }else if(whereFrom.equals("Passport")){
                surfaceView.setBackgroundDrawable(getResources().getDrawable(R.drawable.passport));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // 开启相机
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mCamera = Camera.open(0);
                // i=0 表示后置相机
            } else {
                mCamera = Camera.open();
            }
            return;
        }
        Uri uri = data.getData();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ConstantsCode.IMPORT_PHOTO:
                    if (uri == null) {
                        return;
                    }
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                            photoUrl = cursor.getString(columnIndex);
                        }
                        cursor.close();
                    } else {
                        if (uri != null) {
                            String tmpPath = uri.getPath();
                            if (tmpPath != null && (tmpPath.endsWith(".jpg") ||
                                    tmpPath.endsWith(".png") || tmpPath.endsWith(".gif"))) {
                                photoUrl = tmpPath;
                            }
                        }
                    }
                    break;
            }
            take();
            img.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            img.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap
                    (photoUrl, surfaceView.getWidth(), surfaceView.getHeight()));
        }
    }

    //三星手机系统适配，检测照片旋转
    public static int getPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /* 图像数据处理还未完成时的回调函数 */
    private Camera.ShutterCallback mShutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            // 一般显示进度条
        }
    };

    /* 图像数据处理完成后的回调函数 */
    private Camera.PictureCallback mJpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 保存图片
            mFileName = UUID.randomUUID().toString() + ".jpg";
            FileOutputStream out = null;
            try {
                out = openFileOutput(mFileName, Context.MODE_PRIVATE);
                int degree = getPictureDegree(mFileName);
                Bitmap oldBitmap = PhotoBitmapManager.
                        decodeSampledBitmapFromByteArray(data, surfaceView.getWidth(), surfaceView.getHeight());
                if (degree == 0) {
                    out.write(data);
                }else {
                    byte[] newData = null;
                    Matrix matrix = new Matrix();
                    matrix.setRotate(degree);
                    Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
                            oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    newData = baos.toByteArray();
                    out.write(newData);
                }

               /* byte[] newData = null;
                if (mCurrentOrientation) {
                    // 竖屏时，旋转图片再保存
                    Bitmap oldBitmap = PhotoBitmapManager.decodeSampledBitmapFromByteArray(data, surfaceView.getWidth(), surfaceView.getHeight());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    oldBitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(90);
                    Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
                            oldBitmap.getWidth(), oldBitmap.getHeight(),
                            matrix, true);
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    newData = baos.toByteArray();
                    out.write(newData);
                } else {
                    out.write(data);
                }*/

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    System.gc();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            photoUrl = getFileStreamPath(mFileName).getAbsolutePath();
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览
        this.width = width;
        this.height = height;
        if (width > height) {
            //旋转相机
            //横屏
            mCamera.setDisplayOrientation(0);
        } else {
            //竖屏
            mCamera.setDisplayOrientation(90);
        }
        initCamera();//实现相机的参数初始化
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // SurfaceView创建时，建立Camera和SurfaceView的联系
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // SurfaceView销毁时，取消Camera预览
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开启相机
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
            // i=0 表示后置相机
        } else {
            mCamera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 释放相机
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }

    }

    @Override
    protected void onDestroy() {
        img.setImageResource(0);
        surfaceView.setBackgroundResource(0);
        img = null;
        surfaceView = null;
        System.gc();
        super.onDestroy();
    }

    // 取照片能适用的最大的SIZE
    private Size getBestSupportedSize(List<Size> sizes) {
        Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }

    //获取预览最接近屏幕大小
    private Size getBestSupportedSize1(List<Size> sizes) {
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float) width / (float) height;
        Size best = null;
        for (Size s : sizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
            if (tmp < mindiff) {
                mindiff = tmp;
                best = s;
            }
        }
        return best;
    }

    //相机参数的初始化设置
    private void initCamera() {
        parameters = mCamera.getParameters();
        Size largestSize = getBestSupportedSize1(parameters.getSupportedPreviewSizes());// 设置捕捉图片尺寸
        parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
        largestSize = getBestSupportedSize(parameters.getSupportedPictureSizes());
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setPictureSize(largestSize.width, largestSize.height);// 部分定制手机，无法正常识别该方法。
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        if (flash) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        } else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        mCamera.setParameters(parameters);
        mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
        try {
            mCamera.startPreview();
        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    }
}
