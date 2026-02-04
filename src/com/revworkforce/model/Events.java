package com.revworkforce.model;

import java.time.LocalDate;

public class Events {

    private int eventId;
    private int empId;
    private String eventType;
    private LocalDate eventDate;

    public Events() {}

    public Events(int empId, String eventType, LocalDate eventDate) {
        this.empId = empId;
        this.eventType = eventType;
        this.eventDate = eventDate;
    }

    public Events(int eventId, int empId, String eventType, LocalDate eventDate) {
        this.eventId = eventId;
        this.empId = empId;
        this.eventType = eventType;
        this.eventDate = eventDate;
    }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    @Override
    public String toString() {
        return "Events{" +
                "eventId=" + eventId +
                ", empId=" + empId +
                ", eventType='" + eventType + '\'' +
                ", eventDate=" + eventDate +
                '}';
    }
}
