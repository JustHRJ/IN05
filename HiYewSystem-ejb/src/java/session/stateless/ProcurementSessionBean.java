/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ProcurementBidEntity;
import entity.SupplierEntity;
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
public class ProcurementSessionBean implements ProcurementSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createProcurementBid(ProcurementBidEntity pb) {
        em.persist(pb);
    }

    @Override
    public List<ProcurementBidEntity> getAllBids() {
        Query q = em.createQuery("SELECT pb FROM ProcurementBidEntity pb");
        return q.getResultList();
    }

    @Override
    public List<ProcurementBidEntity> getAllOpenBids() {
        try {
            Query q = em.createQuery("SELECT pb FROM ProcurementBidEntity pb WHERE pb.status = :status");
            q.setParameter("status", "Open");
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ProcurementBidEntity> getAllClosedBids() {
        try {
            Query q = em.createQuery("SELECT pb FROM ProcurementBidEntity pb WHERE pb.status = :status");
            q.setParameter("status", "Closed");
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ProcurementBidEntity> getBidsOpenForSupplier(String companyName) {
        try {
            Query q = em.createQuery("SELECT pb FROM ProcurementBidEntity pb WHERE pb.companyName = :companyName");
            q.setParameter("companyName", companyName);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void updateQuotedPrice(ProcurementBidEntity pb, double price) {
        ProcurementBidEntity p = em.find(ProcurementBidEntity.class, pb.getId());
        p.setQuotedPrice(price);
        System.out.println("StatelessBean: updateQuotedPrice");
    }
    
    @Override
    public void updatePBStatus(ProcurementBidEntity pb, String status) {
        ProcurementBidEntity p = em.find(ProcurementBidEntity.class, pb.getId());
        p.setStatus(status);
        System.out.println("StatelessBean: updatePBStatus");
    }
    
    @Override
    public void deleteItem(ProcurementBidEntity pb) {
        System.out.println("Deleting ProcurementBidEntity: " + pb.getId());
        Query query = em.createQuery("DELETE FROM ProcurementBidEntity pb WHERE pb.id= :id");
        query.setParameter("id", pb.getId()).executeUpdate();
    }
    
   @Override
    public List<String> getAllSuppliers() {
        Query q = em.createQuery("SELECT s.companyName FROM SupplierEntity s");
        return q.getResultList();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
