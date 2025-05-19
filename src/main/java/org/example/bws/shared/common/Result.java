package org.example.bws.shared.common;


import java.io.Serializable;

/**
 * 统一返回封装对象
 * @author 刘仁杰
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    // 状态码：200表示成功，其他值表示失败
    private int status;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return new Result<>(200, "ok", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "ok", data);
    }

    public static <T> Result<T> failure(int status, String msg, T data) {
        return new Result<>(status, msg, data);
    }

    private Result(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}