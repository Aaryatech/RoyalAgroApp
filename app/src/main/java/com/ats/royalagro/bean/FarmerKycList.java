package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 12/12/17.
 */

public class FarmerKycList {

    private Boolean error;
    private String message;
    private Integer kycId;
    private Integer fId;
    private Integer kycType;
    private String kycNo;
    private String kycPhoto;
    private String kycRemarks;
    private Integer kycIsVerified;
    private Integer kycVerifBy;
    private String kycVerifDate;
    private int isUsed;

    public FarmerKycList(Integer kycId, Integer fId, Integer kycType, String kycNo, String kycPhoto, String kycRemarks, Integer kycIsVerified, Integer kycVerifBy, String kycVerifDate,int isUsed) {
        this.kycId = kycId;
        this.fId = fId;
        this.kycType = kycType;
        this.kycNo = kycNo;
        this.kycPhoto = kycPhoto;
        this.kycRemarks = kycRemarks;
        this.kycIsVerified = kycIsVerified;
        this.kycVerifBy = kycVerifBy;
        this.kycVerifDate = kycVerifDate;
        this.isUsed=isUsed;
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

    public Integer getKycId() {
        return kycId;
    }

    public void setKycId(Integer kycId) {
        this.kycId = kycId;
    }

    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public Integer getKycType() {
        return kycType;
    }

    public void setKycType(Integer kycType) {
        this.kycType = kycType;
    }

    public String getKycNo() {
        return kycNo;
    }

    public void setKycNo(String kycNo) {
        this.kycNo = kycNo;
    }

    public String getKycPhoto() {
        return kycPhoto;
    }

    public void setKycPhoto(String kycPhoto) {
        this.kycPhoto = kycPhoto;
    }

    public String getKycRemarks() {
        return kycRemarks;
    }

    public void setKycRemarks(String kycRemarks) {
        this.kycRemarks = kycRemarks;
    }

    public Integer getKycIsVerified() {
        return kycIsVerified;
    }

    public void setKycIsVerified(Integer kycIsVerified) {
        this.kycIsVerified = kycIsVerified;
    }

    public Integer getKycVerifBy() {
        return kycVerifBy;
    }

    public void setKycVerifBy(Integer kycVerifBy) {
        this.kycVerifBy = kycVerifBy;
    }

    public String getKycVerifDate() {
        return kycVerifDate;
    }

    public void setKycVerifDate(String kycVerifDate) {
        this.kycVerifDate = kycVerifDate;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "FarmerKycList{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", kycId=" + kycId +
                ", fId=" + fId +
                ", kycType=" + kycType +
                ", kycNo='" + kycNo + '\'' +
                ", kycPhoto='" + kycPhoto + '\'' +
                ", kycRemarks='" + kycRemarks + '\'' +
                ", kycIsVerified=" + kycIsVerified +
                ", kycVerifBy=" + kycVerifBy +
                ", kycVerifDate='" + kycVerifDate + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
