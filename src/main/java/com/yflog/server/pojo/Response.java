package com.yflog.server.pojo;

/**
 * Created by vincent on 10/2/16.
 */
public class Response {

    private int status;
    private String errmsg;
    private Object data;


    public Response() {
        status = 200;
        errmsg = "OK";
        data = null;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getData() {
        return data;
    }

    public Response setData(Object data) {
        this.data = data;
        return this;
    }

    public void set(int status, String errmsg) {
        this.status = status;
        this.errmsg = errmsg;
    }

    public void set(int status, String errMsg, Object data) {
        this.status = status;
        this.errmsg = errMsg;
        this.data = data;
    }

}
