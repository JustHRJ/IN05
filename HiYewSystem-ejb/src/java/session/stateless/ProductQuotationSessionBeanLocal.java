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

    public void conductMerge(ProductQuotation productQuotation);

    public void updateProductQuotationPrices(ArrayList<ProductQuotationDescription> list);

    public void updateProductQuotationStatus(ProductQuotation productQuotation);

}
