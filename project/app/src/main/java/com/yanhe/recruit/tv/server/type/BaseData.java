package com.yanhe.recruit.tv.server.type;

import com.yanhe.recruit.tv.server.inf.ResConvert;
import com.yanhe.recruit.tv.server.inf.ResField;
import com.yanhe.recruit.tv.utils.StringUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class BaseData {

    protected boolean parse(JSONObject jsData) {
        return false;
    }

    public void parseJson(JSONObject jsData) {
        if (parse(jsData)) {
            return;
        }
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            ResField fieldInfo = field.getAnnotation(ResField.class);
            if (fieldInfo != null) {
                fieldName = !StringUtils.isEmpty(fieldInfo.value()) ? fieldInfo.value() : fieldName;
            }
            try {
                if (jsData.has(fieldName) && jsData.get(fieldName) != JSONObject.NULL) {
                    try {
                        Object v = jsData.get(fieldName);
                        if (fieldInfo != null && fieldInfo.convert() != null) {
                            Constructor constructor = fieldInfo.convert().getConstructor();
                            if (constructor != null) {
                                ResConvert convert = (ResConvert)constructor.newInstance();
                                v = convert.convert(v.toString());
                            }
                        }
                        field.setAccessible(true);
                        field.set(this, v);
                        field.setAccessible(false);
                    }
                    catch (IllegalAccessException e1) {
                        Logger.d("%s.set field value error: %s", this.getClass().getSimpleName(), e1.getMessage());
                    } catch (NoSuchMethodException e) {
                        Logger.d("%s field convert not define constructor method error. %s", e.getMessage());
                    } catch (InvocationTargetException e) {
                        Logger.d("%s field invocation  convert constructor method error. %s", e.getMessage());
                    } catch (InstantiationException e) {
                        Logger.d("%s field convert instantiation error. %s", e.getMessage());
                    }
                }
            } catch (JSONException e) {
                Logger.d("Base get value from json error: %s", e.getMessage());
            }
        }
    }

    @Override
    public String toString () {
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object v = field.get(this);
                field.setAccessible(false);
                sb.append(String.format("%s = %s \\\n", field.getName(), v));
            } catch (IllegalAccessException e) {
            }
        }
        return sb.toString();
    }


    public void parseJson(Object jsData) {
    }
}
