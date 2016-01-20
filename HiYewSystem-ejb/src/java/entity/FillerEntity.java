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
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

/**
 *
 * @author K.guoxiang
 */
@Entity
public class FillerEntity implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    private String fillerCode;
    private FillerComposition filler;
    @Size(min = 3, message = "Please enter item name more than 2 characters!")
    private String fillerName;
    private String wireGrade;

//     @Digits(integer=6,fraction=0, message = "Invalid input! Note: only up to 6 digits integer. Example: 1234")
    private int quantity;
    
    private int bookedQuantity;

    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double cost;

    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double sellingPrice;

    private int reorderPoint;

    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double averageWeight;
    
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double wireLength;
    
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double diameter;
    
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double density;
    
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double mass;

    @OneToMany(mappedBy = "item")
    private Set<StorageInfoEntity> storageInfos = new HashSet<StorageInfoEntity>();

    @OneToOne
    public FillerComposition getFiller(){
        return filler;
    }
    
    public void setFiller(FillerComposition filler){
        this.filler = filler;
    }
    
    
    public FillerEntity() {
    }

    public FillerEntity(String fillerCode, String fillerName, String wireGrade, int quantity, double cost, double sellingPrice, int reorderPoint, double averageWeight) {
        this.fillerCode = fillerCode;
        this.fillerName = fillerName;
        this.wireGrade = wireGrade;
        this.quantity = quantity;
        this.cost = cost;
        this.sellingPrice = sellingPrice;
        this.reorderPoint = reorderPoint;
        this.averageWeight = averageWeight;
    }

    public FillerEntity(String fillerCode, FillerComposition filler, String fillerName, String wireGrade, int quantity, int bookedQuantity, double cost, double sellingPrice, int reorderPoint, double averageWeight, double wireLength, double diameter) {
        this.fillerCode = fillerCode;
        this.filler = filler;
        this.fillerName = fillerName;
        this.wireGrade = wireGrade;
        this.quantity = quantity;
        this.bookedQuantity = bookedQuantity;
        this.cost = cost;
        this.sellingPrice = sellingPrice;
        this.reorderPoint = reorderPoint;
        this.averageWeight = averageWeight;
        this.wireLength = wireLength;
        this.diameter = diameter;
    }

    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getFillerCode() != null ? getFillerCode().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FillerEntity)) {
            return false;
        }
        FillerEntity other = (FillerEntity) object;
        if ((this.getFillerCode() == null && other.getFillerCode() != null) || (this.getFillerCode() != null && !this.fillerCode.equals(other.fillerCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ItemEntity[ itemCode=" + getFillerCode() + " ]";
    }

    /**
     * @return the fillerCode
     */
    public String getFillerCode() {
        return fillerCode;
    }

    /**
     * @param fillerCode the fillerCode to set
     */
    public void setFillerCode(String fillerCode) {
        this.fillerCode = fillerCode;
    }

    /**
     * @return the fillerName
     */
    public String getFillerName() {
        return fillerName;
    }

    /**
     * @param fillerName the fillerName to set
     */
    public void setFillerName(String fillerName) {
        this.fillerName = fillerName;
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
    public String getWireGrade() {
        return wireGrade;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setWireGrade(String wireGrade) {
        this.wireGrade = wireGrade;
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
     * @return the wireLength
     */
    public double getWireLength() {
        return wireLength;
    }

    /**
     * @param wireLength the wireLength to set
     */
    public void setWireLength(double wireLength) {
        this.wireLength = wireLength;
    }

    /**
     * @return the diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * @param diameter the diameter to set
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * @return the bookedQuantity
     */
    public int getBookedQuantity() {
        return bookedQuantity;
    }

    /**
     * @param bookedQuantity the bookedQuantity to set
     */
    public void setBookedQuantity(int bookedQuantity) {
        this.bookedQuantity = bookedQuantity;
    }

    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * @return the density
     */
    public double getDensity() {
        return density;
    }

    /**
     * @param density the density to set
     */
    public void setDensity(double density) {
        this.density = density;
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
