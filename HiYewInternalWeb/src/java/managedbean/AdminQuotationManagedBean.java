/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.FillerEntity;
import entity.Metal;
import entity.Project;
import entity.Quotation;
import entity.QuotationDescription;
import entity.WeldJob;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;
import session.stateless.HiYewDSSSessionBeanLocal;
import session.stateless.ProjectSessionBeanLocal;
import session.stateless.QuotationSessionBeanLocal;

/**
 *
 * @author: jitcheong
 */
@Named(value = "adminQuotationManagedBean")
@ViewScoped
public class AdminQuotationManagedBean implements Serializable {
    @EJB
    private ProjectSessionBeanLocal projectSessionBean;
    
    @EJB
    private HiYewDSSSessionBeanLocal hiYewDSSSessionBean;

    @EJB
    private QuotationSessionBeanLocal quotationSessionBean;
    
    
    
    

    private String status = "Pending";
    private Integer year = Calendar.getInstance().get(Calendar.YEAR); //by default, get current year quotations
    private ArrayList<Quotation> receivedCustomerNewQuotations;
    private ArrayList<QuotationDescription> displayQuotationDescriptions;

    private Map<String, String> statuses;
    private Map<String, String> years;

    private Date companyEarliestEndDate;

    private Quotation selectedQuotation;

    private QuotationDescription selectedQuotationDescription;
    
    private double surfaceAreaToWeld;
    
    private ArrayList<FillerEntity> matchedFillers;
    
    private FillerEntity selectedFillerForWeld;
    
    private ArrayList<WeldJob> similarWeldJobs;
    
    private String secondMetalName;
    
    private int weldJobAvgDuration;

    /**
     * Creates a new instance of AdminQuotationManagedBean
     */
    public AdminQuotationManagedBean() {

        receivedCustomerNewQuotations = new ArrayList<>();
        displayQuotationDescriptions = new ArrayList<>();
        matchedFillers = new ArrayList<FillerEntity>();
        similarWeldJobs = new ArrayList<WeldJob>();

        statuses = new HashMap<>();
        years = new HashMap<>();
        selectedQuotation = new Quotation();
        
        
    }

    @PostConstruct
    public void init() {

        receivedCustomerNewQuotations = new ArrayList<>(quotationSessionBean.receivedCustomerNewQuotations(status, year));

        statuses.put("Pending", "Pending");
        statuses.put("Processed", "Processed");
        years.put("2014", "2014");
        years.put("2015", "2015");
    }

    public void filterByYearAndStatus() {

        receivedCustomerNewQuotations = new ArrayList<>(quotationSessionBean.receivedCustomerNewQuotations(status, year));
        System.out.println(year);
        System.out.println("value change");
    }

    public String formatDate(Timestamp t) {
        if (t != null) {
            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
            return sd.format(t.getTime());
        }
        return "";
    }

    public Timestamp formatDateToTimestamp(Date date) {
        Timestamp t = new Timestamp(date.getTime());
        return t;
    }

    public void onEditRow(RowEditEvent event) {
        Quotation q = (Quotation) event.getObject();//gives me unedited value
        q.setCompanyEarliestEnd(formatDateToTimestamp(companyEarliestEndDate));
        quotationSessionBean.conductMerge(q);
    }

    public void selectQuotation(Quotation q) {

        System.out.println(q.getQuotationNo());
        displayQuotationDescriptions = new ArrayList<>(q.getQuotationDescriptions());
        selectedQuotation = q;
        //System.out.println("quotation descriptions size is " + q.getQuotationDescriptions().size());
    }

    public void updateQuotationPricesAndSample() {

        quotationSessionBean.updateQuotationPrices(displayQuotationDescriptions);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quotations updated successfully", ""));

    }

