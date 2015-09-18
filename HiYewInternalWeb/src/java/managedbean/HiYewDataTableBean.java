/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.MachineEntity;
import entity.PayrollEntity;
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
    
    public List<Vector> getLeaves(){
        return hiYewSystemBean.viewAllLeave();
    }
    
    public List<MachineEntity> getMachines(){
        return hiYewSystemBean.getAllMachine();
    }
    
    public List<MachineEntity> getExpiredMachines(){
        return hiYewSystemBean.checkMachineExpiry();
    }
    
    public List<PayrollEntity> getReleasePay(){
        return hiYewSystemBean.getReleasingPayRecords();
    }
    
    public List<Vector> getPayRecords(){
        return hiYewSystemBean.payRecords();
    }
    
    public List<String> getMachineNames(){
        return hiYewSystemBean.machineNames();
    } 
}
