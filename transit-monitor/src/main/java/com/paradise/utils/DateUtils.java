package com.paradise.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间，日期相关的公共方法
 *
 * @author Paradise
 */
@Slf4j
public class DateUtils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 比较时间差是否符合要求
     *
     * @param oracleDate 查询出的时间
     * @param sysdate    数据库当前时间
     * @param limitMin   时差限制- 分钟
     * @return true 在限制时间内 false 超出限定值
     */
    public static boolean dateCompare(String oracleDate, String sysdate, int limitMin) {
        try {
            Date o = simpleDateFormat.parse(oracleDate);
            Date s = simpleDateFormat.parse(sysdate);
            return Math.abs(s.getTime() - o.getTime()) <= limitMin * 60 * 1000;
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * 比较时间差是否符合要求
     *
     * @param oracleDate 查询出的时间
     * @param sysdate    数据库当前时间
     * @param limitMin   时差限制- 分钟
     * @return true 在限制时间内 false 超出限定值
     */
    public static boolean dateCompare(Date oracleDate, Date sysdate, int limitMin) {
        if (oracleDate == null || sysdate == null) {
            return false;
        }
        return Math.abs(oracleDate.getTime() - sysdate.getTime()) <= limitMin * 60 * 1000;
    }

    public static boolean dateCompare(String oracleDate, Long limitMin) {
        try {
            Date sysDate = simpleDateFormat.parse(oracleDate);
            return Math.abs(sysDate.getTime() - System.currentTimeMillis()) <= limitMin * 60 * 1000;
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public static boolean dateCompare(Date date, Long limitMin) {
        return Math.abs(date.getTime() - System.currentTimeMillis()) <= limitMin * 60 * 1000;
    }
}
