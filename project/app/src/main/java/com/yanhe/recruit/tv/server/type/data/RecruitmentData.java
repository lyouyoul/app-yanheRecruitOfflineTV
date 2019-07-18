package com.yanhe.recruit.tv.server.type.data;

import com.yanhe.recruit.tv.server.type.BaseData;


/**
 * 招聘信息
 */
public class RecruitmentData extends BaseData {
    private int index;
    private String companyName;
    private Integer number; // 人数
    private String id; // | ObjectId | 否 | 岗位id |
    private String  companyId; // | string | 是 | 企业id |
    private String name; // | string | 是 | 岗位名称 |
    private String sex; // | int | 是 | 性别，0：不限，1：男, 2：女 |
    private String age; // | Array | 是 | 年龄要求，格式:[min,max] |
    private String education; //| int | 是 | 学历要求， 0:不限，1：小学及以上，2：初中及以上，3：高中及中专以上，4：专科及以上，5：本科及以上，6：研究生及以上，7:博士及以上，8：博士后及以上 |
    private String speciality; //| string | 否 | 专业 |
    private String condition ; //| string | 否 | 其他条件描述 |
    private String salary ; //| array |是 | 薪资要求 [min, max] |
    private String workAddr; // | string | 是 | 工作地址 |
    private String workHours; // | string | 是 | 工作时间 |

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getWorkAddr() {
        return workAddr;
    }

    public void setWorkAddr(String workAddr) {
        this.workAddr = workAddr;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }
}
