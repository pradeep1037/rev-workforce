package com.revworkforce.service;

import java.util.List;

import com.revworkforce.model.HolidayCalendar;
import com.revworkforce.model.Announcements;
import com.revworkforce.model.Employee;

public interface ICommonService {

    List<HolidayCalendar> getAllHolidays();

    List<Announcements> getAnnouncementsForUser(int userId);

    List<Employee> getEmployeeDirectory();
}
