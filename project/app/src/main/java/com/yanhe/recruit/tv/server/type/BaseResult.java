package com.yanhe.recruit.tv.server.type;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 网络请求返回基类
 * @param <T>
 */
public class BaseResult<T extends BaseData> {
    private T      context;
    private int    status;
    private String msg;

    public BaseResult() {
        this.setContext(null);
        this.setMsg("");
        this.setStatus(0);
    }

    public BaseResult(String msg, int status, T context) {
        this.setMsg(msg);
        this.setStatus(status);
        this.setContext(context);
    }

    /**
     * 获取返回数据
     * @return
     */
    public T getContext () {
        return context;
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

    public BaseResult setContext (T context) {
        this.context = context;
        return this;
    }

    public BaseResult setStatus (int status) {
        this.status = status;
        return this;
    }

    public BaseResult setMsg (String msg) {
        this.msg = msg;
        return this;
    }

    public void parseJson (JSONObject json) {
        try {
            if (json == null) {
                return;
            }
            this.status = json.has("status") ? json.getInt("status") : 0;
            this.msg = json.has("msg") ? json.getString("msg") : "";
            if (json.has("context") && json.get("context") != JSONObject.NULL) {
                Object objData = json.get("context");
                Logger.d("objData type is: %s", objData.getClass());
                if (objData.getClass() == String.class) {
                    JSONObject jsData = new JSONObject();
                    jsData.put("string", objData.toString());
                    context.parseJson(jsData);
                }else if(objData.getClass() == JSONArray.class){
                    if(objData !=null){
                        context.parseJson(objData);
                    }
                }
                else {
                    JSONObject jsData = json.getJSONObject("context");
                    if (context != null) {
                        context.parseJson(jsData);
                    }
                }
            }
        } catch (Exception e) {
            Logger.d("baseResult parse json error: %s", e.getMessage());
        }
    }

    @Override
    public String toString () {
        return "BaseResult{" +
                "context=" + context +
                ", status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}
