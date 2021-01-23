package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 12/12/17.
 */

public class CropVarietyData {

    private List<CropVarietyListById> cropVarietyListById;
    private Info info;

    public List<CropVarietyListById> getCropVarietyListById() {
        return cropVarietyListById;
    }

    public void setCropVarietyListById(List<CropVarietyListById> cropVarietyListById) {
        this.cropVarietyListById = cropVarietyListById;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "CropVarietyData{" +
                "cropVarietyListById=" + cropVarietyListById +
                ", info=" + info +
                '}';
    }
}
