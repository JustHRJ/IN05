/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

/**
 *
 * @author K.guoxiang
 */
@Entity
public class ItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String itemCode;
    
    @Size(min=3,message = "Please enter item name more than 2 characters!")
    private String itemName;
    private String itemType;
    
//     @Digits(integer=6,fraction=0, message = "Invalid input! Note: only up to 6 digits integer. Example: 1234")
    private int quantity;
    
    @Digits(integer=9,fraction=2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double cost;
    
    @Digits(integer=9,fraction=2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double sellingPrice;
    
    private int reorderPoint;
    
    @Digits(integer=9,fraction=2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double averageWeight;
    
    @OneToMany(mappedBy = "item")
    private Set<StorageInfoEntity> storageInfos = new HashSet<StorageInfoEntity>();
    

    public ItemEntity() {
    }

    public ItemEntity(String itemCode, String itemName, String itemType, int quantity, double cost, double sellingPrice, int reorderPoint, double averageWeight) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
        this.cost = cost;
        this.sellingPrice = sellingPrice;
        this.reorderPoint = reorderPoint;
        this.averageWeight = averageWeight;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getItemCode() != null ? getItemCode().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemEntity)) {
            return false;
        }
        ItemEntity other = (ItemEntity) object;
        if ((this.getItemCode() == null && other.getItemCode() != null) || (this.getItemCode() != null && !this.itemCode.equals(other.itemCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ItemEntity[ itemCode=" + getItemCode() + " ]";
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * @return the sellingPrice
     */
    public double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * @return the reorderPoint
     */
    public int getReorderPoint() {
        return reorderPoint;
    }

    /**
     * @param reorderPoint the reorderPoint to set
     */
    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    /**
     * @return the itemType
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the averageWeight
     */
    public double getAverageWeight() {
        return averageWeight;
    }

    /**
     * @param averageWeight the averageWeight to set
     */
    public void setAverageWeight(double averageWeight) {
        this.averageWeight = averageWeight;
    }

    /**
     * @return the bins
     */
//    public Set<BinEntity> getBins() {
//        return bins;
//    }
//
//    /**
//     * @param bins the bins to set
//     */
//    public void setBins(Set<BinEntity> bins) {
//        this.bins = bins;
//    }
//    
//    public void addBin(BinEntity bin){
//        this.bins.add(bin);
//    }
    
}
