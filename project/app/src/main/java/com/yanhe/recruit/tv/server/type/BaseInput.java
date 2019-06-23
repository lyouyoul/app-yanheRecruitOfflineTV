package com.yanhe.recruit.tv.server.type;

import com.loopj.android.http.RequestParams;
import com.yanhe.recruit.tv.server.inf.HttpParam;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

/**
 * 网络请求输入基类
 * @author yangtxiang
 * @date 2019-05-13
 */
public class BaseInput {
    /**
     * 转换成RequestParams
     * @return
     */
    public RequestParams asParams () {
        RequestParams params = new RequestParams();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            HttpParam paramInfo = field.getAnnotation(HttpParam.class);
            if (paramInfo != null) {
                fieldName = paramInfo.value();
            }
            Object fieldValue = null;
            try {
                field.setAccessible(true);
                fieldValue = field.get(this);
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                Logger.d("获取%s.%s字段失败", this.getClass().getName(), fieldName);
            }
            params.add(fieldName, fieldValue == null ? "" : fieldValue.toString());
        }
        return  params;
    }
}
