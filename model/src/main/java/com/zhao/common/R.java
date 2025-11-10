package com.zhao.common;

import lombok.Data;

@Data
public class R {
    private Integer code;
    private String message;
    private Object data;

    public static R create(Integer code, String message, Object data) {
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }


    public static R ok(Object data) {
        R r = new R();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    public static R ok(String message, Object data) {
        R r = new R();
        r.setCode(200);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static R error(String message, Object data) {
        R r = new R();
        r.setCode(500);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
}
