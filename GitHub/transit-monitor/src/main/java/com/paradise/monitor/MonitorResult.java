package com.paradise.monitor;

import lombok.Data;

/**
 * 巡检结果封装
 *
 * @author Paradise
 */
@Data
public class MonitorResult<T> {
    private String code;
    private String name;
    private T data;

    public enum Result_Code {
        ;
        /**
         * 一切正常
         */
        public static final String NORMAL = "0";
        public static final String ERROR = "0";
        /**
         * 无法访问
         */
        public static final String INACCESSIBLE = "";
        /**
         * 登录出错
         */
        public static final String LOGIN_ERROR = "";
        /**
         * 无短信发送
         */
        public static final String NO_SMS_SEND = "";
        /**
         * 号码推送异常
         */
        public static final String SMS_PUSH_ERROR = "";

    }

    private MonitorResult(String code, String name, T data) {
        this.code = code;
        this.name = name;
        this.data = data;
    }

    private MonitorResult(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private MonitorResult(String code) {
        this.code = code;
    }

    public static MonitorResult success() {
        return new MonitorResult(Result_Code.NORMAL);
    }

    public static MonitorResult success(String msg) {
        return new MonitorResult(Result_Code.NORMAL, msg);
    }

    @SuppressWarnings("unchecked")
    public static MonitorResult error(String code, String name, Object data) {
        return new MonitorResult(code, name, data);
    }

    public static MonitorResult error(String code) {
        return new MonitorResult(code);
    }

    public static MonitorResult error(String code, String name) {
        return new MonitorResult(code, name);
    }

}
