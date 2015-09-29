/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author JustHRJ
 */
@Entity
public class LeaveEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int number_of_leave;
    private String remarks;
    private String status;
    private Timestamp appliedTime;
    private Timestamp approvedTime;
    private Timestamp startDate;
    private Timestamp endDate;
    private EmployeeEntity employee = new EmployeeEntity();
    private String type;

    @ManyToOne
    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public LeaveEntity() {

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
        if (!(object instanceof LeaveEntity)) {
            return false;
        }
        LeaveEntity other = (LeaveEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LeaveEntity[ id=" + id + " ]";
    }

    /**
     * @return the number_of_leave
     */
    public int getNumber_of_leave() {
        return number_of_leave;
    }

    /**
     * @param number_of_leave the number_of_leave to set
     */
    public void setNumber_of_leave(int number_of_leave) {
        this.number_of_leave = number_of_leave;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * @return the appliedTime
     */
    public Timestamp getAppliedTime() {
        return appliedTime;
    }

    /**
     * @param appliedTime the appliedTime to set
     */
    public void setAppliedTime(Timestamp appliedTime) {
        this.appliedTime = appliedTime;
    }

    /**
     * @return the approvedTime
     */
    public Timestamp getApprovedTime() {
        return approvedTime;
    }

    /**
     * @param approvedTime the approvedTime to set
     */
    public void setApprovedTime(Timestamp approvedTime) {
        this.approvedTime = approvedTime;
    }

    /**
     * @return the startDate
     */
    public Timestamp getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Timestamp getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
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

}
