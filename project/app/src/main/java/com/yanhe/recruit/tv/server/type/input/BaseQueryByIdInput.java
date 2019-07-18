package com.yanhe.recruit.tv.server.type.input;

import com.yanhe.recruit.tv.server.type.BaseInput;

/***
 * 根据id查找统一使用此类接收参数，可增加分页参数
 */
public class BaseQueryByIdInput extends BaseInput {
    Integer id;

    public BaseQueryByIdInput () {
    }

    public BaseQueryByIdInput (Integer id) {
        this.id = id;
    }

    public Integer getId () {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }
}
