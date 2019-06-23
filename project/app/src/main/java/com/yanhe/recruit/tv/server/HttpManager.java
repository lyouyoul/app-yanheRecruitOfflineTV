package com.yanhe.recruit.tv.server;

import com.yanhe.recruit.tv.server.inf.HttpMethod;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求管理器
 * @author yangtxiang
 * @date 2019-05-13
 */
public class HttpManager {
    private static HttpManager httpManager;
    private Map<String, Class<? extends HttpMethod>> getMethods;
    private Map<String, Class<? extends HttpMethod>> postMethods;
    private Map<String, Class<? extends HttpMethod>> putMethods;
    private Map<String, Class<? extends HttpMethod>> deleteMethods;

    /**
     * httpManager构造方法
     */
    private HttpManager() {
        getMethods = new HashMap<>();
        postMethods = new HashMap<>();
        putMethods = new HashMap<>();
        deleteMethods = new HashMap<>();
    }

    /**
     * 获取单一实例
     * @return
     */
    public static HttpManager getInstance() {
        if (httpManager == null) {
            httpManager = new HttpManager();
        }
        return httpManager;
    }

    /**
     * 注册网络请求方法类
     * @param methodType 方法类型
     * @param methodName 方法名称
     * @param method     方法类类型
     */
    public void register (HttpRequestMethodType methodType, String methodName, Class<? extends HttpMethod> method) {
        Map<String, Class<? extends HttpMethod>> items;
        switch (methodType) {
            case POST:
                items = postMethods;
                break;
            case DELETE:
                items = deleteMethods;
                break;
            case PUT:
                items = putMethods;
                break;
            default:
                items = getMethods;
        }
        if (!items.containsKey(methodName)) {
            items.put(methodName, method);
        }
    }

    /**
     * 注册get请求
     * @param methodName
     * @param method
     */
    public void registerGet(String methodName,Class< ? extends HttpMethod> method) {
       register(HttpRequestMethodType.GET, methodName, method);
    }

    /**
     * 注册post请求
     * @param methodName
     * @param method
     */
    public void registerPost(String methodName, Class<? extends HttpMethod> method) {
        register(HttpRequestMethodType.POST, methodName, method);
    }

    /**
     * 注册put请求
     * @param methodName
     * @param method
     */
    public void registerPut(String methodName, Class<? extends HttpMethod> method) {
        register(HttpRequestMethodType.PUT, methodName, method);
    }

    /**
     * 注册delete请求
     * @param methodName
     * @param method
     */
    public void registerDelete(String methodName, Class<? extends HttpMethod> method) {
        register(HttpRequestMethodType.DELETE, methodName, method);
    }

    protected HttpMethod generateMethodInstance (Class<? extends HttpMethod> clazz) {
        try {
            Constructor constructor = clazz.getConstructor();
            return (HttpMethod) constructor.newInstance();
        } catch (NoSuchMethodException e) {
            Logger.d("%s类未定义无参数的构造方法", clazz.getName());
        } catch (IllegalAccessException e) {
            Logger.d("%s类没有构造方法没有public修饰符", clazz.getName());
        } catch (InstantiationException e) {
            Logger.d("实例化%s类异常", clazz.getName());
        } catch (InvocationTargetException e) {
            Logger.d("%s类调用构造方法失败", clazz.getName());
        }
        return null;
    }

    /**
     * 获取网络请求类实例
     * @param methodType
     * @param methodName
     * @return
     */
    public HttpMethod getMethod (HttpRequestMethodType methodType, String methodName) {
        Map<String, Class<? extends HttpMethod>> items;
        switch (methodType) {
            case POST:
                items = postMethods;
                break;
            case DELETE:
                items = deleteMethods;
                break;
            case PUT:
                items = putMethods;
                break;
            default:
                items = getMethods;
        }
        if (items.containsKey(methodName)) {
            Class<? extends HttpMethod> clazz = items.get(methodName);
            return generateMethodInstance(clazz);
        }
        return null;
    }

    /**
     * 网络请求类型
     */
    public enum HttpRequestMethodType {
        /** get请求类型 */
        GET,
        /** post请求类型 */
        POST,
        /** delete请求类型 */
        DELETE,
        /** put请求类型 */
        PUT
    }

}
