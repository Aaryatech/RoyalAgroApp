package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 8/12/17.
 */

public class TalukaData {

    private List<TalukaList> talukaList;
    private Info info;

    public List<TalukaList> getTalukaList() {
        return talukaList;
    }

    public void setTalukaList(List<TalukaList> talukaList) {
        this.talukaList = talukaList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "TalukaData{" +
                "talukaList=" + talukaList +
                ", info=" + info +
                '}';
    }
}
