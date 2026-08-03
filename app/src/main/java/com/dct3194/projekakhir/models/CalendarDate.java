package com.dct3194.projekakhir.models;

public class CalendarDate {

    private String day;
    private boolean currentMonth;
    private boolean selected;

    public CalendarDate(String day, boolean currentMonth, boolean selected) {
        this.day = day;
        this.currentMonth = currentMonth;
        this.selected = selected;
    }

    public String getDay() {
        return day;
    }

    public boolean isCurrentMonth() {
        return currentMonth;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}