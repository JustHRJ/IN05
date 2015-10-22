/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerEntity;
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

    public void createItem(FillerEntity item);

    public List<FillerEntity> getAllItems();

    public FillerEntity getExistingItem(String itemCode);

    public void updateItemDetails(FillerEntity item);

    public void stockUp(FillerEntity item, int inQty);

    public void stockDown(FillerEntity item, int outQty);

    public void deleteItem(FillerEntity item);

    public void updateCost(FillerEntity item, double newCost);

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

    public List<FillerEntity> getAllLowStockItems();

    public void addStorageInfo(FillerEntity item, ShelveEntity shelve, int storedQty);

    public Long getUnallocatedQty(FillerEntity item);

    public List<StorageInfoEntity> getAllStorageInfoOfShelve(ShelveEntity shelve);

    public List<StorageInfoEntity> getAllStorageInfoOfItem(FillerEntity item);

    public StorageInfoEntity getStorageInfo(FillerEntity item, ShelveEntity shelve);

    public void deleteStorageInfo(FillerEntity item, ShelveEntity shelve);

    public void reduceStorageQty(FillerEntity item, ShelveEntity shelve, int reduceAmt);

    public void updateRackStatus(RackEntity rack, String status);

    public FillerEntity getExistingItemByName(String itemName);

    public void updateShelveStatus(ShelveEntity shelve, String status);

    public void checkAllShelvesInRackStatus(RackEntity rack);

    public boolean checkIfGotItemsInRack(RackEntity rack);

    public void deleteRack(RackEntity rack);

    public void deleteShelve(ShelveEntity shelve);

    public double getWireVolume(FillerEntity item);

    public List<StorageInfoEntity> getStorageInfoOfRack(RackEntity rack);

    public void reduceShelveFillCapac(ShelveEntity shelve, FillerEntity item, int reduceQty);

    public void addShelveFillCapac(ShelveEntity shelve, FillerEntity item, int storedQty);

    public double getShelveFreeSpace(ShelveEntity shelve);

    public boolean checkIfItemInShelve(FillerEntity item, ShelveEntity shelve);

    public List<String> getFillerCodeAutoComplete(String input);

}
