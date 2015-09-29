/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ItemEntity;
import entity.RackEntity;
import entity.ShelveEntity;
import entity.StorageInfoEntity;
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
public class HiYewICSSessionBean implements HiYewICSSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createItem(ItemEntity item) {
        em.persist(item);
    }

    @Override
    public List<ItemEntity> getAllItems() {
        Query q = em.createQuery("SELECT i FROM ItemEntity i");
        return q.getResultList();
    }

    @Override
    public List<ItemEntity> getAllLowStockItems() {
        Query q = em.createQuery("SELECT i FROM ItemEntity i WHERE i.quantity<=i.reorderPoint");
        return q.getResultList();
    }

    @Override
    public ItemEntity getExistingItem(String itemCode) {
        try {
            Query q = em.createQuery("Select i FROM ItemEntity i WHERE i.itemCode=:itemCode");
            q.setParameter("itemCode", itemCode);
            return (ItemEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ItemEntity getExistingItemByName(String itemName) {
        try {
            System.out.println("getExistingItemByName1 name =" + itemName);
            Query q = em.createQuery("Select i FROM ItemEntity i WHERE i.itemName=:itemName");
            q.setParameter("itemName", itemName);
            return (ItemEntity) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("getExistingItemByName2");
            return null;
        }
    }

    @Override
    public void updateItemDetails(ItemEntity item) {
        System.out.println("Update item: " + item.getItemCode());
        ItemEntity i = em.find(ItemEntity.class, item.getItemCode());
        i.setItemName(item.getItemName());
        i.setItemType(item.getItemType());
        i.setQuantity(item.getQuantity());
        i.setCost(item.getCost());
        i.setSellingPrice(item.getSellingPrice());
        i.setReorderPoint(item.getReorderPoint());
        i.setAverageWeight(item.getAverageWeight());
    }

    @Override
    public void stockUp(ItemEntity item, int inQty) {
        ItemEntity i = em.find(ItemEntity.class, item.getItemCode());
        i.setQuantity(i.getQuantity() + inQty);
        System.out.println("StatelessBean: stockUpOk");
    }

    @Override
    public void stockDown(ItemEntity item, int outQty) {
        ItemEntity i = em.find(ItemEntity.class, item.getItemCode());
        i.setQuantity(i.getQuantity() - outQty);
        System.out.println("StatelessBean: stockDownOk");
    }

    @Override
    public void deleteItem(ItemEntity item) {
        System.out.println("Deleting item: " + item.getItemCode());
        Query query = em.createQuery("DELETE FROM ItemEntity i WHERE i.itemCode = :itemCode");
        query.setParameter("itemCode", item.getItemCode()).executeUpdate();
    }

    @Override
    public void updateCost(ItemEntity item, double newCost) {
        ItemEntity i = em.find(ItemEntity.class, item.getItemCode());
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
        Query q = em.createQuery("SELECT r FROM RackEntity r");
        if (q.getResultList().isEmpty()) {
            return "A";
        } else {
            System.out.println("Got Existing Racks in DB");
            Query q2 = em.createQuery("SELECT max(r.rackID) FROM RackEntity r");
            String biggestCharID = q2.getSingleResult().toString();
            char charBiggestChar = biggestCharID.charAt(0);
            System.out.println("Current biggest char = " + charBiggestChar);
            charBiggestChar++;
            System.out.println("Next bigger char = " + charBiggestChar);
            return charBiggestChar + "";
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
    public void addStorageInfo(ItemEntity item, ShelveEntity shelve, int storedQty) {
        if (checkIfItemInShelve(item, shelve)) {
            System.out.println(".................sessionBean:addStorageInfo:1");
            Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.item=:item AND si.shelve=:shelve");
            q.setParameter("item", item);
            q.setParameter("shelve", shelve);
            StorageInfoEntity storageInfo = (StorageInfoEntity) q.getSingleResult();
            storageInfo.setStoredQty(storageInfo.getStoredQty() + storedQty);
            System.out.println("StatelessBean: addStorageInfo");
        } else {
            System.out.println(".................sessionBean:addStorageInfo:2");
            StorageInfoEntity storageInfo = new StorageInfoEntity(item, shelve, storedQty);
            em.persist(storageInfo);
        }
    }

    public boolean checkIfItemInShelve(ItemEntity item, ShelveEntity shelve) {

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
    public Long getUnallocatedQty(ItemEntity item) {
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
    public List<StorageInfoEntity> getAllStorageInfoOfItem(ItemEntity item) {
        Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.item = :item");
        q.setParameter("item", item);
        return q.getResultList();
    }

    @Override
    public StorageInfoEntity getStorageInfo(ItemEntity item, ShelveEntity shelve) {
        Query q = em.createQuery("SELECT si FROM StorageInfoEntity si WHERE si.item = :item AND si.shelve = :shelve");
        q.setParameter("item", item);
        q.setParameter("shelve", shelve);
        return (StorageInfoEntity) q.getSingleResult();
    }

    @Override
    public void reduceStorageQty(ItemEntity item, ShelveEntity shelve, int reduceAmt) {

        StorageInfoEntity si = getStorageInfo(item, shelve);
        if (reduceAmt < si.getStoredQty()) {
            si.setStoredQty(si.getStoredQty() - reduceAmt);
        } else if (reduceAmt == si.getStoredQty()) {
            deleteStorageInfo(item, shelve);
        }
        System.out.println("StatelessBean: reduceStorageQty");
    }

    @Override
    public void deleteStorageInfo(ItemEntity item, ShelveEntity shelve) {
        System.out.println("Deleting StorageInfo: " + item.getItemCode() + " " + shelve.getShelveID());
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
            if (shelve.getStatus().equals("Full")) {
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

}
