package com.ats.royalagro.bean;


import java.util.Date;

/**
 * Created by maxadmin on 8/12/17.
 */

public class EventData {

    private int eveId;

    private String eveDate;

    private String eveTime;

    private int eveType;

    private int regId;

    private int distId;

    private int talId;

    private int gaonId;

    private String eveName;

    private String evePlace;

    private int eveApproxAttendance;

    private String eveOrgName;

    private String eveOrgMobile;

    private String eveOrgMobile2;

    private String eveRemarks;

    private String eveOutput;

    private int eveStatus;

    private String empRefDatetime;

    private int empRefId;

    public EventData(int eveId, String eveDate, String eveTime, int eveType, int regId, int distId, int talId, int gaonId, String eveName, String evePlace, int eveApproxAttendance, String eveOrgName, String eveOrgMobile, String eveOrgMobile2, String eveRemarks, String eveOutput, int eveStatus, String empRefDatetime, int empRefId) {
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

    public int getEveId() {
        return eveId;
    }

    public void setEveId(int eveId) {
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

    public int getEveType() {
        return eveType;
    }

    public void setEveType(int eveType) {
        this.eveType = eveType;
    }

    public int getRegId() {
        return regId;
    }

    public void setRegId(int regId) {
        this.regId = regId;
    }

    public int getDistId() {
        return distId;
    }

    public void setDistId(int distId) {
        this.distId = distId;
    }

    public int getTalId() {
        return talId;
    }

    public void setTalId(int talId) {
        this.talId = talId;
    }

    public int getGaonId() {
        return gaonId;
    }

    public void setGaonId(int gaonId) {
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

    public int getEveApproxAttendance() {
        return eveApproxAttendance;
    }

    public void setEveApproxAttendance(int eveApproxAttendance) {
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

    public int getEveStatus() {
        return eveStatus;
    }

    public void setEveStatus(int eveStatus) {
        this.eveStatus = eveStatus;
    }

    public String getEmpRefDatetime() {
        return empRefDatetime;
    }

    public void setEmpRefDatetime(String empRefDatetime) {
        this.empRefDatetime = empRefDatetime;
    }

    public int getEmpRefId() {
        return empRefId;
    }

    public void setEmpRefId(int empRefId) {
        this.empRefId = empRefId;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "eveId=" + eveId +
                ", eveDate=" + eveDate +
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
