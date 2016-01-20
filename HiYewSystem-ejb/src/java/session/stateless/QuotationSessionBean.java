package session.stateless;

import entity.Customer;
import entity.Quotation;
import entity.QuotationDescription;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class QuotationSessionBean implements QuotationSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    @Override
    public String getQuotationNo(String username) {
        System.out.println("QuotationSessionBean.java getQuotationNo ==== " + username);
        Customer c = em.find(Customer.class, username);
        return generateQuotationNo(c);
    }

    public String generateQuotationNo(Customer customer) {
        String custName = customer.getName();
        String newQuotationNo = "";
        String[] splitArray = null;
        if (custName.contains("-")) {
            splitArray = custName.split("-");
            for (String s : splitArray) {
                newQuotationNo += s.substring(0, 1);
            }
        } else if (custName.contains(" ")) {
            splitArray = custName.split(" ");
            for (String s : splitArray) {
                newQuotationNo += s.substring(0, 1);
            }
        } else {
            newQuotationNo = custName;
        }
        newQuotationNo += new SimpleDateFormat("ddMMyyss").format(Calendar.getInstance().getTime());

        return newQuotationNo;
    }

    public void updateRequestForm(String link, String quotationNo){
        Quotation quote = em.find(Quotation.class, quotationNo);
        if(quote == null){
            System.out.println("quotation not found");
        } else{
            quote.setDocument(link);
            em.merge(quote);
        }
    }
    
    
    public void conductMerge(Quotation q) {
        System.out.println("here");
        String id = q.getQuotationNo();
        Quotation merging = em.find(Quotation.class, id);
        if(merging == null){
            System.out.println("got problem");
        } else{
            System.out.println(q.getQuotationNo());
            merging = q;
            em.merge(merging);
        }
     
//        String folderName = q.getQuotationNo();
//        boolean check = new File("C:\\Users\\JustHRJ\\Desktop\\Final Presentation\\IN05\\HiYewInternalWeb\\web\\projectDocuments\\" + folderName).mkdir();
//        if (check) {
//            System.out.println("folder has been created");
//        } else {
//            System.out.println("folder has not been created");
//        }

    }

    @Override
    public void createQuotation(Quotation quotation) {
        em.persist(quotation);
    }

    @Override
    public void createQuotationDesciption(QuotationDescription quotationDescription) {

        em.persist(quotationDescription);
    }

    @Override
    public List<Quotation> receivedQuotations(String username) {
        em.flush();
        System.out.println("QuotationSessionBean.java receivedQuotations(String username) username ===== " + username);

        Query query = em.createQuery("Select q FROM Quotation AS q where q.date >= :threeDaysAgo AND q.customer.userName=:username ");

        Date now = addDays(new Date(), -3);
        Timestamp threeDaysAgo = new Timestamp(now.getTime());

        query.setParameter("threeDaysAgo", threeDaysAgo);
        query.setParameter("username", username);

        List<Quotation> quotations = query.getResultList();
        return quotations;
    }

    public Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public Date getFirstDayOfYear(int year) {
        Calendar cld = Calendar.getInstance();
        cld.set(year, 0, 0);
        return cld.getTime();
    }

    public Date getLastDayOfYear(int year) {
        Calendar cld = Calendar.getInstance();
        cld.set(year + 1, 0, -1);
        return cld.getTime();
    }

    ///SMS Administrator///
    @Override
    public void updateQuotationPrices(ArrayList<QuotationDescription> list) {

        for (QuotationDescription qd : list) {
            em.merge(qd);
        }
    }

    @Override
    public void updateQuotationStatus(Quotation q) {
        Quotation quotation = em.find(Quotation.class, q.getQuotationNo());
        quotation.setStatus("Processed");

    }

    @Override
    public List<Quotation> receivedCustomerNewQuotations(String status, Integer year) {
        System.out.println("start");
        Query query = em.createQuery("Select q FROM Quotation AS q where q.status= :status and q.date >= :firstDayOfYear and q.date <= :lastDayOfYear");

        Date first = getFirstDayOfYear(year);
        Timestamp firstDayOfYear = new Timestamp(first.getTime());

        Date last = getLastDayOfYear(year);
        Timestamp lastDayOfYear = new Timestamp(last.getTime());

        query.setParameter("status", status);
        query.setParameter("firstDayOfYear", firstDayOfYear);
        query.setParameter("lastDayOfYear", lastDayOfYear);

        List<Quotation> quotations = new ArrayList<Quotation>();
        
        for(Object o: query.getResultList()){
            Quotation q = (Quotation) o;
            q.getCustomer();
            quotations.add(q);
        }
        
        System.out.println("quotation size is " + quotations.size());
        return quotations;
    }
}
