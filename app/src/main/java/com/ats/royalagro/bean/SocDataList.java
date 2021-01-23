package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 9/12/17.
 */

public class SocDataList {

    private boolean error;
    private String message;
    private int tempId;
    private int socId;
    private String farmerFname;
    private String farmerMname;
    private String farmerLname;
    private String farmerAddr;
    private String farmerVillege;
    private String farmerTal;
    private String farmerDist;
    private String farmerMobile;
    private String farmerMobile2;
    private String farmerAreaAcre;
    private String farmerGatNo;
    private int enterBy;
    private String enterDatetime;
    private int enterMode;
    private int visitBy;
    private String visitDatetime;
    private int tempStatus;
    private String visitRemarks;

    public SocDataList() {
    }

    public SocDataList(int socId, String farmerFname, String farmerMname, String farmerLname, String farmerAddr, String farmerMobile, String farmerAreaAcre, int enterBy, int visitBy, int tempStatus) {
        this.socId = socId;
        this.farmerFname = farmerFname;
        this.farmerMname = farmerMname;
        this.farmerLname = farmerLname;
        this.farmerAddr = farmerAddr;
        this.farmerMobile = farmerMobile;
        this.farmerAreaAcre = farmerAreaAcre;
        this.enterBy = enterBy;
        this.visitBy = visitBy;
        this.tempStatus = tempStatus;
    }

    public SocDataList(int tempId, int socId, String farmerFname, String farmerMname, String farmerLname, String farmerAddr, String farmerVillege, String farmerTal, String farmerDist, String farmerMobile, String farmerMobile2, String farmerAreaAcre, String farmerGatNo, int enterBy, String enterDatetime, int enterMode, int visitBy, String visitDatetime, int tempStatus, String visitRemarks) {
        this.tempId = tempId;
        this.socId = socId;
        this.farmerFname = farmerFname;
        this.farmerMname = farmerMname;
        this.farmerLname = farmerLname;
        this.farmerAddr = farmerAddr;
        this.farmerVillege = farmerVillege;
        this.farmerTal = farmerTal;
        this.farmerDist = farmerDist;
        this.farmerMobile = farmerMobile;
        this.farmerMobile2 = farmerMobile2;
        this.farmerAreaAcre = farmerAreaAcre;
        this.farmerGatNo = farmerGatNo;
        this.enterBy = enterBy;
        this.enterDatetime = enterDatetime;
        this.enterMode = enterMode;
        this.visitBy = visitBy;
        this.visitDatetime = visitDatetime;
        this.tempStatus = tempStatus;
        this.visitRemarks = visitRemarks;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public int getSocId() {
        return socId;
    }

    public void setSocId(int socId) {
        this.socId = socId;
    }

    public String getFarmerFname() {
        return farmerFname;
    }

    public void setFarmerFname(String farmerFname) {
        this.farmerFname = farmerFname;
    }

    public String getFarmerMname() {
        return farmerMname;
    }

    public void setFarmerMname(String farmerMname) {
        this.farmerMname = farmerMname;
    }

    public String getFarmerLname() {
        return farmerLname;
    }

    public void setFarmerLname(String farmerLname) {
        this.farmerLname = farmerLname;
    }

    public String getFarmerAddr() {
        return farmerAddr;
    }

    public void setFarmerAddr(String farmerAddr) {
        this.farmerAddr = farmerAddr;
    }

    public String getFarmerVillege() {
        return farmerVillege;
    }

    public void setFarmerVillege(String farmerVillege) {
        this.farmerVillege = farmerVillege;
    }

    public String getFarmerTal() {
        return farmerTal;
    }

    public void setFarmerTal(String farmerTal) {
        this.farmerTal = farmerTal;
    }

    public String getFarmerDist() {
        return farmerDist;
    }

    public void setFarmerDist(String farmerDist) {
        this.farmerDist = farmerDist;
    }

    public String getFarmerMobile() {
        return farmerMobile;
    }

    public void setFarmerMobile(String farmerMobile) {
        this.farmerMobile = farmerMobile;
    }

    public String getFarmerMobile2() {
        return farmerMobile2;
    }

    public void setFarmerMobile2(String farmerMobile2) {
        this.farmerMobile2 = farmerMobile2;
    }

    public String getFarmerAreaAcre() {
        return farmerAreaAcre;
    }

    public void setFarmerAreaAcre(String farmerAreaAcre) {
        this.farmerAreaAcre = farmerAreaAcre;
    }

    public String getFarmerGatNo() {
        return farmerGatNo;
    }

    public void setFarmerGatNo(String farmerGatNo) {
        this.farmerGatNo = farmerGatNo;
    }

    public int getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(int enterBy) {
        this.enterBy = enterBy;
    }

    public String getEnterDatetime() {
        return enterDatetime;
    }

    public void setEnterDatetime(String enterDatetime) {
        this.enterDatetime = enterDatetime;
    }

    public int getEnterMode() {
        return enterMode;
    }

    public void setEnterMode(int enterMode) {
        this.enterMode = enterMode;
    }

    public int getVisitBy() {
        return visitBy;
    }

    public void setVisitBy(int visitBy) {
        this.visitBy = visitBy;
    }

    public String getVisitDatetime() {
        return visitDatetime;
    }

    public void setVisitDatetime(String visitDatetime) {
        this.visitDatetime = visitDatetime;
    }

    public int getTempStatus() {
        return tempStatus;
    }

    public void setTempStatus(int tempStatus) {
        this.tempStatus = tempStatus;
    }

    public String getVisitRemarks() {
        return visitRemarks;
    }

    public void setVisitRemarks(String visitRemarks) {
        this.visitRemarks = visitRemarks;
    }

    @Override
    public String toString() {
        return "SocDataList{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", tempId=" + tempId +
                ", socId=" + socId +
                ", farmerFname='" + farmerFname + '\'' +
                ", farmerMname='" + farmerMname + '\'' +
                ", farmerLname='" + farmerLname + '\'' +
                ", farmerAddr='" + farmerAddr + '\'' +
                ", farmerVillege='" + farmerVillege + '\'' +
                ", farmerTal='" + farmerTal + '\'' +
                ", farmerDist='" + farmerDist + '\'' +
                ", farmerMobile='" + farmerMobile + '\'' +
                ", farmerMobile2='" + farmerMobile2 + '\'' +
                ", farmerAreaAcre=" + farmerAreaAcre +
                ", farmerGatNo='" + farmerGatNo + '\'' +
                ", enterBy=" + enterBy +
                ", enterDatetime='" + enterDatetime + '\'' +
                ", enterMode=" + enterMode +
                ", visitBy=" + visitBy +
                ", visitDatetime='" + visitDatetime + '\'' +
                ", tempStatus=" + tempStatus +
                ", visitRemarks='" + visitRemarks + '\'' +
                '}';
    }
}
