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
import javax.persistence.ManyToOne;

/**
 *
 * @author User
 */
@Entity
public class RFQDescription implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rfqDescNo;
    private String jobDesc;
    private String qty;
    private String price;
    
    @ManyToOne
    private RFQ rfq;
    @Id
    private String quotationNo;

    public RFQDescription() {
    }

    public RFQDescription(Integer rfqDescNo, String jobDesc, String qty, String price, RFQ rfq) {
        this.rfqDescNo = rfqDescNo;
        this.jobDesc = jobDesc;
        this.qty = qty;
        this.price = price;
        this.rfq = rfq;
    }

    /**
     * @return the rfqDescNo
     */
    public Integer getRfqDescNo() {
        return rfqDescNo;
    }

    /**
     * @param rfqDescNo the rfqDescNo to set
     */
    public void setRfqDescNo(Integer rfqDescNo) {
        this.rfqDescNo = rfqDescNo;
    }

    /**
     * @return the jobDesc
     */
    public String getJobDesc() {
        return jobDesc;
    }

    /**
     * @param jobDesc the jobDesc to set
     */
    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return the rfq
     */
    public RFQ getRfq() {
        return rfq;
    }

    /**
     * @param rfq the rfq to set
     */
    public void setRfq(RFQ rfq) {
        this.rfq = rfq;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRfqDescNo() != null ? getRfqDescNo().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RFQDescription)) {
            return false;
        }
        RFQDescription other = (RFQDescription) object;
        if ((this.getRfqDescNo() == null && other.getRfqDescNo() != null) || (this.getRfqDescNo() != null && !this.rfqDescNo.equals(other.rfqDescNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RFQDescription[ id=" + getRfqDescNo() + " ]";
    }
    
}
