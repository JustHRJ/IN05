/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;

/**
 *
 * @author K.guoxiang
 */
@Entity
public class RackEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String rackID;

    private String location;

    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 1 decimal places. Example: 1234.3")
    private double length;
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 1 decimal places. Example: 1234.3")
    private double width;
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 1 decimal places. Example: 1234.3")
    private double height;

    private String status;
    
    //@Digits(integer = 9, fraction = 1, message = "Invalid input! Note: only up to 9 integers and 1 decimal places. Example: 1234.3")
    private double filledCapac;

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "rack")
    private Collection<ShelveEntity> shelves = new ArrayList<ShelveEntity>();

    public RackEntity() {
    }

    public RackEntity(String rackID, String location, double length, double width, double height, String status) {
        this.rackID = rackID;
        this.location = location;
        this.length = length;
        this.width = width;
        this.height = height;
        this.status = status;
    }

    public RackEntity(String rackID, String location, double length, double width, double height, String status, double filledCapac) {
        this.rackID = rackID;
        this.location = location;
        this.length = length;
        this.width = width;
        this.height = height;
        this.status = status;
        this.filledCapac = filledCapac;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRackID() != null ? getRackID().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RackEntity)) {
            return false;
        }
        RackEntity other = (RackEntity) object;
        if ((this.getRackID() == null && other.getRackID() != null) || (this.getRackID() != null && !this.rackID.equals(other.rackID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RackEntity[ rackID=" + getRackID() + " ]";
    }

    /**
     * @return the rackID
     */
    public String getRackID() {
        return rackID;
    }

    /**
     * @param rackID the rackID to set
     */
    public void setRackID(String rackID) {
        this.rackID = rackID;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
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
     * @return the shelves
     */
    public Collection<ShelveEntity> getShelves() {
        return shelves;
    }

    /**
     * @param shelves the shelves to set
     */
    public void setShelves(Collection<ShelveEntity> shelves) {
        this.shelves = shelves;
    }

    public void addShelveToRack(ShelveEntity shelve) {
        shelves.add(shelve);
    }

    /**
     * @return the filledCapac
     */
    public double getFilledCapac() {
        return filledCapac;
    }

    /**
     * @param filledCapac the filledCapac to set
     */
    public void setFilledCapac(double filledCapac) {
        this.filledCapac = filledCapac;
    }

}
