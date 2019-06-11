package com.paradise.project;

import java.text.SimpleDateFormat;

/**
 * 过境项目相关的常量
 *
 * @author Paradise
 */
public class ProjectConstant {
    /**
     * 开启发送
     */
    public static final String SMS_SEND_ENABLE = "1";
    /**
     * 关闭发送
     */
    public static final String SMS_SEND_DISABLE = "2";
    /**
     * 限制发送量
     */
    public static final Integer SMS_LIMIT_COUNT_TRUE = 1;
    /**
     * 不限制发送量
     */
    public static final Integer SMS_LIMIT_COUNT_FALSE = 2;
    /**
     * 发送成功 参数
     */
    public static final String SMS_RESULT_SEND_SUCCESS = "2";
    /**
     * 发送失败 参数
     */
    public static final String SMS_RESULT_SEND_FAIL = "3";

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


}
