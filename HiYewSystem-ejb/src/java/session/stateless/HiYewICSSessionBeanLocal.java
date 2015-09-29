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
import javax.ejb.Local;

/**
 *
 * @author K.guoxiang
 */
@Local
public interface HiYewICSSessionBeanLocal {

    public void createItem(ItemEntity item);

    public List<ItemEntity> getAllItems();

    public ItemEntity getExistingItem(String itemCode);

    public void updateItemDetails(ItemEntity item);

    public void stockUp(ItemEntity item, int inQty);

    public void stockDown(ItemEntity item, int outQty);

    public void deleteItem(ItemEntity item);

    public void updateCost(ItemEntity item, double newCost);

    public void createRack(RackEntity rack);

    public void createShelve(ShelveEntity shelve);

    public void createBin(ShelveEntity shelve);

    public void addShelveToRack(RackEntity rack, ShelveEntity shelve);

    public RackEntity getExistingRack(String rackID);

    public ShelveEntity getExistingShelve(String shelveID);

    public ArrayList<ShelveEntity> getShelvesInRack(String rackID);

    public String getNextIDForRack();

    public String getNextIDForShelve(String rackID);

    public List<RackEntity> getAllRacks();

    public List<ItemEntity> getAllLowStockItems();

    public void addStorageInfo(ItemEntity item, ShelveEntity shelve, int storedQty);

    public Long getUnallocatedQty(ItemEntity item);

    public List<StorageInfoEntity> getAllStorageInfoOfShelve(ShelveEntity shelve);

    public List<StorageInfoEntity> getAllStorageInfoOfItem(ItemEntity item);

    public StorageInfoEntity getStorageInfo(ItemEntity item, ShelveEntity shelve);

    public void deleteStorageInfo(ItemEntity item, ShelveEntity shelve);

    public void reduceStorageQty(ItemEntity item, ShelveEntity shelve, int reduceAmt);

    public void updateRackStatus(RackEntity rack, String status);

}
