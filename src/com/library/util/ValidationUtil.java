package com.library.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    /**
     * 验证是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 验证是否为数字
     */
    public static boolean isNumber(String str) {
        if (isEmpty(str)) return false;
        return Pattern.matches("\\d+", str);
    }

    /**
     * 验证是否为小数
     */
    public static boolean isDecimal(String str) {
        if (isEmpty(str)) return false;
        return Pattern.matches("\\d+\\.?\\d*", str);
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isEmail(String email) {
        if (isEmpty(email)) return false;
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证手机号格式
     */
    public static boolean isPhone(String phone) {
        if (isEmpty(phone)) return false;
        String regex = "^1[3-9]\\d{9}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证ISBN格式
     */
    public static boolean isISBN(String isbn) {
        if (isEmpty(isbn)) return false;
        // 简单验证，实际ISBN验证更复杂
        String regex = "^978-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$";
        return Pattern.matches(regex, isbn);
    }

    /**
     * 验证密码强度（至少6位）
     */
    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) return false;
        return password.length() >= 6;
    }
}
