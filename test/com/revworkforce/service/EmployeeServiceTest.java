package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;
import com.revworkforce.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private IEmployeeDao employeeDao;

    @Mock
    private ILeaveDao leaveDao;

    @Mock
    private ILeaveBalanceDao leaveBalanceDao;

    @Mock
    private INotificationsDao notificationsDao;

    @Mock
    private IPerformanceReviewDao performanceDao;

    @Mock
    private IGoalsDao goalsDao;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= PROFILE =================

    @Test
    void getMyProfile_shouldReturnEmployee() {
        Employee emp = new Employee();
        emp.setUserId(10);

        when(employeeDao.getEmployeeByUserId(10)).thenReturn(emp);

        Employee result = employeeService.getMyProfile(10);

        assertNotNull(result);
        assertEquals(10, result.getUserId());
        verify(employeeDao).getEmployeeByUserId(10);
    }

    // ================= MANAGER =================

    @Test
    void getMyManager_shouldReturnManager() {
        Employee emp = new Employee();
        emp.setEmpId(1);
        emp.setManagerId(2);

        Employee manager = new Employee();
        manager.setEmpId(2);

        when(employeeDao.getEmployeeById(1)).thenReturn(emp);
        when(employeeDao.getEmployeeById(2)).thenReturn(manager);

        Employee result = employeeService.getMyManager(1);

        assertNotNull(result);
        assertEquals(2, result.getEmpId());
        verify(employeeDao).getEmployeeById(1);
        verify(employeeDao).getEmployeeById(2);
    }

    // ================= APPLY LEAVE (SUCCESS) =================

    @Test
    void applyLeave_shouldSucceed_whenBalanceIsEnough() {
        LeaveApplication leave = new LeaveApplication();
        leave.setEmpId(1);
        leave.setLeaveTypeId(1);
        leave.setStartDate(LocalDate.now());
        leave.setEndDate(LocalDate.now().plusDays(2));

        LeaveBalance balance = new LeaveBalance();
        balance.setBalanceDays(5);

        Employee emp = new Employee();
        emp.setEmpId(1);
        emp.setManagerId(2);
        emp.setFirstName("Naveen");

        Employee manager = new Employee();
        manager.setUserId(100);

        when(leaveBalanceDao.getBalance(1, 1)).thenReturn(balance);
        when(employeeDao.getEmployeeById(1)).thenReturn(emp);
        when(employeeDao.getEmployeeById(2)).thenReturn(manager);

        boolean result = employeeService.applyLeave(leave);

        assertTrue(result);
        assertEquals("PENDING", leave.getStatus());
        verify(leaveDao).applyLeave(leave);
        verify(notificationsDao).addNotification(any(Notifications.class));
    }

    // ================= APPLY LEAVE (DATE ERROR) =================

    @Test
    void applyLeave_shouldThrowException_whenEndDateBeforeStartDate() {
        LeaveApplication leave = new LeaveApplication();
        leave.setStartDate(LocalDate.now());
        leave.setEndDate(LocalDate.now().minusDays(1));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> employeeService.applyLeave(leave)
        );

        assertEquals("End date cannot be before start date", ex.getMessage());
    }

    // ================= APPLY LEAVE (INSUFFICIENT BALANCE) =================

    @Test
    void applyLeave_shouldThrowException_whenBalanceIsInsufficient() {
        LeaveApplication leave = new LeaveApplication();
        leave.setEmpId(1);
        leave.setLeaveTypeId(1);
        leave.setStartDate(LocalDate.now());
        leave.setEndDate(LocalDate.now().plusDays(5));

        LeaveBalance balance = new LeaveBalance();
        balance.setBalanceDays(2);

        when(leaveBalanceDao.getBalance(1, 1)).thenReturn(balance);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> employeeService.applyLeave(leave)
        );

        assertEquals("Insufficient leave balance", ex.getMessage());
    }

    // ================= LEAVES =================

    @Test
    void getMyLeaves_shouldReturnLeaveList() {
        when(leaveDao.getLeavesByEmployee(1)).thenReturn(List.of(new LeaveApplication()));

        List<LeaveApplication> leaves = employeeService.getMyLeaves(1);

        assertEquals(1, leaves.size());
        verify(leaveDao).getLeavesByEmployee(1);
    }

    // ================= NOTIFICATIONS =================

    @Test
    void getMyNotifications_shouldReturnNotifications() {
        when(notificationsDao.getNotificationsByUserId(10))
                .thenReturn(List.of(new Notifications()));

        List<Notifications> notifications = employeeService.getMyNotifications(10);

        assertEquals(1, notifications.size());
        verify(notificationsDao).getNotificationsByUserId(10);
    }

    @Test
    void markNotificationAsRead_shouldReturnTrue() {
        when(notificationsDao.markAsRead(5)).thenReturn(true);

        boolean result = employeeService.markNotificationAsRead(5);

        assertTrue(result);
        verify(notificationsDao).markAsRead(5);
    }

    // ================= PERFORMANCE =================

    @Test
    void submitPerformanceReview_shouldReturnTrue() {
        PerformanceReview review = new PerformanceReview();

        when(performanceDao.addPerformanceReview(review)).thenReturn(true);

        assertTrue(employeeService.submitPerformanceReview(review));
        verify(performanceDao).addPerformanceReview(review);
    }

    // ================= GOALS =================

    @Test
    void getMyGoals_shouldReturnGoals() {
        when(goalsDao.getGoalsByEmpId(1)).thenReturn(List.of(new Goals()));

        List<Goals> goals = employeeService.getMyGoals(1);

        assertEquals(1, goals.size());
        verify(goalsDao).getGoalsByEmpId(1);
    }
}
