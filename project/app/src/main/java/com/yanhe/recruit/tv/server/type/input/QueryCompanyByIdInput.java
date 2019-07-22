package com.yanhe.recruit.tv.server.type.input;

import com.yanhe.recruit.tv.server.type.BaseInput;

/***
 * 根据id查找统一使用此类接收参数，可增加分页参数
 */
public class QueryCompanyByIdInput extends BaseInput {
    private String companyId;

    public QueryCompanyByIdInput() {
    }

    public QueryCompanyByIdInput(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
