package com.yanhe.recruit.tv.server.type.data;

import com.yanhe.recruit.tv.server.type.BaseData;

import java.util.List;


/**
 * @author zhl
 * 公司信息
 */
public class CompanyData extends BaseData {
    private String code;
    private String name;
    private String content;  //简介
    private String region; //户籍
    private String address; //详情地址
    private String contactPer; // 联系人
    private String telPhone;  //电话
    private String faceImg; //头图
    private String email; //邮箱
    private Boolean isSignUp; //是否签到
    private RecruitmentData[] stations;


    public RecruitmentData[] getStations() {
        return stations;
    }

    public void setStations(RecruitmentData[] stations) {
        this.stations = stations;
    }

    public Boolean getSignUp() {
        return isSignUp;
    }

    public void setSignUp(Boolean signUp) {
        isSignUp = signUp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPer() {
        return contactPer;
    }

    public void setContactPer(String contactPer) {
        this.contactPer = contactPer;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
