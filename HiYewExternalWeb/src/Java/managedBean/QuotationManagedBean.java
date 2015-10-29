package managedBean;

import entity.Quotation;
import entity.QuotationDescription;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.MetalSessionBeanLocal;
import session.stateless.QuotationSessionBeanLocal;

@Named(value = "quotationManagedBean")
@SessionScoped
public class QuotationManagedBean implements Serializable {

    @EJB
    private QuotationSessionBeanLocal quotationSessionBean;
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    @EJB
    private MetalSessionBeanLocal metalSessionBean;

    private String username = "";
    private String date = "";
    private String quotationNo = "";
    private Integer count;
    private String txt1 = "";
    private Date latestEndDate = null;
    //private String latestEndDate = "";
    private ArrayList<QuotationDescription> cacheList = new ArrayList<>();
    private Quotation newQuotation;
    private QuotationDescription newQuotationDesc;

    private ArrayList<Quotation> receivedQuotations;
    private ArrayList<QuotationDescription> displayQuotationDescriptions;

    public QuotationManagedBean() {
        System.out.println("QuotationManagedBean.java QuotationManagedBean()");
        newQuotation = new Quotation();
        newQuotationDesc = new QuotationDescription();
        receivedQuotations = new ArrayList<>();
        displayQuotationDescriptions = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        count = 1;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            System.out.println("QuotationManagedBean.java init() ===== Username is " + username);
        }
        date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

