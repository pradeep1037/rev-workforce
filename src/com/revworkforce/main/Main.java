package com.revworkforce.main;

import com.revworkforce.controller.*;
import com.revworkforce.dao.*;
import com.revworkforce.dao.impl.*;
import com.revworkforce.model.Users;
import com.revworkforce.service.*;
import com.revworkforce.service.impl.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        /* ================= DAO LAYER ================= */

        IUserDao userDao = new UserDaoImpl();
        IEmployeeDao employeeDao = new EmployeeDaoImpl();
        IDepartmentDao departmentDao = new DepartmentDaoImpl();
        ILeaveDao leaveDao = new LeaveDaoImpl();
        ILeaveBalanceDao leaveBalanceDao = new LeaveBalanceDaoImpl();
        INotificationsDao notificationDao = new NotificationsDaoImpl();
        IPerformanceReviewDao performanceDao = new PerformanceReviewDaoImpl();
        IAnnouncementsDao announcementDao = new AnnouncementsDaoImpl();
        IHolidayCalendarDao holidayDao = new HolidayCalendarDaoImpl();
        IGoalsDao goalsDao=new GoalsDaoImpl();
        IEventsDao iEventsDao=new EventsDaoImpl();


        /* ================= SERVICE LAYER ================= */

        IAuthService authService =
                new AuthServiceImpl(userDao);

        IAdminService adminService =
                new AdminServiceImpl(
                        userDao,
                        employeeDao,
                        leaveBalanceDao,
                        departmentDao,
                        announcementDao,
                        holidayDao,
                        notificationDao
                );


        IEmployeeService employeeService =
                new EmployeeServiceImpl(
                        employeeDao,
                        leaveDao,
                        leaveBalanceDao,
                        notificationDao,
                        performanceDao,
                        goalsDao
                );

        IManagerService managerService =
                new ManagerServiceImpl(
                        employeeDao,
                        leaveDao,
                        leaveBalanceDao,
                        notificationDao,
                        performanceDao
                );

        ICommonService commonService =
                new CommonServiceImpl(
                        holidayDao,
                        announcementDao,
                        employeeDao
                );


        /* ================= CONTROLLER LAYER ================= */

        AuthController authController =
                new AuthController(authService);



        /* ================= APPLICATION LOOP ================= */

        Scanner scanner = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("   WELCOME TO REV WORKFORCE HRM");
        System.out.println("====================================");

        while (true) {

            Users loggedInUser = authController.login();

            if (loggedInUser != null) {

                EmployeeController employeeController =
                        new EmployeeController(employeeService);

                ManagerController managerController =
                        new ManagerController(managerService, employeeService);

                AdminController adminController =
                        new AdminController(loggedInUser, adminService);

                MainController mainController =
                        new MainController(
                                adminController,
                                employeeController,
                                managerController
                        );

                mainController.routeUser(loggedInUser);
            }

            System.out.print("\nDo you want to exit? (yes/no): ");
            String exitChoice = scanner.nextLine();

            if (exitChoice.equalsIgnoreCase("yes")) {
                System.out.println("Thank you for using Rev Workforce HRM.");
                break;
            }
        }


        scanner.close();
    }
}
