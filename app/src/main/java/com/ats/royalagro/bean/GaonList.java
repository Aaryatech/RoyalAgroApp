package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 8/12/17.
 */

public class GaonList {

    private Boolean error;
    private String message;
    private Integer gaonId;
    private Integer talId;
    private Integer distId;
    private Integer regId;
    private String gaonName;
    private Integer gaonDistNsk;
    private Integer gaonDistTal;
    private Integer gaonDistDist;
    private Integer gaonDistReg;
    private String gaonRemarks;
    private Integer isUsed;

    public GaonList() {
    }

    public GaonList(Integer gaonId, Integer talId, Integer distId, Integer regId, String gaonName, Integer gaonDistNsk, Integer gaonDistTal, Integer gaonDistDist, Integer gaonDistReg, String gaonRemarks, Integer isUsed) {
        this.gaonId = gaonId;
        this.talId = talId;
        this.distId = distId;
        this.regId = regId;
        this.gaonName = gaonName;
        this.gaonDistNsk = gaonDistNsk;
        this.gaonDistTal = gaonDistTal;
        this.gaonDistDist = gaonDistDist;
        this.gaonDistReg = gaonDistReg;
        this.gaonRemarks = gaonRemarks;
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

    public Integer getGaonId() {
        return gaonId;
    }

    public void setGaonId(Integer gaonId) {
        this.gaonId = gaonId;
    }

    public Integer getTalId() {
        return talId;
    }

    public void setTalId(Integer talId) {
        this.talId = talId;
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

    public String getGaonName() {
        return gaonName;
    }

    public void setGaonName(String gaonName) {
        this.gaonName = gaonName;
    }

    public Integer getGaonDistNsk() {
        return gaonDistNsk;
    }

    public void setGaonDistNsk(Integer gaonDistNsk) {
        this.gaonDistNsk = gaonDistNsk;
    }

    public Integer getGaonDistTal() {
        return gaonDistTal;
    }

    public void setGaonDistTal(Integer gaonDistTal) {
        this.gaonDistTal = gaonDistTal;
    }

    public Integer getGaonDistDist() {
        return gaonDistDist;
    }

    public void setGaonDistDist(Integer gaonDistDist) {
        this.gaonDistDist = gaonDistDist;
    }

    public Integer getGaonDistReg() {
        return gaonDistReg;
    }

    public void setGaonDistReg(Integer gaonDistReg) {
        this.gaonDistReg = gaonDistReg;
    }

    public String getGaonRemarks() {
        return gaonRemarks;
    }

    public void setGaonRemarks(String gaonRemarks) {
        this.gaonRemarks = gaonRemarks;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "GaonList{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", gaonId=" + gaonId +
                ", talId=" + talId +
                ", distId=" + distId +
                ", regId=" + regId +
                ", gaonName='" + gaonName + '\'' +
                ", gaonDistNsk=" + gaonDistNsk +
                ", gaonDistTal=" + gaonDistTal +
                ", gaonDistDist=" + gaonDistDist +
                ", gaonDistReg=" + gaonDistReg +
                ", gaonRemarks='" + gaonRemarks + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
