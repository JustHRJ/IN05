/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.MachineEntity;
import entity.MachineMaintainenceEntity;
import entity.MachineRepairEntity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JustHRJ
 */
@Local
public interface MachineSystemBeanLocal {

    public List<MachineRepairEntity> repairList(MachineEntity machine);

    public void createRepair(MachineRepairEntity machine, Date date, String machineName);

    public boolean extendMachineExpiry(String machineNumber);

    public List<MachineEntity> checkMachineExpiry();

    public boolean updateMachine(String machineName, MachineEntity machine, String status, Date machineMaint);

    public void deleteMachine(String machineName);

    public List<MachineEntity> getAllMachine();

    public boolean notExistMachine(String id);

    public List<String> machineNames();

    public boolean addMachineMaintainence(String machineName, Date mScheduleDate, String mScheduleHour, String maintainenceComments, String mServiceProvider, String mServiceContact);

    public List<MachineMaintainenceEntity> machineMaintainenceList();

    public List<Long> getMachineMaintID(String machineName);

    public List<MachineMaintainenceEntity> machineMaintainenceListExpired();

    public List<String> machineMaintainenceNames();

    public List<MachineMaintainenceEntity> machineMaintainenceListWeek();

    public boolean updateMachineSchedule(MachineMaintainenceEntity mSchedule, Date scheduleDate, String mScheduleHour, String mServiceProvider, String mServiceContact);

    public boolean deleteMachineMaintainence(String id);

    public boolean existMachineName(String name);

    public boolean addMachine(String machineName, String machineIdentity, Timestamp machineExpiry, String description, int extension, String machineType);

    public int getNoAlert();
    
}
