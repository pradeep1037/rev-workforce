package com.revworkforce.dao;

import com.revworkforce.model.HolidayCalendar;
import java.time.LocalDate;
import java.util.List;

public interface IHolidayCalendarDao {

    boolean addHoliday(HolidayCalendar holiday);

    HolidayCalendar getHolidayById(int holidayId);

    List<HolidayCalendar> getAllHolidays();

    boolean deleteHoliday(int holidayId);
}
