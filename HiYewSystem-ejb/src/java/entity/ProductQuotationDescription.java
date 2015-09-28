package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@IdClass(CompositeProductQuotationDescriptionKey.class)
@Entity
public class ProductQuotationDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer productQuotationDescNo;
    private String productType;
    private String itemName;
    private Double unitPrice;
    private Integer quantity;
    private String remarks;

    @ManyToOne
    private ProductQuotation productQuotation;
    @Id
    private String productQuotationNo;

    public ProductQuotationDescription() {
    }

    public ProductQuotationDescription(Integer productQuotationDescNo, String productType, String itemName, Integer quantity, Double unitPrice, String remarks, ProductQuotation productQuotation) {
        this.productQuotationDescNo = productQuotationDescNo;
        this.productType = productType;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.remarks = remarks;
        this.productQuotation = productQuotation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getProductQuotationDescNo() != null ? getProductQuotationDescNo().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductQuotationDescription)) {
            return false;
        } else {
            ProductQuotationDescription other = (ProductQuotationDescription) object;
            if ((this.getProductQuotationDescNo() == null && other.getProductQuotationDescNo() != null) || (this.getProductQuotationDescNo() != null && !this.productQuotationDescNo.equals(other.productQuotationDescNo))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductQuotationDescription entity [ID =" + getProductQuotationDescNo() + "]";
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

    /**
     * @return the productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the unitPrice
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the productQuotation
     */
    public ProductQuotation getProductQuotation() {
        return productQuotation;
    }

    /**
     * @param productQuotation the productQuotation to set
     */
    public void setProductQuotation(ProductQuotation productQuotation) {
        this.productQuotation = productQuotation;
    }

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
}
