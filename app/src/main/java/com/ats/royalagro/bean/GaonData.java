package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 8/12/17.
 */

public class GaonData {

    private List<GaonList> gaonList;
    private Info info;

    public List<GaonList> getGaonList() {
        return gaonList;
    }

    public void setGaonList(List<GaonList> gaonList) {
        this.gaonList = gaonList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "GaonData{" +
                "gaonList=" + gaonList +
                ", info=" + info +
                '}';
    }
}
