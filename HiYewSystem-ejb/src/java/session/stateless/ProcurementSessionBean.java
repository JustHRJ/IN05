/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ProcurementBidEntity;
import entity.SupplierEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    public List<ProcurementBidEntity> getBidsOverview() {
        Query q = em.createQuery("SELECT pb FROM ProcurementBidEntity pb GROUP BY pb.bidRefNum");
        return q.getResultList();
    }

    @Override
    public List<ProcurementBidEntity> getBidsOfBidRef(int batchNum) {
        try {
            Query q = em.createQuery("SELECT pb FROM ProcurementBidEntity pb WHERE pb.bidRefNum = :bidRefNum");
            q.setParameter("bidRefNum", batchNum);
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
    public void updatePBStatusByBatch(int batchRefNum, String status) {
        ArrayList<ProcurementBidEntity> pbList = new ArrayList<ProcurementBidEntity>();
        try {
            Query q = em.createQuery("SELECT pb FROM ProcurementBidEntity pb WHERE pb.bidRefNum = :bidRefNum");
            q.setParameter("bidRefNum", batchRefNum);
            pbList.addAll(q.getResultList());
            for (int i = 0; i < pbList.size(); i++) {
                ProcurementBidEntity pbInList = pbList.get(i);
                pbInList.setStatus(status);
            }

        } catch (NoResultException e) {

        }
    }

    @Override
    public void updateAllPBStatus() {
        ArrayList<ProcurementBidEntity> pbList = new ArrayList<ProcurementBidEntity>();
        try {
            Query q = em.createQuery("SELECT pb FROM ProcurementBidEntity pb");
            pbList.addAll(q.getResultList());
            for (int i = 0; i < pbList.size(); i++) {
                ProcurementBidEntity pbInList = pbList.get(i);
                if (!pbInList.getStatus().equals("Completed")) {
                    Date expiryDate = new Date(pbInList.getBidEnd().getTime());
                    if(expiryDate.before(setTimeToZero(getToday()))){
                        pbInList.setStatus("Closed");
                    }else{
                        pbInList.setStatus("Open");
                    }
                }

            }

        } catch (NoResultException e) {

        }
    }

    @Override
    public void updateAcceptStatus(ProcurementBidEntity pb, String status) {
        ProcurementBidEntity p = em.find(ProcurementBidEntity.class, pb.getId());
        p.setIfAccept(status);
        System.out.println("StatelessBean: updateAcceptStatus");
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

    @Override
    public SupplierEntity getSupplierByUsername(String username) {
        SupplierEntity c = em.find(SupplierEntity.class, username);
        return c;
    }

    @Override
    public int getNextBatchNo() {
        int nextBatchNum = 0;
        try {
            Query q = em.createQuery("SELECT MAX(pb.bidRefNum) FROM ProcurementBidEntity pb");
            nextBatchNum = (Integer) q.getSingleResult();
            nextBatchNum++;
            return nextBatchNum;
        } catch (NoResultException e) {
            nextBatchNum++;
            return nextBatchNum;
        }
    }

    private Date getToday() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    private Date getNextDay(Date dt) {
        Calendar c = Calendar.getInstance();
        Date nextDay = dt;
        c.setTime(nextDay);
        c.add(Calendar.DATE, 1);
        nextDay = c.getTime();
        return nextDay;
    }

    private Date getTomorrow() {
        Calendar c = Calendar.getInstance();
        Date tml = getToday();
        c.setTime(tml);
        c.add(Calendar.DATE, 1);
        tml = c.getTime();
        return tml;
    }

    private static Date setTimeToZero(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
