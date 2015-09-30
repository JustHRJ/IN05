package entity;

import java.io.Serializable;

public class CompositeProductQuotationDescriptionKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productQuotationNo;
    private Integer productQuotationDescNo;

    /**
     * @return the productQuotationNo
     */
    public String getProductQuotationNo() {
        return productQuotationNo;
    }

    /**
     * @param productQuotationNo the productQuotationNo to set
     */
    public void setProductQuotationNo(String productQuotationNo) {
        this.productQuotationNo = productQuotationNo;
    }

    /**
     * @return the productQuotationDescNo
     */
    public Integer getProductQuotationDescNo() {
        return productQuotationDescNo;
    }

    /**
     * @param productQuotationDescNo the productQuotationDescNo to set
     */
    public void setProductQuotationDescNo(Integer productQuotationDescNo) {
        this.productQuotationDescNo = productQuotationDescNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productQuotationDescNo != null ? productQuotationDescNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CompositeProductQuotationDescriptionKey)) {
            return false;
        }
        CompositeProductQuotationDescriptionKey other = (CompositeProductQuotationDescriptionKey) object;
        if ((this.productQuotationDescNo == null && other.productQuotationDescNo != null) || (this.productQuotationDescNo != null && !this.productQuotationNo.equals(other.productQuotationNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CompositeProductQuotationDescKey entity [ID =" + productQuotationDescNo + "]";
    }

}
