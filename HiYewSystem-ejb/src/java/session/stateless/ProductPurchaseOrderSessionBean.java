package session.stateless;

import entity.Customer;
import entity.ProductPurchaseOrder;
import entity.ProductQuotationDescription;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProductPurchaseOrderSessionBean implements ProductPurchaseOrderSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    public void createProductPurchaseOrder(ProductPurchaseOrder productPurchaseOrder) {
        em.persist(productPurchaseOrder);
    }

    public void conductMerge(ProductPurchaseOrder productPurchaseOrder) {
        em.merge(productPurchaseOrder);
    }

    public List<ProductPurchaseOrder> receivedProductPurchaseOrder(String username) {
        Customer customer = em.find(Customer.class, username);
        return customer.getProductPurchaseOrderList();
    }

    public List<ProductPurchaseOrder> receivedCustomerNewProductPOList(String status) {
        Query query;
        if (status.equals("Settled")) {
            query = em.createQuery("SELECT q FROM ProductPurchaseOrder AS q where q.status='Delivered'");
        } else {
            query = em.createQuery("SELECT q FROM ProductPurchaseOrder AS q where q.status='Pending' or q.status='Processed' or q.status='Relayed'");
        }
        
//        Query query = em.createQuery("SELECT po FROM ProductPurchaseOrder AS po where po.status= :status");
//
//        query.setParameter("status", status);

        List<ProductPurchaseOrder> productPOList = query.getResultList();
        System.out.println("productPOList.size() ===== " + productPOList.size());
        return productPOList;
    }

    public List<ProductQuotationDescription> retrieveProductQuotationDescriptionList(String purchaseOrderNo) {
        System.out.println("ProductPurchaseOrderSessionBean.java receivedProductQuotationList() purchaseOrderNo ===== " + purchaseOrderNo);

        Query query = em.createQuery("SELECT qd FROM ProductQuotationDescription AS qd WHERE qd.productQuotation.productQuotationNo=:productQuotationDescNo");

        query.setParameter("productQuotationDescNo", purchaseOrderNo);

        List<ProductQuotationDescription> productQuotationDescriptionList = query.getResultList();
        return productQuotationDescriptionList;
    }

    public void updatePODeliveryDate(ProductPurchaseOrder inProductPurchaseOrder) {
        em.merge(inProductPurchaseOrder);
    }

    public void updateProductPODeliveredStatus(ProductPurchaseOrder inProductPurchaseOrder) {
        ProductPurchaseOrder ppo = em.find(ProductPurchaseOrder.class, inProductPurchaseOrder.getProductPurchaseOrderID());
        ppo.setStatus("Delivered");
    }

    public void updateProductPORelayedStatus(ProductPurchaseOrder inProductPurchaseOrder) {
        ProductPurchaseOrder ppo = em.find(ProductPurchaseOrder.class, inProductPurchaseOrder.getProductPurchaseOrderID());
        ppo.setStatus("Relayed");
    }

    public void updateProductPOStatus(ProductPurchaseOrder inProductPurchaseOrder) {
        ProductPurchaseOrder ppo = em.find(ProductPurchaseOrder.class, inProductPurchaseOrder.getProductPurchaseOrderID());
        System.out.println("updateProductPOStatus......................................... Processed");
        ppo.setStatus("Processed");
    }
}
