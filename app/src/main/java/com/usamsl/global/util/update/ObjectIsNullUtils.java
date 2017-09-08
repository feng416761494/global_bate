package com.usamsl.global.util.update;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
 */
public class ObjectIsNullUtils {

    public static boolean TextIsNull(String content){

        if(content == null || content.equals("")){
            return false;
        }else{
            return true;
        }
    }


    public static boolean objectIsNull(Object obj){

        if(obj == null){
            return false;
        }else{
            return true;
        }
    }

    public static boolean listIsNull(List<Object> list){

        if(list == null || list.size() == 0){
            return false;
        }
        return true;
    }
}
