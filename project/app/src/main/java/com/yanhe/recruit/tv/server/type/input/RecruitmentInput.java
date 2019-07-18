package com.yanhe.recruit.tv.server.type.input;

import com.yanhe.recruit.tv.server.type.BaseInput;

public class RecruitmentInput extends BaseInput {
    private String companyId;
    private String recruitId;

    public RecruitmentInput() {
    }

    public RecruitmentInput(String companyId, String recruitId) {
        this.companyId = companyId;
        this.recruitId = recruitId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(String recruitId) {
        this.recruitId = recruitId;
    }
}