    //Only if req for metal is yes, then price can be null 
    public boolean check() {
        boolean option = true;
        for (int i = 0; i < displayQuotationDescriptions.size(); i++) {
            QuotationDescription qd = displayQuotationDescriptions.get(i);
            if (qd.getPrice() == null) {
                if (!qd.getRequestForMetalSample().equals("Yes")) {
                    option = false;
                }
            }
        }
        return option;
    }

    public void updateQuotationStatus() {
        if (check() == true) {
            quotationSessionBean.updateQuotationStatus(selectedQuotation);
            selectedQuotation = new Quotation();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Prices must be quoted if we are not requesting for metal sample", ""));
        }

    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the receivedCustomerNewQuotations
     */
    public ArrayList<Quotation> getReceivedCustomerNewQuotations() {
        return receivedCustomerNewQuotations;
    }

    /**
     * @param receivedCustomerNewQuotations the receivedCustomerNewQuotations to
     * set
     */
    public void setReceivedCustomerNewQuotations(ArrayList<Quotation> receivedCustomerNewQuotations) {
        this.receivedCustomerNewQuotations = receivedCustomerNewQuotations;
    }

    /**
     * @return the statuses
     */
    public Map<String, String> getStatuses() {
        return statuses;
    }

    /**
     * @param statuses the statuses to set
     */
    public void setStatuses(Map<String, String> statuses) {
        this.statuses = statuses;
    }

    /**
     * @return the years
     */
    public Map<String, String> getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(Map<String, String> years) {
        this.years = years;
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

    /**
     * @return the companyEarliestEndDate
     */
    public Date getCompanyEarliestEndDate() {
        return companyEarliestEndDate;
    }

    /**
     * @param companyEarliestEndDate the companyEarliestEndDate to set
     */
    public void setCompanyEarliestEndDate(Date companyEarliestEndDate) {
        this.companyEarliestEndDate = companyEarliestEndDate;
    }

    public String clickPerformATP() {
//        System.out.println(this.selectedItem.getFillerCode());
//       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SelectedQuotationDescription", this.selectedQuotationDescription);
//       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SelectedQuotation", this.selectedQuotation);
//        //  FacesContext.getCurrentInstance().getExternalContext().getFlash().put("SelectedItem", this.selectedItem);
//        System.out.println("here2");
//        return "ics-item-details?faces-redirect=true";
        matchedFillers.clear();
        return "";
    }

    /**
     * @return the selectedQuotationDescription
     */
    public QuotationDescription getSelectedQuotationDescription() {
        return selectedQuotationDescription;
    }

    /**
     * @param selectedQuotationDescription the selectedQuotationDescription to
     * set
     */
    public void setSelectedQuotationDescription(QuotationDescription selectedQuotationDescription) {
        this.selectedQuotationDescription = selectedQuotationDescription;
    }

    /**
     * @return the surfaceAreaToWeld
     */
    public double getSurfaceAreaToWeld() {
        return surfaceAreaToWeld;
    }

    /**
     * @param surfaceAreaToWeld the surfaceAreaToWeld to set
     */
    public void setSurfaceAreaToWeld(double surfaceAreaToWeld) {
        this.surfaceAreaToWeld = surfaceAreaToWeld;
    }
    
    public void generateATP() {
        if((this.getSecondMetalName()==null)||(this.getSecondMetalName().length()==0)){
            this.setSecondMetalName(this.selectedQuotationDescription.getMetalName());
        }
        matchedFillers.clear();
        Metal m = hiYewDSSSessionBean.getExistingMetal(this.selectedQuotationDescription.getMetalName());
        if(m!=null){
        matchedFillers.addAll(hiYewDSSSessionBean.getListOfMatchedFillers(m));
        }
        getSimilarWeldJobs().clear();
        getSimilarWeldJobs().addAll(hiYewDSSSessionBean.getSimilarPastProjects(this.selectedQuotationDescription.getMetalName(), this.getSecondMetalName(), this.getSelectedQuotationDescription().getWeldingType()));
        getWeldJobEstimatedDuration();
       
    }
    
    public int numOfFillersNeeded(String itemCode){
        FillerEntity f = hiYewDSSSessionBean.getExistingItem(itemCode);
        return hiYewDSSSessionBean.quantityNeeded(f, this.surfaceAreaToWeld,this.selectedQuotationDescription.getQty());
    }

    /**
     * @return the matchedFillers
     */
    public ArrayList<FillerEntity> getMatchedFillers() {
        return matchedFillers;
    }

    /**
     * @param matchedFillers the matchedFillers to set
     */
    public void setMatchedFillers(ArrayList<FillerEntity> matchedFillers) {
        this.matchedFillers = matchedFillers;
    }

    /**
     * @return the selectedFillerForWeld
     */
    public FillerEntity getSelectedFillerForWeld() {
        return selectedFillerForWeld;
    }

    /**
     * @param selectedFillerForWeld the selectedFillerForWeld to set
     */
    public void setSelectedFillerForWeld(FillerEntity selectedFillerForWeld) {
        this.selectedFillerForWeld = selectedFillerForWeld;
    }
    
    public double getFillerTotalCost(){
        if(selectedFillerForWeld!=null){
        int amountRequired = hiYewDSSSessionBean.quantityNeeded(selectedFillerForWeld, surfaceAreaToWeld, selectedQuotationDescription.getQty());
        return amountRequired*selectedFillerForWeld.getCost();
        }else{
            return 0;
        }
    }



    /**
     * @return the secondMetalName
     */
    public String getSecondMetalName() {
        return secondMetalName;
    }

    /**
     * @param secondMetalName the secondMetalName to set
     */
    public void setSecondMetalName(String secondMetalName) {
        this.secondMetalName = secondMetalName;
    }

    /**
     * @return the similarWeldJobs
     */
    public ArrayList<WeldJob> getSimilarWeldJobs() {
        return similarWeldJobs;
    }

    /**
     * @param similarWeldJobs the similarWeldJobs to set
     */
    public void setSimilarWeldJobs(ArrayList<WeldJob> similarWeldJobs) {
        this.similarWeldJobs = similarWeldJobs;
    }
    
   public void getWeldJobEstimatedDuration(){
       this.setWeldJobAvgDuration(hiYewDSSSessionBean.deriveAverageDuration(similarWeldJobs));
   }

    /**
     * @return the weldJobAvgDuration
     */
    public int getWeldJobAvgDuration() {
        return weldJobAvgDuration;
    }

    /**
     * @param weldJobAvgDuration the weldJobAvgDuration to set
     */
    public void setWeldJobAvgDuration(int weldJobAvgDuration) {
        this.weldJobAvgDuration = weldJobAvgDuration;
    }
    
        public Timestamp getPlannedEnd(int days) {
        Timestamp planStart = null;
        Project p2 = projectSessionBean.getProjectWithEarliestCompletionDate();
        if (days != -1) {
            //check for any project slack which can accomodate the new project duration
            Project p1 = projectSessionBean.getProjectDurationWithSlack(days);

            if (p1 != null) {
                //compare and get earliest between proj slack and earliest proj completion date
                if (p2 != null) {
                    planStart = getEarliestTimeStamp(p1.getActualEnd(), p2.getPlannedEnd());
                } else {
                    planStart = p1.getActualEnd();
                }
            } else {
                //if there is no project slack equals new proj dur, then check and take the earliest project expected completion date

                if (p2 != null) {
                    planStart = p2.getPlannedEnd();
                }
            }
            //assign plannedEnd
            if (planStart != null) {
                return projectSessionBean.addDays(planStart, days);
            }
        } 
        
       return planStart;
        
    }

    public Timestamp getEarliestTimeStamp(Timestamp a, Timestamp b) {
        if (a != null && b != null) {
            if (a.after(b)) {
                return b;
            } else {
                return a;
            }
        }
        return null;
    }
    
   

}
