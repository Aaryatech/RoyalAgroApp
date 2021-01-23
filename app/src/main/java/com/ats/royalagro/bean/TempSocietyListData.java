package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 9/12/17.
 */

public class TempSocietyListData {

    private List<SocDataList> socDataList;
    private Info info;

    public List<SocDataList> getSocDataList() {
        return socDataList;
    }

    public void setSocDataList(List<SocDataList> socDataList) {
        this.socDataList = socDataList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "TempSocietyListData{" +
                "socDataList=" + socDataList +
                ", info=" + info +
                '}';
    }
}
