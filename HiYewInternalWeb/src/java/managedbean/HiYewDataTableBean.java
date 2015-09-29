/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.LeaveEntity;
import entity.MachineEntity;
import entity.MachineMaintainenceEntity;
import entity.PayrollEntity;
import entity.TrainingScheduleEntity;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import session.stateful.HiYewSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "hiYewDataTableBean")
@RequestScoped
public class HiYewDataTableBean {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;

    /**
     * Creates a new instance of HiYewDataTableBean
     */
    public HiYewDataTableBean() {

    }

    public List<MachineMaintainenceEntity> getMaintainenceMachineWeek() {
        return hiYewSystemBean.machineMaintainenceListWeek();
    }

    public List<MachineMaintainenceEntity> getMaintainenceMachineExpired() {
        return hiYewSystemBean.machineMaintainenceListExpired();
    }

    public List<MachineMaintainenceEntity> getMaintainenceMachine() {
        return hiYewSystemBean.machineMaintainenceList();
    }

    public List<Vector> getLeaves() {
        return hiYewSystemBean.viewAllLeave();
    }

    public List<MachineEntity> getMachines() {
        return hiYewSystemBean.getAllMachine();
    }

    public List<MachineEntity> getExpiredMachines() {
        return hiYewSystemBean.checkMachineExpiry();
    }

    public List<PayrollEntity> getReleasePay() {
        return hiYewSystemBean.getReleasingPayRecords();
    }

    public List<PayrollEntity> getPayRecords() {
        return hiYewSystemBean.payRecords();
    }

    public List<String> getMachineNames() {
        return hiYewSystemBean.machineNames();
    }

    public List<String> getMachineMaint() {
        return hiYewSystemBean.machineMaintainenceNames();
    }

    public List<String> getEmployeeNames() {
        return hiYewSystemBean.getEmployee();
    }

    public List<TrainingScheduleEntity> getTrainingSchedule() {
        return hiYewSystemBean.trainingSchedueList();
    }

}
