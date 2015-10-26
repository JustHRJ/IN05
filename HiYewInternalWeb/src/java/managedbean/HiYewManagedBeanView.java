/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.EmployeeEntity;
import entity.TrainingScheduleEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import session.stateless.HiYewSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@ManagedBean
@ViewScoped
public class HiYewManagedBeanView implements Serializable {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;
    private String employeeName;
    private TrainingScheduleEntity selectedTraining;
    private EmployeeEntity selectedEmployee;

    /**
     * Creates a new instance of HiYewManagedBeanView
     */
    public HiYewManagedBeanView() {
    }

    public TrainingScheduleEntity getSelectedTraining() {
        return selectedTraining;
    }

    public void setSelectedTraining(TrainingScheduleEntity selectedTraining) {
        this.selectedTraining = selectedTraining;
    }

    public EmployeeEntity getSelectedEmployee() {
        return selectedEmployee;
    }

    public void processEmployeeTraining() {
        boolean check = hiYewSystemBean.addTrainingEmployee(selectedTraining, getEmployeeName());
        if (check) {
            FacesMessage msg = new FacesMessage("Employee Added", getEmployeeName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Please select a course, else it is either full or employee is in the course / clashed.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public int getRemainder(TrainingScheduleEntity training) {
        int currentSize = training.getEmployeeRecords().size();
        return training.getTrianingSize() - currentSize;
    }

    public void deleteTrainingEmployee() {
        System.out.println(selectedTraining.getTrainingName());
        System.out.println(employeeName);

// cannot get employee details
        boolean check = hiYewSystemBean.deleteTrainingEmployee(selectedTraining, employeeName);
        if (check) {
            FacesMessage msg = new FacesMessage("Deleted");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    /**
     * @param selectedEmployee the selectedEmployee to set
     */
    public void setSelectedEmployee(EmployeeEntity selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public List<EmployeeEntity> getEmployeeTraining() {
        return hiYewSystemBean.employeeTraining(selectedTraining);
    }

    public List<String> getEmployeeTrainingName() {
        return hiYewSystemBean.employeeTrainingName(selectedTraining);

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
