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

/**
 *
 * @author JustHRJ
 */
@Entity
public class DocumentControlEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String purchaseOrder;
    private String custDeliveryOrder;
    private String productWeldSheet;
    private String serviceReport;
    private String invoice;
    private String comDeliveyOrder;
    private String requestForm;
    

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
        if (!(object instanceof DocumentControlEntity)) {
            return false;
        }
        DocumentControlEntity other = (DocumentControlEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DocumentControlEntity[ id=" + id + " ]";
    }

    /**
     * @return the purchaseOrder
     */
    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    /**
     * @param purchaseOrder the purchaseOrder to set
     */
    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    /**
     * @return the custDeliveryOrder
     */
    public String getCustDeliveryOrder() {
        return custDeliveryOrder;
    }

    /**
     * @param custDeliveryOrder the custDeliveryOrder to set
     */
    public void setCustDeliveryOrder(String custDeliveryOrder) {
        this.custDeliveryOrder = custDeliveryOrder;
    }

    /**
     * @return the productWeldSheet
     */
    public String getProductWeldSheet() {
        return productWeldSheet;
    }

    /**
     * @param productWeldSheet the productWeldSheet to set
     */
    public void setProductWeldSheet(String productWeldSheet) {
        this.productWeldSheet = productWeldSheet;
    }

    /**
     * @return the serviceReport
     */
    public String getServiceReport() {
        return serviceReport;
    }

    /**
     * @param serviceReport the serviceReport to set
     */
    public void setServiceReport(String serviceReport) {
        this.serviceReport = serviceReport;
    }

    /**
     * @return the invoice
     */
    public String getInvoice() {
        return invoice;
    }

    /**
     * @param invoice the invoice to set
     */
    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    /**
     * @return the comDeliveyOrder
     */
    public String getComDeliveyOrder() {
        return comDeliveyOrder;
    }

    /**
     * @param comDeliveyOrder the comDeliveyOrder to set
     */
    public void setComDeliveyOrder(String comDeliveyOrder) {
        this.comDeliveyOrder = comDeliveyOrder;
    }

    /**
     * @return the requestForm
     */
    public String getRequestForm() {
        return requestForm;
    }

    /**
     * @param requestForm the requestForm to set
     */
    public void setRequestForm(String requestForm) {
        this.requestForm = requestForm;
    }
    
}
