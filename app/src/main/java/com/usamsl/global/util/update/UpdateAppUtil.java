package com.usamsl.global.util.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.main.MainActivity;


/**
 * 2017/2/23
 * 下载新版本
 */
public class UpdateAppUtil {

    /**
     * 更新APP
     *
     * @param context
     */
    public static void updateApp(final Context context, final String app_http) {
        MainActivity.getAPPServerVersion(context, new MainActivity.VersionCallBack() {
            @Override
            public void callBack(final String versionCode) {
                if (versionCode != null) {
                    //服务器版本号和本地版本号作比较，如果不同则会提示更新
                    if (!versionCode.equals(Constants.version)) {
                        ConfirmDialog dialog = new ConfirmDialog(context, new Callback() {
                            @Override
                            public void callback() {
                                //服务器app_http
                                DownloadAppUtils.downloadForAutoInstall(context, app_http, "demo.apk", "正在更新");
                            }
                        });
                        dialog.setContent("发现新版本\n是否下载更新?");
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                }
            }
        });
    }

}
