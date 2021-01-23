package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 12/12/17.
 */

public class CropList {

    private Boolean error;
    private String message;
    private Integer cropId;
    private String cropName;
    private Integer isUsed;

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

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "CropList{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", cropId=" + cropId +
                ", cropName='" + cropName + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
