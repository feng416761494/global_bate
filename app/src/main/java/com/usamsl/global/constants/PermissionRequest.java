package com.usamsl.global.constants;

import android.Manifest;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Administrator on 2017/1/21.
 * 权限申请
 */
public class PermissionRequest {
    public static String[] str={ Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

}
