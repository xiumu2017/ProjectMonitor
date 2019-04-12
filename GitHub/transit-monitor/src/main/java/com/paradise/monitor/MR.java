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
        /**
         * 巡检异常
         */
        public static final String CHECK_ERROR = "-1";
        /**
         * 服务器认证失败
         */
        public static final String SERVER_AUTH_ERROR = "SERVER_AUTH_ERROR";
        /**
         * 服务器连接失败
         */
        public static final String SERVER_CONNECT_ERROR = "SERVER_CONNECT_ERROR";
        /**
         * 服务器命令执行失败
         */
        public static final String SERVER_EXECUTE_ERROR = "SERVER_EXECUTE_ERROR";
        /**
         * WEB无法访问
         */
        public static final String WEB_INACCESSIBLE = "WEB_INACCESSIBLE";
        /**
         * 登录出错
         */
        public static final String WEB_LOGIN_ERROR = "WEB_LOGIN_ERROR";
        /**
         * 无短信发送
         */
        public static final String SMS_NO_SEND = "SMS_NO_SEND";
        /**
         * 号码推送异常 - 长时间未推送
         */
        public static final String SMS_PUSH_ERROR = "SMS_PUSH_ERROR";
        /**
         * 短信提交失败 - MAS/OA 网路问题，项目问题，防火墙问题
         */
        public static final String SMS_SUBMIT_ERROR = "SMS_SUBMIT_ERROR";
        /**
         * 短信发送失败 - MAS/OA 欠费问题，老版接口问题
         */
        public static final String SMS_SEND_ERROR = "SMS_SEND_ERROR";

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
        MR mr = new MR(Result_Code.NORMAL);
        mr.setName("ALL IS WELL!");
        return mr;
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
