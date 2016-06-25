package com.progen.timesetup;

import java.util.ArrayList;
import java.util.Date;

public abstract class CalendarBuilder {

    protected CalendarBuilder next;

    public CalendarBuilder setNext(CalendarBuilder calendarBuilder) {
        this.next = calendarBuilder;
        return calendarBuilder;
    }

    public ArrayList<String> generateCalendarData(Date startDate, Date endDate, String calendarId) {
        ArrayList<String> queryList = new ArrayList<String>();
        queryList = this.buildCalendarData(startDate, endDate, calendarId);
        if (this.next != null) {
            queryList.addAll(this.next.generateCalendarData(startDate, endDate, calendarId));
        }
        return queryList;

    }

    protected abstract ArrayList buildCalendarData(Date startDate, Date endDate, String calendarId);
}
