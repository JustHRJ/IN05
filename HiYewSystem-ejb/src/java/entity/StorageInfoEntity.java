/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author K.guoxiang
 */
@Entity
public class StorageInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "itemCode")
    private FillerEntity item;

    @ManyToOne
    @JoinColumn(name = "shelveID")
    private ShelveEntity shelve;

    private int storedQty;

    public StorageInfoEntity() {
    }

    public StorageInfoEntity(FillerEntity item, ShelveEntity shelve, int storedQty) {
        this.item = item;
        this.shelve = shelve;
        this.storedQty = storedQty;
    }

    @Override
    public String toString() {
        return "entity.StorageInfoEntity[ id=" + getId() + " ]";
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the item
     */
    public FillerEntity getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(FillerEntity item) {
        this.item = item;
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
     * @return the storedQty
     */
    public int getStoredQty() {
        return storedQty;
    }

    /**
     * @param storedQty the storedQty to set
     */
    public void setStoredQty(int storedQty) {
        this.storedQty = storedQty;
    }

}
