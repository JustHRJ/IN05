/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author K.guoxiang
 */
@Entity
public class BinEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private int binID;
    
    private double binLength;
    private double binWidth;
    private double binHeight;
    private String status;
    
    @ManyToOne
    private ShelveEntity shelve = new ShelveEntity();
    
    @ManyToMany(cascade={CascadeType.PERSIST})
    @JoinTable
    private Set<ItemEntity> items = new HashSet<ItemEntity>();

    public BinEntity() {
    }

    public BinEntity(int binID, double binLength, double binWidth, double binHeight, String status) {
        this.binID = binID;
        this.binLength = binLength;
        this.binWidth = binWidth;
        this.binHeight = binHeight;
        this.status = status;
    }

    public BinEntity(int binID, double binLength, double binWidth, double binHeight, String status,ShelveEntity shelve) {
        this.binID = binID;
        this.binLength = binLength;
        this.binWidth = binWidth;
        this.binHeight = binHeight;
        this.status = status;
        this.shelve = shelve;
    }
    
    
    
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (getBinID() != null ? getBinID().hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof BinEntity)) {
//            return false;
//        }
//        BinEntity other = (BinEntity) object;
//        if ((this.getBinID() == null && other.getBinID() != null) || (this.getBinID() != null && !this.binID.equals(other.binID))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        return "entity.BinEntity[ binID=" + getBinID() + " ]";
    }

    /**
     * @return the binID
     */
    public int getBinID() {
        return binID;
    }

    /**
     * @param binID the binID to set
     */
    public void setBinID(int binID) {
        this.binID = binID;
    }

    /**
     * @return the binLength
     */
    public double getBinLength() {
        return binLength;
    }

    /**
     * @param binLength the binLength to set
     */
    public void setBinLength(double binLength) {
        this.binLength = binLength;
    }

    /**
     * @return the binWidth
     */
    public double getBinWidth() {
        return binWidth;
    }

    /**
     * @param binWidth the binWidth to set
     */
    public void setBinWidth(double binWidth) {
        this.binWidth = binWidth;
    }

    /**
     * @return the binHeight
     */
    public double getBinHeight() {
        return binHeight;
    }

    /**
     * @param binHeight the binHeight to set
     */
    public void setBinHeight(double binHeight) {
        this.binHeight = binHeight;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the shelve
     */
    public ShelveEntity getShelve() {
        return shelve;
    }

    /**
     * @param shelve the shelve to set
     */
    public void setShelve(ShelveEntity shelve) {
        this.shelve = shelve;
    }

    /**
     * @return the items
     */
    public Set<ItemEntity> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(Set<ItemEntity> items) {
        this.items = items;
    }
    
    public void addItemToBin(ItemEntity item){
        this.items.add(item);
    }
    
}
