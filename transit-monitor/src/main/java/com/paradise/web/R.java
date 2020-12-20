package com.paradise.web;


import lombok.Getter;
import lombok.Setter;

/**
 * @author Paradise
 */
@Getter
@Setter
public class R<T> {

    private Integer code;
    private String msg;
    private T data;
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer DEFAULT_ERROR_CODE = 500;
    private static final String SUCCESS_MSG = "Operation Success!";
    private static final String SERVER_ERROR = "Server error!";

    private R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public static <T> R<T> success(String msg) {
        return new R<>(SUCCESS_CODE, msg, null);
    }

    public static <T> R<T> success() {
        return new R<>(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static <T> R<T> success(String msg, T data) {
        R<T> r = new R<>(SUCCESS_CODE, msg);
        r.setData(data);
        return r;
    }

    public static <T> R<T> success(T data) {
        R<T> r = new R<>(SUCCESS_CODE, SUCCESS_MSG);
        r.setData(data);
        return r;
    }

    public static <T> R<T> error(Integer code, String msg) {
        return new R<>(code, msg);
    }

    public static <T> R<T> error() {
        return new R<>(DEFAULT_ERROR_CODE, SERVER_ERROR);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(DEFAULT_ERROR_CODE, msg);
    }

}
