package session.stateless;

import entity.Customer;
import entity.ProductPurchaseOrder;
import entity.ProductQuotation;
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

    public List<ProductQuotationDescription> retrieveProductQuotationDescriptionList(String purchaseOrderNo) {
        System.out.println("ProductPurchaseOrderSessionBean.java receivedProductQuotationList() purchaseOrderNo ===== " + purchaseOrderNo);

        Query query = em.createQuery("SELECT qd FROM ProductQuotationDescription AS qd WHERE qd.productQuotation.productQuotationNo=:productQuotationDescNo");

        query.setParameter("productQuotationDescNo", purchaseOrderNo);

        List<ProductQuotationDescription> productQuotationDescriptionList = query.getResultList();
        return productQuotationDescriptionList;
    }
}
