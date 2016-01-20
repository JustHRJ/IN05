/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Customer;
import entity.FillerEntity;
import entity.Metal;
import entity.Project;
import entity.Quotation;
import entity.QuotationDescription;
import entity.WeldJob;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
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

    private String link = "";

    private boolean img = false;
    private boolean pdf = false;

    private Date companyEarliestEndDate;

    private Quotation selectedQuotation;

    private QuotationDescription selectedQuotationDescription;

    private double surfaceAreaToWeld;
    
    private double oldVolume;
    private double newVolume;
    private double volumeNeeded;
    private double massOfFillerReq;
    private double densityOfSteel;

    private ArrayList<FillerEntity> matchedFillers;

    private FillerEntity selectedFillerForWeld;

    private ArrayList<WeldJob> similarWeldJobs;

    private String secondMetalName;

    private int weldJobAvgDuration;

    private double weldJobMaterialCost;
    private double weldJobManpowerCost;

    private int profitMargin;
    private double weldJobPriceToQuote;
    private double weldJobPriceToQuotePerQty;
    private double materialPlusManpowerCost;
    private HashMap projectDaysMap;
    private int numOfTimesConductedATP;
    private HashMap projectNumOfATPResult;

    private FillerEntity recommendedFiller;

    /**
     * Creates a new instance of AdminQuotationManagedBean
     */
    public AdminQuotationManagedBean() {

        receivedCustomerNewQuotations = new ArrayList<>();
        displayQuotationDescriptions = new ArrayList<>();
        matchedFillers = new ArrayList<FillerEntity>();
        similarWeldJobs = new ArrayList<WeldJob>();
        projectDaysMap = new HashMap();
        projectNumOfATPResult = new HashMap();

        statuses = new HashMap<>();
        years = new HashMap<>();
        selectedQuotation = new Quotation();

    }

    @PostConstruct
    public void init() {

        receivedCustomerNewQuotations = new ArrayList<>(quotationSessionBean.receivedCustomerNewQuotations(status, year));
        statuses.put("2 Pending", "Processed");
        statuses.put("1 Unprocessed", "Pending");
        statuses.put("(3)Accepted", "Accepted");
        statuses.put("(4)Rejected", "Rejected");
        years.put("2015", "2015");
        years.put("2016", "2016");

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

    public String getPopulateMessage() {
        Customer c = getSelectedQuotation().getCustomer();
        String message = "";
        message += "Dear " + c.getName() + "," + "<br/><br/>";
        message += "Thank you for your enquiry" + "<br/><br/>";
        message += "We are pleased to quote you the following" + "<br/>";
        if (getSelectedQuotation().getQuotationDescriptions().size() > 0) {
            for (int i = 0; i < getSelectedQuotation().getQuotationDescriptions().size(); i++) {
                message += "Description: " + getSelectedQuotation().getQuotationDescriptions().get(i).getItemDesc() + "<br/>";
                message += "Qty: " + getSelectedQuotation().getQuotationDescriptions().get(i).getQty().toString() + "<br/>";
                message += "Material: " + getSelectedQuotation().getQuotationDescriptions().get(i).getMetalName() + "<br/>";
                if (displayQuotationDescriptions.size() > 0) {
                    message += "Quoted price per piece" + displayQuotationDescriptions.get(i).getPrice().toString() + "<br/><br/>";
                }
            }
        }
        message += "Please do not hesitate to contact us if you have any enquiries" + "<br/><br/>";

        message += "Thank you" + "<br/><br/>";
        message += "Best Regards," + "<br/>";
        message += "Han Kiat";
        return message;
    }

    public String getPopulateCustName() {
        System.out.println("populating customer details");
        System.out.println("name of customer - " + getSelectedQuotation().getCustomer().getName());
        System.out.println(getSelectedQuotation().getQuotationNo());
        Customer c = getSelectedQuotation().getCustomer();
        return c.getName();
    }

    public String getPopulateCustMail() {
        Customer c = getSelectedQuotation().getCustomer();
        return c.getEmail();
    }

    public void selectQuotation(Quotation q) {

        System.out.println(q.getQuotationNo());
        displayQuotationDescriptions = new ArrayList<>(q.getQuotationDescriptions());
        setSelectedQuotation(q);
        getSelectedQuotation().getCustomer();

        //System.out.println("quotation descriptions size is " + q.getQuotationDescriptions().size());
    }

    public void updateQuotationPricesAndSample() {
        if (selectedQuotation.getStatus().equals("Accepted") || selectedQuotation.getStatus().equals("Rejected")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quotations cannot be updated as it is already accepted / Rejected", ""));
        } else {
            quotationSessionBean.updateQuotationPrices(displayQuotationDescriptions);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quotations updated successfully", ""));
        }
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
            quotationSessionBean.updateQuotationStatus(getSelectedQuotation());
            showDialog3();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Prices must be quoted if we are not requesting for metal sample", ""));
        }

    }

    public void showDialog() {
        selectedQuotation.setQuotationDescriptions(displayQuotationDescriptions);
        System.out.println("Show Dialog - Creating template upon pricing");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDialogVar').show();");
    }

    public void showDialog3() {
        System.out.println("Show Dialog - Creating description template upon pricing");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDialogVar3').show();");
    }

    public void showDialog7() {
        System.out.println("Show Dialog - Creating description template upon pricing");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDialogVar6').hide();");
        img = true;
        pdf = false;
        setLink(selectedQuotation.getDocument());
        System.out.println(link);
    }

    public void showDialog8() {
        System.out.println("Show Dialog - Creating description template upon pricing");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDialogVar6').hide();");
        pdf = true;
        img = false;
        setLink(selectedQuotation.getDocument());
        System.out.println(link);
    }

    public void showDialog4(Quotation item) {
        selectedQuotation = item;
        System.out.println("Show Dialog - Creating template upon pricing");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDialogVar6').show();");
    }

    public boolean checkForPdf() {
        return pdf;
    }

    public boolean checkForImg() {
        if (img == true) {
            return true;
        }
        return false;
    }

    public String viewPDF() {
        try {
            String pdfURL = selectedQuotation.getDocument();
            System.out.println("pdfURl =" + pdfURL);
            if (pdfURL == null || pdfURL.equals("")) {
                return "";
            } else {
                return pdfURL;
            }
        } catch (Exception ex) {
            return "";
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
        similarWeldJobs.clear();
        this.setSecondMetalName(null);
        selectedFillerForWeld = new FillerEntity();
        weldJobAvgDuration = 0;
        weldJobManpowerCost = 0;
        weldJobMaterialCost = 0;
        weldJobPriceToQuote = 0;
        weldJobPriceToQuotePerQty = 0;
        profitMargin = 0;
        numOfTimesConductedATP = 0;
        recommendedFiller = new FillerEntity();

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
        setVolumeNeeded(getNewVolume() - getOldVolume());
        massOfFillerReq = volumeNeeded * densityOfSteel;
        if ((this.getSecondMetalName() == null) || (this.getSecondMetalName().length() == 0)) {
            this.setSecondMetalName(this.selectedQuotationDescription.getMetalName());
        }
        matchedFillers.clear();
        Metal m = hiYewDSSSessionBean.getExistingMetal(this.selectedQuotationDescription.getMetalName());
        if (m != null) {
            matchedFillers.addAll(hiYewDSSSessionBean.getListOfMatchedFillers(m));
        }
        recommendedFiller = deriveRecommended(matchedFillers);
        similarWeldJobs.clear();
        System.out.println("Metal 1 " + this.selectedQuotationDescription.getMetalName());
        System.out.println("Metal 2 " + this.getSecondMetalName());
        System.out.println("Weld Type " + this.getSelectedQuotationDescription().getWeldingType());
        similarWeldJobs.addAll(hiYewDSSSessionBean.getSimilarPastProjects(this.selectedQuotationDescription.getMetalName(), this.getSecondMetalName(), this.getSelectedQuotationDescription().getWeldingType()));
        System.out.println("Size of Similar Weld Jobs " + similarWeldJobs.size());
        getFillerTotalCost();
      // getWeldJobEstimatedDuration();
      //  deriveManpowerCost(weldJobAvgDuration);
      //  deriveWeldJobTotalPrice();
      //  pricePerUnit();

    }

    public FillerEntity deriveRecommended(ArrayList<FillerEntity> returnedFillers) {
        FillerEntity recommendedFiller = new FillerEntity();
        double lowestPrice = 999999;
        //sdasdadas

        for (int i = 0; i < returnedFillers.size(); i++) {
            int numNeeded = numOfFillersNeeded(returnedFillers.get(i).getFillerCode());
            double fillerPrice = numNeeded * returnedFillers.get(i).getCost();
            int qtyLeft = (returnedFillers.get(i).getQuantity() - returnedFillers.get(i).getBookedQuantity()) - numNeeded;
            System.out.println("@@@@@@@@@@@@@ " + returnedFillers.get(i).getFillerCode());
            System.out.println("@@@@@@@@@@@@@ " + numNeeded);
            System.out.println("@@@@@@@@@@@@@ " + qtyLeft);
            if (qtyLeft >= 0) {
                System.out.println("*************************enuff qty");
                //check whish has the lowest price...
                if (fillerPrice < lowestPrice) {
                    System.out.println("*************************price lower");
                    lowestPrice = fillerPrice;
                    recommendedFiller = returnedFillers.get(i);
                }

            }
        }
        System.out.println("###########################################Recommeded Filler: " + recommendedFiller.getFillerCode());
        return recommendedFiller;
    }

    public int numOfFillersNeeded(String itemCode) {

        FillerEntity f = hiYewDSSSessionBean.getExistingItem(itemCode);
        return hiYewDSSSessionBean.quantityNeeded(f, this.massOfFillerReq, this.selectedQuotationDescription.getQty());

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

    public double getFillerTotalCost() {
        if (selectedFillerForWeld != null) {
            int amountRequired = hiYewDSSSessionBean.quantityNeeded(selectedFillerForWeld, this.selectedQuotationDescription.getSurfaceVol(), selectedQuotationDescription.getQty());
            this.setWeldJobMaterialCost(amountRequired * selectedFillerForWeld.getCost());
            return amountRequired * selectedFillerForWeld.getCost();

        } else {
            System.out.println("selected filler is null");
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

    public void getWeldJobEstimatedDuration() {
        this.setWeldJobAvgDuration(hiYewDSSSessionBean.deriveAverageDaysNeededForWeldJob(similarWeldJobs, this.getSelectedQuotationDescription().getSurfaceVol(), this.getSelectedQuotationDescription().getQty()));
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
        if (days >= 0) {
            //check for any project slack which can accomodate the new project duration
            Project p1 = projectSessionBean.getProjectDurationWithSlack(days);

            if (p1 != null) {
                //compare and get earliest between proj slack and earliest proj completion date
                if (p2 != null) {
                    planStart = getEarliestTimeStamp(p1.getActualEnd(), p2.getPlannedEnd());
                    System.out.println("A");
                } else {
                    planStart = p1.getActualEnd();
                    System.out.println("B");
                }
            } else {
                //if there is no project slack equals new proj dur, then check and take the earliest project expected completion date

                if (p2 != null) {
                    planStart = p2.getPlannedEnd();
                    System.out.println("C");
                }
            }
            //assign plannedEnd
            if (planStart != null) {
                System.out.println("D");
                System.out.println("Plan Start+++++++++++++++" + planStart);
                return projectSessionBean.addDays(planStart, days);
            } else { // in the case when there is no running proj
                System.out.println("getPlannedEnd() = no running project");
                Timestamp today = new Timestamp(new Date().getTime());
                planStart = today;
                System.out.println("E");
                return projectSessionBean.addDays(planStart, days);
            }
        } else // if days < 0 means no proj reference
        {
            System.out.println("F");
            return null;
        }

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

    public void deriveManpowerCost(int days) {
        if (days >= 0) {
            this.setWeldJobManpowerCost(hiYewDSSSessionBean.getAvgManpowerCostPerDay() * days);
        } else {
            this.setWeldJobManpowerCost(0);
        }
        // return hiYewDSSSessionBean.getAvgManpowerCostPerDay() * days;
    }

    /**
     * @return the weldJobMaterialCost
     */
    public double getWeldJobMaterialCost() {
        return weldJobMaterialCost;
    }

    /**
     * @param weldJobMaterialCost the weldJobMaterialCost to set
     */
    public void setWeldJobMaterialCost(double weldJobMaterialCost) {
        this.weldJobMaterialCost = weldJobMaterialCost;
    }

    /**
     * @return the weldJobManpowerCost
     */
    public double getWeldJobManpowerCost() {
        return weldJobManpowerCost;
    }

    /**
     * @param weldJobManpowerCost the weldJobManpowerCost to set
     */
    public void setWeldJobManpowerCost(double weldJobManpowerCost) {
        this.weldJobManpowerCost = weldJobManpowerCost;
    }

    /**
     * @return the profitMargin
     */
    public int getProfitMargin() {
        return profitMargin;
    }

    /**
     * @param profitMargin the profitMargin to set
     */
    public void setProfitMargin(int profitMargin) {
        this.profitMargin = profitMargin;
    }

    /**
     * @return the weldJobPriceToQuote
     */
    public double getWeldJobPriceToQuote() {
        return weldJobPriceToQuote;
    }

    /**
     * @param weldJobPriceToQuote the weldJobPriceToQuote to set
     */
    public void setWeldJobPriceToQuote(double weldJobPriceToQuote) {
        this.weldJobPriceToQuote = weldJobPriceToQuote;
    }

    /**
     * @return the weldJobPriceToQuotePerQty
     */
    public double getWeldJobPriceToQuotePerQty() {
        return weldJobPriceToQuotePerQty;
    }

    /**
     * @param weldJobPriceToQuotePerQty the weldJobPriceToQuotePerQty to set
     */
    public void setWeldJobPriceToQuotePerQty(double weldJobPriceToQuotePerQty) {
        this.weldJobPriceToQuotePerQty = weldJobPriceToQuotePerQty;
    }

    public void handleKeyEvent() {
        System.out.println("Change!profit Margin:" + this.profitMargin);
        deriveWeldJobTotalPrice();
        pricePerUnit();
        System.out.println("Change!weldjobTotalPrice():" + this.weldJobPriceToQuote);
        System.out.println("Change!pricePerUnit():" + this.weldJobPriceToQuotePerQty);
    }

    public void deriveWeldJobTotalPrice() {
        System.out.println("deriveWeldJobTotalPrice() weldJobMaterialCost" + weldJobMaterialCost);
        System.out.println("deriveWeldJobTotalPrice() weldJobManpowerCost" + weldJobManpowerCost);
        if (weldJobMaterialCost >= 0) {
//            System.out.println("deriveWeldJobTotalPrice()!profitMargin:" + this.profitMargin);
//            System.out.println("deriveWeldJobTotalPrice()!weldJobMaterialCost:" + this.weldJobMaterialCost);
//            System.out.println("deriveWeldJobTotalPrice()!weldJobManpowerCost:" + this.weldJobManpowerCost);
            double baseCost = this.weldJobMaterialCost + this.weldJobManpowerCost;
            double marginCost = baseCost * (this.profitMargin / 100.0);
            double totalPrice = baseCost + marginCost;
//            System.out.println("Base Cost: " + baseCost);
//            System.out.println("Margin Cost: " + marginCost);
//            System.out.println("Number to set in weldJobPriceToQuote: " + totalPrice);
//            System.out.println("Number to set in weldJobPriceToQuote: " + totalPrice);
            this.setWeldJobPriceToQuote(totalPrice);

        } else {
            double baseCost = this.weldJobMaterialCost + 1 + this.weldJobManpowerCost;
            double marginCost = baseCost * (this.profitMargin / 100.0);
            double totalPrice = baseCost + marginCost;
            this.setWeldJobPriceToQuote(totalPrice);
        }
    }

    public void pricePerUnit() {
        if ((this.selectedQuotationDescription != null)) {
            this.setWeldJobPriceToQuotePerQty(round(this.weldJobPriceToQuote / this.selectedQuotationDescription.getQty(), 2));
            //   return this.weldJobPriceToQuotePerQty;
        } else {
            this.setWeldJobPriceToQuotePerQty(0);
        }
    }

    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * @return the materialPlusManpowerCost
     */
    public double getMaterialPlusManpowerCost() {
        return materialPlusManpowerCost;
    }

    /**
     * @param materialPlusManpowerCost the materialPlusManpowerCost to set
     */
    public void setMaterialPlusManpowerCost(double materialPlusManpowerCost) {
        this.materialPlusManpowerCost = materialPlusManpowerCost;
    }

    public void clickUseForCalculation() {
        System.out.println("CLICK USE FOR CALCULATION");
        getFillerTotalCost();
        deriveWeldJobTotalPrice();
        pricePerUnit();
    }

    public void clickTransfer() {

        if (matchedFillers.size() > 0) {
            if ((matchedFillers.size() > 0) && (similarWeldJobs.size() > 0)) {
                this.selectedQuotationDescription.setPrice(this.weldJobPriceToQuotePerQty);
                if (getProjectDaysMap().get(this.getSelectedQuotation()) != null) {
                    int duration = (int) getProjectDaysMap().get(this.getSelectedQuotation());
                    getProjectDaysMap().put(this.getSelectedQuotation(), duration + this.weldJobAvgDuration);
                } else {
                    getProjectDaysMap().put(this.getSelectedQuotation(), this.weldJobAvgDuration);
                }
                if (getProjectNumOfATPResult().get(this.getSelectedQuotation()) == null) {
                    numOfTimesConductedATP++;
                    getProjectNumOfATPResult().put(this.getSelectedQuotation(), numOfTimesConductedATP);
                } else {
                    int prevNum = (int) getProjectNumOfATPResult().get(this.getSelectedQuotation());
                    prevNum++;
                    getProjectNumOfATPResult().put(this.getSelectedQuotation(), prevNum);
                }

                setCompanyEarliestDate();
            }

            this.selectedQuotationDescription.setRequestForMetalSample("No");

        } else {
            this.selectedQuotationDescription.setRequestForMetalSample("Yes");
        }

    }

    public void setCompanyEarliestDate() {
        for (int i = 0; i < this.receivedCustomerNewQuotations.size(); i++) {

            System.out.println("setCompanyEarliestDate() quotaion: " + this.receivedCustomerNewQuotations.get(i).getQuotationNo());
            // Get a set of the entries
            Date planEndDate = new Date();
            int numOfAtp = 0;
            Set set = projectDaysMap.entrySet();
            Set set2 = projectNumOfATPResult.entrySet();
            // Get an iterator
            Iterator it = set.iterator();
            Iterator it2 = set2.iterator();
            // Display elements
            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                if (me.getKey() == (this.receivedCustomerNewQuotations.get(i))) {
                    int duration = (int) me.getValue();
                    System.out.println("setCompanyEarliestDate() days: " + duration);
                    planEndDate = this.getPlannedEnd(duration);
                }
            }

            while (it2.hasNext()) {
                Map.Entry me = (Map.Entry) it2.next();
                if (me.getKey() == (this.receivedCustomerNewQuotations.get(i))) {
                    numOfAtp = (int) me.getValue();

                }
            }
            System.out.println("setCompanyEarliestDate() planEndDate: " + planEndDate);
            System.out.println("setCompanyEarliestDate() numofatp: " + numOfAtp);

            Timestamp planEndTimeStamp = new Timestamp(planEndDate.getTime());
            //earliest date company can deliver is later than clients
            if ((planEndTimeStamp != null) && (this.receivedCustomerNewQuotations.get(i).getCustomerLatestEnd() != null)) {
                if (this.getEarliestTimeStamp(planEndTimeStamp, this.receivedCustomerNewQuotations.get(i).getCustomerLatestEnd()) == this.receivedCustomerNewQuotations.get(i).getCustomerLatestEnd()) {
                    if (numOfAtp == this.receivedCustomerNewQuotations.get(i).getQuotationDescriptions().size()) {
                        this.receivedCustomerNewQuotations.get(i).setCompanyEarliestEnd(planEndTimeStamp);

                    }
                }
            } else if (planEndTimeStamp != null) {
                if (numOfAtp == this.receivedCustomerNewQuotations.get(i).getQuotationDescriptions().size()) {
                    this.receivedCustomerNewQuotations.get(i).setCompanyEarliestEnd(planEndTimeStamp);
                }
            }
            quotationSessionBean.conductMerge(this.receivedCustomerNewQuotations.get(i));
        }
    }

    /**
     * @return the projectDaysMap
     */
    public HashMap getProjectDaysMap() {
        return projectDaysMap;
    }

    /**
     * @param projectDaysMap the projectDaysMap to set
     */
    public void setProjectDaysMap(HashMap projectDaysMap) {
        this.projectDaysMap = projectDaysMap;
    }

    /**
     * @return the numOfTimesConductedATP
     */
    public int getNumOfTimesConductedATP() {
        return numOfTimesConductedATP;
    }

    /**
     * @param numOfTimesConductedATP the numOfTimesConductedATP to set
     */
    public void setNumOfTimesConductedATP(int numOfTimesConductedATP) {
        this.numOfTimesConductedATP = numOfTimesConductedATP;
    }

    /**
     * @return the projectNumOfATPResult
     */
    public HashMap getProjectNumOfATPResult() {
        return projectNumOfATPResult;
    }

    /**
     * @param projectNumOfATPResult the projectNumOfATPResult to set
     */
    public void setProjectNumOfATPResult(HashMap projectNumOfATPResult) {
        this.projectNumOfATPResult = projectNumOfATPResult;
    }

    /**
     * @return the recommendedFiller
     */
    public FillerEntity getRecommendedFiller() {
        return recommendedFiller;
    }

    /**
     * @param recommendedFiller the recommendedFiller to set
     */
    public void setRecommendedFiller(FillerEntity recommendedFiller) {
        this.recommendedFiller = recommendedFiller;
    }

    /**
     * @return the selectedQuotation
     */
    public Quotation getSelectedQuotation() {
        return selectedQuotation;
    }

    /**
     * @param selectedQuotation the selectedQuotation to set
     */
    public void setSelectedQuotation(Quotation selectedQuotation) {
        this.selectedQuotation = selectedQuotation;
    }

    public boolean checkPending2() {
        System.out.println(status);
        if (status == null) {
            return false;
        }
        if (status.equals("Pending")) {
            return true;
        }
        return false;
    }

    public boolean checkPending() {
        System.out.println(status);
        if (status == null) {
            return false;
        }
        if (status.equals("Processed")) {
            return true;
        }
        return false;
    }

    public boolean checkAccepted() {
        if (selectedQuotation.getStatus().equals("Accepted") || selectedQuotation.getStatus().equals("Rejected")) {
            return true;
        }
        return false;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the oldVolume
     */
    public double getOldVolume() {
        return oldVolume;
    }

    /**
     * @param oldVolume the oldVolume to set
     */
    public void setOldVolume(double oldVolume) {
        this.oldVolume = oldVolume;
    }

    /**
     * @return the newVolume
     */
    public double getNewVolume() {
        return newVolume;
    }

    /**
     * @param newVolume the newVolume to set
     */
    public void setNewVolume(double newVolume) {
        this.newVolume = newVolume;
    }

    /**
     * @return the volumeNeeded
     */
    public double getVolumeNeeded() {
        return volumeNeeded;
    }

    /**
     * @param volumeNeeded the volumeNeeded to set
     */
    public void setVolumeNeeded(double volumeNeeded) {
        this.volumeNeeded = volumeNeeded;
    }

    /**
     * @return the massOfFillerReq
     */
    public double getMassOfFillerReq() {
        return massOfFillerReq;
    }

    /**
     * @param massOfFillerReq the massOfFillerReq to set
     */
    public void setMassOfFillerReq(double massOfFillerReq) {
        this.massOfFillerReq = massOfFillerReq;
    }

    /**
     * @return the densityOfSteel
     */
    public double getDensityOfSteel() {
        return densityOfSteel;
    }

    /**
     * @param densityOfSteel the densityOfSteel to set
     */
    public void setDensityOfSteel(double densityOfSteel) {
        this.densityOfSteel = densityOfSteel;
    }

}
