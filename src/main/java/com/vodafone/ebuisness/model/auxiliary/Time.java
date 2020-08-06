package com.vodafone.ebuisness.model.auxiliary;


public class Time {

    private Integer hour;
    private Integer minute;
    private Double second;

    public Time() {
    }

    public Time(int hour, int minute, Double second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Double getSecond() {
        return second;
    }

    public void setSecond(Double second) {
        this.second = second;
    }
}
