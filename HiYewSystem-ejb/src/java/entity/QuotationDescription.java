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
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author: Jit Cheong
 */

@IdClass(CompositeQuotationDescKey.class)
@Entity
public class QuotationDescription implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    @Id
    private Integer quotationDescNo;
    private String itemDesc;
    private Integer qty;
    private Double price;
    
    @ManyToOne
    private Quotation quotation;
    @Id
    private String quotationNo;

    public QuotationDescription() {
        
    }

    public QuotationDescription(Integer quotationDescNo, String jobDesc, Integer qty, Double price, Quotation quotation) {
        this.quotationDescNo = quotationDescNo;
        this.itemDesc = jobDesc;
        this.qty = qty;
        this.price = price;
        this.quotation = quotation;
       
    }

    /**
     * @return the rfqDescNo
     */
    public Integer getQuotationDescNo() {
        return quotationDescNo;
    }

    /**
     * @param quotationDescNo the rfqDescNo to set
     */
    public void setQuotationDescNo(Integer quotationDescNo) {
        this.quotationDescNo = quotationDescNo;
    }

    /**
     * @return the jobDesc
     */
    public String getItemDesc() {
        return itemDesc;
    }

    /**
     * @param itemDesc the jobDesc to set
     */
    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    /**
     * @return the qty
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the quotation
     */
    public Quotation getQuotation() {
        return quotation;
    }

    /**
     * @param quotation the quotation to set
     */
    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getQuotationDescNo() != null ? getQuotationDescNo().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuotationDescription)) {
            return false;
        }
        QuotationDescription other = (QuotationDescription) object;
        if ((this.getQuotationDescNo() == null && other.getQuotationDescNo() != null) || (this.getQuotationDescNo() != null && !this.quotationDescNo.equals(other.quotationDescNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RFQDescription[ id=" + getQuotationDescNo() + " ]";
    }

    /**
     * @return the quotationNo
     */
    public String getQuotationNo() {
        return quotationNo;
    }

    /**
     * @param quotationNo the quotationNo to set
     */
    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }
    
}
