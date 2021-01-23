package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 8/12/17.
 */

public class DistrictData {

    private List<DistrictList> districtList;
    private Info info;

    public List<DistrictList> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictList> districtList) {
        this.districtList = districtList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "DistrictData{" +
                "districtList=" + districtList +
                ", info=" + info +
                '}';
    }
}
