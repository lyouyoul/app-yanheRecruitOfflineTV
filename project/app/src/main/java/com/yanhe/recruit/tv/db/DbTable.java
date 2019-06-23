package com.yanhe.recruit.tv.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据实例元注释
 * @author yangtxiang
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DbTable {
    /**
     * 表名称
     * @return
     */
    String tableName() default "";

    /**
     * 表说明
     * @return
     */
    String summery() default "";

    int version() default 1;
}
