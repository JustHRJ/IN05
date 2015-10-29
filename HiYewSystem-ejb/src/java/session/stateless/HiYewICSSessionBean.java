/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerComposition;
import entity.FillerEntity;
import entity.RackEntity;
import entity.ShelveEntity;
import entity.StorageInfoEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
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
public class HiYewICSSessionBean implements HiYewICSSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    final double pi = 3.142;

    @Override
    public void createItem(FillerEntity item) {
        em.persist(item);
    }

    @Override
    public List<FillerEntity> getAllItems() {
        Query q = em.createQuery("SELECT i FROM FillerEntity i");
        return q.getResultList();
    }

    @Override
    public List<FillerEntity> getAllLowStockItems() {
        Query q = em.createQuery("SELECT i FROM FillerEntity i WHERE i.quantity<=i.reorderPoint");
        return q.getResultList();
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
    public List<String> getFillerCodeAutoComplete(String input) {
        ArrayList<String> fillerCodes = new ArrayList<String>();
        Query q = em.createQuery("SELECT i.fillerCode FROM FillerEntity i WHERE i.fillerCode LIKE :input");
        q.setParameter("input", input+'%');
        fillerCodes.addAll(q.getResultList());
        return fillerCodes;
    }

    @Override
    public FillerEntity getExistingItemByName(String fillerName) {
        try {
            System.out.println("getExistingItemByName1 name =" + fillerName);
            Query q = em.createQuery("Select i FROM FillerEntity i WHERE i.fillerName=:fillerName");
            q.setParameter("fillerName", fillerName);
            return (FillerEntity) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("getExistingItemByName2");
            return null;
        }
    }

    @Override
    public void updateItemDetails(FillerEntity item) {
        System.out.println("Update item: " + item.getFillerCode());
        FillerEntity i = em.find(FillerEntity.class, item.getFillerCode());
        i.setFillerName(item.getFillerName());
        i.setWireGrade(item.getWireGrade());
        i.setQuantity(item.getQuantity());
        i.setCost(item.getCost());
        i.setSellingPrice(item.getSellingPrice());
        i.setReorderPoint(item.getReorderPoint());
        i.setAverageWeight(item.getAverageWeight());
        i.setWireLength(item.getWireLength());
        i.setDiameter(item.getDiameter());
    }

    @Override
    public void stockUp(FillerEntity item, int inQty) {
        FillerEntity i = em.find(FillerEntity.class, item.getFillerCode());
        i.setQuantity(i.getQuantity() + inQty);
        System.out.println("StatelessBean: stockUpOk");
    }

    @Override
    public void stockDown(FillerEntity item, int outQty) {
        FillerEntity i = em.find(FillerEntity.class, item.getFillerCode());
        i.setQuantity(i.getQuantity() - outQty);
        System.out.println("StatelessBean: stockDownOk");
    }

    @Override
    public void deleteItem(FillerEntity item) {
        System.out.println("Deleting item: " + item.getFillerCode());
        Query query = em.createQuery("DELETE FROM FillerEntity i WHERE i.fillerCode = :fillerCode");
        query.setParameter("fillerCode", item.getFillerCode()).executeUpdate();
    }

    @Override
    public void updateCost(FillerEntity item, double newCost) {
        FillerEntity i = em.find(FillerEntity.class, item.getFillerCode());
        i.setCost(newCost);
        System.out.println("StatelessBean: updateCost");
    }

    @Override
    public void createRack(RackEntity rack) {
        em.persist(rack);
    }

    @Override
    public void createShelve(ShelveEntity shelve) {
        em.persist(shelve);
    }

    @Override
    public void createBin(ShelveEntity shelve) {
        em.persist(shelve);

    }

    @Override
    public void addShelveToRack(RackEntity rack, ShelveEntity shelve) {
        rack.addShelveToRack(shelve);
    }

    @Override
    public RackEntity getExistingRack(String rackID) {
        try {
            Query q = em.createQuery("Select r FROM RackEntity r WHERE r.rackID=:rackID");
            q.setParameter("rackID", rackID);
            return (RackEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ShelveEntity getExistingShelve(String shelveID) {
        try {
            Query q = em.createQuery("Select s FROM ShelveEntity s WHERE s.shelveID=:shelveID");
            q.setParameter("shelveID", shelveID);
            return (ShelveEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ArrayList<ShelveEntity> getShelvesInRack(String rackID) {
        ArrayList<ShelveEntity> shelves = new ArrayList<ShelveEntity>();
        System.out.println("here at getShelvesInRack.........rack id = " + rackID);
        if (getExistingRack(rackID) != null) {
            Query q = em.createQuery("Select s FROM ShelveEntity s WHERE s.shelveID LIKE :shelveID");
            q.setParameter("shelveID", rackID + '%');
//            RackEntity selectedRack = getExistingRack(rackID);
            shelves.addAll(q.getResultList());
            System.out.println("here at getShelvesInRack.........selectedRack.getShelves() = " + shelves.size());
            return shelves;
        } else {
            return shelves;
        }
    }

    @Override
    public String getNextIDForRack() {
        Query q = em.createQuery("SELECT r.rackID FROM RackEntity r");
        ArrayList<String> alpha = new ArrayList<String>();
        alpha.addAll(q.getResultList());
        if (alpha.isEmpty()) {
            return "A";
        } else {
            System.out.println("Got Existing Racks in DB");
            Collections.sort(alpha);
            int startingIndex = 65;
            String toReturn = "";
            for (int i = 0; i < alpha.size(); i++) {
                char c = alpha.get(i).charAt(0);
                if (c != startingIndex) {
                    toReturn = (char) startingIndex + "";
                    return toReturn;
                }
                startingIndex++;
            }
            toReturn = (char) startingIndex + "";
            return toReturn;
        }
    }

    @Override
    public String getNextIDForShelve(String rackID) {
        System.out.println("Inside ejb, getNextIDForShelve, rackID: " + rackID);
        Query q = em.createQuery("SELECT s FROM ShelveEntity s WHERE s.shelveID LIKE :code");
        q.setParameter("code", rackID + "%");
        if (q.getResultList().isEmpty()) {
            System.out.println("Inside ejb, getNextIDForShelve, No Shelve For This Rack Yet");
            return rackID + "1";
        } else {
            System.out.println("Inside ejb, getNextIDForShelve, Got Shelve For This Rack Liao");
            Query q2 = em.createQuery("SELECT s.shelveID FROM ShelveEntity s WHERE s.shelveID LIKE :code");
            q2.setParameter("code", rackID + "%");
            ArrayList<String> shelveIDList = new ArrayList<>();
            shelveIDList.addAll(q2.getResultList());
            int maxInt = 0;
            for (int i = 0; i < shelveIDList.size(); i++) {
                System.out.println("shelve of this rack: " + shelveIDList.get(i));
                String shelveIdInList = shelveIDList.get(i);
                int intID = Integer.parseInt(shelveIdInList.substring(1, shelveIdInList.length()));
                if (intID > maxInt) {
                    maxInt = intID;
                }
            }

            maxInt++;
            int nextInt = maxInt;
            System.out.println("Next biggest int: " + nextInt);
            System.out.println("Next ID: " + rackID + "" + nextInt);
            return rackID + "" + nextInt;
        }
    }

    @Override
    public List<RackEntity> getAllRacks() {
        Query q = em.createQuery("SELECT r FROM RackEntity r");
        return q.getResultList();
    }
    

    @Override
    public void addStorageInfo(FillerEntity item, ShelveEntity shelve, int storedQty) {
        if (checkIfItemInShelve(item, shelve)) {
            System.out.println(".................sessionBean:addStorageInfo:1");
            Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.item=:item AND si.shelve=:shelve");
            q.setParameter("item", item);
            q.setParameter("shelve", shelve);
            StorageInfoEntity storageInfo = (StorageInfoEntity) q.getSingleResult();
            storageInfo.setStoredQty(storageInfo.getStoredQty() + storedQty);
            addShelveFillCapac(shelve, item, storedQty);
            System.out.println("StatelessBean: addStorageInfo");
        } else {
            System.out.println(".................sessionBean:addStorageInfo:2");
            StorageInfoEntity storageInfo = new StorageInfoEntity(item, shelve, storedQty);
            addShelveFillCapac(shelve, item, storedQty);
            em.persist(storageInfo);
        }
    }

    @Override
    public void addShelveFillCapac(ShelveEntity shelve, FillerEntity item, int storedQty) {
        Query q = em.createQuery("SELECT s FROM ShelveEntity s WHERE s.shelveID = :shelveID");
        q.setParameter("shelveID", shelve.getShelveID());
        ShelveEntity selectedShelve = (ShelveEntity) q.getSingleResult();
        double itemVolume = getWireVolume(item);
        System.out.println("item volume is = " + itemVolume);
        double volumeToAdd = itemVolume * storedQty;
        System.out.println("volume to add is = " + volumeToAdd);
        double existingCapac = selectedShelve.getFilledCapac();
        System.out.println("existingCapac is = " + existingCapac);
        double sum = round((volumeToAdd + existingCapac),3);
        System.out.println("sum is = " + sum);
        selectedShelve.setFilledCapac(sum);
    }
    
    @Override
    public double getShelveFreeSpace(ShelveEntity shelve){
        Query q = em.createQuery("SELECT s FROM ShelveEntity s WHERE s.shelveID = :shelveID");
        q.setParameter("shelveID", shelve.getShelveID());
        ShelveEntity selectedShelve = (ShelveEntity) q.getSingleResult();
        double shelveVolume = selectedShelve.getHeight()*selectedShelve.getWidth()*selectedShelve.getShelveLength();
        return round((shelveVolume-selectedShelve.getFilledCapac()),2);
    }

    @Override
    public boolean checkIfItemInShelve(FillerEntity item, ShelveEntity shelve) {

        try {
            System.out.println(".................sessionBean:checkIfItemInShelve:1");
            Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.item=:item AND si.shelve=:shelve");
            q.setParameter("item", item);
            q.setParameter("shelve", shelve);
            if (q.getSingleResult() != null) {
                System.out.println(".................sessionBean:checkIfItemInShelve:1.1");
                return true;
            } else {
                System.out.println(".................sessionBean:checkIfItemInShelve:1.2");
                return false;
            }

        } catch (NoResultException e) {
            System.out.println(".................sessionBean:checkIfItemInShelve:2");
            return false;
        }

    }

    @Override
    public Long getUnallocatedQty(FillerEntity item) {
        Long qty = 0L;
        Long sum = 0L;
        try {
            System.out.println(".................sessionBean:getUnallocatedQty:1");
            Query q = em.createQuery("SELECT SUM(si.storedQty) FROM StorageInfoEntity si WHERE si.item=:item");
            q.setParameter("item", item);
            if (q.getSingleResult() != null) {
                sum = (Long) q.getSingleResult();
                System.out.println(".................sessionBean:getUnallocatedQty:allocated= " + sum);
                System.out.println(".................sessionBean:getUnallocatedQty:totalQty= " + item.getQuantity());
                return item.getQuantity() - sum;
            } else {
                return new Long(item.getQuantity());
            }

        } catch (NoResultException e) {
            System.out.println(".................sessionBean:getUnallocatedQty:2 " + new Long(item.getQuantity()));
            return new Long(item.getQuantity());
        }
    }

    @Override
    public List<StorageInfoEntity> getAllStorageInfoOfShelve(ShelveEntity shelve) {
        Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.shelve = :shelve");
        q.setParameter("shelve", shelve);
        return q.getResultList();
    }

    @Override
    public List<StorageInfoEntity> getAllStorageInfoOfItem(FillerEntity item) {
        Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.item = :item");
        q.setParameter("item", item);
        return q.getResultList();
    }

    @Override
    public List<StorageInfoEntity> getStorageInfoOfRack(RackEntity rack) {
        Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.shelve.shelveID LIKE :shelveID");
        q.setParameter("shelveID", rack.getRackID() + '%');
        System.out.println("here at sessionBean getStorageInfoOfRack " + q.getResultList().size());
        return q.getResultList();
    }

    @Override
    public StorageInfoEntity getStorageInfo(FillerEntity item, ShelveEntity shelve) {
        Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.item = :item AND si.shelve = :shelve");
        q.setParameter("item", item);
        q.setParameter("shelve", shelve);
        return (StorageInfoEntity) q.getSingleResult();
    }

    @Override
    public void reduceStorageQty(FillerEntity item, ShelveEntity shelve, int reduceAmt) {
        StorageInfoEntity si = getStorageInfo(item, shelve);
        if (reduceAmt < si.getStoredQty()) {
            si.setStoredQty(si.getStoredQty() - reduceAmt);

        } else if (reduceAmt == si.getStoredQty()) {
            deleteStorageInfo(item, shelve);
        }
        reduceShelveFillCapac(shelve, item, reduceAmt);
        System.out.println("StatelessBean: reduceStorageQty");
    }

    @Override
    public void reduceShelveFillCapac(ShelveEntity shelve, FillerEntity item, int reduceQty) {
        Query q = em.createQuery("SELECT s FROM ShelveEntity s WHERE s.shelveID = :shelveID");
        q.setParameter("shelveID", shelve.getShelveID());
        ShelveEntity selectedShelve = (ShelveEntity) q.getSingleResult();
        double volumeToReduce = getWireVolume(item) * reduceQty;
        System.out.println("here at session bean reduceShelveFillCapac() wire volume = " + getWireVolume(item));
        System.out.println("here at session bean reduceShelveFillCapac() reduceQty = " + reduceQty);
        System.out.println("here at session bean reduceShelveFillCapac() = " + volumeToReduce);
        System.out.println("here at session bean reduceShelveFillCapac() selectedShelve.getFilledCapac() = " + selectedShelve.getFilledCapac());
        double existingCapac = selectedShelve.getFilledCapac();
        double newTotal = existingCapac - volumeToReduce;
        selectedShelve.setFilledCapac(round(newTotal,3));
    }

    @Override
    public void deleteStorageInfo(FillerEntity item, ShelveEntity shelve) {
        System.out.println("Deleting StorageInfo: " + item.getFillerCode() + " " + shelve.getShelveID());
        Query query = em.createQuery("DELETE FROM StorageInfoEntity si WHERE si.item = :item AND si.shelve = :shelve");
        query.setParameter("item", item);
        query.setParameter("shelve", shelve);
        query.executeUpdate();
    }

    @Override
    public void updateRackStatus(RackEntity rack, String status) {
        RackEntity r = em.find(RackEntity.class, rack.getRackID());
        r.setStatus(status);
    }

    @Override
    public void updateShelveStatus(ShelveEntity shelve, String status) {
        ShelveEntity s = em.find(ShelveEntity.class, shelve.getShelveID());
        s.setStatus(status);
    }

    @Override
    public void checkAllShelvesInRackStatus(RackEntity rack) {
        ArrayList<ShelveEntity> shelves = new ArrayList<ShelveEntity>();
        shelves.addAll(getShelvesInRack(rack.getRackID()));
        int fullCounter = 0;
        for (int i = 0; i < shelves.size(); i++) {
            ShelveEntity shelve = shelves.get(i);
            double threshold = (shelve.getHeight() * shelve.getShelveLength() * shelve.getWidth())*0.95;
            if (shelve.getFilledCapac()>=threshold) {
                fullCounter++;
            }
        }
        if (fullCounter == shelves.size()) {
            updateRackStatus(rack, "Full");
        } else {
            updateRackStatus(rack, "Not Full");
        }

    }

    @Override
    public boolean checkIfGotItemsInRack(RackEntity rack) {
        ArrayList<ShelveEntity> shelves = new ArrayList<ShelveEntity>();
        ArrayList<StorageInfoEntity> siList = new ArrayList<StorageInfoEntity>();
        shelves.addAll(getShelvesInRack(rack.getRackID()));
        boolean gotItem = false;
        for (int i = 0; i < shelves.size(); i++) {
            ShelveEntity shelve = shelves.get(i);
            siList.addAll(getAllStorageInfoOfShelve(shelve));
            if (siList.size() > 0) {
                gotItem = true;
                return gotItem;
            }
            siList.clear();
        }
        return gotItem;
    }

    @Override
    public void deleteRack(RackEntity rack) {
        System.out.println("Deleting rack: " + rack.getRackID());
        ArrayList<ShelveEntity> shelves = new ArrayList<ShelveEntity>();
        shelves.addAll(getShelvesInRack(rack.getRackID()));
        for (int i = 0; i < shelves.size(); i++) {
            ShelveEntity shelve = shelves.get(i);
            deleteShelve(shelve);
        }
        Query query = em.createQuery("DELETE FROM RackEntity r WHERE r.rackID = :rackID");
        query.setParameter("rackID", rack.getRackID()).executeUpdate();
    }

    @Override
    public void deleteShelve(ShelveEntity shelve) {
        System.out.println("Deleting shelve: " + shelve.getShelveID());
        Query query = em.createQuery("DELETE FROM ShelveEntity s WHERE s.shelveID = :shelveID");
        query.setParameter("shelveID", shelve.getShelveID()).executeUpdate();
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

    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
