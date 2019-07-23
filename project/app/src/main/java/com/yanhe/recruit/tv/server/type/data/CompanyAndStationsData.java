package com.yanhe.recruit.tv.server.type.data;

public class CompanyAndStationsData {
    private CompanyData company;
    private RecruitmentData[] stations;

    public CompanyData getCompany() {
        return company;
    }

    public void setCompany(CompanyData company) {
        this.company = company;
    }

    public RecruitmentData[] getStations() {
        return stations;
    }

    public void setStations(RecruitmentData[] stations) {
        this.stations = stations;
    }
}
