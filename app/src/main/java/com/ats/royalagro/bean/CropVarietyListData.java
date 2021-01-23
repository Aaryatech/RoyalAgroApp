package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 12/12/17.
 */

public class CropVarietyListData {

    private List<CropVarietyList> cropVarietyList;
    private Info info;

    public List<CropVarietyList> getCropVarietyList() {
        return cropVarietyList;
    }

    public void setCropVarietyList(List<CropVarietyList> cropVarietyList) {
        this.cropVarietyList = cropVarietyList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "CropVarietyListData{" +
                "cropVarietyList=" + cropVarietyList +
                ", info=" + info +
                '}';
    }
}
