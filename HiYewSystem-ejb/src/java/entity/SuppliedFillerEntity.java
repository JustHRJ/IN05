/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;

/**
 *
 * @author K.guoxiang
 */
@Entity
public class SuppliedFillerEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String fillerCode;
    
    private String fillerName;
    private String fillerGrade;
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private int fillerLength;
    @Digits(integer = 9, fraction = 2, message = "Invalid input! Note: only up to 9 integers and 2 decimal places. Example: 1234.32")
    private int fillerDiameter;
    private String description;
   
    
    @ManyToOne
    @JoinColumn(name = "userName")
    private SupplierEntity supplier;
    


    public String getFillerCode() {
        return fillerCode;
    }

    public void setFillerCode(String fillerCode) {
        this.fillerCode = fillerCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fillerCode != null ? fillerCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fillerCode fields are not set
        if (!(object instanceof SuppliedFillerEntity)) {
            return false;
        }
        SuppliedFillerEntity other = (SuppliedFillerEntity) object;
        if ((this.fillerCode == null && other.fillerCode != null) || (this.fillerCode != null && !this.fillerCode.equals(other.fillerCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SuppliedFillerEntity[ id=" + fillerCode + " ]";
    }

    /**
     * @return the supplier
     */
    public SupplierEntity getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
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
     * @return the fillerGrade
     */
    public String getFillerGrade() {
        return fillerGrade;
    }

    /**
     * @param fillerGrade the fillerGrade to set
     */
    public void setFillerGrade(String fillerGrade) {
        this.fillerGrade = fillerGrade;
    }

    /**
     * @return the fillerLength
     */
    public int getFillerLength() {
        return fillerLength;
    }

    /**
     * @param fillerLength the fillerLength to set
     */
    public void setFillerLength(int fillerLength) {
        this.fillerLength = fillerLength;
    }

    /**
     * @return the fillerDiameter
     */
    public int getFillerDiameter() {
        return fillerDiameter;
    }

    /**
     * @param fillerDiameter the fillerDiameter to set
     */
    public void setFillerDiameter(int fillerDiameter) {
        this.fillerDiameter = fillerDiameter;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }


    
}
