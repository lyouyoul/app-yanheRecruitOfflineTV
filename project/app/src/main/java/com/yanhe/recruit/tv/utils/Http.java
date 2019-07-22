package com.yanhe.recruit.tv.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cz.msebera.android.httpclient.Header;

/**
 * 网络请求库
 * @author yangtxiang
 * @date 2019-05-07
 * @upgrade 2019-05-13 整理
 */
public class Http extends Object {

    private AsyncHttpClient client;
    private PersistentCookieStore appCookieStore;
    private Context mContext;
    public Http(Context context) {
        super();
        mContext = context;
        client = new AsyncHttpClient();
        appCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(appCookieStore);
    }

    /**
     * 网络请求
     * @param method
     * @param url
     * @param params
     * @param response 返回 string|JsonObject
     */
    public void fetch (final HttpMethod method, final String url, RequestParams params, final ResponseHandler response) {
        TextHttpResponseHandler mListener = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Logger.d("%s %s error:%s %s", method.toString(), url, statusCode, responseString);
                if (response != null && !StringUtils.isEmpty(responseString)) {
                    response.onSuccess(statusCode, responseString);
                    List<Header> mHeaders = Arrays.asList(headers);
                    int contentTypeIndex = ListUtils.findIndex(mHeaders, new ListUtils.ListFindFunc<Header>() {
                        @Override
                        public boolean compare(Header item) {
                            return "content-type".equals(item.getName().toLowerCase());
                        }
                    });
                    boolean isJson = false;
                    if (contentTypeIndex != -1) {
                        String contentType = mHeaders.get(contentTypeIndex).getValue();
                        isJson = contentType.indexOf("application/json", 0) >= 0;
                    }
                    if (isJson && !"".equals(responseString) && responseString.startsWith("{")) {
                        try {
                            JSONObject json = new JSONObject(responseString);
                            response.onSuccess(json);
                        } catch (Exception e) {
//                            JSONObject jsError = new JSONObject();
//                            try {
//                                jsError.put("ok", false);
//                                jsError.put("msg", e.getMessage());
//                                jsError.put("status", 403);
//                                response.onSuccess(jsError);
//                            } catch (JSONException e1) {
//                            }
                            Logger.e("%s %s,%s,%s", method.toString(), url, " parseJson error: %s", e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Logger.d("%s %s recv: %s,%s", method.toString(), url, statusCode, responseString);
                if (response != null) {
                    response.onSuccess(statusCode, responseString);
                    List<Header> mHeaders = Arrays.asList(headers);
                    int contentTypeIndex = ListUtils.findIndex(mHeaders, new ListUtils.ListFindFunc<Header>() {
                        @Override
                        public boolean compare(Header item) {
                            return "content-type".equals(item.getName().toLowerCase());
                        }
                    });
                    boolean isJson = false;
                    if (contentTypeIndex != -1) {
                        String contentType = mHeaders.get(contentTypeIndex).getValue();
                        isJson = contentType.indexOf("application/json", 0) >= 0;
                    }
                    if (isJson && !"".equals(responseString) && responseString.startsWith("{")) {
                        try {
                            JSONObject json = new JSONObject(responseString);
                            response.onSuccess(json);
                        } catch (JSONException e) {
                            Logger.e("%s %s,%s,%s", method.toString(), url, " parseJson error: %s", e.getMessage());
                        }
                    }
                }
            }
        };
        params = params == null ? new RequestParams() : params;
        switch (method) {
            case POST:
                client.post(url, params, mListener);
                break;
            case PUT:
                client.put(url, params, mListener);
                break;
            case DELETE:
                client.delete(url, params, mListener);
                break;
            case HEAD:
                client.head(url, params, mListener);
                break;
            default:
                client.get(url, params, mListener);
        }
    }

    /**
     * get 请求
     * @param url
     * @param params
     * @param response 返回 string|JsonObject
     */
    public void get(final String url, RequestParams params, final ResponseHandler response) {
        fetch(HttpMethod.GET, url, params, response);
    }

    /**
     * post 请求
     * @param url
     * @param params
     * @param response 返回 string|JsonObject
     */
    public void post(final String url, RequestParams params, final ResponseHandler response) {
        fetch(HttpMethod.POST, url, params, response);
    }

    /**
     * delete 请求
     * @param url
     * @param params
     * @param response 返回 string|JsonObject
     */
    public void delete(final String url, RequestParams params, final ResponseHandler response) {
        fetch(HttpMethod.DELETE, url, params, response);
    }

    /**
     * put 请求
     * @param url
     * @param params
     * @param response 返回 string|JsonObject
     */
    public void put(final String url, RequestParams params, final ResponseHandler response) {
        fetch(HttpMethod.DELETE, url, params, response);
    }

    /**
     * head 请求
     * @param url
     * @param params
     * @param response 返回 string|JsonObject
     */
    public void head(final String url, RequestParams params, final ResponseHandler response) {
        fetch(HttpMethod.HEAD, url, params, response);
    }

    /**
     * 网络请求
     * @param method 请求方法
     * @param url
     * @param params
     * @param clazz
     * @param response 返回 string|<T>
     * @param <T>
     */
    public <T> void fetch (final HttpMethod method, final String url, RequestParams params,final Class<T> clazz, final GenericResponseHandler<T> response) {
        params = params == null ? new RequestParams() : params;
        Logger.d("%s [%s] params: %s", method.toString(), url, params);

        TextHttpResponseHandler mListener = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Logger.d("%s [%s] error: (%d) %s %s", method.toString(), url, statusCode, responseString, throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Logger.d("%s [%s] result:%s", method.toString(), url, responseString);
                T obj = new Gson().fromJson(responseString, clazz);
                if (response != null) {
                    response.onSuccess(obj);
                }
            }
        };
        params = params == null ? new RequestParams() : params;
        switch (method) {
            case POST:
                client.post(url, params, mListener);
                break;
            case PUT:
                client.put(url, params, mListener);
                break;
            case DELETE:
                client.delete(url, params, mListener);
                break;
            case HEAD:
                client.head(url, params, mListener);
                break;
            default:
                client.get(url, params, mListener);
        }
    }

    /**
     * get 请求
     * @param url
     * @param params
     * @param clazz
     * @param response
     * @param <T>
     */
    public <T> void get (String url, RequestParams params, final Class<T> clazz, GenericResponseHandler<T> response) {
        fetch(HttpMethod.GET, url, params, clazz, response);
    }

    /**
     * post 请求
     * @param url
     * @param params
     * @param clazz
     * @param response
     * @param <T>
     */
    public <T> void post (String url, RequestParams params, final Class<T> clazz, GenericResponseHandler<T> response) {
        fetch(HttpMethod.POST, url, params, clazz, response);
    }

    /**
     * put 请求
     * @param url
     * @param params
     * @param clazz
     * @param response
     * @param <T>
     */
    public <T> void put (String url, RequestParams params, final Class<T> clazz, GenericResponseHandler<T> response) {
        fetch(HttpMethod.PUT, url, params, clazz, response);
    }

    /**
     * delete 请求
     * @param url
     * @param params
     * @param clazz
     * @param response
     * @param <T>
     */
    public <T> void delete (String url, RequestParams params, final Class<T> clazz, GenericResponseHandler<T> response) {
        fetch(HttpMethod.DELETE, url, params, clazz, response);
    }

    /**
     * head 请求
     * @param url
     * @param params
     * @param clazz
     * @param response
     * @param <T>
     */
    public <T> void head(String url, RequestParams params, final Class<T> clazz, GenericResponseHandler<T> response) {
        fetch(HttpMethod.HEAD, url, params, clazz, response);
    }

    public interface ResponseHandler {
        void onSuccess(int statusCode, String text);
        void onSuccess(JSONObject json);
    }

    public interface GenericResponseHandler<T> {
        void onSuccess(T data);
    }

    public enum HttpMethod {
        /** get请求 */
        GET,
        /** post请求 */
        POST,
        /** put请求 */
        PUT,
        /** delete请求 */
        DELETE,
        /** head 请求 */
        HEAD
    }

}
