package com.revworkforce.test;

import com.revworkforce.dao.IDepartmentDao;
import com.revworkforce.dao.impl.DepartmentDaoImpl;
import com.revworkforce.model.Department;

import java.util.List;

public class DepartmentDaoTest {

    public static void main(String[] args) {

        IDepartmentDao departmentDao = new DepartmentDaoImpl();

        System.out.println("========== INITIAL DEPARTMENTS ==========");
        printAllDepartments(departmentDao);

        /* ---------------- INSERT TEST ---------------- */
        System.out.println("\n========== INSERT TEST ==========");
        Department newDept = new Department("Research");

        boolean inserted = departmentDao.addDepartment(newDept);
        System.out.println("Insert successful: " + inserted);

        printAllDepartments(departmentDao);

        /* ---------------- FETCH BY ID TEST ---------------- */
        System.out.println("\n========== FETCH BY ID TEST ==========");
        Department dept = departmentDao.getDepartmentById(1);

        if (dept != null) {
            System.out.println("Fetched Department: " + dept);
        } else {
            System.out.println("Department with ID 1 not found");
        }

        /* ---------------- UPDATE TEST ---------------- */
        System.out.println("\n========== UPDATE TEST ==========");
        if (dept != null) {
            dept.setDepartmentName("Human Resources");
            boolean updated = departmentDao.updateDepartment(dept);
            System.out.println("Update successful: " + updated);
        }

        printAllDepartments(departmentDao);

        /* ---------------- DELETE TEST (OPTIONAL) ---------------- */
        System.out.println("\n========== DELETE TEST ==========");
        // delete only if you are sure (example ID used safely)
        int deleteId = 5;

        if (deleteId > 4) { // ensures base departments are not deleted
            boolean deleted = departmentDao.deleteDepartment(deleteId);
            System.out.println("Delete successful (ID " + deleteId + "): " + deleted);
        } else {
            System.out.println("Skipping delete to protect base departments");
        }

        printAllDepartments(departmentDao);

        System.out.println("\n========== TEST COMPLETED ==========");
    }

    /* ---------------- HELPER METHODS ---------------- */

    private static void printAllDepartments(IDepartmentDao dao) {
        List<Department> departments = dao.getAllDepartments();
        for (Department d : departments) {
            System.out.println(d);
        }
    }

    private static int getLastDepartmentId(IDepartmentDao dao) {
        List<Department> departments = dao.getAllDepartments();
        if (!departments.isEmpty()) {
            return departments.get(departments.size() - 1).getDepartmentId();
        }
        return -1;
    }
}
