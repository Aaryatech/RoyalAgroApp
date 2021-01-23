package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 8/12/17.
 */

public class TalukaList {

    private Boolean error;
    private String message;
    private Integer talId;
    private Integer distId;
    private Integer regId;
    private String talName;
    private Integer talDistNsk;
    private Integer talDistDist;
    private Integer talDistReg;
    private String talRemarks;
    private Integer isUsed;

    public TalukaList() {
    }

    public TalukaList(Integer talId, Integer distId, Integer regId, String talName, Integer talDistNsk, Integer talDistDist, Integer talDistReg, String talRemarks, Integer isUsed) {
        this.talId = talId;
        this.distId = distId;
        this.regId = regId;
        this.talName = talName;
        this.talDistNsk = talDistNsk;
        this.talDistDist = talDistDist;
        this.talDistReg = talDistReg;
        this.talRemarks = talRemarks;
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

    public String getTalName() {
        return talName;
    }

    public void setTalName(String talName) {
        this.talName = talName;
    }

    public Integer getTalDistNsk() {
        return talDistNsk;
    }

    public void setTalDistNsk(Integer talDistNsk) {
        this.talDistNsk = talDistNsk;
    }

    public Integer getTalDistDist() {
        return talDistDist;
    }

    public void setTalDistDist(Integer talDistDist) {
        this.talDistDist = talDistDist;
    }

    public Integer getTalDistReg() {
        return talDistReg;
    }

    public void setTalDistReg(Integer talDistReg) {
        this.talDistReg = talDistReg;
    }

    public String getTalRemarks() {
        return talRemarks;
    }

    public void setTalRemarks(String talRemarks) {
        this.talRemarks = talRemarks;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "TalukaList{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", talId=" + talId +
                ", distId=" + distId +
                ", regId=" + regId +
                ", talName='" + talName + '\'' +
                ", talDistNsk=" + talDistNsk +
                ", talDistDist=" + talDistDist +
                ", talDistReg=" + talDistReg +
                ", talRemarks='" + talRemarks + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
