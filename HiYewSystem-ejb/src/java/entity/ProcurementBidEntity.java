/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

/**
 *
 * @author K.guoxiang
 */
@Entity
public class ProcurementBidEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String itemCode;
    
    private String grade;
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double wireLength;
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double diameter;
    private int requiredQty;
    private String companyName;
    private Timestamp byWhen;
    private Timestamp bidEnd;
    private String status;
    
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private double quotedPrice;

    public ProcurementBidEntity() {
    }

    public ProcurementBidEntity(String itemCode, String grade, double wireLength, double diameter, int requiredQty, String companyName, Timestamp byWhen, Timestamp bidEnd, String status, double quotedPrice) {
        this.itemCode = itemCode;
        this.grade = grade;
        this.wireLength = wireLength;
        this.diameter = diameter;
        this.requiredQty = requiredQty;
        this.companyName = companyName;
        this.byWhen = byWhen;
        this.bidEnd = bidEnd;
        this.status = status;
        this.quotedPrice = quotedPrice;
    }

    
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcurementBidEntity)) {
            return false;
        }
        ProcurementBidEntity other = (ProcurementBidEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProcurementBidEntity[ id=" + id + " ]";
    }

    /**
     * @return the itemName
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
     * @return the grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(String grade) {
        this.grade = grade;
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
     * @return the requiredQty
     */
    public int getRequiredQty() {
        return requiredQty;
    }

    /**
     * @param requiredQty the requiredQty to set
     */
    public void setRequiredQty(int requiredQty) {
        this.requiredQty = requiredQty;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the byWhen
     */
    public Timestamp getByWhen() {
        return byWhen;
    }

    /**
     * @param byWhen the byWhen to set
     */
    public void setByWhen(Timestamp byWhen) {
        this.byWhen = byWhen;
    }

    /**
     * @return the bidEnd
     */
    public Timestamp getBidEnd() {
        return bidEnd;
    }

    /**
     * @param bidEnd the bidEnd to set
     */
    public void setBidEnd(Timestamp bidEnd) {
        this.bidEnd = bidEnd;
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
     * @return the quotedPrice
     */
    public double getQuotedPrice() {
        return quotedPrice;
    }

    /**
     * @param quotedPrice the quotedPrice to set
     */
    public void setQuotedPrice(double quotedPrice) {
        this.quotedPrice = quotedPrice;
    }

}
