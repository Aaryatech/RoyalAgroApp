package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 15/12/17.
 */

public class District {

    private Boolean error;
    private String message;
    private Integer distId;
    private Integer regId;
    private String distName;
    private Integer distDistNsk;
    private Integer distDistReg;
    private String distRemarks;
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

    public Integer getDistId() {
        return distId;
    }

    public void setDistId(Integer distId) {
        this.distId = distId;
    }

    public Integer getRegId() {
        return regId;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public Integer getDistDistNsk() {
        return distDistNsk;
    }

    public void setDistDistNsk(Integer distDistNsk) {
        this.distDistNsk = distDistNsk;
    }

    public Integer getDistDistReg() {
        return distDistReg;
    }

    public void setDistDistReg(Integer distDistReg) {
        this.distDistReg = distDistReg;
    }

    public String getDistRemarks() {
        return distRemarks;
    }

    public void setDistRemarks(String distRemarks) {
        this.distRemarks = distRemarks;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "District{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", distId=" + distId +
                ", regId=" + regId +
                ", distName='" + distName + '\'' +
                ", distDistNsk=" + distDistNsk +
                ", distDistReg=" + distDistReg +
                ", distRemarks='" + distRemarks + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
