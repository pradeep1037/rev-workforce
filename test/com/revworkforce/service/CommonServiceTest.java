package com.revworkforce.service;

import com.revworkforce.dao.IAnnouncementsDao;
import com.revworkforce.dao.IEmployeeDao;
import com.revworkforce.dao.IHolidayCalendarDao;
import com.revworkforce.model.Announcements;
import com.revworkforce.model.Employee;
import com.revworkforce.model.HolidayCalendar;
import com.revworkforce.service.impl.CommonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommonServiceTest {

    @Mock
    private IHolidayCalendarDao holidayDao;

    @Mock
    private IAnnouncementsDao announcementsDao;

    @Mock
    private IEmployeeDao employeeDao;

    @InjectMocks
    private CommonServiceImpl commonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= HOLIDAYS =================

    @Test
    void getAllHolidays_shouldReturnHolidays() {

        when(holidayDao.getAllHolidays())
                .thenReturn(List.of(new HolidayCalendar()));

        List<HolidayCalendar> holidays = commonService.getAllHolidays();

        assertEquals(1, holidays.size());
        verify(holidayDao).getAllHolidays();
    }

    // ================= ANNOUNCEMENTS =================

    @Test
    void getAnnouncementsForUser_shouldReturnAnnouncements() {

        when(announcementsDao.getAnnouncementsByUser(10))
                .thenReturn(List.of(new Announcements()));

        List<Announcements> announcements =
                commonService.getAnnouncementsForUser(10);

        assertEquals(1, announcements.size());
        verify(announcementsDao).getAnnouncementsByUser(10);
    }

    // ================= EMPLOYEE DIRECTORY =================

    @Test
    void getEmployeeDirectory_shouldReturnEmployees() {

        when(employeeDao.getAllEmployees())
                .thenReturn(List.of(new Employee()));

        List<Employee> employees = commonService.getEmployeeDirectory();

        assertEquals(1, employees.size());
        verify(employeeDao).getAllEmployees();
    }
}
