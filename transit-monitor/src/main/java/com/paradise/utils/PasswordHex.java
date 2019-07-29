package com.paradise.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 生成随机密码，用于定期更换Linux登录密码
 *
 * @author Paradise
 */
public class PasswordHex {

    public static String hexWord(String password) {
        if (StringUtils.isNotEmpty(password)) {
            char[] chars = password.toCharArray();
            for (Character c : chars) {
                int x = (int) c;
                System.out.println(x);
            }
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println(getPassword(10));
            Thread.sleep(10);
        }
    }

    public static String getPassword(int length) {

        Random random = new Random(System.currentTimeMillis());
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            int x = random.nextInt(127);
            if (!roundCheck(x)) {
                i--;
                continue;
            }
            chars[i] = (char) x;
        }
        return new String(chars);
    }

    private static boolean roundCheck(int x) {
        // 数字 48 - 57
        // 大写字母 65 - 90
        // 小写写字母 97 - 122
        // 33! 35# 37% 38& 43+ 61= 63? 64@
        Integer[] arr = {33, 35, 37, 38, 53, 61, 63};
        List<Integer> list = Arrays.asList(arr);
        return list.contains(x) || (x > 47 && x < 58) || (x > 64 && x < 91) || (x > 96 && x < 123);
    }
}
