package com.usamsl.global.index.step.step7.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtil {

    public static String newImageName() {
        String uuidStr = UUID.randomUUID().toString();
        return uuidStr.replaceAll("-", "") + ".jpg";
    }

    @SuppressWarnings("resource")
    public static byte[] getByteFromPath(String filePath) {
        byte[] bytes = null;
        try {
            File file = new File(filePath);
            if (file.length() > Integer.MAX_VALUE) {
                throw new IOException("File is to large :" + file.getName());
            }
            if (file.exists()) {
                int offset = 0;
                int numRead = 0;
                InputStream is = new FileInputStream(file);
                bytes = new byte[(int) file.length()];
                while (offset < bytes.length &&
                        (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }
                if (offset < bytes.length) {
                    throw new IOException("Could not completely read file "
                            + file.getName());
                }
                is.close();
                return bytes;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bytes = null;
        } finally {
            bytes = null;
        }

        return null;
    }

    /**
     * 把照片存到本地
     *
     * @param bmp
     * @return
     */
    public static String saveImage(Context context, Bitmap bmp) {
        String photoUrl;
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "msl");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        photoUrl = Environment.getExternalStorageDirectory() + "/msl/" + fileName;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoUrl)));
        return photoUrl;
    }
}
