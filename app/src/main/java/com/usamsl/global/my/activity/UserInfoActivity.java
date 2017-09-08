package com.usamsl.global.my.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.my.biz.UserInfoBiz;
import com.usamsl.global.my.custom.CircleImageView;
import com.usamsl.global.my.custom.CustomUserInfoDialog_Photo;
import com.usamsl.global.my.custom.CustomUserInfoDialog_Profession;
import com.usamsl.global.my.custom.CustomUserInfoDialog_Sex;
import com.usamsl.global.my.entity.UserInfoEntity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/1.
 * 个人信息详情页面
 */
public class UserInfoActivity extends Activity implements View.OnClickListener{
    //返回
    private ImageView imgBack;
    //用户头像
    private CircleImageView imgUserPhoto;
    //用户名，用户手机号，用户邮箱
    private EditText edUserName,edEmail;
    //性别，职业
    private RelativeLayout relativeUserGender,relativeUserProfession;
    //性别，职业显示,保存
    private TextView tvUserSex,tvUserProfession,tvSave,edUserPhontNum;
    //用户数据的实体类
    private UserInfoEntity.Result resultEntity;
    //相册中获取的图片的Url
    private String photoUrl = "";
    //用于区分用户是否改变信息的变量
    private static int USER_HAS_EDIT_INFO = 100;
    //人员类型
    private String [] professions = {"在职人员","自由职业者","学龄前儿童","学生","退休人员"};
    //人员类型对应的ID
    private int PROFESSIONS_SELECT_POSTION = -1;
    //广播接收
    private UserInfoReceiver receiver;
    //文件的目录名称，文件名以时间格式为准
    File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),getPhotoName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setViews();
        getUserInfoData();
        receiver = new UserInfoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("USER_SELECT_PROFESSION");
        filter.addAction("USER_SELECT_SEX");
        this.registerReceiver(receiver,filter);
    }


    /**
     * 控件初始化
     */
    private void setViews() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgUserPhoto = (CircleImageView) findViewById(R.id.imgUserPhoto);
        edUserName = (EditText) findViewById(R.id.editUserName);
        edUserPhontNum = (TextView) findViewById(R.id.editUserPhoneNum);
        edEmail = (EditText) findViewById(R.id.editUserEmail);
        relativeUserGender = (RelativeLayout) findViewById(R.id.relativeUserGender);
        relativeUserProfession = (RelativeLayout) findViewById(R.id.relativeUserProfession);
        tvUserSex = (TextView) findViewById(R.id.tvUserSex);
        tvUserProfession = (TextView) findViewById(R.id.tvUserProfession);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvSave.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgUserPhoto.setOnClickListener(this);
        relativeUserGender.setOnClickListener(this);
        relativeUserProfession.setOnClickListener(this);
        imgUserPhoto.setBorderColor(this.getResources().getColor(R.color.Transparent));
        //定位光标到最后
        edUserName.setSelection(edUserName.getText().length());
