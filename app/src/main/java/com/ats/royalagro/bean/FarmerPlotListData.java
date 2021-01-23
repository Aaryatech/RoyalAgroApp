package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 12/12/17.
 */

public class FarmerPlotListData {

    private List<FarmerPlotList> farmerPlotList;
    private Info info;

    public List<FarmerPlotList> getFarmerPlotList() {
        return farmerPlotList;
    }

    public void setFarmerPlotList(List<FarmerPlotList> farmerPlotList) {
        this.farmerPlotList = farmerPlotList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "FarmerPlotListData{" +
                "farmerPlotList=" + farmerPlotList +
                ", info=" + info +
                '}';
    }
}
