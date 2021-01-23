package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 12/12/17.
 */

public class CropData {

    private List<CropList> cropList;
    private Info info;

    public List<CropList> getCropList() {
        return cropList;
    }

    public void setCropList(List<CropList> cropList) {
        this.cropList = cropList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "CropData{" +
                "cropList=" + cropList +
                ", info=" + info +
                '}';
    }
}
