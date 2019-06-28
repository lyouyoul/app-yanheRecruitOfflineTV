package com.yanhe.recruit.tv.model;

/**
 * 企业展示
 */
public class CompanyShowItem {
    private Integer index;
    private String comName;
    private String job;
    private Integer numberOfPeople;
    private String treatment;//待遇
    private String claim; //要求

    public CompanyShowItem() {
    }

    public CompanyShowItem( String comName, String job, Integer numberOfPeople, String treatment, String claim) {
        this.index = index;
        this.comName = comName;
        this.job = job;
        this.numberOfPeople = numberOfPeople;
        this.treatment = treatment;
        this.claim = claim;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }
}
