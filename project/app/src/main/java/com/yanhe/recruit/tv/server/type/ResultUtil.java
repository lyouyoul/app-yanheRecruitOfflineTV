package com.yanhe.recruit.tv.server.type;

/**
 * 网络请求返回基类
 * @param <T>
 */
public class ResultUtil<T> {
    private T      data;
    private int    status;
    private boolean ok;
    private String msg;

    public ResultUtil () {
        this.setData(null);
        this.setMsg("");
        this.setStatus(0);
    }

    public ResultUtil (String msg, int status, T data) {
        this.setMsg(msg);
        this.setStatus(status);
        this.setData(data);
    }

    /**
     * 获取返回数据
     * @return
     */
    public T getData () {
        return data;
    }

    /** 200 正常, 403 权限, 401 */ /**
     * 获取状态值
     * @return
     */
    public int getStatus () {
        return status;
    }

    /** 错误消息 */ /**
     * 获取返回消息
     * @return
     */
    public String getMsg () {
        return msg;
    }

    public ResultUtil setData (T data) {
        this.data = data;
        return this;
    }

    public ResultUtil setStatus (int status) {
        this.status = status;
        return this;
    }

    public boolean getOk () {
        return ok;
    }

    public ResultUtil setOk (boolean ok) {
        this.ok = ok;
        return this;
    }

    public ResultUtil setMsg (String msg) {
        this.msg = msg;
        return this;
    }
}
