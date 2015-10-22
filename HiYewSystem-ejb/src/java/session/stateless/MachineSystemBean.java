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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JustHRJ
 */
@Stateless
public class MachineSystemBean implements MachineSystemBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    public boolean addMachine(String machineName, String machineIdentity, Timestamp machineExpiry, String description, int extension) {
        MachineEntity machine = new MachineEntity();
        try {
            Query q = em.createQuery("Select machine from MachineEntity machine where machine.machine_number = :id");
            q.setParameter("id", machineIdentity);
            machine = (MachineEntity) q.getSingleResult();
            return false;
        } catch (Exception ex) {
            machine.setMachine_name(machineName);
            machine.setMachine_number(machineIdentity);
            machine.setMachine_expiry(machineExpiry);
            machine.setStatus("Available");
            machine.setDescription(description);
            machine.setExtension(extension);
            Collection<MachineMaintainenceEntity> machineMaint = new ArrayList<MachineMaintainenceEntity>();
            machine.setMachineMaintainence(machineMaint);
            Collection<MachineRepairEntity> repairs = new ArrayList<MachineRepairEntity>();
            machine.setMachineRepair(repairs);
            em.persist(machine);
            return true;
        }
    }

    public boolean existMachineName(String name) {
        MachineEntity m = new MachineEntity();
        try {
            Query q = em.createQuery("select m from MachineEntity m where m.machine_name = :id");
            q.setParameter("id", name);
            m = (MachineEntity) q.getSingleResult();
            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    public boolean deleteMachineMaintainence(String id) {
        try {
            MachineMaintainenceEntity mm = em.find(MachineMaintainenceEntity.class, Long.parseLong(id));
            if (mm == null) {
                return false;
            } else {
                MachineEntity m = mm.getMachine();
                m.getMachineMaintainence().remove(mm);
                em.remove(mm);
                em.merge(m);
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean updateMachineSchedule(MachineMaintainenceEntity mSchedule, Date scheduleDate, String mScheduleHour, String mServiceProvider, String mServiceContact) {
        if (!(scheduleDate == null)) {
            Timestamp time = new Timestamp(scheduleDate.getTime());
            mSchedule.setScheduleDate(time);
        }
        if (!mScheduleHour.isEmpty()) {
            mSchedule.setScheduleTime(mScheduleHour);
        }
        if (!mServiceProvider.isEmpty()) {
            mSchedule.setServiceProvider(mServiceProvider);
        }
        if (!mServiceContact.isEmpty()) {
            mSchedule.setServiceContact(mServiceContact);
        }

        if (!mServiceContact.isEmpty() || !mServiceProvider.isEmpty() || !mScheduleHour.isEmpty() || !(scheduleDate == null)) {
            em.merge(mSchedule);
            return true;
        }
        return false;
    }

    public List<MachineMaintainenceEntity> machineMaintainenceListWeek() {
        List<MachineMaintainenceEntity> result = new ArrayList<MachineMaintainenceEntity>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 6);
        Calendar d = Calendar.getInstance();
        Timestamp time = new Timestamp(c.getTime().getTime());
        Timestamp time1 = new Timestamp(d.getTime().getTime());
        Query q = em.createQuery("select c from MachineMaintainenceEntity c");

        for (Object o : q.getResultList()) {
            MachineMaintainenceEntity m = (MachineMaintainenceEntity) o;
            if (m.getStatus().equals("incomplete")) {
                if (time.after(m.getScheduleDate())) {
                    if (!time1.after(m.getScheduleDate())) {
                        result.add(m);
                    } else if (time1.after(m.getScheduleDate())) {
                        if (time1.getDay() == m.getScheduleDate().getDay() && time1.getMonth() == m.getScheduleDate().getMonth() && time1.getYear() == m.getScheduleDate().getYear()) {
                            result.add(m);
                        }
                    }
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public List<String> machineMaintainenceNames() {
        List<String> result = new ArrayList<String>();
        List<MachineMaintainenceEntity> expiredMachine = machineMaintainenceListExpired();
        List<MachineMaintainenceEntity> weekMachine = machineMaintainenceListWeek();
        List<MachineMaintainenceEntity> otherMachine = machineMaintainenceList();

        for (int i = 0; i < expiredMachine.size(); i++) {
            MachineMaintainenceEntity m = expiredMachine.get(i);
            if (!(result.contains(m.getMachine().getMachine_name()))) {
                result.add(m.getMachine().getMachine_name());
            }
        }
        for (int i = 0; i < weekMachine.size(); i++) {
            MachineMaintainenceEntity m = weekMachine.get(i);
            if (!(result.contains(m.getMachine().getMachine_name()))) {
                result.add(m.getMachine().getMachine_name());
            }
        }
        for (int i = 0; i < otherMachine.size(); i++) {
            MachineMaintainenceEntity m = otherMachine.get(i);
            if (!(result.contains(m.getMachine().getMachine_name()))) {
                result.add(m.getMachine().getMachine_name());
            }
        }

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public List<MachineMaintainenceEntity> machineMaintainenceListExpired() {
        List<MachineMaintainenceEntity> result = new ArrayList<MachineMaintainenceEntity>();
        Calendar d = Calendar.getInstance();
        d.add(Calendar.DATE, -1);
        Timestamp time1 = new Timestamp(d.getTime().getTime());
        Query q = em.createQuery("select c from MachineMaintainenceEntity c");

        for (Object o : q.getResultList()) {
            MachineMaintainenceEntity m = (MachineMaintainenceEntity) o;
            if (m.getStatus().equals("incomplete")) {
                if (time1.after(m.getScheduleDate())) {
                    result.add(m);
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public List<Long> getMachineMaintID(String machineName) {
        MachineEntity machine = new MachineEntity();
        List<Long> result = new ArrayList<Long>();
        try {
            Query q = em.createQuery("Select machine from MachineEntity machine where machine.machine_name =:id");
            q.setParameter("id", machineName);
            machine = (MachineEntity) q.getSingleResult();
            Collection<MachineMaintainenceEntity> main = machine.getMachineMaintainence();
            for (Object o : main) {
                MachineMaintainenceEntity m = (MachineMaintainenceEntity) o;
                result.add(m.getId());
            }

            if (result.isEmpty()) {
                return null;
            } else {
                return result;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public List<MachineMaintainenceEntity> machineMaintainenceList() {
        List<MachineMaintainenceEntity> result = new ArrayList<MachineMaintainenceEntity>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 6);
        Calendar d = Calendar.getInstance();
        Timestamp time = new Timestamp(c.getTime().getTime());
        Timestamp time1 = new Timestamp(d.getTime().getTime());
        Query q = em.createQuery("select c from MachineMaintainenceEntity c");

        for (Object o : q.getResultList()) {
            MachineMaintainenceEntity m = (MachineMaintainenceEntity) o;
            if (m.getStatus().equals("incomplete")) {
                if (!time1.after(m.getScheduleDate()) && !time.after(m.getScheduleDate())) {
                    result.add(m);
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    private boolean checkClashMaintainence(MachineEntity machine) {
        Collection<MachineMaintainenceEntity> maint = machine.getMachineMaintainence();
        for (Object o : maint) {
            MachineMaintainenceEntity mm = (MachineMaintainenceEntity) o;
            if (mm.getStatus().equals("incomplete")) {
                return false;
            }
        }
        return true;
    }

    public boolean addMachineMaintainence(String machineName, Date mScheduleDate, String mScheduleHour, String maintainenceComments, String mServiceProvider, String mServiceContact) {
        MachineEntity machine = new MachineEntity();
        try {
            Query q = em.createQuery("select machine from MachineEntity machine where machine.machine_name =:id");
            q.setParameter("id", machineName);
            machine = (MachineEntity) q.getSingleResult();
            // to check whether is there conflict
            // - is it available the machine during that time?
            // - is it already having an existing schedule?
            MachineMaintainenceEntity maint = new MachineMaintainenceEntity();
            boolean check = checkClashMaintainence(machine);
            if (check) {
                maint.setComments(machineName);
                maint.setScheduleTime(mScheduleHour);
                maint.setServiceProvider(mServiceProvider);
                maint.setServiceContact(mServiceContact);
                Timestamp time = new Timestamp(mScheduleDate.getTime());
                maint.setScheduleDate(time);
                maint.setComments(maintainenceComments);
                maint.setMachine(machine);
                maint.setStatus("incomplete");
                em.persist(maint);
                machine.getMachineMaintainence().add(maint);
                em.merge(machine);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public List<String> machineNames() {
        List<String> names = new ArrayList<String>();

        Query q = em.createQuery("Select c from MachineEntity c");
        for (Object o : q.getResultList()) {
            MachineEntity m = (MachineEntity) o;
            names.add(m.getMachine_name());
        }

        if (names.isEmpty()) {
            return null;
        }
        return names;
    }

    public boolean notExistMachine(String id) {
        List<MachineEntity> machines = checkMachineExpiry();
        if (machines == null) {
            return true;
        }

        for (Object o : machines) {
            MachineEntity m = (MachineEntity) o;
            if (m.getMachine_name().equals(id)) {
                return false;
            }
        }
        return true;
    }

    public List<MachineEntity> getAllMachine() {
        Query q = em.createQuery("Select c from MachineEntity c");
        List<MachineEntity> machineRecords = new ArrayList<MachineEntity>();
        for (Object o : q.getResultList()) {
            MachineEntity machine = (MachineEntity) o;
            machineRecords.add(machine);
        }
        if (machineRecords.isEmpty()) {
            return null;
        } else {
            return machineRecords;
        }
    }

    public void deleteMachine(String machineName) {
        MachineEntity machine = new MachineEntity();
        try {
            Query q = em.createQuery("Select machine from MachineEntity machine where machine.machine_name = :id");
            q.setParameter("id", machineName);
            machine = (MachineEntity) q.getSingleResult();
            machine.setStatus("disabled");
            em.merge(machine);
        } catch (Exception ex) {

        }
    }

    public boolean updateMachine(String machineName, MachineEntity machine, String status, Date machineMaint) {
        try {
            boolean check = false;
            if (!(machineName.isEmpty())) {
                machine.setMachine_name(machineName);
            }
            if (!(status.isEmpty())) {
                if (!(machine.getStatus().equals(status))) {
                    if (!(machine.getStatus().equals("Used"))) {
                        machine.setStatus(status);
                        check = true;
                    }
                }
            }
            boolean check2 = false;
            if (machineMaint != null) {
                Timestamp time = new Timestamp(machineMaint.getTime());
                if (!(time.equals(machine.getMachine_expiry()))) {
                    machine.setMachine_expiry(time);
                    check2 = true;
                }
            }

            if ((!(machineName.isEmpty())) || check || check2) {
                em.merge(machine);
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<MachineEntity> checkMachineExpiry() {

        Timestamp ts = new Timestamp(new java.util.Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.MONTH, 1);
        ts.setTime(cal.getTime().getTime());
        List<MachineEntity> results = new ArrayList<MachineEntity>();
        Query q = em.createQuery("select c from MachineEntity c");

        for (Object o : q.getResultList()) {
            MachineEntity machine = (MachineEntity) o;
            Timestamp time = machine.getMachine_expiry();
            if (ts.after(time)) {
                results.add(machine);
            }
        }
        if (results.isEmpty()) {
            return null;
        }
        return results;
    }

    public boolean extendMachineExpiry(String machineNumber) {
        MachineEntity machine = new MachineEntity();

        try {
            Query q = em.createQuery("select machine from MachineEntity machine where machine.machine_number =:id");
            q.setParameter("id", machineNumber);
            machine = (MachineEntity) q.getSingleResult();
            Collection<MachineMaintainenceEntity> machineR = machine.getMachineMaintainence();
            for (Object o : machineR) {
                MachineMaintainenceEntity mm = (MachineMaintainenceEntity) o;
                if (mm.getStatus().equals("incomplete")) {
                    mm.setStatus("complete");
                    em.merge(mm);
                }
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(machine.getMachine_expiry());
            cal.add(Calendar.MONTH, machine.getExtension());
            Timestamp ts = new Timestamp(cal.getTime().getTime());
            machine.setMachine_expiry(ts);
            machine.setStatus("available");
            em.merge(machine);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void createRepair(MachineRepairEntity machine, Date date, String machineName) {
        Timestamp time = new Timestamp(date.getTime());
        machine.setDate(time);

        MachineEntity m = new MachineEntity();
        try {
            Query q = em.createQuery("select m from MachineEntity m where m.machine_name =:id");
            q.setParameter("id", machineName);
            m = (MachineEntity) q.getSingleResult();
            machine.setMachine(m);
            em.persist(machine);

            m.getMachineRepair().add(machine);

            em.merge(m);

        } catch (Exception ex) {

        }
    }

    public List<MachineRepairEntity> repairList(MachineEntity machine) {
        Collection<MachineRepairEntity> records = machine.getMachineRepair();

        List<MachineRepairEntity> results = new ArrayList<MachineRepairEntity>();

        for (Object o : records) {
            MachineRepairEntity m = (MachineRepairEntity) o;
            results.add(m);
        }
        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

}
