package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 12/12/17.
 */

public class CropVarietyList {

    private Integer varId;
    private Integer cropId;
    private String cropName;
    private String varName;
    private Integer isUsed;

    public Integer getVarId() {
        return varId;
    }

    public void setVarId(Integer varId) {
        this.varId = varId;
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

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "CropVarietyList{" +
                "varId=" + varId +
                ", cropId=" + cropId +
                ", cropName='" + cropName + '\'' +
                ", varName='" + varName + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
