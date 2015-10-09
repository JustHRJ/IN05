/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author JustHRJ
 */
@Entity
public class EmployeeClaimEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileDestination;
    private Timestamp appliedDate;
    private Timestamp approvedDate;
    private double amount;
    private String type;
    private Timestamp claimDate;
    private EmployeeEntity employee = new EmployeeEntity();
    private String status;
    private double claimAmt;
    
    
    
    @ManyToOne
    public EmployeeEntity getEmployee(){
        return employee;
    }
    
    public void setEmployee(EmployeeEntity employee){
        this.employee= employee;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeClaimEntity)) {
            return false;
        }
        EmployeeClaimEntity other = (EmployeeClaimEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EmployeeClaimEntity[ id=" + id + " ]";
    }



    /**
     * @return the appliedDate
     */
    public Timestamp getAppliedDate() {
        return appliedDate;
    }

    /**
     * @param appliedDate the appliedDate to set
     */
    public void setAppliedDate(Timestamp appliedDate) {
        this.appliedDate = appliedDate;
    }

    /**
     * @return the approvedDate
     */
    public Timestamp getApprovedDate() {
        return approvedDate;
    }

    /**
     * @param approvedDate the approvedDate to set
     */
    public void setApprovedDate(Timestamp approvedDate) {
        this.approvedDate = approvedDate;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the claimDate
     */
    public Timestamp getClaimDate() {
        return claimDate;
    }

    /**
     * @param claimDate the claimDate to set
     */
    public void setClaimDate(Timestamp claimDate) {
        this.claimDate = claimDate;
    }

    /**
     * @return the fileDestination
     */
    public String getFileDestination() {
        return fileDestination;
    }

    /**
     * @param fileDestination the fileDestination to set
     */
    public void setFileDestination(String fileDestination) {
        this.fileDestination = fileDestination;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the claimAmt
     */
    public double getClaimAmt() {
        return claimAmt;
    }

    /**
     * @param claimAmt the claimAmt to set
     */
    public void setClaimAmt(double claimAmt) {
        this.claimAmt = claimAmt;
    }


    
}
