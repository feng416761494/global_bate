package com.usamsl.global.login.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/22.
 */
public class PatternUtils {
    public static boolean passwordIsMatcher(String passWord){
        String regEx = "^[a-zA-Z0-9]{6,15}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(passWord);
        boolean b = matcher.matches();
        return matcher.matches();
    }
}
