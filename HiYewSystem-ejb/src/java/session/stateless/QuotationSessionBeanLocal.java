package session.stateless;

import entity.Quotation;
import entity.QuotationDescription;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

@Local
public interface QuotationSessionBeanLocal {

    public String getQuotationNo(String username);

    public void createQuotation(Quotation quotation);

    public void createQuotationDesciption(QuotationDescription quotationDescription);

    public List<Quotation> receivedQuotations(String username);

    public List<Quotation> receivedCustomerNewQuotations(String status, Integer year);

    public void conductMerge(Quotation quotation);

    public void updateQuotationPrices(ArrayList<QuotationDescription> list);

    public void updateQuotationStatus(Quotation q);
}