        receivedQuotations = new ArrayList<>(quotationSessionBean.receivedQuotations(username));
    }

    public void checkToReset() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString() == null) {

        } else if (!username.equals(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString())) {
            reset();
        }
    }

    public void reset() {
        newQuotation = new Quotation();
        newQuotationDesc = new QuotationDescription();
        receivedQuotations = new ArrayList<>();
        displayQuotationDescriptions = new ArrayList<>();

        count = 1;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            System.out.println("QuotationManagedBean.java init(): Username is " + username);
        }
        date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

    public ArrayList<String> completeText(String str) {
        ArrayList<String> results = new ArrayList<>(metalSessionBean.getMetalBySubString(str));
        System.out.println("Size of results: " + results.size());
        return results;
    }

    public void receivedQuotations() {
        System.out.println("QuotationManagedBean.java receivedQuotations() ===== " + username);

        if (!username.equals(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString())) {
            System.out.println("QuotationManagedBean.java receivedQuotations() ===== call method reset()");
            reset();
        }

        receivedQuotations = new ArrayList<>(quotationSessionBean.receivedQuotations(username));
        FacesContext.getCurrentInstance().addMessage("qMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Current quotations are up to date.", ""));
    }

    public String formatDate(Timestamp t) {
        if(t != null){
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        return sd.format(t.getTime());
        }
        return "";

    }

    public void selectQuotation(Quotation q) {
        System.out.println(q.getQuotationNo());
        if (q != null) {
            displayQuotationDescriptions = new ArrayList<>(q.getQuotationDescriptions());
        }

        //System.out.println("quotation descriptions size is " + q.getQuotationDescriptions().size());
    }

    public void addToCacheList(ActionEvent event) {

        if (newQuotationDesc.getMetalName().equals("") || newQuotationDesc.getQty() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Metal Name or quantity must not be left unfilled!", ""));
        } else {

            newQuotationDesc.setQuotationDescNo(count);
            count += 1;
            cacheList.add(newQuotationDesc);

            //reinitialise quotation description
            newQuotationDesc = new QuotationDescription();
            //set textbox to blank again
        }
    }

    public void deleteQuotationDescription(QuotationDescription qd) {
        if (qd != null) {
            cacheList.remove(qd.getQuotationDescNo() - 1);
            reassignQuotationNo();
        }
    }

    public void reassignQuotationNo() {

        for (int i = 0; i < cacheList.size(); i++) {
            cacheList.get(i).setQuotationDescNo(i + 1);
        }
        count = cacheList.size() + 1;
    }

    public void createQuotation(ActionEvent event) {
        if (cacheList.size() < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "RFQ creation must have at least one item job!", ""));
        } else {
            //generate quotatioNo
            quotationNo = quotationSessionBean.getQuotationNo(username);
            //assign quotation
            newQuotation.setQuotationNo(quotationNo);

            if (latestEndDate != null) {
            System.out.println("time is " + latestEndDate.getTime());
              Timestamp t = new Timestamp(latestEndDate.getTime());
              newQuotation.setCustomerLatestEnd(t);
            }else{
              System.out.println("latestStartDate is null");
            }
            
            //try {
            //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            //    Date parsedDate = dateFormat.parse(latestEndDate);
            //    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            //    newQuotation.setCustomerLatestStart(timestamp);
           // } catch (Exception e) {
//
           // }
            //

            newQuotation.setCustomer(customerSessionBean.getCustomerByUsername(username));
            //persist quotation
            quotationSessionBean.createQuotation(newQuotation);
            //add quotationDescription into its respective quotation
            for (QuotationDescription qd : cacheList) {
                qd.setQuotationNo(quotationNo);
                qd.setQuotation(newQuotation);
                newQuotation.addQuotationDescriptions(qd);
                quotationSessionBean.createQuotationDesciption(qd);//persist qd
            }
            quotationSessionBean.conductMerge(newQuotation);
            //add quotation to customer
            customerSessionBean.addQuotation(username, newQuotation);

            //empty cachelist
            cacheList.clear();
            //set count back to 1
            count = 1;
            //reinitialise quotation
            newQuotation = new Quotation();
            newQuotationDesc = new QuotationDescription();
            //set quotation tab to be selected
            //System.out.println("Your RFQ has been sent successfully!");
            FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Your RFQ has been sent successfully!", ""));
        }
    }

    public void setRejectionStatus(Quotation q) {
        System.out.println("Customer rejected quotation!");
        q.setStatus("Rejected");
        quotationSessionBean.conductMerge(q);
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the newQuotation
     */
    public Quotation getNewQuotation() {
        return newQuotation;
    }

    /**
     * @param newQuotation the newQuotation to set
     */
    public void setNewQuotation(Quotation newQuotation) {
        this.newQuotation = newQuotation;
    }

    /**
     * @return the quotationNo
     */
    public String getQuotationNo() {
        return quotationNo;
    }

    /**
     * @param quotationNo the quotationNo to set
     */
    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the cacheList
     */
    public ArrayList<QuotationDescription> getCacheList() {
        return cacheList;
    }

    /**
     * @param cacheList the cacheList to set
     */
    public void setCacheList(ArrayList<QuotationDescription> cacheList) {
        this.cacheList = cacheList;
    }

    /**
     * @return the newQuotationDesc
     */
    public QuotationDescription getNewQuotationDesc() {
        return newQuotationDesc;
    }

    /**
     * @param newQuotationDesc the newQuotationDesc to set
     */
    public void setNewQuotationDesc(QuotationDescription newQuotationDesc) {
        this.newQuotationDesc = newQuotationDesc;
    }

    /**
     * @return the receivedQuotations
     */
    public ArrayList<Quotation> getReceivedQuotations() {
        return receivedQuotations;
    }

    /**
     * @param receivedQuotations the receivedQuotations to set
     */
    public void setReceivedQuotations(ArrayList<Quotation> receivedQuotations) {
        this.receivedQuotations = receivedQuotations;
    }

    /**
     * @return the displayQuotationDescriptions
     */
    public ArrayList<QuotationDescription> getDisplayQuotationDescriptions() {
        return displayQuotationDescriptions;
    }

    /**
     * @param displayQuotationDescriptions the displayQuotationDescriptions to
     * set
     */
    public void setDisplayQuotationDescriptions(ArrayList<QuotationDescription> displayQuotationDescriptions) {
        this.displayQuotationDescriptions = displayQuotationDescriptions;
    }

    public boolean setVisibility(Quotation q) {

        //String id = "approvalPanel";
        //UIComponent foundComponent;
        //for (UIComponent component : FacesContext.getCurrentInstance().getViewRoot().getChildren()) {
        //   if (component.getId().contains(id)) {
        //      foundComponent = component;
        //      break;
        // }
        //}
        if (q.getStatus().equals("Pending") || q.getStatus().equals("Accepted") || q.getStatus().equals("Rejected")) {
            return false;
        }
        return true;
    }

    /**
     * @return the latestStartDate
     */
    //public String getLatestStartDate() {
    //    return latestEndDate;
    //}

    /**
     * @param latestStartDate the latestStartDate to set
     */
    //public void setLatestStartDate(String latestEndDate) {
    //    this.latestEndDate = latestEndDate;
    //}

    /**
     * @return the latestEndDate
     */
    public Date getLatestEndDate() {
        return latestEndDate;
     }
    /**
     * @param latestEndDate the latestEndDate to set
     */
    public void setLatestEndDate(Date latestEndDate) {
        this.latestEndDate = latestEndDate;
    }
}
