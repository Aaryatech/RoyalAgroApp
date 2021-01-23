package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 12/12/17.
 */

public class FarmerCropList {

    private Boolean error;
    private String message;
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

    public FarmerCropList(Integer fCid, Integer fDid, Integer fId, Integer cropId, Integer varId, String fCropArea, String fDtPlantation, String fHarFrdate, String fHarTodate, String fYieldPerAcre, Integer fStatus) {
        this.fCid = fCid;
        this.fDid = fDid;
        this.fId = fId;
        this.cropId = cropId;
        this.varId = varId;
        this.fCropArea = fCropArea;
        this.fDtPlantation = fDtPlantation;
        this.fHarFrdate = fHarFrdate;
        this.fHarTodate = fHarTodate;
        this.fYieldPerAcre = fYieldPerAcre;
        this.fStatus = fStatus;
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

    @Override
    public String toString() {
        return "FarmerCropList{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", fCid=" + fCid +
                ", fDid=" + fDid +
                ", fId=" + fId +
                ", cropId=" + cropId +
                ", varId=" + varId +
                ", fCropArea=" + fCropArea +
                ", fDtPlantation='" + fDtPlantation + '\'' +
                ", fHarFrdate='" + fHarFrdate + '\'' +
                ", fHarTodate='" + fHarTodate + '\'' +
                ", fYieldPerAcre=" + fYieldPerAcre +
                ", fStatus=" + fStatus +
                '}';
    }
}
