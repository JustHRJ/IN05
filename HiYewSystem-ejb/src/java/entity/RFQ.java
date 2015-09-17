/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author User
 */
@Entity
public class RFQ implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String quotationNo;
    private String date;
    private String termsOfPayment; //30, 60, 90 days
    
    @OneToMany(mappedBy = "rfq")
    private List<RFQDescription> rfqDescriptions = new ArrayList<>();
    @ManyToOne
    private Customer customer;

    public RFQ() {
    }

    public RFQ(String quotationNo, String date, String termsOfPayment, Customer customer) {
        this.quotationNo = quotationNo;
        this.date = date;
        this.termsOfPayment = termsOfPayment;
        this.customer = customer;
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

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
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
     * @return the rfqDescriptions
     */
    public List<RFQDescription> getRfqDescriptions() {
        return rfqDescriptions;
    }

    /**
     * @param rfqDescriptions the rfqDescriptions to set
     */
    public void setRfqDescriptions(List<RFQDescription> rfqDescriptions) {
        this.rfqDescriptions = rfqDescriptions;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getQuotationNo() != null ? getQuotationNo().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RFQ)) {
            return false;
        }
        RFQ other = (RFQ) object;
        if ((this.getQuotationNo() == null && other.getQuotationNo() != null) || (this.getQuotationNo() != null && !this.quotationNo.equals(other.quotationNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RFQ[ id=" + getQuotationNo() + " ]";
    }
    
}
