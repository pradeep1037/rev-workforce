package com.revworkforce.service;



import com.revworkforce.dao.*;
import com.revworkforce.model.*;
import com.revworkforce.service.impl.ManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManagerServiceTest {

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

    @InjectMocks
    private ManagerServiceImpl managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= TEAM =================

    @Test
    void getMyTeam_shouldReturnEmployees() {
        when(employeeDao.getEmployeesByManager(1))
                .thenReturn(List.of(new Employee()));

        List<Employee> team = managerService.getMyTeam(1);

        assertEquals(1, team.size());
        verify(employeeDao).getEmployeesByManager(1);
    }

    // ================= PENDING LEAVES =================

    @Test
    void getPendingLeaveRequests_shouldReturnLeaves() {
        when(leaveDao.getPendingLeavesByManager(1))
                .thenReturn(List.of(new LeaveApplication()));

        List<LeaveApplication> leaves =
                managerService.getPendingLeaveRequests(1);

        assertEquals(1, leaves.size());
        verify(leaveDao).getPendingLeavesByManager(1);
    }

    // ================= APPROVE LEAVE (SUCCESS) =================

    @Test
    void approveLeave_shouldSucceed_whenValidRequest() {

        LeaveApplication leave = new LeaveApplication();
        leave.setEmpId(10);
        leave.setLeaveTypeId(1);
        leave.setStatus("PENDING");
        leave.setStartDate(LocalDate.now());
        leave.setEndDate(LocalDate.now().plusDays(2));

        Employee emp = new Employee();
        emp.setEmpId(10);
        emp.setManagerId(1);
        emp.setUserId(100);

        when(leaveDao.getLeaveById(5)).thenReturn(leave);
        when(employeeDao.getEmployeeById(10)).thenReturn(emp);

        boolean result =
                managerService.approveLeave(5, 1, "Approved");

        assertTrue(result);

        verify(leaveDao).approveLeave(5, 1, "Approved");
        verify(leaveBalanceDao)
                .updateBalance(10, 1, -3);
        verify(notificationsDao)
                .addNotification(any(Notifications.class));
    }

    @Test
    void approveLeave_shouldThrowException_whenLeaveNotFound() {

        when(leaveDao.getLeaveById(1)).thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> managerService.approveLeave(1, 1, "ok")
        );

        assertEquals("Leave request not found", ex.getMessage());
    }

    @Test
    void approveLeave_shouldThrowException_whenAlreadyProcessed() {

        LeaveApplication leave = new LeaveApplication();
        leave.setStatus("APPROVED");

        when(leaveDao.getLeaveById(1)).thenReturn(leave);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> managerService.approveLeave(1, 1, "ok")
        );

        assertEquals("Leave already processed", ex.getMessage());
    }

    @Test
    void approveLeave_shouldThrowException_whenUnauthorized() {

        LeaveApplication leave = new LeaveApplication();
        leave.setEmpId(10);
        leave.setStatus("PENDING");

        Employee emp = new Employee();
        emp.setManagerId(99);

        when(leaveDao.getLeaveById(1)).thenReturn(leave);
        when(employeeDao.getEmployeeById(10)).thenReturn(emp);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> managerService.approveLeave(1, 1, "ok")
        );

        assertEquals("Unauthorized approval", ex.getMessage());
    }

    // ================= REJECT LEAVE =================

    @Test
    void rejectLeave_shouldSucceed_whenValidRequest() {

        LeaveApplication leave = new LeaveApplication();
        leave.setEmpId(10);
        leave.setStatus("PENDING");

        Employee emp = new Employee();
        emp.setManagerId(1);
        emp.setUserId(200);

        when(leaveDao.getLeaveById(2)).thenReturn(leave);
        when(employeeDao.getEmployeeById(10)).thenReturn(emp);

        boolean result =
                managerService.rejectLeave(2, 1, "Rejected");

        assertTrue(result);

        verify(leaveDao).rejectLeave(2, 1, "Rejected");
        verify(notificationsDao)
                .addNotification(any(Notifications.class));
    }

    @Test
    void rejectLeave_shouldThrowException_whenLeaveNotFound() {

        when(leaveDao.getLeaveById(1)).thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> managerService.rejectLeave(1, 1, "no")
        );

        assertEquals("Leave request not found", ex.getMessage());
    }

    @Test
    void rejectLeave_shouldThrowException_whenAlreadyProcessed() {

        LeaveApplication leave = new LeaveApplication();
        leave.setStatus("REJECTED");

        when(leaveDao.getLeaveById(1)).thenReturn(leave);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> managerService.rejectLeave(1, 1, "no")
        );

        assertEquals("Leave already processed", ex.getMessage());
    }

    @Test
    void rejectLeave_shouldThrowException_whenUnauthorized() {

        LeaveApplication leave = new LeaveApplication();
        leave.setEmpId(10);
        leave.setStatus("PENDING");

        Employee emp = new Employee();
        emp.setManagerId(99);

        when(leaveDao.getLeaveById(1)).thenReturn(leave);
        when(employeeDao.getEmployeeById(10)).thenReturn(emp);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> managerService.rejectLeave(1, 1, "no")
        );

        assertEquals("Unauthorized rejection", ex.getMessage());
    }

    // ================= PERFORMANCE =================

    @Test
    void getTeamReviews_shouldReturnReviews() {

        when(performanceDao.getReviewsByManager(1))
                .thenReturn(List.of(new PerformanceReview()));

        List<PerformanceReview> reviews =
                managerService.getTeamReviews(1);

        assertEquals(1, reviews.size());
        verify(performanceDao).getReviewsByManager(1);
    }

    @Test
    void providePerformanceFeedback_shouldReturnTrue() {

        when(performanceDao.updateManagerFeedback(1, 5, "Good"))
                .thenReturn(true);

        assertTrue(
                managerService.providePerformanceFeedback(1, 5, "Good")
        );

        verify(performanceDao)
                .updateManagerFeedback(1, 5, "Good");
    }
}

