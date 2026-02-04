package com.revworkforce.model;

import java.time.LocalDate;

public class HolidayCalendar {

    private int holidayId;
    private LocalDate holidayDate;
    private String description;

    public HolidayCalendar() {}

    public HolidayCalendar(LocalDate holidayDate, String description) {
        this.holidayDate = holidayDate;
        this.description = description;
    }

    public HolidayCalendar(int holidayId, LocalDate holidayDate, String description) {
        this.holidayId = holidayId;
        this.holidayDate = holidayDate;
        this.description = description;
    }

    public int getHolidayId() { return holidayId; }
    public void setHolidayId(int holidayId) { this.holidayId = holidayId; }

    public LocalDate getHolidayDate() { return holidayDate; }
    public void setHolidayDate(LocalDate holidayDate) { this.holidayDate = holidayDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "HolidayCalendar{" +
                "holidayId=" + holidayId +
                ", holidayDate=" + holidayDate +
                ", description='" + description + '\'' +
                '}';
    }
}
