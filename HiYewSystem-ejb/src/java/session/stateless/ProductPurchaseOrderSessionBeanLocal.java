package session.stateless;

import entity.ProductPurchaseOrder;
import entity.ProductQuotationDescription;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ProductPurchaseOrderSessionBeanLocal {

    public void createProductPurchaseOrder(ProductPurchaseOrder productPurchaseOrder);

    public void conductMerge(ProductPurchaseOrder productPurchaseOrder);

    public List<ProductPurchaseOrder> receivedProductPurchaseOrder(String username);
    
    public List<ProductQuotationDescription> retrieveProductQuotationDescriptionList(String purchaseOrderNo);
}
