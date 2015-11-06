/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerEntity;
import entity.Metal;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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

}
