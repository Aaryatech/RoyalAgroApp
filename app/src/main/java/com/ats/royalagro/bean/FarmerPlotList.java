package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 12/12/17.
 */

public class FarmerPlotList {

    private Boolean error;
    private String message;
    private Integer fPlotId;
    private Integer fId;
    private String fGatNo;
    private String fPlotNo;
    private String plotOwnerName;
    private String mobileNo;
    private String aadharNo;
    private String panNo;
    private String relation;
    private String fPlotArea;
    private String fWaterResourceRemark;
    private String waterResource;
    private Integer isUsed;

    public FarmerPlotList(Integer fPlotId, Integer fId, String fGatNo, String fPlotNo, String plotOwnerName, String mobileNo, String aadharNo, String panNo, String relation, String fPlotArea, String fWaterResourceRemark, String waterResource, Integer isUsed) {
        this.fPlotId = fPlotId;
        this.fId = fId;
        this.fGatNo = fGatNo;
        this.fPlotNo = fPlotNo;
        this.plotOwnerName = plotOwnerName;
        this.mobileNo = mobileNo;
        this.aadharNo = aadharNo;
        this.panNo = panNo;
        this.relation = relation;
        this.fPlotArea = fPlotArea;
        this.fWaterResourceRemark = fWaterResourceRemark;
        this.waterResource = waterResource;
        this.isUsed = isUsed;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getfPlotId() {
        return fPlotId;
    }

    public void setfPlotId(Integer fPlotId) {
        this.fPlotId = fPlotId;
    }

    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public String getfGatNo() {
        return fGatNo;
    }

    public void setfGatNo(String fGatNo) {
        this.fGatNo = fGatNo;
    }

    public String getfPlotNo() {
        return fPlotNo;
    }

    public void setfPlotNo(String fPlotNo) {
        this.fPlotNo = fPlotNo;
    }

    public String getPlotOwnerName() {
        return plotOwnerName;
    }

    public void setPlotOwnerName(String plotOwnerName) {
        this.plotOwnerName = plotOwnerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getfPlotArea() {
        return fPlotArea;
    }

    public void setfPlotArea(String fPlotArea) {
        this.fPlotArea = fPlotArea;
    }

    public String getfWaterResourceRemark() {
        return fWaterResourceRemark;
    }

    public void setfWaterResourceRemark(String fWaterResourceRemark) {
        this.fWaterResourceRemark = fWaterResourceRemark;
    }

    public String getWaterResource() {
        return waterResource;
    }

    public void setWaterResource(String waterResource) {
        this.waterResource = waterResource;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "FarmerPlotList{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", fPlotId=" + fPlotId +
                ", fId=" + fId +
                ", fGatNo='" + fGatNo + '\'' +
                ", fPlotNo='" + fPlotNo + '\'' +
                ", plotOwnerName='" + plotOwnerName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", aadharNo='" + aadharNo + '\'' +
                ", panNo='" + panNo + '\'' +
                ", relation='" + relation + '\'' +
                ", fPlotArea='" + fPlotArea + '\'' +
                ", fWaterResourceRemark='" + fWaterResourceRemark + '\'' +
                ", waterResource='" + waterResource + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
