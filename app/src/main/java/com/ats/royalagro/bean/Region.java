package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 15/12/17.
 */

public class Region {

    private Boolean error;
    private String message;
    private Integer regId;
    private String regName;
    private Integer regDistNsk;
    private String regRemarks;
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

    public Integer getRegId() {
        return regId;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public Integer getRegDistNsk() {
        return regDistNsk;
    }

    public void setRegDistNsk(Integer regDistNsk) {
        this.regDistNsk = regDistNsk;
    }

    public String getRegRemarks() {
        return regRemarks;
    }

    public void setRegRemarks(String regRemarks) {
        this.regRemarks = regRemarks;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "Region{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", regId=" + regId +
                ", regName='" + regName + '\'' +
                ", regDistNsk=" + regDistNsk +
                ", regRemarks='" + regRemarks + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
