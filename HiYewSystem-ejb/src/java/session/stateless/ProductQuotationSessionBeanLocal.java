package session.stateless;

import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ProductQuotationSessionBeanLocal {

    public String getProductQuotationNo(String username);

    public void createProductQuotation(ProductQuotation productQuotation);

    public void createProductQuotationDesciption(ProductQuotationDescription productQuotationDescription);

    public List<ProductQuotation> receivedProductQuotationList(String username);

    public List<ProductQuotation> receivedCustomerNewProductQuotationList(String status);
    
    public List<ProductQuotationDescription> retrieveProductQuotationDescriptionList(String purchaseOrderNo);

    public void conductMerge(ProductQuotation productQuotation);

    public void updateProductQuotationPrices(ArrayList<ProductQuotationDescription> list);

    public void updateProductQuotationRelayedStatus(ProductQuotation productQuotation);
    
    public void updateProductQuotationStatus(ProductQuotation productQuotation);
    
    public List<List<List<String>>> deriveRevenueGraph_Year();
    
    public List<List<List<List<String>>>> deriveRevenueGraph_Quarter();
    
    public List<List<List<List<String>>>> deriveRevenueGraph_Month();
}
