package entity;

import java.io.Serializable;

public class CompositeQuotationDescKey implements Serializable {
    private static final long serialVersionUID = 1L;
   
    private String quotationNo;
    private Integer quotationDescNo;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (quotationNo != null ? quotationNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompositeQuotationDescKey)) {
            return false;
        }
        CompositeQuotationDescKey other = (CompositeQuotationDescKey) object;
        if ((this.quotationNo == null && other.quotationNo != null) || (this.quotationNo != null && !this.quotationNo.equals(other.quotationNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CompositeRFQDescKey[ id=" + quotationNo + " ]";
    }
    
}
