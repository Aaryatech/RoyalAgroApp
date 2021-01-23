package com.ats.royalagro.bean;

/**
 * Created by maxadmin on 11/12/17.
 */

public class FarmerDataByTempId {

    private FarmerHeader farmerHeader;
    private Object farmerPlotList;
    private Object farmerCropList;
    private Object farmerKycList;
    private Info info;

    public FarmerHeader getFarmerHeader() {
        return farmerHeader;
    }

    public void setFarmerHeader(FarmerHeader farmerHeader) {
        this.farmerHeader = farmerHeader;
    }

    public Object getFarmerPlotList() {
        return farmerPlotList;
    }

    public void setFarmerPlotList(Object farmerPlotList) {
        this.farmerPlotList = farmerPlotList;
    }

    public Object getFarmerCropList() {
        return farmerCropList;
    }

    public void setFarmerCropList(Object farmerCropList) {
        this.farmerCropList = farmerCropList;
    }

    public Object getFarmerKycList() {
        return farmerKycList;
    }

    public void setFarmerKycList(Object farmerKycList) {
        this.farmerKycList = farmerKycList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "FarmerDataByTempId{" +
                "farmerHeader=" + farmerHeader +
                ", farmerPlotList=" + farmerPlotList +
                ", farmerCropList=" + farmerCropList +
                ", farmerKycList=" + farmerKycList +
                ", info=" + info +
                '}';
    }
}
