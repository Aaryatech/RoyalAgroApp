package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 9/12/17.
 */

public class EventList {

    private Integer eveId;
    private String eveDate;
    private String eveTime;
    private Integer eveType;
    private Integer regId;
    private Integer distId;
    private Integer talId;
    private Integer gaonId;
    private String eveName;
    private String evePlace;
    private Integer eveApproxAttendance;
    private String eveOrgName;
    private String eveOrgMobile;
    private String eveOrgMobile2;
    private String eveRemarks;
    private String eveOutput;
    private Integer eveStatus;
    private String empRefDatetime;
    private Integer empRefId;

    public EventList(Integer eveId, String eveDate, String eveTime, Integer eveType, Integer regId, Integer distId, Integer talId, Integer gaonId, String eveName, String evePlace, Integer eveApproxAttendance, String eveOrgName, String eveOrgMobile, String eveOrgMobile2, String eveRemarks, String eveOutput, Integer eveStatus, String empRefDatetime, Integer empRefId) {
        this.eveId = eveId;
        this.eveDate = eveDate;
        this.eveTime = eveTime;
        this.eveType = eveType;
        this.regId = regId;
        this.distId = distId;
        this.talId = talId;
        this.gaonId = gaonId;
        this.eveName = eveName;
        this.evePlace = evePlace;
        this.eveApproxAttendance = eveApproxAttendance;
        this.eveOrgName = eveOrgName;
        this.eveOrgMobile = eveOrgMobile;
        this.eveOrgMobile2 = eveOrgMobile2;
        this.eveRemarks = eveRemarks;
        this.eveOutput = eveOutput;
        this.eveStatus = eveStatus;
        this.empRefDatetime = empRefDatetime;
        this.empRefId = empRefId;
    }

    public Integer getEveId() {
        return eveId;
    }

    public void setEveId(Integer eveId) {
        this.eveId = eveId;
    }

    public String getEveDate() {
        return eveDate;
    }

    public void setEveDate(String eveDate) {
        this.eveDate = eveDate;
    }

    public String getEveTime() {
        return eveTime;
    }

    public void setEveTime(String eveTime) {
        this.eveTime = eveTime;
    }

    public Integer getEveType() {
        return eveType;
    }

    public void setEveType(Integer eveType) {
        this.eveType = eveType;
    }

    public Integer getRegId() {
        return regId;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public Integer getDistId() {
        return distId;
    }

    public void setDistId(Integer distId) {
        this.distId = distId;
    }

    public Integer getTalId() {
        return talId;
    }

    public void setTalId(Integer talId) {
        this.talId = talId;
    }

    public Integer getGaonId() {
        return gaonId;
    }

    public void setGaonId(Integer gaonId) {
        this.gaonId = gaonId;
    }

    public String getEveName() {
        return eveName;
    }

    public void setEveName(String eveName) {
        this.eveName = eveName;
    }

    public String getEvePlace() {
        return evePlace;
    }

    public void setEvePlace(String evePlace) {
        this.evePlace = evePlace;
    }

    public Integer getEveApproxAttendance() {
        return eveApproxAttendance;
    }

    public void setEveApproxAttendance(Integer eveApproxAttendance) {
        this.eveApproxAttendance = eveApproxAttendance;
    }

    public String getEveOrgName() {
        return eveOrgName;
    }

    public void setEveOrgName(String eveOrgName) {
        this.eveOrgName = eveOrgName;
    }

    public String getEveOrgMobile() {
        return eveOrgMobile;
    }

    public void setEveOrgMobile(String eveOrgMobile) {
        this.eveOrgMobile = eveOrgMobile;
    }

    public String getEveOrgMobile2() {
        return eveOrgMobile2;
    }

    public void setEveOrgMobile2(String eveOrgMobile2) {
        this.eveOrgMobile2 = eveOrgMobile2;
    }

    public String getEveRemarks() {
        return eveRemarks;
    }

    public void setEveRemarks(String eveRemarks) {
        this.eveRemarks = eveRemarks;
    }

    public String getEveOutput() {
        return eveOutput;
    }

    public void setEveOutput(String eveOutput) {
        this.eveOutput = eveOutput;
    }

    public Integer getEveStatus() {
        return eveStatus;
    }

    public void setEveStatus(Integer eveStatus) {
        this.eveStatus = eveStatus;
    }

    public String getEmpRefDatetime() {
        return empRefDatetime;
    }

    public void setEmpRefDatetime(String empRefDatetime) {
        this.empRefDatetime = empRefDatetime;
    }

    public Integer getEmpRefId() {
        return empRefId;
    }

    public void setEmpRefId(Integer empRefId) {
        this.empRefId = empRefId;
    }

    @Override
    public String toString() {
        return "EventList{" +
                "eveId=" + eveId +
                ", eveDate='" + eveDate + '\'' +
                ", eveTime='" + eveTime + '\'' +
                ", eveType=" + eveType +
                ", regId=" + regId +
                ", distId=" + distId +
                ", talId=" + talId +
                ", gaonId=" + gaonId +
                ", eveName='" + eveName + '\'' +
                ", evePlace='" + evePlace + '\'' +
                ", eveApproxAttendance=" + eveApproxAttendance +
                ", eveOrgName='" + eveOrgName + '\'' +
                ", eveOrgMobile='" + eveOrgMobile + '\'' +
                ", eveOrgMobile2='" + eveOrgMobile2 + '\'' +
                ", eveRemarks='" + eveRemarks + '\'' +
                ", eveOutput='" + eveOutput + '\'' +
                ", eveStatus=" + eveStatus +
                ", empRefDatetime='" + empRefDatetime + '\'' +
                ", empRefId=" + empRefId +
                '}';
    }
}
