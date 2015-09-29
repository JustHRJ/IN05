/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.ICS;

import entity.RackEntity;
import entity.ShelveEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;
import session.stateless.HiYewICSSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "viewRacksManagedBean")
@ViewScoped
public class ViewRacksManagedBean implements Serializable {

    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;

    private List<RackEntity> racks;
    private List<RackEntity> filteredRacks;
    private RackEntity selectedRack;
    private List<ShelveEntity> shelvesInsideSelectRack;
    private List<RackEntity> filteredShelves;
    private ShelveEntity selectedShelve;

    private boolean openShelvePanel;

    /**
     * Creates a new instance of ViewRacksManagedBean
     */
    public ViewRacksManagedBean() {
        racks = new ArrayList<RackEntity>();
        shelvesInsideSelectRack = new ArrayList<ShelveEntity>();
    }

    @PostConstruct
    public void init() {
        racks.addAll(hiYewICSSessionBean.getAllRacks());
        openShelvePanel = true;
    }

    /**
     * @return the racks
     */
    public List<RackEntity> getRacks() {
        return racks;
    }

    /**
     * @param racks the racks to set
     */
    public void setRacks(List<RackEntity> racks) {
        this.racks = racks;
    }

    /**
     * @return the filteredRacks
     */
    public List<RackEntity> getFilteredRacks() {
        return filteredRacks;
    }

    /**
     * @param filteredRacks the filteredRacks to set
     */
    public void setFilteredRacks(List<RackEntity> filteredRacks) {
        this.filteredRacks = filteredRacks;
    }

    /**
     * @return the selectedRack
     */
    public RackEntity getSelectedRack() {
        return selectedRack;
    }

    /**
     * @param selectedRack the selectedRack to set
     */
    public void setSelectedRack(RackEntity selectedRack) {
        this.selectedRack = selectedRack;
    }

    public void passSelectedRackToNext() {
        System.out.println(this.selectedRack.getRackID());
        openShelvePanel = false;
        getShelvesInsideSelectRack().clear();
        getShelvesInsideSelectRack().addAll(hiYewICSSessionBean.getShelvesInRack(this.selectedRack.getRackID()));

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SelectedRack", this.selectedRack);
        //  FacesContext.getCurrentInstance().getExternalContext().getFlash().put("SelectedItem", this.selectedItem);
        System.out.println("here at view racks managed bean");
        //       return "rackDetails?faces-redirect=true";
    }

    /**
     * @return the shelvesInsideSelectRack
     */
    public List<ShelveEntity> getShelvesInsideSelectRack() {
        return shelvesInsideSelectRack;
    }

    /**
     * @param shelvesInsideSelectRack the shelvesInsideSelectRack to set
     */
    public void setShelvesInsideSelectRack(List<ShelveEntity> shelvesInsideSelectRack) {
        this.shelvesInsideSelectRack = shelvesInsideSelectRack;
    }

    /**
     * @return the filteredShelves
     */
    public List<RackEntity> getFilteredShelves() {
        return filteredShelves;
    }

    /**
     * @param filteredShelves the filteredShelves to set
     */
    public void setFilteredShelves(List<RackEntity> filteredShelves) {
        this.filteredShelves = filteredShelves;
    }

    /**
     * @return the openShelvePanel
     */
    public boolean isOpenShelvePanel() {
        return openShelvePanel;
    }

    /**
     * @param openShelvePanel the openShelvePanel to set
     */
    public void setOpenShelvePanel(boolean openShelvePanel) {
        this.openShelvePanel = openShelvePanel;
    }

    public String passSelectedShelveToNext() {
        System.out.println(selectedShelve.getShelveID());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SelectedShelve", this.selectedShelve);
        //  FacesContext.getCurrentInstance().getExternalContext().getFlash().put("SelectedItem", this.selectedItem);
        System.out.println("here at passSelectedShelveToNext()");
        return "allocateStorage?faces-redirect=true";
    }

    /**
     * @return the selectedShelve
     */
    public ShelveEntity getSelectedShelve() {
        return selectedShelve;
    }

    /**
     * @param selectedShelve the selectedShelve to set
     */
    public void setSelectedShelve(ShelveEntity selectedShelve) {
        this.selectedShelve = selectedShelve;
    }

    public void onRowEdit(RowEditEvent event) {
        hiYewICSSessionBean.updateRackStatus(((RackEntity) event.getObject()), ((RackEntity) event.getObject()).getStatus());
        FacesMessage msg = new FacesMessage("Rack" + ((RackEntity) event.getObject()).getRackID() + " Edited", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Rack" + ((RackEntity) event.getObject()).getRackID() + " Cancelled", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
