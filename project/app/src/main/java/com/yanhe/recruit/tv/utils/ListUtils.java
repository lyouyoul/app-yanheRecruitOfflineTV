package com.yanhe.recruit.tv.utils;

import java.util.List;

public class ListUtils {

    /**
     * 列表查找, 如果未找到返回null
     * @param items 待查找的列表
     * @param func  查找项比较器
     * @param <T>
     * @return
     */
    public static<T> T find (List<T> items, ListFindFunc func) {
        for(T item : items) {
            if (func.compare(item)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 列表查找
     * @param items
     * @param func
     * @param <T>
     * @return 找到返回>=0,否则返回 -1
     */
    public static<T> int findIndex (List<T> items, ListFindFunc func) {
        for (int i = 0; i < items.size(); i++) {
            if (func.compare(items.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 列表查找比较器接口
     * @param <T>
     */
    public interface ListFindFunc<T> {
        /**
         * 相同返回 true
         * @param item
         * @return
         */
        boolean compare(T item);
    }
}
