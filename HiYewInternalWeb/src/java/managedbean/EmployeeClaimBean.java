/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.EmployeeClaimEntity;
import entity.EmployeeEntity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import session.stateful.HiYewSystemBeanLocal;
import sun.misc.IOUtils;

/**
 *
 * @author JustHRJ
 */
@Named(value = "employeeClaimBean")
@RequestScoped
public class EmployeeClaimBean {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;
    private EmployeeEntity employee;
    private EmployeeClaimEntity employeeClaim;
    private Date claimTime;
    private UploadedFile file;
    private String destination = "C:\\Users\\JustHRJ\\Desktop\\IN05\\HiYewInternalWeb\\web\\image\\receipts\\";
    private EmployeeClaimEntity selectedClaim;
    private String months = "";
    private String employeeName = "";

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

    public void applyForClaim() throws IOException {
        Timestamp time = new Timestamp(claimTime.getTime());
        employeeClaim.setClaimDate(time);
        boolean check = hiYewSystemBean.applyClaim(employee.getEmployee_name(), employeeClaim);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("employeeName", employee.getEmployee_name());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("employeeClaim", employeeClaim);
        if (check) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/HRMS/uploadReceipt.xhtml");
        }
    }

    public void upload(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            hiYewSystemBean.attachDocument((EmployeeClaimEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("employeeClaim"), "../image/receipts/" + event.getFile().getFileName());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/HRMS/viewApprovedClaim.xhtml");
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
        if (("select").equals(months) && !("select").equals(employeeName)) {
            return hiYewSystemBean.approvedClaimRecords(employeeName);
        } else if ("select".equals(employeeName) && !("select".equals(months))) {
            return hiYewSystemBean.approvedClaimRecordsM(months);
        } else {
            return hiYewSystemBean.approvedClaimRecordsA(employeeName, months);
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
     * @return the employeeName
     */
}
