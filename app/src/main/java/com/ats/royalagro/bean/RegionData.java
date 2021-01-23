package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 8/12/17.
 */

public class RegionData {

    private List<RegionList> regionList;
    private Info info;

    public List<RegionList> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<RegionList> regionList) {
        this.regionList = regionList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "RegionData{" +
                "regionList=" + regionList +
                ", info=" + info +
                '}';
    }
}
