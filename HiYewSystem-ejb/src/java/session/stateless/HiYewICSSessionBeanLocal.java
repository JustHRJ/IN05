/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.BinEntity;
import entity.ItemEntity;
import entity.RackEntity;
import entity.ShelveEntity;
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

    public void addBinToShelve(ShelveEntity shelve, BinEntity bin);

    public void addItemToBin(ItemEntity item, BinEntity bin);

    public RackEntity getExistingRack(String rackID);

    public ShelveEntity getExistingShelve(String shelveID);

    public BinEntity getExistingBin(String binID);

    public ArrayList<ShelveEntity> getShelvesInRack(String rackID);

    public ArrayList<BinEntity> getBinsInShelve(String shelveID);

    public ArrayList<ItemEntity> getItemInBins(String binID);

    public String getNextIDForRack();

    public String getNextIDForShelve(String rackID);

  
    
}
