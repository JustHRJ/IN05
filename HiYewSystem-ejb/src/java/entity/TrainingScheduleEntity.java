/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author JustHRJ
 */
@Entity
public class TrainingScheduleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String trainingName;
    private Timestamp trainingStartDate;
    private Timestamp trainingEndDate;
    private int trianingSize;
    private String TrainingDescription;
    private String trainingCode;
    private Collection<EmployeeEntity> employeeRecords = new ArrayList<EmployeeEntity>();

    @ManyToMany(cascade = {CascadeType.ALL})
    public Collection<EmployeeEntity> getEmployeeRecords() {
        return employeeRecords;
    }

    public void setEmployeeRecords(Collection<EmployeeEntity> employeeRecords) {
        this.employeeRecords = employeeRecords;
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
        if (!(object instanceof TrainingScheduleEntity)) {
            return false;
        }
        TrainingScheduleEntity other = (TrainingScheduleEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TrainingScheduleEntity[ id=" + id + " ]";
    }

    /**
     * @return the trainingName
     */
    public String getTrainingName() {
        return trainingName;
    }

    /**
     * @param trainingName the trainingName to set
     */
    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    /**
     * @return the trainingStartDate
     */
    public Timestamp getTrainingStartDate() {
        return trainingStartDate;
    }

    /**
     * @param trainingStartDate the trainingStartDate to set
     */
    public void setTrainingStartDate(Timestamp trainingStartDate) {
        this.trainingStartDate = trainingStartDate;
    }

    /**
     * @return the trainingEndDate
     */
    public Timestamp getTrainingEndDate() {
        return trainingEndDate;
    }

    /**
     * @param trainingEndDate the trainingEndDate to set
     */
    public void setTrainingEndDate(Timestamp trainingEndDate) {
        this.trainingEndDate = trainingEndDate;
    }

    /**
     * @return the trianingSize
     */
    public int getTrianingSize() {
        return trianingSize;
    }

    /**
     * @param trianingSize the trianingSize to set
     */
    public void setTrianingSize(int trianingSize) {
        this.trianingSize = trianingSize;
    }

    /**
     * @return the TrainingDescription
     */
    public String getTrainingDescription() {
        return TrainingDescription;
    }

    /**
     * @param TrainingDescription the TrainingDescription to set
     */
    public void setTrainingDescription(String TrainingDescription) {
        this.TrainingDescription = TrainingDescription;
    }

    /**
     * @return the trainingCode
     */
    public String getTrainingCode() {
        return trainingCode;
    }

    /**
     * @param trainingCode the trainingCode to set
     */
    public void setTrainingCode(String trainingCode) {
        this.trainingCode = trainingCode;
    }

}
