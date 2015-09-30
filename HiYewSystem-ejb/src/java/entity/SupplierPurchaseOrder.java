/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.ManyToOne;

/**
 *
 * @author Randy Ng
 */
@Entity
public class SupplierPurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private String supPONo;
    private Timestamp date;
    private String termsOfPayment = "30"; //30, 60, 90 days
    private String description;
    private String supCompanyName;
    private String supPoStatus = "Pending";
    private int quantity;
    
    //@ManyToOne
    //private SupplierEntity supplier = new SupplierEntity();
    
    public SupplierPurchaseOrder() {
        
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getSupPONo() != null ? getSupPONo().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quotation)) {
            return false;
        }
        SupplierPurchaseOrder other = (SupplierPurchaseOrder) object;
        if ((this.getSupPONo() == null && other.getSupPONo() != null) || (this.getSupPONo() != null && !this.supPONo.equals(other.supPONo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SupplierPurchaseOrder[ id=" + getSupPONo() + " ]";
    }
    
    /**
     * @return the supPONo
     */
    public String getSupPONo() {
        return supPONo;
    }

    /**
     * @param supPONo the supPONo to set
     */
    public void setSupPONo(String supPONo) {
        this.supPONo = supPONo;
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
    
    /**
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Timestamp date) {
        this.date = date;
    }

    /**
     * @return the termsOfPayment
     */
    public String getTermsOfPayment() {
        return termsOfPayment;
    }

    /**
     * @param termsOfPayment the termsOfPayment to set
     */
    public void setTermsOfPayment(String termsOfPayment) {
        this.termsOfPayment = termsOfPayment;
    }

    /**
     * @return the supCompanyName
     */
    public String getSupCompanyName() {
        return supCompanyName;
    }

    /**
     * @param supCompanyName the supCompanyName to set
     */
    public void setSupCompanyName(String supCompanyName) {
        this.supCompanyName = supCompanyName;
    }

    /**
     * @return the poStatus
     */
    public String getSupPoStatus() {
        return supPoStatus;
    }

    /**
     * @param poStatus the poStatus to set
     */
    public void setSupPoStatus(String supPoStatus) {
        this.supPoStatus = supPoStatus;
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
     * @return the supplier
     */
    //public SupplierEntity getSupplier() {
    //    return supplier;
    //}

    /**
     * @param supplier the supplier to set
     */
    //public void setSupplier(SupplierEntity supplier) {
    //    this.supplier = supplier;
    //}
    
}
