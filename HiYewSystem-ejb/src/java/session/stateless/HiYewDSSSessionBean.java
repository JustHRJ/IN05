/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerEntity;
import entity.Metal;
import entity.WeldJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
    public List<FillerEntity> getListOfMatchedFillers(Metal metal){
        ArrayList<FillerEntity> matchedFillers = new ArrayList<FillerEntity>();
        matchedFillers.addAll(metal.getFillers());
        return matchedFillers;
    }
    
    @Override
    public int getAvailableQty(FillerEntity filler){
        return filler.getQuantity() - filler.getBookedQuantity();
    }
    
    @Override
    public Metal getExistingMetal(String metalName) {
        try {
            Query q = em.createQuery("Select i FROM Metal i WHERE i.metalName=:metalName");
            q.setParameter("metalName", metalName);
            return (Metal) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public int quantityNeeded(FillerEntity filler, double surfaceAreaToweld,int qty){
        double fillerVolume = getWireVolume(filler);
        System.out.println("Surface Area = "+surfaceAreaToweld);
        System.out.println("Filler Volume = "+fillerVolume);
        int qtyNeeded = (int)roundUp(((surfaceAreaToweld*qty)/fillerVolume),0);
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
    
      @Override
    public List<WeldJob> getSimilarPastProjects(String metal1, String metal2, String weldingType) {
        System.out.println("getSimilarPastProjectDuration: Start");
        Integer days = -1;
        //find weldJobs with two similar metals for welding 
        Query query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND "
                + "( (w.metal1=:metal1 OR w.metal1=:metal2) AND (w.metal2=:metal1 OR w.metal2=:metal2) AND (w.weldingType = :weldingType) ) ");
        query.setParameter("metal1", metal1);
        query.setParameter("metal2", metal2);
        query.setParameter("weldingType", weldingType);

        List<WeldJob> weldJobs = query.getResultList();
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
    public Integer deriveAverageDuration(ArrayList<WeldJob> similarWeldJobs) {
      
        double totalMins = 0;
        if((similarWeldJobs==null)||(similarWeldJobs.size()<1)){
            return -1;
        }else{
            for (int i = 0; i < similarWeldJobs.size(); i++) {
                int totalQtyWelded = similarWeldJobs.get(i).getQuantityWelded();
                double surfaceArea = similarWeldJobs.get(i).getSurfaceArea();
                int daysTook = similarWeldJobs.get(i).getDuration();
                double weldingDurationPerCm3 = (totalQtyWelded*surfaceArea)/(daysTook * 60 * 60); //go by minutes
                totalMins += weldingDurationPerCm3;             
            }
            return (int)roundUp(((totalMins/similarWeldJobs.size())*60*60),0);
        }

    }
    
    @Override
    public Integer getDifferenceDays(Timestamp t1, Timestamp t2) {

        Date d1 = new Date(t1.getTime());
        Date d2 = new Date(t2.getTime());
        long diff = d2.getTime() - d1.getTime();
        return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }

}
