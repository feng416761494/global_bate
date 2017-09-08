package com.usamsl.global.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/23.
 * 用正则表达式验证正确与否
 */
public class Verification {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或7或8，其他位置的可以为0-9
    */
        String telRegex = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        /*String REGEX_EMAIL = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$";*/
        boolean flag;
        if (TextUtils.isEmpty(email)) {
            flag = false;
        } else {
            //return Pattern.matches(REGEX_EMAIL, email);
            if (email.indexOf("@") != -1 && email.indexOf(".") != -1) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }
}
