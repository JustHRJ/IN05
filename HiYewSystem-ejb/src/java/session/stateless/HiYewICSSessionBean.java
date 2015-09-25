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
    public void updateCost(ItemEntity item, double newCost){
        ItemEntity i = em.find(ItemEntity.class, item.getItemCode());
        i.setCost(newCost);
        System.out.println("StatelessBean: updateCost");
    }
    
    @Override
    public void createRack(RackEntity rack){
        em.persist(rack);
    }
    
    @Override
    public void createShelve(ShelveEntity shelve){
        em.persist(shelve);
    }
    
    @Override
    public void createBin(ShelveEntity shelve){
        em.persist(shelve);
        
    }
    
    @Override
    public void addShelveToRack(RackEntity rack, ShelveEntity shelve){
        rack.addShelveToRack(shelve);
    }
    
    @Override
    public void addBinToShelve(ShelveEntity shelve, BinEntity bin){
        shelve.addBinToShelve(bin);
        
    }
    
//    @Override
//    public void addItemToBin(ItemEntity item, BinEntity bin){
//        bin.addItemToBin(item);
//    }
    
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
   public BinEntity getExistingBin(String binID) {
        try {
            Query q = em.createQuery("Select b FROM BinEntity b WHERE b.binID=:binID");
            q.setParameter("shelveID", binID);
            return (BinEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
   
    @Override
  public ArrayList<ShelveEntity> getShelvesInRack(String rackID){
      ArrayList<ShelveEntity> shelves = new ArrayList<ShelveEntity>();
      if(getExistingRack(rackID)!=null){
          RackEntity selectedRack = getExistingRack(rackID);
          shelves.addAll(selectedRack.getShelves());
          return shelves;
      }else{
          return shelves;
      }
  }
  
   @Override
  public ArrayList<BinEntity> getBinsInShelve(String shelveID){
      ArrayList<BinEntity> bins = new ArrayList<BinEntity>();
      if(getExistingShelve(shelveID)!=null){
          ShelveEntity selectedShelve = getExistingShelve(shelveID);
          bins.addAll(selectedShelve.getBins());
          return bins;
      }else{
          return bins;
      }
  }
  
//  @Override
//  public ArrayList<ItemEntity> getItemInBins(String binID){
//      ArrayList<ItemEntity> items = new ArrayList<ItemEntity>();
//      if(getExistingBin(binID)!=null){
//          BinEntity selectedBin = getExistingBin(binID);
//          items.addAll(selectedBin.getItems());
//          return items;
//      }else{
//          return items;
//      }
//  }
  
    @Override
  public String getNextIDForRack(){
       Query q = em.createQuery("SELECT r FROM RackEntity r");
       if(q.getResultList().isEmpty()){
           return "A";
       }else{
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
  public String getNextIDForShelve(String rackID){
      System.out.println("Inside ejb, getNextIDForShelve, rackID: "+ rackID);
       Query q = em.createQuery("SELECT s FROM ShelveEntity s WHERE s.shelveID LIKE :code");
       q.setParameter("code", rackID + "%");
       if(q.getResultList().isEmpty()){
           System.out.println("Inside ejb, getNextIDForShelve, No Shelve For This Rack Yet");
           return rackID+"1";
       }else{
           System.out.println("Inside ejb, getNextIDForShelve, Got Shelve For This Rack Liao");
            Query q2 = em.createQuery("SELECT s.shelveID FROM ShelveEntity s WHERE s.shelveID LIKE :code");
            q2.setParameter("code", rackID + "%");
           ArrayList<String> shelveIDList = new ArrayList<>();
           shelveIDList.addAll(q2.getResultList());
           int maxInt = 0;
           for(int i = 0;i<shelveIDList.size();i++){
               System.out.println("shelve of this rack: " + shelveIDList.get(i));
               String shelveIdInList = shelveIDList.get(i);
               int intID = Integer.parseInt(shelveIdInList.substring(1, shelveIdInList.length()));
               if (intID>maxInt){
                   maxInt = intID;
               }
           }
           
           
           maxInt++;
           int nextInt = maxInt;
           System.out.println("Next biggest int: "+nextInt);
           System.out.println("Next ID: "+rackID +""+ nextInt);
           return rackID +""+ nextInt ;
       }
  }

//  public BinEntity getBinOfItem(ItemEntity item){
//      
//  }
    
}
