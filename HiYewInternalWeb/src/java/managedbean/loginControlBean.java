/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.EmployeeEntity;
import entity.LeaveEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import session.stateless.HiYewSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "loginControlBean")
@SessionScoped
public class loginControlBean implements Serializable {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;

    private String loginPosition = "";
    private String username = "";
    private String password = "";
    private boolean logined = false;
    private String employeeName = "";

    /**
     * Creates a new instance of loginControlBean
     */
    public loginControlBean() {
    }

    public void redirect() throws IOException {
        System.out.println("hello");
        if (true) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/hrms-employee-schedule.xhtml");
        }
    }

    public void checkLogoutRedirect() throws IOException {
        if (logined) {
            loginPosition = "";
            username = "";
            password = "";
            logined = false;
            employeeName = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/login.xhtml");

        } else {

        }
    }

    public String getEAlert() {
        if (loginPosition.equals("admin")) {
            int noOfAlert = hiYewSystemBean.getENoAlert();
            return "Pass Expiry Alert (" + String.valueOf(noOfAlert) + ")";
        } else {
            int noOfAlert = hiYewSystemBean.getENoAlert(username);
            return "Pass Expiry Alert (" + String.valueOf(noOfAlert) + ")";
        }
    }

    public List<EmployeeEntity> getExpiredEmployee() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.expiredEmployees();
        } else {
            return hiYewSystemBean.expiredEmployee(username);
        }
    }

    public List<Vector> getEmployeeTrainingToday() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.employeeTrainingToday();
        } else {
            return hiYewSystemBean.employeeTrainingTodayUser(username);
        }
    }

    public List<String> validateLeaveName() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.getEmployee();
        } else {
            return hiYewSystemBean.getEmployeeE(username);
        }
    }

    public List<Vector> getEmployeeTraining7Days() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.employeeTraining7Days();
        } else {
            return hiYewSystemBean.employeeTraining7DaysUser(username);
        }
    }

    public List<Vector> getEmployeeTrainingMonth() {

        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.employeeTrainingMonth();
        } else {
            return hiYewSystemBean.employeeTrainingMonthUser(username);
        }
    }

    public void returnPage() throws IOException {
        if (logined) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/index.xhtml");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/login.xhtml");
        }
    }

    public List<LeaveEntity> getEmployeeLeaveMonth() {

        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.employeeLeaveMonth();
        } else {
            return hiYewSystemBean.employeeLeaveMonthUser(username);
        }
    }

    public List<LeaveEntity> getEmployeeLeave7Days() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.employeeLeave7days();
        } else {
            return hiYewSystemBean.employeeLeave7daysUser(username);
        }
    }

    public List<LeaveEntity> getEmployeeLeaveToday() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.employeeLeaveToday();
        } else {
            return hiYewSystemBean.employeeLeaveTodayUser(username);
        }
    }

    public List<EmployeeEntity> getEmployees() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.viewAllEmployee();
        } else {
            return hiYewSystemBean.viewEmployee(username);
        }
    }

    public void checkLoginRedirect() throws IOException {
        if (logined) {

        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/login.xhtml");
        }
    }

    public void checkLoginRedirectE() throws IOException {
        if (logined) {
            if (loginPosition.equals("admin")) {

            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/index.xhtml");
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/login.xhtml");
        }
    }

    public String getEmployeeNameLog() {
        if (username == "") {
            return "";
        }

        List<String> s = hiYewSystemBean.getEmployeeE(username);
        return s.get(0);
    }

    public List<LeaveEntity> getLeave() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.viewEmployeeLeave(employeeName);
        } else {
            return hiYewSystemBean.viewEmployeeLeaveU(username);
        }
    }

    public boolean checkValid() {
        return !loginPosition.equals("admin");
    }

    public boolean checkValid1() {
        return loginPosition.equals("admin");
    }

    public String logged() {
        if (logined) {
            return "Log Out";
        } else {
            return "Log In";
        }
    }

    public void userAction() throws IOException {
        if (logined) {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            username = "";
            password = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/login.xhtml");

        } else {
        }
    }

    public void checkLogin() throws IOException {
        String result = hiYewSystemBean.login(username, password);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("loginMessage");
        if (result.equals("disabled")) {
            FacesMessage msg = new FacesMessage("Failed to login!", "Account has been locked");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (result.equals("reset")) {
            password = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/changePassword.xhtml");

        } else if (result.equals("firstTime")) {
            password = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/firstChangePassword.xhtml");

        } else if (result.equals("fail")) {
            // FacesMessage msg = new FacesMessage("Failed to login", "Wrong username or password");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to login!", "Wrong username or password"));
        } else {
            loginPosition = result;
            logined = true;
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/index.xhtml");

        }
    }

    public String getEmployeeNameP() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("employeeNameP", hiYewSystemBean.getEmployeeEs(username));
        return hiYewSystemBean.getEmployeeEs(username);
    }

    /**
     * @return the loginPosition
     */
    public String getLoginPosition() {
        return loginPosition;
    }

    /**
     * @param loginPosition the loginPosition to set
     */
    public void setLoginPosition(String loginPosition) {
        this.loginPosition = loginPosition;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the logined
     */
    public boolean isLogined() {
        return logined;
    }

    /**
     * @param logined the logined to set
     */
    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void firstLogin() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/login.xhtml");
    }
}
