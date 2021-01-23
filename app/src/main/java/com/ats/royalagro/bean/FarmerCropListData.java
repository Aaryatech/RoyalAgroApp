package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 12/12/17.
 */

public class FarmerCropListData {

    private List<FarmerCropList> farmerCropList;
    private Info info;

    public List<FarmerCropList> getFarmerCropList() {
        return farmerCropList;
    }

    public void setFarmerCropList(List<FarmerCropList> farmerCropList) {
        this.farmerCropList = farmerCropList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "FarmerCropListData{" +
                "farmerCropList=" + farmerCropList +
                ", info=" + info +
                '}';
    }
}
