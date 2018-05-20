package com.xema.midas.util;

/**
 * Created by xema0 on 2018-05-20.
 */

public class CommonUtils {
    public static boolean isValidId(String id) {
        return id.length() > 4 && id.length() < 20;
    }

    public static boolean isValidPassword(String pw) {
        return pw.length() > 4 && pw.length() < 20;
    }
}
