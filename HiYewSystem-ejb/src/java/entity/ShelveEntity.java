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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author K.guoxiang
 */
@Entity
public class ShelveEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String shelveID;
    
    private double shelveLength;
    private double width;
    private double height;
    private String status;
    
    @ManyToOne
    private RackEntity rack = new RackEntity();
    
    @OneToMany(mappedBy = "shelve")
    private Set<StorageInfoEntity> storageInfos = new HashSet<StorageInfoEntity>();

    public ShelveEntity() {
    }

    public ShelveEntity(String shelveID, double shelveLength, double width, double height, String status) {
        this.shelveID = shelveID;
        this.shelveLength = shelveLength;
        this.width = width;
        this.height = height;
        this.status = status;
    }

    public ShelveEntity(String shelveID, double shelveLength, double width, double height, String status, RackEntity rack) {
        this.shelveID = shelveID;
        this.shelveLength = shelveLength;
        this.width = width;
        this.height = height;
        this.status = status;
        this.rack = rack;
    }
    
    

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getShelveID() != null ? getShelveID().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShelveEntity)) {
            return false;
        }
        ShelveEntity other = (ShelveEntity) object;
        if ((this.getShelveID() == null && other.getShelveID() != null) || (this.getShelveID() != null && !this.shelveID.equals(other.shelveID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ShelveEntity[ shelveID=" + getShelveID() + " ]";
    }

    /**
     * @return the shelveID
     */
    public String getShelveID() {
        return shelveID;
    }

    /**
     * @param shelveID the shelveID to set
     */
    public void setShelveID(String shelveID) {
        this.shelveID = shelveID;
    }

    /**
     * @return the length
     */
    public double getShelveLength() {
        return shelveLength;
    }

    /**
     * @param length the length to set
     */
    public void setShelveLength(double shelveLength) {
        this.shelveLength = shelveLength;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
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
     * @return the rack
     */
    public RackEntity getRack() {
        return rack;
    }

    /**
     * @param rack the rack to set
     */
    public void setRack(RackEntity rack) {
        this.rack = rack;
    }

    /**
     * @return the storageInfos
     */
    public Set<StorageInfoEntity> getStorageInfos() {
        return storageInfos;
    }

    /**
     * @param storageInfos the storageInfos to set
     */
    public void setStorageInfos(Set<StorageInfoEntity> storageInfos) {
        this.storageInfos = storageInfos;
    }


    
}
