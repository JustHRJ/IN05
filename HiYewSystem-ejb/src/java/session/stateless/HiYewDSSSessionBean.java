/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.EmployeeEntity;
import entity.FillerEntity;
import entity.Metal;
import entity.ShelveEntity;
import entity.StorageInfoEntity;
import entity.WeldJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author K.guoxiang
 */
@Stateless
public class HiYewDSSSessionBean implements HiYewDSSSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;
    final double pi = 3.142;

    @Override
    public List<FillerEntity> getListOfMatchedFillers(Metal metal) {
        ArrayList<FillerEntity> matchedFillers = new ArrayList<FillerEntity>();
        matchedFillers.addAll(metal.getFillers());
        return matchedFillers;
    }

    @Override
    public int getAvailableQty(FillerEntity filler) {
        return filler.getQuantity() - filler.getBookedQuantity();
    }

    @Override
    public Metal getExistingMetal(String metalName) {
        //sadasdsa
        try {
            Query q = em.createQuery("Select i FROM Metal i WHERE i.metalName=:metalName");
            q.setParameter("metalName", metalName);
            return (Metal) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public int quantityNeeded(FillerEntity filler, double massOfFillerReq, int qty) {
     
        System.out.println("Surface Area = " + massOfFillerReq);
        System.out.println("Filler Mass = " + filler.getMass());
        int qtyNeeded = (int) roundUp(((massOfFillerReq * qty) / filler.getMass()), 0);
        return qtyNeeded;
    }

    @Override
    public FillerEntity getExistingItem(String fillerCode) {
        try {
            Query q = em.createQuery("Select i FROM FillerEntity i WHERE i.fillerCode=:fillerCode");
            q.setParameter("fillerCode", fillerCode);
            return (FillerEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public double getWireVolume(FillerEntity item) {
        double r = (item.getDiameter() / 10) / 2;
        double h = item.getWireLength() / 10;
        double volume = pi * (r * r) * h;
        System.out.println("here at session bean getWireVolume() = " + volume);
        //   return round(volume,2);
        return volume;
    }

    private double roundUp(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.CEILING);
        return bd.doubleValue();
    }

    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public List<WeldJob> getSimilarPastProjects(String metal1, String metal2, String weldingType) {
        System.out.println("getSimilarPastProjectDuration: Start");

        //find weldJobs with two similar metals for welding 
        Query query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND "
                + "( (w.metal1=:metal1 OR w.metal1=:metal2) AND (w.metal2=:metal1 OR w.metal2=:metal2) AND (w.weldingType = :weldingType) ) ");
        query.setParameter("metal1", metal1);
        query.setParameter("metal2", metal2);
        query.setParameter("weldingType", weldingType);

        List<WeldJob> weldJobs = query.getResultList();
        System.out.println("As getSimilarPastProjects Session Bean: Weld Jobs: " + weldJobs.size());
        //if not, find weldJobs with one similar metal for welding
        if (weldJobs.isEmpty()) {
            query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND (w.weldingType = :weldingType) AND "
                    + "( (w.metal1=:metal1 OR w.metal1=:metal2) OR (w.metal2=:metal1 OR w.metal2=:metal2) ) ");

            query.setParameter("metal1", metal1);
            query.setParameter("metal2", metal2);
            query.setParameter("weldingType", weldingType);

            weldJobs = query.getResultList();
        }
        return weldJobs;
    }

    @Override
    public Integer deriveAverageDaysNeededForWeldJob(ArrayList<WeldJob> similarWeldJobs, double surfaceVolToWeld, int qtyToWeld) {

        double totalMins = 0;
        double avgMinsPerCm3 = 0;

        double totalCm3ToWeld;
        if ((surfaceVolToWeld > 0) && (qtyToWeld > 0)) {
            totalCm3ToWeld = surfaceVolToWeld * qtyToWeld;
        } else {
            return -1;
        }

        if ((similarWeldJobs == null) || (similarWeldJobs.size() < 1)) {
            return -1;
        } else {
            System.out.println("List Size" + similarWeldJobs.size());
            for (int i = 0; i < similarWeldJobs.size(); i++) {
                System.out.println();
                int totalQtyWelded = similarWeldJobs.get(i).getTotalQuantity();
                double surfaceArea = similarWeldJobs.get(i).getSurfaceArea();
                int daysTook = similarWeldJobs.get(i).getDuration();
                double weldingDurationPerCm3 = (daysTook * 24 * 60) / (totalQtyWelded * surfaceArea); //go by minutes
                System.out.println("Total Mins used for 1 cm3 for WeldJob" + similarWeldJobs.get(i).getWeldJobId() + ": " + weldingDurationPerCm3);
                totalMins += weldingDurationPerCm3;
                System.out.print("Days took:" + daysTook);
                System.out.print("Qty:" + totalQtyWelded);
                System.out.print("surfaceArea:" + surfaceArea);
            }

            System.out.print("Total Mins:" + totalMins);
            avgMinsPerCm3 = totalMins / similarWeldJobs.size();
            double numberOfMinsNeeded = totalCm3ToWeld * avgMinsPerCm3;
            return (int) roundUp(((numberOfMinsNeeded / 60) / 24), 0);
        }

    }

    @Override
    public Integer getDifferenceDays(Timestamp t1, Timestamp t2) {

        Date d1 = new Date(t1.getTime());
        Date d2 = new Date(t2.getTime());
        long diff = d2.getTime() - d1.getTime();
        return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }

    @Override
    public double getAvgManpowerCostPerDay() {
        ArrayList<EmployeeEntity> allEmployees = new ArrayList<EmployeeEntity>();
        allEmployees.addAll(getAllEngineers());
        double total = 0;
        for (int i = 0; i < allEmployees.size(); i++) {
            total += allEmployees.get(i).getEmployee_basic();
        }
        if (allEmployees.size() > 0) {
            return (int) roundUp((total / allEmployees.size()) / 22, 2);
        } else {
            return -1;
        }
    }

    private List<EmployeeEntity> getAllEngineers() {
        Query q = em.createQuery("select c from EmployeeEntity c where c.employee_account_status = :role");
        q.setParameter("role", "staff");
        return q.getResultList();
    }

    @Override
    public HashMap whichShelveToTake(FillerEntity fillerToTake, int qtyToTake) {
        System.out.println("*************whichShelveToTake qtyNeeded:" + qtyToTake );
        HashMap map = new HashMap();
        ArrayList<StorageInfoEntity> itemStorages = new ArrayList<StorageInfoEntity>();
        ShelveEntity shelveToTakeFrom = new ShelveEntity();
        Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.item = :item");
        q.setParameter("item", fillerToTake);
        itemStorages.addAll(q.getResultList());
        System.out.println("*************whichShelveToTake itemStorages:" + itemStorages.size() );
        for (int i = 0; i < itemStorages.size(); i++) {
            int qtyStoredInThatShelve = itemStorages.get(i).getStoredQty();
            System.out.println("*************whichShelveToTake qtyStoredInThatShelve:" + qtyStoredInThatShelve);
            if (qtyStoredInThatShelve >= qtyToTake) {
                shelveToTakeFrom = itemStorages.get(i).getShelve();
                System.out.println("*************whichShelveToTake one shot get all:" + shelveToTakeFrom.getShelveID());
                break;
            }
        }
        //need take from multiple shelve
        if (shelveToTakeFrom.getShelveID() != null) {
            map.put(shelveToTakeFrom, qtyToTake);
             System.out.println("*************whichShelveToTake put 1 shot get all result in map");
        } else {
              System.out.println("*************whichShelveToTake cannot 1 shot get all");
            int qty = qtyToTake;
            for (int i = 0; i < itemStorages.size(); i++) {
                int qtyStoredInThatShelve = itemStorages.get(i).getStoredQty();
                if (qtyStoredInThatShelve < qty) {
                    System.out.println("*************whichShelveToTake: not enuff in: "+ itemStorages.get(i).getShelve() + "it has: " + qtyStoredInThatShelve);
                    map.put(itemStorages.get(i).getShelve(), qtyStoredInThatShelve);
                    qty = qty - qtyStoredInThatShelve;
                } else {
                    System.out.println("*************whichShelveToTake: not enuff in: last draw"+ itemStorages.get(i).getShelve() + "it has: " + qty);
                    map.put(itemStorages.get(i).getShelve(), qty);
                    break;
                }
            }
        }
        return map;
    }
}