//        edUserPhontNum.setSelection(edUserName.getText().length());
        edEmail.setSelection(edUserName.getText().length());
        //输入框监听文字内容是否发生变化
        userInfoEditListener();
    }

    /**
     * 使用系统当前日期加以调整作为照片的名称
     * **/
    private String getPhotoName() {
        // TODO Auto-generated method stub
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return format.format(date)+".jpg";
    }

    /**
     * 获取用户信息
     */
    private void getUserInfoData() {
        UserInfoBiz biz = new UserInfoBiz(this);
        biz.getUserInfoData();
    }

    /**
     * 得到用户信息数据
     * @param entity
     */
    public void setUserInfoData(UserInfoEntity entity){
        this.resultEntity = entity.getResult();
        if(ObjectIsNullUtils.objectIsNull(resultEntity)){
            if(ObjectIsNullUtils.TextIsNull(resultEntity.getPath_name())){//头像
                ImageLoadUtils.loadUserPhoto(resultEntity.getPath_name(),imgUserPhoto,this);
            }
            if(ObjectIsNullUtils.TextIsNull(resultEntity.getCust_name())){ //名字
                edUserName.setText(resultEntity.getCust_name());
                edUserName.setSelection(edUserName.getText().length());
            }
            if(ObjectIsNullUtils.TextIsNull(resultEntity.getSex())){ //性别
                tvUserSex.setText(resultEntity.getSex());
            }
            if(ObjectIsNullUtils.TextIsNull(resultEntity.getCust_type())){ //职业
                tvUserProfession.setText(resultEntity.getCust_type());
            }
            if(ObjectIsNullUtils.TextIsNull(resultEntity.getCust_tel())){ //手机号
                edUserPhontNum.setText(resultEntity.getCust_tel());
            }
            if(ObjectIsNullUtils.TextIsNull(resultEntity.getCust_email())){ //邮箱
                edEmail.setText(resultEntity.getCust_email());
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                isUserInfoChanged();
                break;

            case R.id.tvSave://保存
                saveUserInfo("save");
                break;

            case R.id.imgUserPhoto://头像，此处头像只从相册获取
                final CustomUserInfoDialog_Photo dialog_photo = new CustomUserInfoDialog_Photo(this);
                dialog_photo.setPhotoClickListener(new CustomUserInfoDialog_Photo.PhotoClickListener() {
                    @Override
                    public void album() {//进入相册
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, ConstantsCode.IMPORT_PHOTO);
                        dialog_photo.dismiss();
                    }

                    @Override
                    public void openCamera() {//打开相机
                        String SDState = Environment.getExternalStorageState();
                        if(SDState.equals(Environment.MEDIA_MOUNTED)){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));
                            startActivityForResult(intent, ConstantsCode.OPEN_CAMERA);
                        }
                        dialog_photo.dismiss();
                    }
                });
                dialog_photo.show();
                break;

            case R.id.relativeUserProfession://职业选择
                CustomUserInfoDialog_Profession dialog = new CustomUserInfoDialog_Profession(this);
                dialog.show();
                break;

            case R.id.relativeUserGender://性别
                CustomUserInfoDialog_Sex dialog_sex = new CustomUserInfoDialog_Sex(this);
                dialog_sex.show();
                break;
        }
    }


    /**
     * 是否更改信息
     */
    private void isUserInfoChanged() {
        //判断性别和职业是否发生改变
        if(ObjectIsNullUtils.TextIsNull(tvUserSex.getText().toString()) && ObjectIsNullUtils.TextIsNull(tvUserProfession.getText().toString())){
            if(!tvUserSex.getText().toString().equals(resultEntity.getSex())){
                if(!tvUserSex.getText().toString().equals("点此选择")){
                    USER_HAS_EDIT_INFO = 200;
                }
            }else if(!tvUserProfession.getText().toString().equals(resultEntity.getCust_type())){
                if(!tvUserProfession.getText().toString().equals("点此选择")){
                    USER_HAS_EDIT_INFO = 200;
                }
            }
        }
        //如果photoUrl ！= null 说明用户从相册调取过照片并且设置了头像,此时也是属于信息又更改过的
        if(ObjectIsNullUtils.TextIsNull(photoUrl)){
            USER_HAS_EDIT_INFO = 200;
        }
        //USER_HAS_EDIT_INFO == 100 用户什么都没有做，信息没有任何更改
        if(USER_HAS_EDIT_INFO == 100){
            this.finish();
        }else{
            //弹窗，提示用户是否保存当前更改过的信息
            saveDailog();
        }
    }

    /**
     * 提示用户是否保存当前信息的更改
     */
    private void saveDailog() {
        final CustomIsFormSaveDialog dialog = new CustomIsFormSaveDialog(UserInfoActivity.this, "是否保存更改内容", "提示","取消","确定");
        dialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                dialog.dismiss();
                saveUserInfo("back");
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        isUserInfoChanged();
    }

    /**
     * 保存用户信息
     */
    private void saveUserInfo(final String doWhat) {
        String url = UrlSet.saveUserInfo;
        OkHttpClient client = OkHttpManager.myClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token",Constants.TOKEN);
        //电话
        if(ObjectIsNullUtils.TextIsNull(edUserPhontNum.getText().toString().trim())){
            builder.add("cust_tel",edUserPhontNum.getText().toString());
        }else{
            builder.add("cust_tel","");
        }
        //邮箱
        if(ObjectIsNullUtils.TextIsNull(edEmail.getText().toString().trim())){
            builder.add("cust_email",edEmail.getText().toString());
        }else{
            builder.add("cust_email","");
        }
        //姓名
        if(ObjectIsNullUtils.TextIsNull(edUserName.getText().toString().trim())){
            builder.add("cust_name",edUserName.getText().toString());
        }else{
            builder.add("cust_name","");
        }
        //性别
        if(ObjectIsNullUtils.TextIsNull(tvUserSex.getText().toString())){
            if(!tvUserSex.getText().toString().equals("点此选择")){
                builder.add("sex",tvUserSex.getText().toString());
            }else{
                builder.add("sex","");
            }
        }else{
            builder.add("sex","");
        }
        //职业类型
        if(ObjectIsNullUtils.TextIsNull(tvUserProfession.getText().toString())){
            if(!tvUserProfession.getText().toString().equals("点此选择")){
                builder.add("cust_type",tvUserProfession.getText().toString());
            }else{
                builder.add("cust_type","");
            }
        }else{
            builder.add("cust_type","");
        }
        //头像照片
        if(ObjectIsNullUtils.TextIsNull(resultEntity.getCust_photo())){
            builder.add("cust_photo",resultEntity.getCust_photo());
        }else{
            builder.add("cust_photo","0");
        }
        //生成表单实体对象
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("FENG","错误"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject json = new JSONObject(result);
                    if(json.optInt("error_code") == 0){
                        //doWhat   是来自title处的保存，或者是来自返回键触发的
                        //如果是页面保存，则只提示。不会关闭页面
                        if(doWhat.equals("save")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserInfoActivity.this, "信息保存成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //用户提交过了头像，将photoUrl置成空
                            photoUrl = "";
                            USER_HAS_EDIT_INFO = 100;
                            //如果   PROFESSIONS_SELECT_POSTION != -1     说明用户选择了一个职业人员类型
                            if(PROFESSIONS_SELECT_POSTION != -1){
                                resultEntity.setCust_type(professions[PROFESSIONS_SELECT_POSTION]);
                            }
                            //设置用户的性别
                            resultEntity.setSex(tvUserSex.getText().toString());
                            //如果是来自返回，则需要关闭页面
                        }else if(doWhat.equals("back")){
                            UserInfoActivity.this.finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //头像图片有更改
//                File file = null;
//                if(ObjectIsNullUtils.TextIsNull(photoUrl)){
//                    file = new File(photoUrl);
//                }
//                if(file != null){
//                    uploadUserInfoPhoto(file,doWhat);
//                }
            }
        });
    }


    /**
     *用户的信息是否发生变化
     */
    private void userInfoEditListener() {
        //用户名字
        edUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(ObjectIsNullUtils.TextIsNull(s.toString())){
                    if(!s.toString().equals("点击修改")){
                        if(!s.toString().equals(resultEntity.getCust_name())){
                            USER_HAS_EDIT_INFO = 200;
                        }else{
                            USER_HAS_EDIT_INFO = 100;
                        }
                    }
                }
            }
        });
        //
