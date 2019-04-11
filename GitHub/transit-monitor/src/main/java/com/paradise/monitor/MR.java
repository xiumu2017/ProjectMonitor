package com.paradise.monitor;

import lombok.Data;

/**
 * 巡检结果封装
 *
 * @author Paradise
 */
@Data
public class MR<T> {
    private String code;
    private String name;
    private T data;

    public enum Result_Code {
        ;
        /**
         * 一切正常
         */
        public static final String NORMAL = "0";
        public static final String ERROR = "-1";
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

    private MR(String code, String name, T data) {
        this.code = code;
        this.name = name;
        this.data = data;
    }

    private MR(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private MR(String code) {
        this.code = code;
    }

    public static MR success() {
        return new MR(Result_Code.NORMAL);
    }

    public static MR success(String msg) {
        return new MR(Result_Code.NORMAL, msg);
    }

    @SuppressWarnings("unchecked")
    public static MR error(String code, String name, Object data) {
        return new MR(code, name, data);
    }

    public static MR error(String code) {
        return new MR(code);
    }

    public static MR error(String code, String name) {
        return new MR(code, name);
    }

}
