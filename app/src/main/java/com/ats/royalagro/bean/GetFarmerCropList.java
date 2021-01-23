package com.ats.royalagro.bean;

/**
 * Created by MAXADMIN on 22/2/2018.
 */

public class GetFarmerCropList {

    private Integer fCid;
    private Integer fDid;
    private Integer fId;
    private Integer cropId;
    private Integer varId;
    private String fCropArea;
    private String fDtPlantation;
    private String fHarFrdate;
    private String fHarTodate;
    private String fYieldPerAcre;
    private Integer fStatus;
    private String cropName;

    public Integer getfCid() {
        return fCid;
    }

    public void setfCid(Integer fCid) {
        this.fCid = fCid;
    }

    public Integer getfDid() {
        return fDid;
    }

    public void setfDid(Integer fDid) {
        this.fDid = fDid;
    }

    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public Integer getVarId() {
        return varId;
    }

    public void setVarId(Integer varId) {
        this.varId = varId;
    }

    public String getfCropArea() {
        return fCropArea;
    }

    public void setfCropArea(String fCropArea) {
        this.fCropArea = fCropArea;
    }

    public String getfDtPlantation() {
        return fDtPlantation;
    }

    public void setfDtPlantation(String fDtPlantation) {
        this.fDtPlantation = fDtPlantation;
    }

    public String getfHarFrdate() {
        return fHarFrdate;
    }

    public void setfHarFrdate(String fHarFrdate) {
        this.fHarFrdate = fHarFrdate;
    }

    public String getfHarTodate() {
        return fHarTodate;
    }

    public void setfHarTodate(String fHarTodate) {
        this.fHarTodate = fHarTodate;
    }

    public String getfYieldPerAcre() {
        return fYieldPerAcre;
    }

    public void setfYieldPerAcre(String fYieldPerAcre) {
        this.fYieldPerAcre = fYieldPerAcre;
    }

    public Integer getfStatus() {
        return fStatus;
    }

    public void setfStatus(Integer fStatus) {
        this.fStatus = fStatus;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    @Override
    public String toString() {
        return "GetFarmerCropList{" +
                "fCid=" + fCid +
                ", fDid=" + fDid +
                ", fId=" + fId +
                ", cropId=" + cropId +
                ", varId=" + varId +
                ", fCropArea='" + fCropArea + '\'' +
                ", fDtPlantation='" + fDtPlantation + '\'' +
                ", fHarFrdate='" + fHarFrdate + '\'' +
                ", fHarTodate='" + fHarTodate + '\'' +
                ", fYieldPerAcre='" + fYieldPerAcre + '\'' +
                ", fStatus=" + fStatus +
                ", cropName='" + cropName + '\'' +
                '}';
    }
}
