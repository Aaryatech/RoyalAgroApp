package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 9/12/17.
 */

public class SocietyListData {

    private List<SocietyList> societyList ;
    private Info info;

    public List<SocietyList> getSocietyList() {
        return societyList;
    }

    public void setSocietyList(List<SocietyList> societyList) {
        this.societyList = societyList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "SocietyListData{" +
                "societyList=" + societyList +
                ", info=" + info +
                '}';
    }
}
