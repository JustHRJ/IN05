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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.ProcurementSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "viewPBManagedBean")
@ViewScoped
public class ViewPBManagedBean implements Serializable{
    @EJB
    private ProcurementSessionBeanLocal procurementSessionBean;
    private List<ProcurementBidEntity> pbList;
    private List<ProcurementBidEntity> filteredPBs;
    private ProcurementBidEntity selectedPB;
    /**
     * Creates a new instance of ViewPBManagedBean
     */
    public ViewPBManagedBean() {
         pbList = new ArrayList<>();
        selectedPB = new ProcurementBidEntity();
    }
    @PostConstruct
    public void init() {
        pbList = procurementSessionBean.getAllBids();
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
    
    public String formatDate(Timestamp date) {
        if (date != null) {
            Date date1 = new Date(date.getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(date1);
        } else {
            return "";
        }
    }
    
}
