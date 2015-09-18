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

/**
 *
 * @author JustHRJ
 */
@Entity
public class MachineMaintainenceEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Timestamp scheduleDate;
    private String scheduleTime;
    private String comments;
    private String ServiceContact;
    private String serviceProvider;

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
        if (!(object instanceof MachineMaintainenceEntity)) {
            return false;
        }
        MachineMaintainenceEntity other = (MachineMaintainenceEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MachineMaintainence[ id=" + id + " ]";
    }

    /**
     * @return the scheduleDate
     */
    public Timestamp getScheduleDate() {
        return scheduleDate;
    }

    /**
     * @param scheduleDate the scheduleDate to set
     */
    public void setScheduleDate(Timestamp scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    /**
     * @return the scheduleTime
     */
    public String getScheduleTime() {
        return scheduleTime;
    }

    /**
     * @param scheduleTime the scheduleTime to set
     */
    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the ServiceContact
     */
    public String getServiceContact() {
        return ServiceContact;
    }

    /**
     * @param ServiceContact the ServiceContact to set
     */
    public void setServiceContact(String ServiceContact) {
        this.ServiceContact = ServiceContact;
    }

    /**
     * @return the serviceProvider
     */
    public String getServiceProvider() {
        return serviceProvider;
    }

    /**
     * @param serviceProvider the serviceProvider to set
     */
    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
    
}
