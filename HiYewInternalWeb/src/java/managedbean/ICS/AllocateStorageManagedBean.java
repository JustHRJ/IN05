/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.ICS;

import entity.FillerEntity;
import entity.ShelveEntity;
import entity.StorageInfoEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.HiYewICSSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@ManagedBean
@Named(value = "allocateStorageManagedBean")
@ViewScoped
public class AllocateStorageManagedBean implements Serializable {

    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;

    private String fillerCode;
    private int allocationQty;

    private int removalQty;

    private ShelveEntity selectedShelve;
    private List<StorageInfoEntity> infoList;
    private List<StorageInfoEntity> filteredInfoList;

    private FillerEntity selectedItem;
    private StorageInfoEntity selectedSI;

    /**
     * Creates a new instance of AllocateStorageManagedBean
     */
    public AllocateStorageManagedBean() {
        System.out.println("opening allocation MB");
        infoList = new ArrayList<>();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedShelve") != null) {
            selectedShelve = (ShelveEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedShelve");
        } else {
            selectedShelve = new ShelveEntity();
        }

    }

    @PostConstruct
    public void init() {
        infoList = hiYewICSSessionBean.getAllStorageInfoOfShelve(selectedShelve);

    }

    /**
     * @return the fillerCode
     */
    public String getFillerCode() {
        return fillerCode;
    }

    /**
     * @param fillerCode the fillerCode to set
     */
    public void setFillerCode(String fillerCode) {
        this.fillerCode = fillerCode;
    }

    /**
     * @return the allocationQty
     */
    public int getAllocationQty() {
        return allocationQty;
    }

    /**
     * @param allocationQty the allocationQty to set
     */
    public void setAllocationQty(int allocationQty) {
        this.allocationQty = allocationQty;
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

    public void addItemToShelve() {

        if (hiYewICSSessionBean.getExistingItem(fillerCode) == null) {
            System.out.println("Non exist itemcode");
            FacesContext.getCurrentInstance().addMessage("formMain:itemCode", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Such Item", "No such item in inventory"));
        } else {
            System.out.println("Existing itemcode");
            FillerEntity item = hiYewICSSessionBean.getExistingItem(fillerCode);
            if (hiYewICSSessionBean.getShelveFreeSpace(selectedShelve) < (hiYewICSSessionBean.getWireVolume(item) * allocationQty)) {
                FacesContext.getCurrentInstance().addMessage("formMain:quantity", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to allocate", "Insufficient space available!"));
            } else {

                if (allocationQty > hiYewICSSessionBean.getUnallocatedQty(item)) {
                    System.out.println("Qty Overshot");
                    FacesContext.getCurrentInstance().addMessage("formMain:quantity", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Input", "Quantity to allocate is greater than amount available for allocation!"));
                } else {
                    System.out.println("Added Ok");
                    hiYewICSSessionBean.addStorageInfo(item, selectedShelve, allocationQty);
                    infoList = hiYewICSSessionBean.getAllStorageInfoOfShelve(selectedShelve);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success, Items added to shelve", "Items added to shelve"));
                }
            }
        }
    }

    /**
     * @return the infoList
     */
    public List<StorageInfoEntity> getInfoList() {
        return infoList;
    }

    /**
     * @param infoList the infoList to set
     */
    public void setInfoList(List<StorageInfoEntity> infoList) {
        this.infoList = infoList;
    }

    /**
     * @return the filteredInfoList
     */
    public List<StorageInfoEntity> getFilteredInfoList() {
        return filteredInfoList;
    }

    /**
     * @param filteredInfoList the filteredInfoList to set
     */
    public void setFilteredInfoList(List<StorageInfoEntity> filteredInfoList) {
        this.filteredInfoList = filteredInfoList;
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
     * @return the removalQty
     */
    public int getRemovalQty() {
        return removalQty;
    }

    /**
     * @param removalQty the removalQty to set
     */
    public void setRemovalQty(int removalQty) {
        this.removalQty = removalQty;
    }

    public void openDialog() {
        System.out.println("here at openDialog" + selectedSI.getItem().getFillerCode() + " " + selectedShelve.getShelveID());

    }

    public String removeItemFromShelve() {
        System.out.println("here at openDialog " + selectedSI.getItem().getFillerCode() + " " + selectedShelve.getShelveID());
        selectedItem = selectedSI.getItem();
        System.out.println("here at removeItemFromShelve" + selectedItem.getFillerCode() + " " + selectedShelve.getShelveID());
        StorageInfoEntity sie = hiYewICSSessionBean.getStorageInfo(selectedItem, selectedShelve);
        System.out.println("here at openDialog " + sie.getStoredQty());
        if (removalQty > sie.getStoredQty()) {
            System.out.println("removalQty higher than stored qty!");
            FacesContext.getCurrentInstance().addMessage("dialogForm:rQty", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to process request", "Removal Quatity higher than Stored Quantity"));
            return "";
        } else {
            hiYewICSSessionBean.reduceStorageQty(selectedItem, selectedShelve, removalQty);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Items removed from shelve"));
            return "ics-allocate-storage?faces-redirect=true";
        }
    }

    /**
     * @return the selectedSI
     */
    public StorageInfoEntity getSelectedSI() {
        return selectedSI;
    }

    /**
     * @param selectedSI the selectedSI to set
     */
    public void setSelectedSI(StorageInfoEntity selectedSI) {
        this.selectedSI = selectedSI;
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        results = hiYewICSSessionBean.getFillerCodeAutoComplete(query);
        return results;
    }
}
