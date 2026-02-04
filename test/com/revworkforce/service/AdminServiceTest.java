package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;
import com.revworkforce.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @Mock
    private IUserDao userDao;

    @Mock
    private IEmployeeDao employeeDao;

    @Mock
    private ILeaveBalanceDao leaveBalanceDao;

    @Mock
    private IDepartmentDao departmentDao;

    @Mock
    private IAnnouncementsDao announcementDao;

    @Mock
    private IHolidayCalendarDao holidayDao;

    @Mock
    private INotificationsDao notificationsDao;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= ADD EMPLOYEE =================

    @Test
    void addEmployee_shouldSucceed_whenAllStepsPass() {

        Users user = new Users();
        Employee emp = new Employee();

        when(userDao.createUser(user)).thenReturn(10);
        when(employeeDao.addEmployee(emp)).thenReturn(20);
        when(leaveBalanceDao.initializeBalance(20)).thenReturn(true);

        boolean result = adminService.addEmployee(user, emp);

        assertTrue(result);
        assertEquals(10, emp.getUserId());

        verify(userDao).createUser(user);
        verify(employeeDao).addEmployee(emp);
        verify(leaveBalanceDao).initializeBalance(20);
    }

    @Test
    void addEmployee_shouldThrowException_whenUserCreationFails() {

        when(userDao.createUser(any())).thenReturn(0);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adminService.addEmployee(new Users(), new Employee())
        );

        assertEquals("User creation failed", ex.getMessage());
    }

    @Test
    void addEmployee_shouldThrowException_whenEmployeeCreationFails() {

        when(userDao.createUser(any())).thenReturn(1);
        when(employeeDao.addEmployee(any())).thenReturn(0);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adminService.addEmployee(new Users(), new Employee())
        );

        assertEquals("Employee creation failed", ex.getMessage());
    }

    @Test
    void addEmployee_shouldThrowException_whenLeaveBalanceInitFails() {

        when(userDao.createUser(any())).thenReturn(1);
        when(employeeDao.addEmployee(any())).thenReturn(2);
        when(leaveBalanceDao.initializeBalance(2)).thenReturn(false);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adminService.addEmployee(new Users(), new Employee())
        );

        assertEquals("Leave balance initialization failed", ex.getMessage());
    }

    // ================= ASSIGN MANAGER =================

    @Test
    void assignManager_shouldSucceed_whenValidManager() {

        Employee manager = new Employee();
        when(employeeDao.getEmployeeById(2)).thenReturn(manager);
        when(employeeDao.updateManager(1, 2)).thenReturn(true);

        assertTrue(adminService.assignManager(1, 2));

        verify(employeeDao).updateManager(1, 2);
    }

    @Test
    void assignManager_shouldThrowException_whenSameEmployee() {

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adminService.assignManager(1, 1)
        );

        assertEquals("Employee cannot be their own manager", ex.getMessage());
    }

    @Test
    void assignManager_shouldThrowException_whenManagerInvalid() {

        when(employeeDao.getEmployeeById(2)).thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adminService.assignManager(1, 2)
        );

        assertEquals("Invalid manager", ex.getMessage());
    }

    // ================= EMPLOYEE =================

    @Test
    void updateEmployeeStatus_shouldReturnTrue() {

        when(employeeDao.updateEmployeeStatus(1, true)).thenReturn(true);

        assertTrue(adminService.updateEmployeeStatus(1, true));
        verify(employeeDao).updateEmployeeStatus(1, true);
    }

    @Test
    void getEmployeeProfile_shouldReturnEmployee() {

        Employee emp = new Employee();
        when(employeeDao.getEmployeeById(1)).thenReturn(emp);

        assertNotNull(adminService.getEmployeeProfile(1));
        verify(employeeDao).getEmployeeById(1);
    }

    // ================= DEPARTMENT =================

    @Test
    void addDepartment_shouldSucceed_whenNotExists() {

        Department dept = new Department();
        when(departmentDao.existsByName(any())).thenReturn(false);
        when(departmentDao.addDepartment(dept)).thenReturn(true);

        assertTrue(adminService.addDepartment(dept));
    }

    @Test
    void addDepartment_shouldThrowException_whenExists() {

        when(departmentDao.existsByName(any())).thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> adminService.addDepartment(new Department())
        );

        assertEquals("Department already exists", ex.getMessage());
    }

    @Test
    void updateDepartment_shouldReturnTrue() {

        when(departmentDao.updateDepartment(any())).thenReturn(true);

        assertTrue(adminService.updateDepartment(new Department()));
    }

    @Test
    void getAllDepartments_shouldReturnList() {

        when(departmentDao.getAllDepartments())
                .thenReturn(List.of(new Department()));

        assertEquals(1, adminService.getAllDepartments().size());
    }

    // ================= HOLIDAY =================

    @Test
    void addHoliday_shouldReturnTrue() {

        when(holidayDao.addHoliday(any())).thenReturn(true);

        assertTrue(adminService.addHoliday(new HolidayCalendar()));
    }

    @Test
    void removeHoliday_shouldReturnTrue() {

        when(holidayDao.deleteHoliday(1)).thenReturn(true);

        assertTrue(adminService.removeHoliday(1));
    }

    // ================= ANNOUNCEMENT =================

    @Test
    void createAnnouncement_shouldNotifyAllUsers() {

        Announcements announcement = new Announcements();
        announcement.setTitle("Test");

        Users user1 = new Users();
        user1.setUserId(1);
        Users user2 = new Users();
        user2.setUserId(2);

        when(announcementDao.createAnnouncement(announcement)).thenReturn(true);
        when(userDao.getAllActiveUsers()).thenReturn(List.of(user1, user2));

        boolean result = adminService.createAnnouncement(announcement);

        assertTrue(result);
        verify(notificationsDao, times(2))
                .addNotification(any(Notifications.class));
    }

    @Test
    void createAnnouncement_shouldReturnFalse_whenSaveFails() {

        when(announcementDao.createAnnouncement(any())).thenReturn(false);

        assertFalse(adminService.createAnnouncement(new Announcements()));
    }

    @Test
    void deleteAnnouncement_shouldReturnTrue() {

        when(announcementDao.deleteAnnouncement(1)).thenReturn(true);

        assertTrue(adminService.deleteAnnouncement(1));
    }
}
