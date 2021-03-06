package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;

@IdClass(CompositeQuotationDescKey.class)
@Entity
public class QuotationDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer quotationDescNo;
    private String itemDesc;
    private Integer qty;
    private Double price;
    private String requestForMetalSample;
    private String weldingType;
    private String metalName;
    private String remarksToCustomer;
    
    @Digits(integer = 7, fraction = 2, message = "Invalid input! Note: only up to 7 integers and 2 decimal places. Example: 1234.32")
    private Double surfaceVol;
    

    @ManyToOne
    private Quotation quotation;
    @Id
    private String quotationNo;

    public QuotationDescription() {
        //requestForMetalSample = "No";
    }

    public QuotationDescription(Integer quotationDescNo, String jobDesc, Integer qty, Double price, Quotation quotation, String requestForMetalSample, String weldingType, String metalName, String remarks) {
        this.quotationDescNo = quotationDescNo;
        this.itemDesc = jobDesc;
        this.qty = qty;
        this.price = price;
        this.quotation = quotation;
        this.requestForMetalSample = requestForMetalSample;
        this.weldingType = weldingType;
        this.metalName = metalName;
        this.remarksToCustomer = remarks;
    }

    /**
     * @return the quotationDescNo
     */
    public Integer getQuotationDescNo() {
        return quotationDescNo;
    }

    /**
     * @param quotationDescNo the quotationDescNo to set
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
        return "entity.quotationDescription[ id=" + getQuotationDescNo() + " ]";
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
     * @return the requestForMetalSample
     */
    public String getRequestForMetalSample() {
        return requestForMetalSample;
    }

    /**
     * @param requestForMetalSample the requestForMetalSample to set
     */
    public void setRequestForMetalSample(String requestForMetalSample) {
        this.requestForMetalSample = requestForMetalSample;
    }

    /**
     * @return the weldingType
     */
    public String getWeldingType() {
        return weldingType;
    }

    /**
     * @param weldingType the weldingType to set
     */
    public void setWeldingType(String weldingType) {
        this.weldingType = weldingType;
    }

    /**
     * @return the metalName
     */
    public String getMetalName() {
        return metalName;
    }

    /**
     * @param metalName the metalName to set
     */
    public void setMetalName(String metalName) {
        this.metalName = metalName;
    }

    /**
     * @return the remarksToCustomer
     */
    public String getRemarksToCustomer() {
        return remarksToCustomer;
    }

    /**
     * @param remarksToCustomer the remarksToCustomer to set
     */
    public void setRemarksToCustomer(String remarksToCustomer) {
        this.remarksToCustomer = remarksToCustomer;
    }

    /**
     * @return the surfaceVol
     */
    public Double getSurfaceVol() {
        return surfaceVol;
    }

    /**
     * @param surfaceVol the surfaceVol to set
     */
    public void setSurfaceVol(Double surfaceVol) {
        this.surfaceVol = surfaceVol;
    }

}
