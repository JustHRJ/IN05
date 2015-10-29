/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.EmployeeClaimEntity;
import entity.EmployeeEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;
import session.stateless.HiYewSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "employeeClaimBean")
@ViewScoped
public class EmployeeClaimBean implements Serializable {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;
    private EmployeeEntity employee;
    private EmployeeClaimEntity employeeClaim;
    private Date claimTime = null;
    private double amount = 0.00;
    private UploadedFile file;
    private String destination = "C:\\Users\\User\\Desktop\\SouceTreeRepo\\IN05\\HiYewInternalWeb\\web\\images\\receipts\\";
    private EmployeeClaimEntity selectedClaim;
    private String months = "";
    private String employeeName = "";
    private String filename = "";

    private List<EmployeeClaimEntity> employeeClaimEntities;

    /**
     * Creates a new instance of EmployeeClaimBean
     */
    public EmployeeClaimBean() {
        employee = new EmployeeEntity();
        employeeClaim = new EmployeeClaimEntity();
    }

    @PostConstruct
    public void init() {

    }

    /**
     * @return the employee
     */
    public EmployeeEntity getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    /**
     * @return the employeeClaim
     */
    public EmployeeClaimEntity getEmployeeClaim() {
        return employeeClaim;
    }

    /**
     * @param employeeClaim the employeeClaim to set
     */
    public void setEmployeeClaim(EmployeeClaimEntity employeeClaim) {
        this.employeeClaim = employeeClaim;
    }

    public void removeClaim() {

        if (selectedClaim == null) {
            System.out.println("hello");
        } else {
            hiYewSystemBean.removeClaim(selectedClaim);
            employeeClaimEntities.remove(selectedClaim);
        }
    }

    public void updateClaim(RowEditEvent event) {
        System.out.println("here");
        boolean check = hiYewSystemBean.updateClaim((EmployeeClaimEntity) event.getObject(), amount, claimTime);
        if (check) {

        } else {

        }
    }

    public void applyForClaim() throws IOException {
        Timestamp time = new Timestamp(claimTime.getTime());
        employeeClaim.setClaimDate(time);
        String destinations = "../image/receipts/";

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("filename") == null) {
            destination = "";
        } else {
            destination += FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("filename").toString();
        }

        boolean check = hiYewSystemBean.applyClaim(employee.getEmployee_name(), employeeClaim, destinations);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("employeeName", employee.getEmployee_name());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("employeeClaim", employeeClaim);
        if (check) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("filename");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/hrms-view-approved-claim.xhtml");
        }
    }

    public void upload(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filename", event.getFile().getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rejectClaim() {
        hiYewSystemBean.rejectClaim(selectedClaim);
    }

    public void approveClaim() {
        hiYewSystemBean.approveClaim(selectedClaim);
    }

    public void copyFile(String fileName, InputStream in) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @return the claimTime
     */
    public Date getClaimTime() {
        return claimTime;
    }

    /**
     * @param claimTime the claimTime to set
     */
    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<EmployeeClaimEntity> getPendingClaimRecords() {
        return hiYewSystemBean.pendingClaimRecords();

    }

    public List<EmployeeClaimEntity> getApprovedClaimRecords() {

        return employeeClaimEntities;
    }

    public void search(ActionEvent event) {
        if (("select").equals(months) && !("select").equals(employeeName)) {
            employeeClaimEntities = hiYewSystemBean.approvedClaimRecords(employeeName);
        } else if ("select".equals(employeeName) && !("select".equals(months))) {
            employeeClaimEntities = hiYewSystemBean.approvedClaimRecordsM(months);
        } else {
            employeeClaimEntities = hiYewSystemBean.approvedClaimRecordsA(employeeName, months);
        }
    }

    /**
     * @return the selectedClaim
     */
    public EmployeeClaimEntity getSelectedClaim() {
        return selectedClaim;
    }

    /**
     * @param selectedClaim the selectedClaim to set
     */
    public void setSelectedClaim(EmployeeClaimEntity selectedClaim) {
        this.selectedClaim = selectedClaim;
    }

    /**
     * @return the months
     */
    public String getMonths() {
        return months;
    }

    /**
     * @param months the months to set
     */
    public void setMonths(String months) {
        this.months = months;
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

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the employeeName
     */
}
