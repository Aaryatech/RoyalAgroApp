package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 12/12/17.
 */

public class KYCListData {

    private List<FarmerKycList> farmerKycList;
    private Info info;

    public List<FarmerKycList> getFarmerKycList() {
        return farmerKycList;
    }

    public void setFarmerKycList(List<FarmerKycList> farmerKycList) {
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
        return "KYCListData{" +
                "farmerKycList=" + farmerKycList +
                ", info=" + info +
                '}';
    }
}
