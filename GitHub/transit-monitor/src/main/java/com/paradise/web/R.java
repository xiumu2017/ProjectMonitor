package com.paradise.web;


/**
 * @author Paradise
 */
public class R<T> {

    private Integer code;
    private String msg;
    private T data;
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer DEFAULT_ERROR_CODE = 500;
    private static final String SUCCESS_MSG = "Operation Success!";
    private static final String SERVER_ERROR = "Server error!";

    private R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static R success(String msg) {
        return new R(SUCCESS_CODE, msg);
    }

    public static R success() {
        return new R(SUCCESS_CODE, SUCCESS_MSG);
    }

    @SuppressWarnings("unchecked")
    public static R success(String msg, Object data) {
        R r = new R(SUCCESS_CODE, msg);
        r.setData(data);
        return r;
    }

    @SuppressWarnings("unchecked")
    public static R success(Object data) {
        R r = new R(SUCCESS_CODE, SUCCESS_MSG);
        r.setData(data);
        return r;
    }

    public static R error(Integer code, String msg) {
        return new R(code, msg);
    }

    public static R error() {
        return new R(DEFAULT_ERROR_CODE, SERVER_ERROR);
    }

    public static R error(String msg) {
        return new R(DEFAULT_ERROR_CODE, msg);
    }

    public T getData() {
        return data;
    }
}
