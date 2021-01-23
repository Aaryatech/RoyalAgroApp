package com.ats.royalagro.bean;

import java.util.List;

/**
 * Created by maxadmin on 9/12/17.
 */

public class EventListData {

    private List<EventList> eventList;
    private Info info;

    public List<EventList> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventList> eventList) {
        this.eventList = eventList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "EventListData{" +
                "eventList=" + eventList +
                ", info=" + info +
                '}';
    }
}
