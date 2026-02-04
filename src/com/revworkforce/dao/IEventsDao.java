package com.revworkforce.dao;

import com.revworkforce.model.Events;
import java.util.List;

public interface IEventsDao {

    boolean addEvent(Events event);

    Events getEventById(int eventId);

    List<Events> getEventsByEmpId(int empId);

    boolean deleteEvent(int eventId);
}
