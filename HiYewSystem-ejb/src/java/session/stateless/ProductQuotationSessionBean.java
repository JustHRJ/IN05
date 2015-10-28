package session.stateless;

import entity.Customer;
import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProductQuotationSessionBean implements ProductQuotationSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    public String getProductQuotationNo(String username) {
        Customer customer = em.find(Customer.class, username);
        return generateProductQuotationNo(customer);
    }

    private String generateProductQuotationNo(Customer customer) {
        String customerUsername = customer.getName();
        String newProductQuotationNo = "";
        String[] splitArray = null;
        if (customerUsername.contains("-")) {
            splitArray = customerUsername.split("-");
            for (String s : splitArray) {
                newProductQuotationNo += s.substring(0, 1);
            }
        } else if (customerUsername.contains(" ")) {
            splitArray = customerUsername.split(" ");
            for (String s : splitArray) {
                newProductQuotationNo += s.substring(0, 1);
            }
        } else {
            newProductQuotationNo = customerUsername;
        }
        newProductQuotationNo += new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime());

        return newProductQuotationNo;
    }

    public void createProductQuotation(ProductQuotation productQuotation) {
        em.persist(productQuotation);
    }

    public void createProductQuotationDesciption(ProductQuotationDescription productQuotationDescription) {
        em.persist(productQuotationDescription);
    }

    public List<ProductQuotation> receivedProductQuotationList(String username) {
        System.out.println("productQuotationList username ===== " + username);
        Query query = em.createQuery("SELECT q FROM ProductQuotation AS q where q.customer.userName=:username ");

        query.setParameter("username", username);

        List<ProductQuotation> productQuotationList = query.getResultList();
        return productQuotationList;
    }

    public List<ProductQuotation> receivedCustomerNewProductQuotationList(String status) {
        Query query = em.createQuery("SELECT q FROM ProductQuotation AS q where q.status= :status");

        query.setParameter("status", status);

        List<ProductQuotation> productQuotationList = query.getResultList();
        System.out.println("productQuotationList.size() ===== " + productQuotationList.size());
        return productQuotationList;
    }
    
    public List<ProductQuotationDescription> retrieveProductQuotationDescriptionList(String quotationNo) {
        System.out.println("ProductPurchaseOrderSessionBean.java receivedProductQuotationList() quotationNo ===== " + quotationNo);

        Query query = em.createQuery("SELECT qd FROM ProductQuotationDescription AS qd WHERE qd.productQuotation.productQuotationNo=:quotationNo");

        query.setParameter("quotationNo", quotationNo);

        List<ProductQuotationDescription> productQuotationDescriptionList = query.getResultList();
        return productQuotationDescriptionList;
    }

    public void conductMerge(ProductQuotation productQuotation) {
        em.merge(productQuotation);
    }

    public void updateProductQuotationPrices(ArrayList<ProductQuotationDescription> list) {
        for (ProductQuotationDescription productQuotationDescription : list) {
            em.merge(productQuotationDescription);
        }
    }

    public void updateProductQuotationRelayedStatus(ProductQuotation inProductQuotation) {
        ProductQuotation productQuotation = em.find(ProductQuotation.class, inProductQuotation.getProductQuotationNo());
        productQuotation.setStatus("Relayed");
    }

    public void updateProductQuotationStatus(ProductQuotation inProductQuotation) {
        ProductQuotation productQuotation = em.find(ProductQuotation.class, inProductQuotation.getProductQuotationNo());
        productQuotation.setStatus("Processed");
    }

}
