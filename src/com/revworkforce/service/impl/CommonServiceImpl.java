package com.revworkforce.service.impl;

import com.revworkforce.dao.IAnnouncementsDao;
import com.revworkforce.dao.IEmployeeDao;
import com.revworkforce.dao.IHolidayCalendarDao;
import com.revworkforce.model.Announcements;
import com.revworkforce.model.Employee;
import com.revworkforce.model.HolidayCalendar;
import com.revworkforce.service.ICommonService;

import java.util.List;

public class CommonServiceImpl implements ICommonService {

    private IHolidayCalendarDao holidayDao;
    private IAnnouncementsDao announcementsDao;
    private IEmployeeDao employeeDao;

    public CommonServiceImpl(
            IHolidayCalendarDao holidayDao,
            IAnnouncementsDao announcementDao,
            IEmployeeDao employeeDao) {

        this.holidayDao = holidayDao;
        this.announcementsDao = announcementDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public List<HolidayCalendar> getAllHolidays() {
        return holidayDao.getAllHolidays();
    }

    @Override
    public List<Announcements> getAnnouncementsForUser(int userId) {
        return announcementsDao.getAnnouncementsByUser(userId);
    }

    @Override
    public List<Employee> getEmployeeDirectory() {
        return employeeDao.getAllEmployees();
    }
}

