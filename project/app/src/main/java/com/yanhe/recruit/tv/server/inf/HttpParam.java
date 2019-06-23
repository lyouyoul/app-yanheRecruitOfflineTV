package com.yanhe.recruit.tv.server.inf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http请求参数元注释
 * @author yangtxiang
 * @date 2019-05-13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HttpParam {
    /**
     * 参数名称
     * @return
     */
    String value() default "";
}
