/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.EmployeeClaimEntity;
import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.PayrollEntity;
import entity.TrainingScheduleEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author JustHRJ
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HiYewSystemBeanRemoteInterfaceTest {

    @PersistenceContext
    private EntityManager em;
    HiYewSystemBeanRemoteInterface HiYewSystemBeanInterface = lookupSystemUserSessionRemote();

    public HiYewSystemBeanRemoteInterfaceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addEmployee method, of class HiYewSystemBeanRemoteInterface.
     */
    @Test
    public void testAddEmployee() {
        System.out.println("addEmployee - correct case");
        String employee = "Testing3";
        String employee_passNumber = "G1234567H";
        String employee_address = "Ghim Moh";
        int number_of_leave = 12;
        String position = "staff";
        String username = "testing3";
        Timestamp expiry = null;
        String contact = "82236015";
        String addressPostal = "123456";
        String unit = "22";
        String optional = "123";
        double pay = 1230.0;
        Date employedDate = new Date();
        String employeeEmail = "hurulez@gmail.com";
        Vector expResult = new Vector();
        expResult.add(employee);
        expResult.add(username);
        Vector result = HiYewSystemBeanInterface.addEmployee(employee, employee_passNumber, employee_address, number_of_leave, position, username, expiry, contact, addressPostal, unit, optional, pay, employedDate, employeeEmail);
        expResult.add(result.get(2).toString());
        expResult.add(employeeEmail);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testAddEmployee2() {
        System.out.println("addEmployee - reAdd same employee");
        String employee = "Testing3";
        String employee_passNumber = "G1234567H";
        String employee_address = "Ghim Moh";
        int number_of_leave = 12;
        String position = "staff";
        String username = "testing3";
        Timestamp expiry = null;
        String contact = "82236015";
        String addressPostal = "123456";
        String unit = "22";
        String optional = "123";
        double pay = 1230.0;
        Date employedDate = new Date();
        String employeeEmail = "hurulez@gmail.com";
        Vector expResult = null;
        Vector result = HiYewSystemBeanInterface.addEmployee(employee, employee_passNumber, employee_address, number_of_leave, position, username, expiry, contact, addressPostal, unit, optional, pay, employedDate, employeeEmail);

        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testAddEmployee3() {
        System.out.println("addEmployee - exception while adding - Employee Date");
        String employee = "Testing4";
        String employee_passNumber = "G1234567H";
        String employee_address = "Ghim Moh";
        int number_of_leave = 12;
        String position = "staff";
        String username = "testing4";
        Timestamp expiry = null;
        String contact = "82236015";
        String addressPostal = "123456";
        String unit = "22";
        String optional = "123";
        double pay = 1230.0;
        Date employedDate = null; // error
        String employeeEmail = "hurulez@gmail.com";
        Vector expResult = null;
        Vector result = HiYewSystemBeanInterface.addEmployee(employee, employee_passNumber, employee_address, number_of_leave, position, username, expiry, contact, addressPostal, unit, optional, pay, employedDate, employeeEmail);

        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testAddEmployee4() {
        System.out.println("addEmployee - exception while adding - Username (null or '')");
        String employee = "Testing4";
        String employee_passNumber = "G1234567H";
        String employee_address = "Ghim Moh";
        int number_of_leave = 12;
        String position = "staff";
        String username = null; // first case, username == null
        Timestamp expiry = null;
        String contact = "82236015";
        String addressPostal = "123456";
        String unit = "22";
        String optional = "123";
        double pay = 1230.0;
        Date employedDate = new Date();
        String employeeEmail = "hurulez@gmail.com";
        Vector expResult = null;
        Vector result = HiYewSystemBeanInterface.addEmployee(employee, employee_passNumber, employee_address, number_of_leave, position, username, expiry, contact, addressPostal, unit, optional, pay, employedDate, employeeEmail);
        assertEquals(expResult, result);
        username = "";
        result = HiYewSystemBeanInterface.addEmployee(employee, employee_passNumber, employee_address, number_of_leave, position, username, expiry, contact, addressPostal, unit, optional, pay, employedDate, employeeEmail);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of updateEmployee method, of class HiYewSystemBeanRemoteInterface.
     */
    @Test
    public void testUpdateEmployee() {

        System.out.println("updateEmployee - edited either or all three cases");
        EmployeeEntity employee = HiYewSystemBeanInterface.viewEmployeeU("admin");

        String employeeA = "WhatSoEvr"; // edited
        String employeeUnit = "44";
        String employeeOptional = "";
        String address_postal = "44"; // edit
        String contact = "993123"; // edit
        Date pass = null;
        String position = "";
        double pay = 0.0;
        int leave = 0;
        String email = "";

        boolean result = HiYewSystemBeanInterface.updateEmployee(employee, employeeA, employeeUnit, employeeOptional, address_postal, contact, pass, position, pay, leave, email);
        assertTrue(result);

        employee = HiYewSystemBeanInterface.viewEmployeeU("admin");
        employeeA = "";
        employeeUnit = "22"; // edited
        employeeOptional = "";
        address_postal = "";
        contact = "";
        pass = null;
        position = "";
        pay = 0.0;
        leave = 0;
        email = "";
        result = HiYewSystemBeanInterface.updateEmployee(employee, employeeA, employeeUnit, employeeOptional, address_postal, contact, pass, position, pay, leave, email);
        assertTrue(result);

        employee = HiYewSystemBeanInterface.viewEmployeeU("admin");
        employeeA = "";
        employeeUnit = "";
        employeeOptional = "";
        address_postal = "22"; // edited
        contact = "";
        pass = null;
        position = "";
        pay = 0.0;
        leave = 0;
        email = "";
        result = HiYewSystemBeanInterface.updateEmployee(employee, employeeA, employeeUnit, employeeOptional, address_postal, contact, pass, position, pay, leave, email);
        assertTrue(result);

        employee = HiYewSystemBeanInterface.viewEmployeeU("admin");
        employeeA = "";
        employeeUnit = "";
        employeeOptional = "";
        address_postal = "";
        contact = "82236039"; //edited
        pass = null;
        position = "";
        pay = 0.0;
        leave = 0;
        email = "";
        result = HiYewSystemBeanInterface.updateEmployee(employee, employeeA, employeeUnit, employeeOptional, address_postal, contact, pass, position, pay, leave, email);
        assertTrue(result);

        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testUpdateEmployee2() {
        System.out.println("updateEmployee - no change, or same");
        EmployeeEntity employee = HiYewSystemBeanInterface.viewEmployeeU("admin");
        String employeeA = "";
        String employeeUnit = "22"; // edited before
        String employeeOptional = "";
        String address_postal = "";
        String contact = "";
        Date pass = null;
        String position = "";
        double pay = 0.0;
        int leave = 0;
        String email = "";
        boolean result = HiYewSystemBeanInterface.updateEmployee(employee, employeeA, employeeUnit, employeeOptional, address_postal, contact, pass, position, pay, leave, email);
        assertFalse(result);

        employee = HiYewSystemBeanInterface.viewEmployeeU("admin");
        employeeA = "";
        employeeUnit = "";
        employeeOptional = "";
        address_postal = "";
        contact = "";
        pass = null;
        position = "";
        pay = 0.0;
        leave = 0;
        email = "";
        result = HiYewSystemBeanInterface.updateEmployee(employee, employeeA, employeeUnit, employeeOptional, address_postal, contact, pass, position, pay, leave, email);
        assertFalse(result); // no edition has been made

    }

    @Test
    public void testUpdateEmployee3() {
        System.out.println("updateEmployee - no EmployeeEntity registered");
        EmployeeEntity employee = null;
        String employeeA = "";
        String employeeUnit = "22"; // edition made
        String employeeOptional = "";
        String address_postal = "";
        String contact = "";
        Date pass = null;
        String position = "";
        double pay = 0.0;
        int leave = 0;
        String email = "";
        boolean result = HiYewSystemBeanInterface.updateEmployee(employee, employeeA, employeeUnit, employeeOptional, address_postal, contact, pass, position, pay, leave, email);
        assertFalse(result);
    }

//    /**
//     * Test of applyLeave method, of class HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testApplyLeave() {
        System.out.println("applyLeave - correct case");
        String employee = "Justin";
        int days = 1;
        String remarks = "";

        Date start = new Date("11/12/2015");
        Date end = new Date("11/12/2015");
        String type = "paid";
        String expResult = "applied";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testApplyLeave2() {
        System.out.println("applyLeave - apply same day again");
        String employee = "Justin";
        int days = 1;
        String remarks = "";

        Date start = new Date("11/12/2015");
        Date end = new Date("11/12/2015");
        String type = "paid";
        String expResult = "Earlier leave has been applied";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testApplyLeave3() {
        System.out.println("applyLeave - apply between applied leaves");
        String employee = "Ben"; // had applied leave from 26 - 30 nov
        int days = 1;
        String remarks = "";

        Date start = new Date("11/27/2015"); // in between
        Date end = new Date("11/27/2015"); //
        System.out.println(start + " " + end);
        String type = "paid";
        String expResult = "Earlier leave has been applied";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);

        employee = "Ben"; // had applied leave from 26 - 30 nov
        days = 3;
        remarks = "";

        start = new Date("11/25/2015"); // before start (26)
        end = new Date("11/27/2015"); //  after start (26)
        type = "paid";
        expResult = "Earlier leave has been applied";
        result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);

        employee = "Ben"; // had applied leave from 26 - 30 nov
        days = 3;
        remarks = "";

        start = new Date("11/29/2015"); // before end, after start (26,30)
        end = new Date("12/1/2015"); //  after end, after start (26, 30)
        type = "paid";
        expResult = "Earlier leave has been applied";
        result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);

        employee = "Ben"; // had applied leave from 26 - 30 nov
        days = 7;
        remarks = "";

        start = new Date("11/25/2015"); // before start (26)
        end = new Date("12/1/2015"); //  after end (30)
        type = "paid";
        expResult = "Earlier leave has been applied";
        result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.
    }

//    /**
//     * Test of viewAllLeave method, of class HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testApplyLeave4() {
        System.out.println("applyLeave - Missing Name info");
        String employee = "";
        int days = 1;
        String remarks = "";

        Date start = new Date("11/12/2015");
        Date end = new Date("11/12/2015");
        String type = "paid";
        String expResult = "No such Employee";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        employee = null;
        days = 1;
        remarks = "";

        start = new Date("11/12/2015");
        end = new Date("11/12/2015");
        type = "paid";
        expResult = "No such Employee";
        result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
    }

    @Test
    public void testApplyLeave5() {
        System.out.println("applyLeave - Employee Apply more than leave allowance");
        String employee = "Justin";
        int days = 16;
        String remarks = "";

        Date start = new Date("12/1/2015");
        Date end = new Date("12/15/2015");
        String type = "paid";
        String expResult = "not enought leave";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        employee = null;
        days = 1;
        remarks = "";

        start = new Date("11/12/2015");
        end = new Date("11/12/2015");
        type = "paid";
        expResult = "No such Employee";
        result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
    }

    @Test
    public void testApplyLeave6() {
        System.out.println("applyLeave - Employee is disabled");
        String employee = "Jit Cheong";
        int days = 1;
        String remarks = "";

        Date start = new Date("11/12/2015");
        Date end = new Date("11/12/2015");
        String type = "paid";
        String expResult = "Employee is disabled";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testApplyLeave7() {
        System.out.println("applyLeave - End date starts before start date");
        String employee = "Ben";
        int days = -1;
        String remarks = "";

        Date start = new Date("12/1/2015");
        Date end = new Date("12/2/2015");
        String type = "paid";
        String expResult = "End date start before start date";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testApplyLeave8() {
        System.out.println("applyLeave - Applied date is earlier than today");
        String employee = "Ben";
        int days = 1;
        String remarks = "";

        Date start = new Date("11/8/2015");
        Date end = new Date("11/8/2015");
        String type = "paid";
        String expResult = "Applied date is before today!";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testApplyLeave9() {
        System.out.println("applyLeave - Applying leave before employed date");
        String employee = "Jack";
        int days = 1;
        String remarks = "";

        Date start = new Date("11/12/2015");
        Date end = new Date("11/12/2015");
        String type = "paid";
        String expResult = "Cannot apply leave before employed Date";
        String result = HiYewSystemBeanInterface.applyLeave(employee, days, remarks, start, end, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewAllLeave() {
        System.out.println("viewAllLeave - Pending Leave has some records");
        List<Vector> result = HiYewSystemBeanInterface.viewAllLeave();
        assertEquals(result.size(), 1);
        // TODO review the generated test code and remove the default call to fail.

    }
//
//    /**
//     * Test of approveLeaveID method, of class HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testApproveLeaveID() {
        System.out.println("approveLeaveID - correct case");
        Long id = Long.parseLong("2501");
        String name = "Ben";
        HiYewSystemBeanInterface.approveLeaveID(id, name);

    }

    @Test(expected = EJBException.class)
    public void testApproveLeaveID2() {
        System.out.println("approveLeaveID - employee is null or ''");
        Long id = Long.parseLong("2501");
        String name = null;
        HiYewSystemBeanInterface.approveLeaveID(id, name);

    }

//    /**
//     * Test of viewEmployee method, of class HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testViewEmployee() {
        System.out.println("viewEmployee - Employee username exist");
        String employeeName = "admin";

        List<EmployeeEntity> result = HiYewSystemBeanInterface.viewEmployee(employeeName);
        assertEquals(result.size(), 1);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployee2() {
        System.out.println("viewEmployee - employee does not exist");
        String employeeName = "Justin1";

        List<EmployeeEntity> result = HiYewSystemBeanInterface.viewEmployee(employeeName);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployee3() {
        System.out.println("viewEmployee - EmployeeName is null or ''");
        String employeeName = "";

        List<EmployeeEntity> result = HiYewSystemBeanInterface.viewEmployee(employeeName);
        assertNull(result);

        employeeName = null;

        result = HiYewSystemBeanInterface.viewEmployee(employeeName);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }
//
//    /**
//     * Test of viewEmployeeLeave method, of class
//     * HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testViewEmployeeLeave() {
        System.out.println("viewEmployeeLeave view all leave for Jusin");
        String employeeName = "Jit Cheong";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeave(employeeName);
        assertEquals(result.size(), 2);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeLeave2() {
        System.out.println("viewEmployeeLeave view no existing leave for kat");
        String employeeName = "kat";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeave(employeeName);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeLeave3() {
        System.out.println("viewEmployeeLeave view no existing leave for none existing employee");
        String employeeName = "no exist";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeave(employeeName);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeLeave4() {
        System.out.println("viewEmployeeLeave view existing leaves for employeeName is null or ''");
        String employeeName = "";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeave(employeeName);
        assertNull(result);
        employeeName = null;

        result = HiYewSystemBeanInterface.viewEmployeeLeave(employeeName);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }
//
//    /**
//     * Test of viewAllEmployee method, of class HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testViewAllEmployee() {
        System.out.println("viewAllEmployee");
        List<EmployeeEntity> result = HiYewSystemBeanInterface.viewAllEmployee();
        assertEquals(result.size(), 19);
        // TODO review the generated test code and remove the default call to fail.

    }

//
//    /**
//     * Test of EmployeeStatus method, of class HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testEmployeeStatus() {
        System.out.println("EmployeeStatus - Retrieving the status of Justin");
        String employeeName = "Justin";

        String expResult = "admin";
        String result = HiYewSystemBeanInterface.EmployeeStatus(employeeName);
        assertEquals(expResult, result);

    }

    @Test
    public void testEmployeeStatus2() {
        System.out.println("EmployeeStatus - Retrieving the instance or null or ''");
        String employeeName = "";

        String expResult = "";
        String result = HiYewSystemBeanInterface.EmployeeStatus(employeeName);
        assertEquals(expResult, result);

        employeeName = null;

        result = HiYewSystemBeanInterface.EmployeeStatus(employeeName);
        assertEquals(expResult, result);

    }

    @Test
    public void testEmployeeStatus3() {
        System.out.println("EmployeeStatus - Retrieving the status of no existing employee");
        String employeeName = "Justi";

        String expResult = "";
        String result = HiYewSystemBeanInterface.EmployeeStatus(employeeName);
        assertEquals(expResult, result);

    }

//
//    /**
//     * Test of approveByEmployee method, of class
//     * HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testApproveByEmployee() {
        System.out.println("approveByEmployee - admin leave being approved");
        String employee = "Justin";
        boolean result = HiYewSystemBeanInterface.approveByEmployee(employee);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail. 
    }

    @Test
    public void testApproveByEmployee2() {
        System.out.println("approveByEmployee - no such employee");
        String employee = "No such Employee";
        boolean result = HiYewSystemBeanInterface.approveByEmployee(employee);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail. 
    }

    @Test
    public void testApproveByEmployee3() {
        System.out.println("approveByEmployee - employee is null or ''");
        String employee = "";
        boolean result = HiYewSystemBeanInterface.approveByEmployee(employee);
        assertFalse(result);

        employee = null;
        result = HiYewSystemBeanInterface.approveByEmployee(employee);
        assertFalse(result);

// TODO review the generated test code and remove the default call to fail. 
    }

    @Test
    public void testApproveByEmployee4() {
        System.out.println("approveByEmployee - employee pending leave overshot the current limit");
        String employee = "Jared";
        boolean result = HiYewSystemBeanInterface.approveByEmployee(employee);
        assertFalse(result);

// TODO review the generated test code and remove the default call to fail. 
    }
//
//    /**
//     * Test of cancelLeaveApplication method, of class
//     * HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testCancelLeaveApplication() {
        System.out.println("cancelLeaveApplication - Normal Case");
        String employee = "Jared";
        Long id = Long.parseLong("24");

        HiYewSystemBeanInterface.cancelLeaveApplication(employee, id);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test(expected = EJBException.class)
    public void testCancelLeaveApplication2() {
        System.out.println("cancelLeaveApplication - employee name is null or ''");
        String employee = null;
        Long id = Long.parseLong("24");
        HiYewSystemBeanInterface.cancelLeaveApplication(employee, id);
        // TODO review the generated test code and remove the default call to fail.
    }
//
//    /**
//     * Test of viewEmployeeLeavePending method, of class
//     * HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testViewEmployeeLeavePending() {
        System.out.println("viewEmployeeLeavePending -Employee has some Leave Pending");
        String employeeName = "Jared";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeavePending(employeeName);
        assertEquals(result.size(), 1);

    }

    @Test
    public void testViewEmployeeLeavePending2() {
        System.out.println("viewEmployeeLeavePending - Employee Has no Pending Leave");
        String employeeName = "Jack";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeavePending(employeeName);
        assertNull(result);

    }

    @Test
    public void testViewEmployeeLeavePending3() {
        System.out.println("viewEmployeeLeavePending - Employee is ''");
        String employeeName = "";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeavePending(employeeName);
        assertNull(result);

    }

    @Test
    public void testViewEmployeeLeavePending4() {
        System.out.println("viewEmployeeLeavePending - Employee is null");
        String employeeName = null;

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeavePending(employeeName);
        assertNull(result);

    }

    @Test
    public void testViewEmployeeLeavePending5() {
        System.out.println("viewEmployeeLeavePending - Employee does not exist");
        String employeeName = "No Name";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeavePending(employeeName);
        assertNull(result);

    }

//    /**
//     * Test of login method, of class HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testLogin() {
        System.out.println("login - Correct Username and Password");
        String username = "admin";
        String password = "password1";
        String expResult = "admin";
        String result = HiYewSystemBeanInterface.login(username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testLogin2() {
        System.out.println("login - Incorrect Username and/or Password");
        String username = "admin";
        String password = "jitcheon"; // wrong password
        String expResult = "fail";
        String result = HiYewSystemBeanInterface.login(username, password);
        assertEquals(expResult, result);
        username = "admi"; //wrong username
        password = "jitcheong";
        expResult = "fail";
        result = HiYewSystemBeanInterface.login(username, password);
        assertEquals(expResult, result);
        username = "admi"; // wrong username
        password = "jitcheon"; // wrong password
        expResult = "fail";
        result = HiYewSystemBeanInterface.login(username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testLogin3() {
        System.out.println("login - Username is disabled");
        String username = "testing";
        String password = "krn5eermi9";
        String expResult = "disabled";
        String result = HiYewSystemBeanInterface.login(username, password);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testLogin4() {
        System.out.println("login - Username is first time");
        String username = "testing2";
        String password = "kbissaeeuf";
        String expResult = "firstTime";
        String result = HiYewSystemBeanInterface.login(username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

//
//    /**
//     * Test of viewEmployeeLeaveU method, of class
//     * HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testViewEmployeeLeaveU() {
        System.out.println("viewEmployeeLeaveU - There is is instances of leaveEntity");
        String username = "jared";
        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeaveU(username);
        assertEquals(result.size(), 1);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testViewEmployeeLeaveU2() {
        System.out.println("viewEmployeeLeaveU - no existing username");
        String username = "Justin";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeaveU(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeLeaveU3() {
        System.out.println("viewEmployeeLeaveU - No Records");
        String username = "testing2";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeaveU(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeLeaveU4() {
        System.out.println("viewEmployeeLeaveU - usnername is null");
        String username = null;

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeaveU(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeLeaveU5() {
        System.out.println("viewEmployeeLeaveU - usnername is ''");
        String username = "";

        List<LeaveEntity> result = HiYewSystemBeanInterface.viewEmployeeLeaveU(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }
//
//    /**
//     * Test of viewEmployeeU method, of class HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testViewEmployeeU() {
        System.out.println("viewEmployeeU - Employee Exist (right username)");
        String username = "admin";

        EmployeeEntity expResult = new EmployeeEntity();
        expResult.setAccount_status("normal");
        expResult.setAddressPostal("271022");
        expResult.setAvailability(null);
        expResult.setEmailAddress("hurulez@gmail.com");
        expResult.setEmployee_account_status("admin");
        expResult.setEmployee_address("Ghim Moh Link");
        expResult.setEmployee_basic(2400.00);
        expResult.setEmployee_contact("82236015");
        expResult.setEmployee_employedDate(new Timestamp(2015 - 10 - 29));
        expResult.setEmployee_name("Justin");
        expResult.setEmployee_passExpiry(null);
        expResult.setEmployee_passNumber("G1234X");
        expResult.setId(Long.parseLong("1"));
        expResult.setLeaveRecords(null);
        expResult.setOptional("22-214");
        expResult.setPassword("1d9d937aa423df8e442c69f5c016befc");
        expResult.setPayRecords(null);
        expResult.setPreviousPosition(null);
        expResult.setSecretAnswer(null);
        expResult.setSecretQuestion(null);
        expResult.setUnit("22");
        expResult.setUsername("admin");
        EmployeeEntity result = HiYewSystemBeanInterface.viewEmployeeU(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeU2() {
        System.out.println("viewEmployeeU - null");
        String username = null;

        EmployeeEntity result = HiYewSystemBeanInterface.viewEmployeeU(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeU3() {
        System.out.println("viewEmployeeU - ''");
        String username = "";

        EmployeeEntity result = HiYewSystemBeanInterface.viewEmployeeU(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testViewEmployeeU4() {
        System.out.println("viewEmployeeU - Wrong username");
        String username = "wrong username";

        EmployeeEntity expResult = new EmployeeEntity();
        expResult.setAccount_status("normal");
        expResult.setAddressPostal("271022");
        expResult.setAvailability(null);
        expResult.setEmailAddress("hurulez@gmail.com");
        expResult.setEmployee_account_status("admin");
        expResult.setEmployee_address("Ghim Moh Link");
        expResult.setEmployee_basic(2400.00);
        expResult.setEmployee_contact("82236015");
        expResult.setEmployee_employedDate(new Timestamp(2015 - 10 - 29));
        expResult.setEmployee_name("Justin");
        expResult.setEmployee_passExpiry(null);
        expResult.setEmployee_passNumber("G1234X");
        expResult.setId(Long.parseLong("1"));
        expResult.setLeaveRecords(null);
        expResult.setOptional("22-214");
        expResult.setPassword("1d9d937aa423df8e442c69f5c016befc");
        expResult.setPayRecords(null);
        expResult.setPreviousPosition(null);
        expResult.setSecretAnswer(null);
        expResult.setSecretQuestion(null);
        expResult.setUnit("22");
        expResult.setUsername("admin");
        EmployeeEntity result = HiYewSystemBeanInterface.viewEmployeeU(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }
//
//    /**
//     * Test of getEmployee method, of class HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testGetEmployee() {
        System.out.println("getEmployee - existing records of employee");

        List<String> result = HiYewSystemBeanInterface.getEmployee();
        assertEquals(result.size(), 19);
        // TODO review the generated test code and remove the default call to fail.

    }

//
//    /**
//     * Test of getEmployeeE method, of class HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testGetEmployeeE() {
        System.out.println("getEmployeeE - correct case");
        String username = "jared";

        List<String> expResult = new ArrayList<String>();
        expResult.add("Jared");
        List<String> result = HiYewSystemBeanInterface.getEmployeeE(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testGetEmployeeE2() {
        System.out.println("getEmployeeE - no such employee");
        String username = "jared2";

        List<String> result = HiYewSystemBeanInterface.getEmployeeE(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testGetEmployeeE3() {
        System.out.println("getEmployeeE - null or '' employee username");
        String username = "";

        List<String> result = HiYewSystemBeanInterface.getEmployeeE(username);
        assertNull(result);

        username = null;

        result = HiYewSystemBeanInterface.getEmployeeE(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }

//
//    /**
//     * Test of expiredEmployees method, of class HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testExpiredEmployees() {
        System.out.println("expiredEmployees - expired employees");
        List<EmployeeEntity> result = HiYewSystemBeanInterface.expiredEmployees();
        assertEquals(result.size(), 16);

    }
//
//    /**
//     * Test of expiredEmployee method, of class HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testExpiredEmployee() {
        System.out.println("expiredEmployee getting an expired employee");
        String username = "jared";
        List<EmployeeEntity> result = HiYewSystemBeanInterface.expiredEmployee(username);
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testExpiredEmployee2() {
        System.out.println("expiredEmployee getting no expired employee");
        String username = "admin";
        List<EmployeeEntity> result = HiYewSystemBeanInterface.expiredEmployee(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testExpiredEmployee3() {
        System.out.println("expiredEmployee - employee either '' or null");
        String username = "";
        List<EmployeeEntity> result = HiYewSystemBeanInterface.expiredEmployee(username);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.
        username = null;
        result = HiYewSystemBeanInterface.expiredEmployee(username);
        assertNull(result);

    }

//
//    /**
//     * Test of extendEmployeePass method, of class
//     * HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testExtendEmployeePass() {
        System.out.println("extendEmployeePass - normal extension");
        String employeeName = "Jared";
        Timestamp next = new Timestamp(new Date().getTime());

        boolean result = HiYewSystemBeanInterface.extendEmployeePass(employeeName, next);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testExtendEmployeePass2() {
        System.out.println("extendEmployeePass - employee name or time extension is '' or null");
        String employeeName = "";
        Timestamp next = new Timestamp(new Date().getTime());

        boolean result = HiYewSystemBeanInterface.extendEmployeePass(employeeName, next);
        assertFalse(result);

        employeeName = "Jared";
        next = null;

        result = HiYewSystemBeanInterface.extendEmployeePass(employeeName, next);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testExtendEmployeePass3() {
        System.out.println("extendEmployeePass - no such employee");
        String employeeName = "Jared2";
        Timestamp next = new Timestamp(new Date().getTime());

        boolean result = HiYewSystemBeanInterface.extendEmployeePass(employeeName, next);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

//    /**
//     * Test of getENoAlert method, of class HiYewSystemBeanRemoteInterface.
//     */
    @Test
    public void testGetENoAlert() {
        System.out.println("getENoAlert");

        int expResult = 16;
        int result = HiYewSystemBeanInterface.getENoAlert();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
//
//    /**
//     * Test of payRecords method, of class HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testPayRecords() {
        System.out.println("payRecords - exisiting pay records");

        List<PayrollEntity> result = HiYewSystemBeanInterface.payRecords();
        assertEquals(result.size(), 15);
        // TODO review the generated test code and remove the default call to fail.

    }
//
//    /**
//     * Test of createPayroll method, of class HiYewSystemBeanRemoteInterface.
//     */

    @Test
    public void testCreatePayroll() {
        System.out.println("createPayroll - payRecords existing(Jared)");
        String employeeName = "Jack";
        int late = 0;
        int sick = 0;
        double overtime = 0.0;

        boolean result = HiYewSystemBeanInterface.createPayroll(employeeName, late, sick, overtime);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCreatePayroll2() {
        System.out.println("createPayroll - no payRecords existing(Jared)");
        String employeeName = "Jack";
        int late = 0;
        int sick = 0;
        double overtime = 0.0;

        boolean result = HiYewSystemBeanInterface.createPayroll(employeeName, late, sick, overtime);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCreatePayroll3() {
        System.out.println("createPayroll - negative numbers");
        String employeeName = "Justin";
        int late = -3;
        int sick = 2;
        double overtime = 0.0;

        boolean result = HiYewSystemBeanInterface.createPayroll(employeeName, late, sick, overtime);
        assertFalse(result);

        employeeName = "Justin";
        late = 0;
        sick = -2;
        overtime = 0.0;

        result = HiYewSystemBeanInterface.createPayroll(employeeName, late, sick, overtime);
        assertFalse(result);

        employeeName = "Justin";
        late = 0;
        sick = 2;
        overtime = -1.0;

        result = HiYewSystemBeanInterface.createPayroll(employeeName, late, sick, overtime);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCreatePayroll4() {
        System.out.println("createPayroll - employeeName is null or '' or wrong no existing employee name");
        String employeeName = "";
        int late = 0;
        int sick = 0;
        double overtime = 0.0;

        boolean result = HiYewSystemBeanInterface.createPayroll(employeeName, late, sick, overtime);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
        employeeName = null;
        late = 0;
        sick = 0;
        overtime = 0.0;
        result = HiYewSystemBeanInterface.createPayroll(employeeName, late, sick, overtime);
        assertFalse(result);

        employeeName = "no employeeName";
        late = 0;
        sick = 0;
        overtime = 0.0;
        result = HiYewSystemBeanInterface.createPayroll(employeeName, late, sick, overtime);
        assertFalse(result);
    }

    //
    //    /**
    //     * Test of getPayroll method, of class HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testGetPayroll_String_String() {
        System.out.println("getPayroll(employeeName, month");
        String employeeName = "Jared";
        String month = "Oct";
        List<PayrollEntity> result = HiYewSystemBeanInterface.getPayroll(employeeName, month);
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testGetPayroll_String_String2() {
        System.out.println("getPayroll(employeeName, month) - no existing records");
        String employeeName = "Ben";
        String month = "Oct";
        List<PayrollEntity> result = HiYewSystemBeanInterface.getPayroll(employeeName, month);
        assertNull(result);

        employeeName = "Jack";
        month = "Nov";
        result = HiYewSystemBeanInterface.getPayroll(employeeName, month);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testGetPayroll_String_String3() {
        System.out.println("getPayroll(employeeName, month) - EmployeeName is null or ''");
        String employeeName = "";
        String month = "Oct";
        List<PayrollEntity> result = HiYewSystemBeanInterface.getPayroll(employeeName, month);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

        employeeName = null;
        month = "Oct";
        result = HiYewSystemBeanInterface.getPayroll(employeeName, month);
        assertNull(result);
    }

    @Test
    public void testGetPayroll_String_String4() {
        System.out.println("getPayroll(employeeName, month) - month is null or ''");
        String employeeName = "Jack";
        String month = "";
        List<PayrollEntity> result = HiYewSystemBeanInterface.getPayroll(employeeName, month);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

        employeeName = "Jack";
        month = null;
        result = HiYewSystemBeanInterface.getPayroll(employeeName, month);
        assertNull(result);
    }

    //    /**
    //     * Test of existEmployeeName method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testExistEmployeeName() {
        System.out.println("existEmployeeName - employeeName exist");
        String employeeName = "Justin";

        boolean result = HiYewSystemBeanInterface.existEmployeeName(employeeName);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testExistEmployeeName2() {
        System.out.println("existEmployeeName - employeeName does exist");
        String employeeName = "Justin1";

        boolean result = HiYewSystemBeanInterface.existEmployeeName(employeeName);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testExistEmployeeName3() {
        System.out.println("existEmployeeName - employeeName is '' or null");
        String employeeName = "";

        boolean result = HiYewSystemBeanInterface.existEmployeeName(employeeName);
        assertFalse(result);

        employeeName = null;
        result = HiYewSystemBeanInterface.existEmployeeName(employeeName);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    //
    //    /**
    //     * Test of existEmployeeNumber method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testExistEmployeeNumber() {
        System.out.println("existEmployeeNumber - not existing");
        String employeeNumber = "G1234X";

        boolean result = HiYewSystemBeanInterface.existEmployeeNumber(employeeNumber);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testExistEmployeeNumber2() {
        System.out.println("existEmployeeNumber - not existing");
        String employeeNumber = "G1231231234X";
        boolean result = HiYewSystemBeanInterface.existEmployeeNumber(employeeNumber);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testExistEmployeeNumber3() {
        System.out.println("existEmployeeNumber - null or ''");
        String employeeNumber = "";
        boolean result = HiYewSystemBeanInterface.existEmployeeNumber(employeeNumber);
        assertFalse(result);

        employeeNumber = null;
        result = HiYewSystemBeanInterface.existEmployeeNumber(employeeNumber);
        assertFalse(result);

// TODO review the generated test code and remove the default call to fail.
    }

    //
    //    /**
    //     * Test of existEmployeeUsername method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testExistEmployeeUsername() {
        System.out.println("existEmployeeUsername username does not exist");
        String username = "Jason123";

        boolean result = HiYewSystemBeanInterface.existEmployeeUsername(username);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testExistEmployeeUsername2() {
        System.out.println("existEmployeeUsername - username exist");
        String username = "admin";

        boolean result = HiYewSystemBeanInterface.existEmployeeUsername(username);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testExistEmployeeUsername3() {
        System.out.println("existEmployeeUsername - username is null or ''");
        String username = "";

        boolean result = HiYewSystemBeanInterface.existEmployeeUsername(username);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
        username = null;
        result = HiYewSystemBeanInterface.existEmployeeUsername(username);
        assertFalse(result);
    }

    //
    //    /**
    //     * Test of notExistExpiredName method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testNotExistExpiredName() {
        System.out.println("notExistExpiredName - existing record");
        String name = "Jared";
        boolean result = HiYewSystemBeanInterface.notExistExpiredName(name);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testNotExistExpiredName2() {
        System.out.println("notExistExpiredName - no existing record");
        String name = "Justin";
        boolean result = HiYewSystemBeanInterface.notExistExpiredName(name);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testNotExistExpiredName3() {
        System.out.println("notExistExpiredName - name is null or ''");
        String name = "";
        boolean result = HiYewSystemBeanInterface.notExistExpiredName(name);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.
        name = null;
        result = HiYewSystemBeanInterface.notExistExpiredName(name);
        assertTrue(result);
    }

    @Test
    public void testNotExistExpiredName4() {
        System.out.println("notExistExpiredName - no such Employee");
        String name = "no such employee";
        boolean result = HiYewSystemBeanInterface.notExistExpiredName(name);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    //
    //    /**
    //     * Test of getReleasingPayRecords method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testGetReleasingPayRecords() {
        System.out.println("getReleasingPayRecords");

        List<PayrollEntity> result = HiYewSystemBeanInterface.getReleasingPayRecords();
        assertEquals(result.size(), 2);
        // TODO review the generated test code and remove the default call to fail.

    }

    //
    //    /**
    //     * Test of releaseAllPay method, of class HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testReleaseAllPay() {
        System.out.println("releaseAllPay");
        HiYewSystemBeanInterface.releaseAllPay();
        List<PayrollEntity> result = HiYewSystemBeanInterface.getReleasingPayRecords();
        assertNull(result); // should ensure that each time it runs, result from releasing payroll should be 0

        // TODO review the generated test code and remove the default call to fail.      
    }

    //    /**
    //     * Test of updatePay method, of class HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testUpdatePay() {
        System.out.println("updatePay - right case");
        List<PayrollEntity> payrolls = HiYewSystemBeanInterface.getPayroll("Justin");
        PayrollEntity pay = payrolls.get(0);
        boolean bonus = true; // change to true to false
        double others = 0.0;

        boolean result = HiYewSystemBeanInterface.updatePay(pay, bonus, others);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    //    /**
    //     * Test of addTrainingSchedule method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testAddTrainingSchedule() {
        System.out.println("addTrainingSchedule - normal case");
        String trainingName = "Training 1";
        Date trainingStart = new Date();
        Date trainingEnd = new Date();
        String trainingDescription = "";
        int size = 3;
        String trainingCode = "1234";

        boolean result = HiYewSystemBeanInterface.addTrainingSchedule(trainingName, trainingStart, trainingEnd, trainingDescription, size, trainingCode);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testAddTrainingSchedule2() {
        System.out.println("addTrainingSchedule - training code exist");
        String trainingName = "Training 2";
        Date trainingStart = new Date();
        Date trainingEnd = new Date();
        String trainingDescription = "";
        int size = 3;
        String trainingCode = "1234";

        boolean result = HiYewSystemBeanInterface.addTrainingSchedule(trainingName, trainingStart, trainingEnd, trainingDescription, size, trainingCode);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testAddTrainingSchedule3() {
        System.out.println("addTrainingSchedule - variables are null or '' when adding");
        String trainingName = "1234";
        Date trainingStart = new Date();
        Date trainingEnd = new Date();
        String trainingDescription = "";
        int size = 3;
        String trainingCode = "";

        boolean result = HiYewSystemBeanInterface.addTrainingSchedule(trainingName, trainingStart, trainingEnd, trainingDescription, size, trainingCode);
        assertFalse(result);

        trainingName = "training 2";
        trainingStart = new Date();
        trainingEnd = new Date();
        trainingDescription = "";
        size = 3;
        trainingCode = null;

        result = HiYewSystemBeanInterface.addTrainingSchedule(trainingName, trainingStart, trainingEnd, trainingDescription, size, trainingCode);
        assertFalse(result);

        trainingName = "Training 2";
        trainingStart = null;
        trainingEnd = new Date();
        trainingDescription = "";
        size = 3;
        trainingCode = "1237";

        result = HiYewSystemBeanInterface.addTrainingSchedule(trainingName, trainingStart, trainingEnd, trainingDescription, size, trainingCode);
        assertFalse(result);

        trainingName = "Training 3";
        trainingStart = new Date();
        trainingEnd = new Date();
        trainingDescription = "";
        size = -1;
        trainingCode = "1239";

        result = HiYewSystemBeanInterface.addTrainingSchedule(trainingName, trainingStart, trainingEnd, trainingDescription, size, trainingCode);
        assertFalse(result);

        // TODO review the generated test code and remove the default call to fail.
    }

    //
    //    /**
    //     * Test of trainingSchedueList method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testTrainingSchedueList() {
        System.out.println("trainingSchedueList - training have a schedule");

        List<TrainingScheduleEntity> result = HiYewSystemBeanInterface.trainingSchedueList();
        assertEquals(result.size(), 1);
        // TODO review the generated test code and remove the default call to fail.

    }

    //
    //    /**
    //     * Test of addTrainingEmployee method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testUAddTrainingEmployee() {
        System.out.println("addTrainingEmployee");
        List<TrainingScheduleEntity> scheduleList = HiYewSystemBeanInterface.trainingScheduleListAvailable();
        TrainingScheduleEntity schedule = scheduleList.get(0);

        String name = "Jared";

        boolean result = HiYewSystemBeanInterface.addTrainingEmployee(schedule, name);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testUAddTrainingEmployee2() {
        System.out.println("addTrainingEmployee");
        List<TrainingScheduleEntity> scheduleList = HiYewSystemBeanInterface.trainingScheduleListAvailable();
        TrainingScheduleEntity schedule = scheduleList.get(0);
        String name = "Jared";

        boolean result = HiYewSystemBeanInterface.addTrainingEmployee(schedule, name);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testUAddTrainingEmployee3() {
        System.out.println("addTrainingEmployee - schedule is null or name is null or ''");
        List<TrainingScheduleEntity> scheduleList = HiYewSystemBeanInterface.trainingScheduleListAvailable();
        TrainingScheduleEntity schedule = null;
        String name = "Justin";

        boolean result = HiYewSystemBeanInterface.addTrainingEmployee(schedule, name);
        assertFalse(result);

        schedule = scheduleList.get(0);
        name = "";

        result = HiYewSystemBeanInterface.addTrainingEmployee(schedule, name);
        assertFalse(result);

        schedule = scheduleList.get(0);
        name = null;

        result = HiYewSystemBeanInterface.addTrainingEmployee(schedule, name);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    //    /**
    //     * Test of employeeTraining method, of class HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testEmployeeTraining() {
        System.out.println("employeeTraining - right cases");
        List<TrainingScheduleEntity> list = HiYewSystemBeanInterface.trainingSchedueList();
        TrainingScheduleEntity schedule = list.get(0);
        List<EmployeeEntity> result = HiYewSystemBeanInterface.employeeTraining(schedule);
        assertEquals(result.size(), 1);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testEmployeeTraining2() {
        System.out.println("employeeTraining - null cases");
        TrainingScheduleEntity schedule = null;
        List<EmployeeEntity> result = HiYewSystemBeanInterface.employeeTraining(schedule);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }
    //    /**
    //     * Test of deleteTrainingEmployee method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //    */

    @Test // not working
    public void testDeleteTrainingEmployee() {
        System.out.println("deleteTrainingEmployee");
        List<TrainingScheduleEntity> list = HiYewSystemBeanInterface.trainingScheduleListAvailable();
        TrainingScheduleEntity training = list.get(0);
        String employee = "Jared";

        boolean result = HiYewSystemBeanInterface.deleteTrainingEmployee(training, employee);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test // not working
    public void testDeleteTrainingEmployee2() {
        System.out.println("deleteTrainingEmployee - no such employee in list");
        List<TrainingScheduleEntity> list = HiYewSystemBeanInterface.trainingScheduleListAvailable();
        TrainingScheduleEntity training = list.get(0);
        String employee = "Jared";

        boolean result = HiYewSystemBeanInterface.deleteTrainingEmployee(training, employee);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test // not working
    public void testDeleteTrainingEmployee3() {
        System.out.println("deleteTrainingEmployee - null cases");
        List<TrainingScheduleEntity> list = HiYewSystemBeanInterface.trainingScheduleListAvailable();
        TrainingScheduleEntity training = null;
        String employee = "Jared";

        boolean result = HiYewSystemBeanInterface.deleteTrainingEmployee(training, employee);
        assertFalse(result);

        list = HiYewSystemBeanInterface.trainingScheduleListAvailable();
        training = list.get(0);
        employee = null;

        result = HiYewSystemBeanInterface.deleteTrainingEmployee(training, employee);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    //
    //    /**
    //     * Test of getPayroll method, of class HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testGetPayroll_String() {

        System.out.println("getPayroll - correct case");
        String employeeName = "Jared";
        List<PayrollEntity> result = HiYewSystemBeanInterface.getPayroll(employeeName);
        assertEquals(result.size(), 1);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testGetPayroll_String2() {

        System.out.println("getPayroll - correct case - no payroll");
        String employeeName = "Testing3";
        List<PayrollEntity> result = HiYewSystemBeanInterface.getPayroll(employeeName);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testGetPayroll_String3() {

        System.out.println("getPayroll - employee name is null or ''");
        String employeeName = "";
        List<PayrollEntity> result = HiYewSystemBeanInterface.getPayroll(employeeName);
        assertNull(result);
        // TODO review the generated test code and remove the default call to fail.
        employeeName = null;
        result = HiYewSystemBeanInterface.getPayroll(employeeName);
        assertNull(result);

    }

    //
    //    /**
    //     * Test of deleteTraining method, of class HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testDeleteTraining() {
        System.out.println("deleteTraining - correct case");
        String trainingCode = "1234";

        boolean result = HiYewSystemBeanInterface.deleteTraining(trainingCode);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testDeleteTraining2() {
        System.out.println("deleteTraining - deleting a non-existing training");
        String trainingCode = "1234";

        boolean result = HiYewSystemBeanInterface.deleteTraining(trainingCode);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testDeleteTraining3() {
        System.out.println("deleteTraining - training code is null or ''");
        String trainingCode = "";

        boolean result = HiYewSystemBeanInterface.deleteTraining(trainingCode);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
        trainingCode = null;
        result = HiYewSystemBeanInterface.deleteTraining(trainingCode);
        assertFalse(result);

    }

    //
    //    /**
    //     * Test of changePassword method, of class HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testChangePassword() {
        System.out.println("changePassword - password change correct case");
        String employeeName = "Justin";
        String oldPass = "password";
        String newPass = "password1";

        String expResult = "changed";
        String result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testChangePassword2() {
        System.out.println("changePassword - password change incorrect password");
        String employeeName = "Justin";
        String oldPass = "jitcheong";
        String newPass = "password";

        String expResult = "Old password is incorrect";
        String result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    public void testChangePassword3() {
        System.out.println("changePassword - password change old and new password are the same");
        String employeeName = "Justin";
        String oldPass = "password1";
        String newPass = "password1";

        String expResult = "Password same as old password";
        String result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    public void testChangePassword4() {
        System.out.println("changePassword - no such employee");
        String employeeName = "Justin";
        String oldPass = "password";
        String newPass = "password";

        String expResult = "Password same as old password";
        String result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    public void testChangePassword5() {
        System.out.println("changePassword - employee variable is null or ''");
        String employeeName = "";
        String oldPass = "password";
        String newPass = "password1";

        String expResult = "no such user";
        String result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);

        employeeName = null;
        oldPass = "password";
        newPass = "password1";

        expResult = "no such user";
        result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.
    }

    public void testChangePassword6() {
        System.out.println("changePassword - old pass variable is null or ''");
        String employeeName = "Justin";
        String oldPass = "";
        String newPass = "password1";

        String expResult = "no old password input";
        String result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);

        employeeName = "Justin";
        oldPass = null;
        newPass = "password1";

        expResult = "no old password input";
        result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.
    }

    public void testChangePassword7() {
        System.out.println("changePassword - new pass variable is null or ''");
        String employeeName = "Justin";
        String oldPass = "password";
        String newPass = "";

        String expResult = "no old password input";
        String result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);

        employeeName = "Justin";
        oldPass = "password";
        newPass = null;

        expResult = "no old password input";
        result = HiYewSystemBeanInterface.changePassword(employeeName, oldPass, newPass);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.
    }

    //    /**
    //     * Test of rejectLeaveID method, of class HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testRejectLeaveID() {
        System.out.println("rejectLeaveID - correct case");
        Long id = Long.parseLong("23");
        String employee1 = "Jared";
        HiYewSystemBeanInterface.rejectLeaveID(id, employee1);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test(expected = EJBException.class)
    public void testRejectLeaveID2() {
        System.out.println("rejectLeaveID - no name");
        Long id = Long.parseLong("23");
        String employee1 = null;
        HiYewSystemBeanInterface.rejectLeaveID(id, employee1);
        // TODO review the generated test code and remove the default call to fail.
    }

    //
    //    /**
    //     * Test of employeeTrainingName method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
        @Test
        public void testEmployeeTrainingName() {
            System.out.println("employeeTrainingName - have entry");
            List<TrainingScheduleEntity> list = HiYewSystemBeanInterface.trainingSchedueList();
            TrainingScheduleEntity schedule = list.get(0);
          
           
            List<String> result = HiYewSystemBeanInterface.employeeTrainingName(schedule);
            assertEquals(result.size(), 1);
            // TODO review the generated test code and remove the default call to fail.
           
        }
    //
    //    /**
    //     * Test of updateTraining method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testUpdateTraining() {
    //        System.out.println("updateTraining");
    //        TrainingScheduleEntity training = null;
    //        Date start = null;
    //        Date end = null;
    //        int size = 0;
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        boolean expResult = false;
    //        boolean result = instance.updateTraining(training, start, end, size);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of resetPassword method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testResetPassword() {
    //        System.out.println("resetPassword");
    //        String username = "";
    //        String secretQuestion = "";
    //        String secretAnswer = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        Vector expResult = null;
    //        Vector result = instance.resetPassword(username, secretQuestion, secretAnswer);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeLeaveToday method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeLeaveToday() {
    //        System.out.println("employeeLeaveToday");
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<LeaveEntity> expResult = null;
    //        List<LeaveEntity> result = instance.employeeLeaveToday();
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeLeave7days method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeLeave7days() {
    //        System.out.println("employeeLeave7days");
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<LeaveEntity> expResult = null;
    //        List<LeaveEntity> result = instance.employeeLeave7days();
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeTrainingToday method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeTrainingToday() {
    //        System.out.println("employeeTrainingToday");
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<Vector> expResult = null;
    //        List<Vector> result = instance.employeeTrainingToday();
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeTraining7Days method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeTraining7Days() {
    //        System.out.println("employeeTraining7Days");
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<Vector> expResult = null;
    //        List<Vector> result = instance.employeeTraining7Days();
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeLeaveMonth method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeLeaveMonth() {
    //        System.out.println("employeeLeaveMonth");
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<LeaveEntity> expResult = null;
    //        List<LeaveEntity> result = instance.employeeLeaveMonth();
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeTrainingMonth method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeTrainingMonth() {
    //        System.out.println("employeeTrainingMonth");
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<Vector> expResult = null;
    //        List<Vector> result = instance.employeeTrainingMonth();
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeLeaveTodayUser method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeLeaveTodayUser() {
    //        System.out.println("employeeLeaveTodayUser");
    //        String username = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<LeaveEntity> expResult = null;
    //        List<LeaveEntity> result = instance.employeeLeaveTodayUser(username);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeTrainingTodayUser method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeTrainingTodayUser() {
    //        System.out.println("employeeTrainingTodayUser");
    //        String username = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<Vector> expResult = null;
    //        List<Vector> result = instance.employeeTrainingTodayUser(username);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeLeave7daysUser method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeLeave7daysUser() {
    //        System.out.println("employeeLeave7daysUser");
    //        String username = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<LeaveEntity> expResult = null;
    //        List<LeaveEntity> result = instance.employeeLeave7daysUser(username);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeTraining7DaysUser method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeTraining7DaysUser() {
    //        System.out.println("employeeTraining7DaysUser");
    //        String username = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<Vector> expResult = null;
    //        List<Vector> result = instance.employeeTraining7DaysUser(username);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeLeaveMonthUser method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeLeaveMonthUser() {
    //        System.out.println("employeeLeaveMonthUser");
    //        String username = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<LeaveEntity> expResult = null;
    //        List<LeaveEntity> result = instance.employeeLeaveMonthUser(username);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of employeeTrainingMonthUser method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testEmployeeTrainingMonthUser() {
    //        System.out.println("employeeTrainingMonthUser");
    //        String username = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<Vector> expResult = null;
    //        List<Vector> result = instance.employeeTrainingMonthUser(username);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of addNewAdmin method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testAddNewAdmin() {
    //        System.out.println("addNewAdmin");
    //        String employee = "";
    //        String employee_passNumber = "";
    //        String employee_address = "";
    //        int number_of_leave = 0;
    //        String position = "";
    //        String username = "";
    //        Timestamp expiry = null;
    //        String contact = "";
    //        String addressPostal = "";
    //        String unit = "";
    //        String optional = "";
    //        double employeePay = 0.0;
    //        Date employedDate = null;
    //        String email = "";
    //        String password = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        instance.addNewAdmin(employee, employee_passNumber, employee_address, number_of_leave, position, username, expiry, contact, addressPostal, unit, optional, employeePay, employedDate, email, password);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of applyClaim method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testApplyClaim() {
    //        System.out.println("applyClaim");
    //        String employeeName = "";
    //        EmployeeClaimEntity claim = null;
    //        String destination = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        boolean expResult = false;
    //        boolean result = instance.applyClaim(employeeName, claim, destination);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of attachDocument method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testAttachDocument() {
    //        System.out.println("attachDocument");
    //        EmployeeClaimEntity claim = null;
    //        String destination = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        instance.attachDocument(claim, destination);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of pendingClaimRecords method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testPendingClaimRecords() {
    //        System.out.println("pendingClaimRecords");
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<EmployeeClaimEntity> expResult = null;
    //        List<EmployeeClaimEntity> result = instance.pendingClaimRecords();
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of approveClaim method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testApproveClaim() {
    //        System.out.println("approveClaim");
    //        EmployeeClaimEntity claim = null;
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        instance.approveClaim(claim);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of approvedClaimRecords method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testApprovedClaimRecords() {
    //        System.out.println("approvedClaimRecords");
    //        String employeeName = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<EmployeeClaimEntity> expResult = null;
    //        List<EmployeeClaimEntity> result = instance.approvedClaimRecords(employeeName);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of approvedClaimRecordsA method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testApprovedClaimRecordsA() {
    //        System.out.println("approvedClaimRecordsA");
    //        String employeeName = "";
    //        String months = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<EmployeeClaimEntity> expResult = null;
    //        List<EmployeeClaimEntity> result = instance.approvedClaimRecordsA(employeeName, months);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of approvedClaimRecordsM method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testApprovedClaimRecordsM() {
    //        System.out.println("approvedClaimRecordsM");
    //        String months = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<EmployeeClaimEntity> expResult = null;
    //        List<EmployeeClaimEntity> result = instance.approvedClaimRecordsM(months);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of rejectClaim method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testRejectClaim() {
    //        System.out.println("rejectClaim");
    //        EmployeeClaimEntity c = null;
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        instance.rejectClaim(c);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of getEmployeeEs method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testGetEmployeeEs() {
    //        System.out.println("getEmployeeEs");
    //        String username = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        String expResult = "";
    //        String result = instance.getEmployeeEs(username);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of trainingScheduleListAvailable method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testTrainingScheduleListAvailable() {
    //        System.out.println("trainingScheduleListAvailable");
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<TrainingScheduleEntity> expResult = null;
    //        List<TrainingScheduleEntity> result = instance.trainingScheduleListAvailable();
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of pastEmployeeTraining method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testPastEmployeeTraining() {
    //        System.out.println("pastEmployeeTraining");
    //        EmployeeEntity employee = null;
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        List<TrainingScheduleEntity> expResult = null;
    //        List<TrainingScheduleEntity> result = instance.pastEmployeeTraining(employee);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of removeClaim method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testRemoveClaim() {
    //        System.out.println("removeClaim");
    //        EmployeeClaimEntity claim = null;
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        instance.removeClaim(claim);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of changePasswordF method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testChangePasswordF() {
    //        System.out.println("changePasswordF");
    //        String employeeName = "";
    //        String oldPass = "";
    //        String newPass = "";
    //        String secretQuestion = "";
    //        String secretAnswer = "";
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        String expResult = "";
    //        String result = instance.changePasswordF(employeeName, oldPass, newPass, secretQuestion, secretAnswer);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of updateClaim method, of class HiYewSystemBeanRemoteInterface.
    //     */
    //    @Test
    //    public void testUpdateClaim() {
    //        System.out.println("updateClaim");
    //        EmployeeClaimEntity claim = null;
    //        double amount = 0.0;
    //        Date date = null;
    //        HiYewSystemBeanRemoteInterface instance = new HiYewSystemBeanRemoteInterfaceImpl();
    //        boolean expResult = false;
    //        boolean result = instance.updateClaim(claim, amount, date);
    //        assertEquals(expResult, result);
    //        // TODO review the generated test code and remove the default call to fail.
    //        fail("The test case is a prototype.");
    //    }
    //
    //    /**
    //     * Test of sendActivationCode method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */

    @Test
    public void testSendActivationCode() {
        System.out.println("sendActivationCode");
        String email = "hurulez@gmail.com";

        String result = HiYewSystemBeanInterface.sendActivationCode(email);
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testSendActivationCode2() {
        System.out.println("sendActivationCode - email is null or ''");
        String email = "";

        String result = HiYewSystemBeanInterface.sendActivationCode(email);
        assertNull(result);
        email = null;

        result = HiYewSystemBeanInterface.sendActivationCode(email);
        assertNull(result);

// TODO review the generated test code and remove the default call to fail.
    }
    //
    //    /**
    //     * Test of checkActivationCode method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //    */

    @Test
    public void testCheckActivationCode() {
        System.out.println("checkActivationCode - correct code");
        String code = "73cshgujbq";

        boolean result = HiYewSystemBeanInterface.checkActivationCode(code);
        assertTrue(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCheckActivationCode2() {
        System.out.println("checkActivationCode - wrong code");
        String code = "irvi";

        boolean result = HiYewSystemBeanInterface.checkActivationCode(code);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCheckActivationCode3() {
        System.out.println("checkActivationCode -  code is null or ''");
        String code = "";

        boolean result = HiYewSystemBeanInterface.checkActivationCode(code);
        assertFalse(result);

        code = "";

        result = HiYewSystemBeanInterface.checkActivationCode(code);
        assertFalse(result);

// TODO review the generated test code and remove the default call to fail.
    }

    //
    //    /**
    //     * Test of deleteActivationCode method, of class
    //     * HiYewSystemBeanRemoteInterface.
    //     */
    @Test
    public void testDeleteActivationCode() {
        System.out.println("deleteActivationCode - right case");
        String code = "73cshgujbq";

        HiYewSystemBeanInterface.deleteActivationCode(code);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testDeleteActivationCode2() {
        System.out.println("deleteActivationCode - code does not exist");
        String code = "oiqc75oeil";

        HiYewSystemBeanInterface.deleteActivationCode(code);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testDeleteActivationCode3() {
        System.out.println("deleteActivationCode - code is null or ''");
        String code = "";

        HiYewSystemBeanInterface.deleteActivationCode(code);
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("deleteActivationCode - code is null or ''");
        code = null;

        HiYewSystemBeanInterface.deleteActivationCode(code);

    }

    public class HiYewSystemBeanRemoteInterfaceImpl implements HiYewSystemBeanRemoteInterface {

        public Vector addEmployee(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, Timestamp expiry, String contact, String addressPostal, String unit, String optional, double pay, Date employedDate, String employeeEmail) {
            return null;
        }

        public boolean updateEmployee(EmployeeEntity employee, String employeeA, String employeeUnit, String employeeOptional, String address_postal, String contact, Date pass, String position, double pay, int leave, String email) {
            return false;
        }

        public String applyLeave(String employee, int days, String remarks, Date start, Date end, String type) {
            return "";
        }

        public List<Vector> viewAllLeave() {
            return null;
        }

        public void approveLeaveID(Long id, String name) {
        }

        public List<EmployeeEntity> viewEmployee(String employeeName) {
            return null;
        }

        public List<LeaveEntity> viewEmployeeLeave(String employeeName) {
            return null;
        }

        public List<EmployeeEntity> viewAllEmployee() {
            return null;
        }

        public void disableEmployee(String employeeName) {
        }

        public String EmployeeStatus(String employeeName) {
            return "";
        }

        public boolean approveByEmployee(String employee) {
            return false;
        }

        public void cancelLeaveApplication(String employee, Long id) {
        }

        public List<LeaveEntity> viewEmployeeLeavePending(String employeeName) {
            return null;
        }

        public String login(String username, String password) {
            return "";
        }

        public List<LeaveEntity> viewEmployeeLeaveU(String username) {
            return null;
        }

        public EmployeeEntity viewEmployeeU(String username) {
            return null;
        }

        public List<String> getEmployee() {
            return null;
        }

        public List<String> getEmployeeE(String username) {
            return null;
        }

        public List<EmployeeEntity> expiredEmployees() {
            return null;
        }

        public List<EmployeeEntity> expiredEmployee(String username) {
            return null;
        }

        public boolean extendEmployeePass(String employeeName, Timestamp next) {
            return false;
        }

        public int getENoAlert() {
            return 0;
        }

        public List<PayrollEntity> payRecords() {
            return null;
        }

        public boolean createPayroll(String employeeName, int late, int sick, double overtime) {
            return false;
        }

        public List<EmployeeEntity> expiredEmployees(String username) {
            return null;
        }

        public int getENoAlert(String username) {
            return 0;
        }

        public List<PayrollEntity> getPayroll(String employeeName, String month) {
            return null;
        }

        public boolean existEmployeeName(String employeeName) {
            return false;
        }

        public boolean existEmployeeNumber(String employeeNumber) {
            return false;
        }

        public boolean existEmployeeUsername(String username) {
            return false;
        }

        public boolean notExistExpiredName(String name) {
            return false;
        }

        public List<PayrollEntity> getReleasingPayRecords() {
            return null;
        }

        public void releaseAllPay() {
        }

        public boolean updatePay(PayrollEntity pay, boolean bonus, double others) {
            return false;
        }

        public boolean addTrainingSchedule(String trainingName, Date trainingStart, Date trainingEnd, String trainingDescription, int size, String trainingCode) {
            return false;
        }

        public List<TrainingScheduleEntity> trainingSchedueList() {
            return null;
        }

        public boolean addTrainingEmployee(TrainingScheduleEntity schedule, String name) {
            return false;
        }

        public List<EmployeeEntity> employeeTraining(TrainingScheduleEntity schedule) {
            return null;
        }

        public boolean deleteTrainingEmployee(TrainingScheduleEntity training, String employee) {
            return false;
        }

        public List<PayrollEntity> getPayroll(String employeeName) {
            return null;
        }

        public boolean deleteTraining(String trainingCode) {
            return false;
        }

        public String changePassword(String employeeName, String oldPass, String newPass) {
            return "";
        }

        public boolean updateEmployee(EmployeeEntity employee, String employeeA, String employeeUnit, String employeeOptional, String address_postal, String contact, String email) {
            return false;
        }

        public void reenableEmployee(String employeeName) {
        }

        public void rejectLeaveID(Long id, String employee1) {
        }

        public List<String> employeeTrainingName(TrainingScheduleEntity schedule) {
            return null;
        }

        public boolean updateTraining(TrainingScheduleEntity training, Date start, Date end, int size) {
            return false;
        }

        public Vector resetPassword(String username, String secretQuestion, String secretAnswer) {
            return null;
        }

        public List<LeaveEntity> employeeLeaveToday() {
            return null;
        }

        public List<LeaveEntity> employeeLeave7days() {
            return null;
        }

        public List<Vector> employeeTrainingToday() {
            return null;
        }

        public List<Vector> employeeTraining7Days() {
            return null;
        }

        public List<LeaveEntity> employeeLeaveMonth() {
            return null;
        }

        public List<Vector> employeeTrainingMonth() {
            return null;
        }

        public List<LeaveEntity> employeeLeaveTodayUser(String username) {
            return null;
        }

        public List<Vector> employeeTrainingTodayUser(String username) {
            return null;
        }

        public List<LeaveEntity> employeeLeave7daysUser(String username) {
            return null;
        }

        public List<Vector> employeeTraining7DaysUser(String username) {
            return null;
        }

        public List<LeaveEntity> employeeLeaveMonthUser(String username) {
            return null;
        }

        public List<Vector> employeeTrainingMonthUser(String username) {
            return null;
        }

        public void addNewAdmin(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, Timestamp expiry, String contact, String addressPostal, String unit, String optional, double employeePay, Date employedDate, String email, String password) {
        }

        public boolean applyClaim(String employeeName, EmployeeClaimEntity claim, String destination) {
            return false;
        }

        public void attachDocument(EmployeeClaimEntity claim, String destination) {
        }

        public List<EmployeeClaimEntity> pendingClaimRecords() {
            return null;
        }

        public void approveClaim(EmployeeClaimEntity claim) {
        }

        public List<EmployeeClaimEntity> approvedClaimRecords(String employeeName) {
            return null;
        }

        public List<EmployeeClaimEntity> approvedClaimRecordsA(String employeeName, String months) {
            return null;
        }

        public List<EmployeeClaimEntity> approvedClaimRecordsM(String months) {
            return null;
        }

        public void rejectClaim(EmployeeClaimEntity c) {
        }

        public String getEmployeeEs(String username) {
            return "";
        }

        public List<TrainingScheduleEntity> trainingScheduleListAvailable() {
            return null;
        }

        public List<TrainingScheduleEntity> pastEmployeeTraining(EmployeeEntity employee) {
            return null;
        }

        public void removeClaim(EmployeeClaimEntity claim) {
        }

        public String changePasswordF(String employeeName, String oldPass, String newPass, String secretQuestion, String secretAnswer) {
            return "";
        }

        public boolean updateClaim(EmployeeClaimEntity claim, double amount, Date date) {
            return false;
        }

        public String sendActivationCode(String email) {
            return "";
        }

        public boolean checkActivationCode(String code) {
            return false;
        }

        public void deleteActivationCode(String code) {
        }
    }

    private HiYewSystemBeanRemoteInterface lookupSystemUserSessionRemote() {
        try {
            Context c = new InitialContext();
            return (HiYewSystemBeanRemoteInterface) c.lookup("java:global/HiYewSystem/HiYewSystem-ejb/HiYewSystemBean!session.stateless.HiYewSystemBeanRemoteInterface");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
