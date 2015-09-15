/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.EmployeeEntity;
import entity.LeaveEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import stateful.session.HiYewSystemServerRemote;

/**
 *
 * @author JustHRJ
 */
@Named(value = "loginControlBean")
@SessionScoped
public class loginControlBean implements Serializable {

    @EJB
    private HiYewSystemServerRemote hiYewSystemBean;
    private String loginPosition = "";
    private String username;
    private String password;
    private boolean logined = false;
    private String employeeName;

    /**
     * Creates a new instance of loginControlBean
     */
    public loginControlBean() {
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

    public List<String> validateLeaveName() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.getEmployee();
        } else {
            return hiYewSystemBean.getEmployeeE(username);
        }
    }

    public List<EmployeeEntity> getEmployees() {
        if (loginPosition.equals("admin")) {
            return hiYewSystemBean.viewAllEmployee();
        } else {
            return hiYewSystemBean.viewEmployee(username);
        }
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

    public String logged() {
        if (logined) {
            return "Log Out";
        } else {
            return "Log In";
        }
    }

    public String userAction() {
        if (logined) {
            username = new String();
            password = new String();
            employeeName = new String();
            logined = false;
            loginPosition = new String();
            return "login";
        } else {
            return "login";
        }
    }

    public String checkLogin() {
        String result = hiYewSystemBean.login(username, password);
        if (result.equals("disabled") || result.equals("fail")) {
            FacesMessage msg = new FacesMessage("Failed to login", "Please check username or password");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "login";
        } else {
            loginPosition = result;
            logined = true;
            return "index";
        }
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
}
