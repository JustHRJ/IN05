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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.ProcurementSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "pbDetailsManagedBean")
@ViewScoped
public class PbDetailsManagedBean implements Serializable{
    @EJB
    private ProcurementSessionBeanLocal procurementSessionBean;

    private List<ProcurementBidEntity> pbList;
    private List<ProcurementBidEntity> filteredPBs;
    private List<ProcurementBidEntity> quotedList;
    private List<ProcurementBidEntity> pendingList;
    private ProcurementBidEntity selectedPB;
    private int selectedBidRefNum;
    private boolean disableAcceptButton;
    private double cheapestBid;
    
    /**
     * Creates a new instance of PbDetailsManagedBean
     */
    public PbDetailsManagedBean() {
         pbList = new ArrayList<>();
         quotedList = new ArrayList<>();
         pendingList = new ArrayList<>();
         if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedBidBatch") != null) {
            selectedBidRefNum = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedBidBatch");
        } else {
            selectedBidRefNum = 0;
        }
         disableAcceptButton = false;
    }
    
    @PostConstruct
    public void init() {
        pbList = procurementSessionBean.getBidsOfBidRef(selectedBidRefNum);
        if((pendingList.size()>0)||(quotedList.size()>0)){
            pendingList.clear();
            quotedList.clear();
        }
        for(int i =0;i<pbList.size();i++){
            if(pbList.get(i).getIfAccept().equals("Pending")){
               pendingList.add(pbList.get(i));
            }else{
                quotedList.add(pbList.get(i));
            }
        }
        setCheapestBid(999999999);
        for(int i=0;i<quotedList.size();i++){ 
            if(quotedList.get(i).getQuotedPrice()<getCheapestBid()){
                setCheapestBid(quotedList.get(i).getQuotedPrice());
            }
        }
        
        if((pbList.get(0).getBidEnd().before(getToday()))&&(pbList.get(0).getStatus().equals("Closed"))){
            disableAcceptButton = false;
        }else{
            disableAcceptButton = true;
        }
    }

    /**
     * @return the pbList
     */
    public List<ProcurementBidEntity> getPbList() {
        return pbList;
    }

    /**
     * @param pbList the pbList to set
     */
    public void setPbList(List<ProcurementBidEntity> pbList) {
        this.pbList = pbList;
    }

    /**
     * @return the filteredPBs
     */
    public List<ProcurementBidEntity> getFilteredPBs() {
        return filteredPBs;
    }

    /**
     * @param filteredPBs the filteredPBs to set
     */
    public void setFilteredPBs(List<ProcurementBidEntity> filteredPBs) {
        this.filteredPBs = filteredPBs;
    }

    /**
     * @return the selectedPB
     */
    public ProcurementBidEntity getSelectedPB() {
        return selectedPB;
    }

    /**
     * @param selectedPB the selectedPB to set
     */
    public void setSelectedPB(ProcurementBidEntity selectedPB) {
        this.selectedPB = selectedPB;
    }

    /**
     * @return the selectedBidRefNum
     */
    public int getSelectedBidRefNum() {
        return selectedBidRefNum;
    }

    /**
     * @param selectedBidRefNum the selectedBidRefNum to set
     */
    public void setSelectedBidRefNum(int selectedBidRefNum) {
        this.selectedBidRefNum = selectedBidRefNum;
    }
    
    public String formatDate(Timestamp date) {
        if (date != null) {
            Date date1 = new Date(date.getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(date1);
        } else {
            return "";
        }
    }
    
    public void acceptPB(ProcurementBidEntity pb){
        System.out.println(pb.getId());
        if(getToday().before(pb.getBidEnd())){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You are unable to accpet a bid before the session ends!"));
        }else{
            for(int i =0;i<pbList.size();i++){
                ProcurementBidEntity pbInList = pbList.get(i);
                if(pbInList.equals(pb)){
                    procurementSessionBean.updateAcceptStatus(pbInList, "Accepted");
                }
                else{
                    procurementSessionBean.updateAcceptStatus(pbInList, "Rejected");
                }
                procurementSessionBean.updatePBStatusByBatch(selectedBidRefNum, "Completed");
                
            }
            pbList = procurementSessionBean.getBidsOfBidRef(selectedBidRefNum);
            if((pendingList.size()>0)||(quotedList.size()>0)){
            pendingList.clear();
            quotedList.clear();
        }
        for(int i =0;i<pbList.size();i++){
            if(pbList.get(i).getIfAccept().equals("Pending")){
               pendingList.add(pbList.get(i));
            }else{
                quotedList.add(pbList.get(i));
            }
        }
            setDisableAcceptButton(true);
        }
        
        
    }
    
     public Date getToday() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    /**
     * @return the disableAcceptButton
     */
    public boolean isDisableAcceptButton() {
        return disableAcceptButton;
    }

    /**
     * @param disableAcceptButton the disableAcceptButton to set
     */
    public void setDisableAcceptButton(boolean disableAcceptButton) {
        this.disableAcceptButton = disableAcceptButton;
    }

    /**
     * @return the quotedList
     */
    public List<ProcurementBidEntity> getQuotedList() {
        return quotedList;
    }

    /**
     * @param quotedList the quotedList to set
     */
    public void setQuotedList(List<ProcurementBidEntity> quotedList) {
        this.quotedList = quotedList;
    }

    /**
     * @return the pendingList
     */
    public List<ProcurementBidEntity> getPendingList() {
        return pendingList;
    }

    /**
     * @param pendingList the pendingList to set
     */
    public void setPendingList(List<ProcurementBidEntity> pendingList) {
        this.pendingList = pendingList;
    }

    /**
     * @return the cheapestBid
     */
    public double getCheapestBid() {
        return cheapestBid;
    }

    /**
     * @param cheapestBid the cheapestBid to set
     */
    public void setCheapestBid(double cheapestBid) {
        this.cheapestBid = cheapestBid;
    }
}

    /**
     * @return the buttonText
     */
    
