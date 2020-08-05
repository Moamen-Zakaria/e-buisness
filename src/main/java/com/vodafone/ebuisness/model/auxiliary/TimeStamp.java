package com.vodafone.ebuisness.model.auxiliary;

public class TimeStamp {

    private Date date;
    private Time time;

    public TimeStamp() {
    }

    public TimeStamp(Date date, Time time) {
        this.date = date;
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