//
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
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
                        //显示头像
                        imgUserPhoto.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap(photoUrl, imgUserPhoto.getWidth(), imgUserPhoto.getHeight()));
                        break;
                    case ConstantsCode.OPEN_CAMERA:
                        Uri path = data.getData();
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(filePath); // 根据路径获取数据
                            Bitmap bitmap = BitmapFactory.decodeStream(fis);
                            imgUserPhoto.setImageBitmap(bitmap);// 显示图片
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fis.close();// 关闭流
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                }
            }
        }else{
            if(requestCode == ConstantsCode.OPEN_CAMERA){
                Uri path = Uri.fromFile(filePath);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(filePath); // 根据路径获取数据
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    imgUserPhoto.setImageBitmap(bitmap);// 显示图片
                    photoUrl = filePath.getPath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(fis != null){
                            fis.close();// 关闭流
                        }else{
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        File file = null;
        if(ObjectIsNullUtils.TextIsNull(photoUrl)){
            file = new File(photoUrl);
        }
        if(file != null){
            uploadUserInfoPhoto(file);
        }
    }

    /**
     * 上传头像
     */
    private void uploadUserInfoPhoto(File file) {
        OkHttpClient client = OkHttpManager.myClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            builder.addFormDataPart("img", "1.jpg", RequestBody.create(Constants.MEDIA_TYPE_PNG, file));
            builder.addFormDataPart("show_order", "1");
            builder.addFormDataPart("visa_datum_type", "4");
            builder.addFormDataPart("is_del", "0");
            if(resultEntity.getCust_photo().equals("")){
                builder.addFormDataPart("attachment_id","0");
            }else{
                builder.addFormDataPart("attachment_id",resultEntity.getCust_photo());
            }
        }
        MultipartBody requestBody = builder.build();
        //构建请求
        final Request request = new Request.Builder()
                .url(UrlSet.upload)//地址
                .post(requestBody)//添加请求体
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final ResultId resultId = gson.fromJson(result, ResultId.class);
                if(ObjectIsNullUtils.objectIsNull(resultId)){
                    switch (resultId.getError_code()){
                        case 0:
                            resultEntity.setCust_photo(resultId.getReason_id()+"");
                            break;
                        case 1:

                            break;
                    }
                }
            }
        });
    }


    /**
     * 接收广播，职业选择
     */
    class UserInfoReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                int selectPostion = bundle.getInt("selectPosition",-1);
                String selectSex = bundle.getString("selectSex");
                //设置职业
                if(selectPostion >= 0){
                    tvUserProfession.setText(professions[selectPostion]);
                    PROFESSIONS_SELECT_POSTION = selectPostion;
                }
                //设置性别
                if(ObjectIsNullUtils.TextIsNull(selectSex)){
                    tvUserSex.setText(selectSex);
                }
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }
}
