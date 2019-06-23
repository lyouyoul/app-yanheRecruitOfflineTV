package com.yanhe.recruit.tv.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字符注释
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface DbField {
    String fieldName();
    String title() default "";
    Class<?> defaultValue() default Void.class;
    boolean allowNull() default true;
    String validMessage() default "字段%s不允许为空";
}
