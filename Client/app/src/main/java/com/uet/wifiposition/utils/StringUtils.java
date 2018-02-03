package com.uet.wifiposition.utils;

/**
 * Created by ducnd on 9/29/17.
 */

public class StringUtils {
    public static boolean isEmpty(String content) {
        if ( content == null || content.equals("")) {
            return true;
        }else {
            return false;
        }
    }
}
