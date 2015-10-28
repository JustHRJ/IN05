/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.PS;

import entity.FillerEntity;
import entity.ProcurementBidEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import session.stateless.ProcurementSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "createBiddingManagedBean")
@ViewScoped
public class CreateBiddingManagedBean implements Serializable {

    @EJB
    private ProcurementSessionBeanLocal procurementSessionBean;

    private ProcurementBidEntity newPB;
    private FillerEntity selectedItem;
    private List<String> supplierList;
    private List<String> selectedSuppliers;
    private Timestamp bidEndTimeStamp;
    private Timestamp byWhenTimeStamp;
    private Date byWhen;
    private Date bidEnd;
    private Date earliestByWhen;

    /**
     * Creates a new instance of CreateBiddingManagedBean
     */
    public CreateBiddingManagedBean() {
        newPB = new ProcurementBidEntity();
        supplierList = new ArrayList<String>();
        selectedSuppliers = new ArrayList<String>();
        earliestByWhen = new Date();
        bidEndTimeStamp = null;
        byWhenTimeStamp = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedItem2") != null) {
            selectedItem = (FillerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedItem2");

        } else {
            selectedItem = new FillerEntity();
        }
    }

    @PostConstruct
    public void init() {
        supplierList.addAll(procurementSessionBean.getAllSuppliers());
    }

    public void onDateSelect(SelectEvent event) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public void onDateSelect1(SelectEvent event) {
        //     SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd yyyy");
        String dateString = event.getObject().toString();
        String subDateStr = dateString.substring(0, 10) + " " + dateString.substring(dateString.length() - 4, dateString.length());
        System.out.println(subDateStr);
        try {
            earliestByWhen = formatter.parse(subDateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date setMinByWhenDate() {
        if (earliestByWhen == null) {
            return getTomorrow();
        } else {
            return getNextDay(earliestByWhen);
        }
    }

    public Date getToday() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public Date getNextDay(Date dt) {
        Calendar c = Calendar.getInstance();
        Date nextDay = dt;
        c.setTime(nextDay);
        c.add(Calendar.DATE, 1);
        nextDay = c.getTime();
        return nextDay;
    }

    public Date getTomorrow() {
        Calendar c = Calendar.getInstance();
        Date tml = getToday();
        c.setTime(tml);
        c.add(Calendar.DATE, 1);
        tml = c.getTime();
        return tml;
    }

    public String createPB() {
        if (bidEnd == null) {
            bidEndTimeStamp = null;
        } else {
            bidEndTimeStamp = new Timestamp(bidEnd.getTime());
        }

        if (byWhen == null) {
            byWhenTimeStamp = null;
        } else {
            byWhenTimeStamp = new Timestamp(byWhen.getTime());
        }
        Timestamp todayCreatedOn = new Timestamp(getToday().getTime());
        int nextBatchRefNum = procurementSessionBean.getNextBatchNo();
        for (int i = 0; i < selectedSuppliers.size(); i++) {
            newPB.setCompanyName(selectedSuppliers.get(i));
            newPB.setBidRefNum(nextBatchRefNum);
            newPB.setStatus("Open");
            newPB.setQuotedPrice(0.00);
            newPB.setCreatedOn(todayCreatedOn);
            newPB.setIfAccept("Pending");
            newPB.setItemCode(selectedItem.getFillerCode());
            newPB.setDiameter(selectedItem.getDiameter());
            newPB.setGrade(selectedItem.getWireGrade());
            newPB.setWireLength(selectedItem.getWireLength());
            newPB.setBidEnd(bidEndTimeStamp);
            newPB.setByWhen(byWhenTimeStamp);
            procurementSessionBean.createProcurementBid(newPB);
        }
     //   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Procurement Quotation Bidding Created and Sent Successfully!"));
        newPB = new ProcurementBidEntity();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Procurement Quotation Bid created successfully!"));
        //           return "viewInventory?faces-redirect=true";
        return "viewProcurementBid?faces-redirect=true";
    }

    /**
     * @return the newPB
     */
    public ProcurementBidEntity getNewPB() {
        return newPB;
    }

    /**
     * @param newPB the newPB to set
     */
    public void setNewPB(ProcurementBidEntity newPB) {
        this.newPB = newPB;
    }

    /**
     * @return the selectedItem
     */
    public FillerEntity getSelectedItem() {
        return selectedItem;
    }

    /**
     * @param selectedItem the selectedItem to set
     */
    public void setSelectedItem(FillerEntity selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * @return the supplierList
     */
    public List<String> getSupplierList() {
        return supplierList;
    }

    /**
     * @param supplierList the supplierList to set
     */
    public void setSupplierList(List<String> supplierList) {
        this.supplierList = supplierList;
    }

    /**
     * @return the selectedSuppliers
     */
    public List<String> getSelectedSuppliers() {
        return selectedSuppliers;
    }

    /**
     * @param selectedSuppliers the selectedSuppliers to set
     */
    public void setSelectedSuppliers(List<String> selectedSuppliers) {
        this.selectedSuppliers = selectedSuppliers;
    }

    /**
     * @return the byWhen
     */
    public Date getByWhen() {
        return byWhen;
    }

    /**
     * @param byWhen the byWhen to set
     */
    public void setByWhen(Date byWhen) {
        this.byWhen = byWhen;
    }

    /**
     * @return the bidEnd
     */
    public Date getBidEnd() {
        return bidEnd;
    }

    /**
     * @param bidEnd the bidEnd to set
     */
    public void setBidEnd(Date bidEnd) {
        this.bidEnd = bidEnd;
    }

    /**
     * @return the earliestByWhen
     */
    public Date getEarliestByWhen() {
        return earliestByWhen;
    }

    /**
     * @param earliestByWhen the earliestByWhen to set
     */
    public void setEarliestByWhen(Date earliestByWhen) {
        this.earliestByWhen = earliestByWhen;
    }

}
