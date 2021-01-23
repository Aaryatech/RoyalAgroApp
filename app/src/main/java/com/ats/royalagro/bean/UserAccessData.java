package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 15/12/17.
 */

public class UserAccessData {

    private Boolean error;
    private Region region;
    private District district;
    private List<TalukaList> talukaList;
    private List<GaonList> gaonList;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public List<TalukaList> getTalukaList() {
        return talukaList;
    }

    public void setTalukaList(List<TalukaList> talukaList) {
        this.talukaList = talukaList;
    }

    public List<GaonList> getGaonList() {
        return gaonList;
    }

    public void setGaonList(List<GaonList> gaonList) {
        this.gaonList = gaonList;
    }

    @Override
    public String toString() {
        return "UserAccessData{" +
                "error=" + error +
                ", region=" + region +
                ", district=" + district +
                ", talukaList=" + talukaList +
                ", gaonList=" + gaonList +
                '}';
    }
}
