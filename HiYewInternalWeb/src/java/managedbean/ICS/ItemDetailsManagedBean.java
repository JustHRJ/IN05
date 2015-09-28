/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.ICS;

import entity.ItemEntity;
import entity.StorageInfoEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import session.stateless.HiYewICSSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "itemDetailsManagedBean")
@ViewScoped
public class ItemDetailsManagedBean implements Serializable {

    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;

    private ItemEntity selectedItem;
    private String activityChoice;
    private int quantityIn;
    private int quantityOut;
    
    private boolean showInPanel;
    private boolean showOutPanel;
    private boolean showInOuttPanel;
    private double inCost;
    private double totalWeight;
    
    private List<StorageInfoEntity> infoList;
    private List<StorageInfoEntity> filteredInfoList;
    

    /**
     * Creates a new instance of ItemDetailsManagedBean
     */
    public ItemDetailsManagedBean() {
        infoList = new ArrayList<>();
        showInOuttPanel =true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedItem") != null) {
            selectedItem = (ItemEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedItem");
        } else {
            selectedItem = new ItemEntity();
        }

    }
    
    @PostConstruct
    public void init(){
        if(selectedItem!=null){
           infoList = hiYewICSSessionBean.getAllStorageInfoOfItem(selectedItem);
        }
    }

    /**
     * @return the selectedItem
     */
    public ItemEntity getSelectedItem() {
        return selectedItem;
    }

    /**
     * @param selectedItem the selectedItem to set
     */
    public void setSelectedItem(ItemEntity selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void updateItemDetails() {
        if (selectedItem.getItemCode() != null) {
            hiYewICSSessionBean.updateItemDetails(selectedItem);
            FacesContext.getCurrentInstance().addMessage("upperMessages", new FacesMessage("Item " +selectedItem.getItemCode() +"'s details updated successfully!"));
            System.out.println("here4");
        }
    }

    /**
     * @return the activityChoice
     */
    public String getActivityChoice() {
        return activityChoice;
    }

    /**
     * @param activityChoice the activityChoice to set
     */
    public void setActivityChoice(String activityChoice) {
        this.activityChoice = activityChoice;
    }

    /**
     * @return the quantityInOut
     */

    private static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}
    
    public void updateStock(){
        System.out.println("here4444444444444444444444444");
        System.out.println("Selected Item Code: " + selectedItem.getItemCode());
        System.out.println("Activity Choice: " + activityChoice);
      
        if(activityChoice.equalsIgnoreCase("stockIn")){
           System.out.println("Qty in: " + quantityIn);
           double totalNewCost = ((selectedItem.getCost()*selectedItem.getQuantity())+(round(inCost,2)*quantityIn));
           System.out.println("Total New Cost: " + totalNewCost);
           int totalNewQty = selectedItem.getQuantity() + quantityIn;
           double newAvgCost = round((totalNewCost/totalNewQty),2);
           selectedItem.setQuantity(totalNewQty);
           selectedItem.setCost(newAvgCost);
           System.out.println("New Total Qty: " +totalNewQty);
           System.out.println("New Avg Cost: " +newAvgCost);
           hiYewICSSessionBean.stockUp(selectedItem, quantityIn);
           System.out.println("Stock up here");
           hiYewICSSessionBean.updateCost(selectedItem, newAvgCost);
           System.out.println("Update Cost here");
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item " +selectedItem.getItemCode() +"'s quantity updated successfully!"));
             
        }else if (activityChoice.equalsIgnoreCase("stockOut")){
            if(quantityOut<=selectedItem.getQuantity()){
            System.out.println("Qty out: " + quantityOut);
            double totalWeight = quantityOut * selectedItem.getAverageWeight();
            selectedItem.setQuantity(selectedItem.getQuantity() - quantityOut);
            hiYewICSSessionBean.stockDown(selectedItem, quantityOut);
            showMessage(totalWeight);
            }else{
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Input", "Insufficient Stock!"));
            }
        }
         
    }
    
    public void showMessage(double totalWeight) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Additional Info", "The total weight for " + quantityOut + " of ItemCode:" + selectedItem.getItemCode() +" is " + totalWeight + " grams.");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
    
    public String deleteItem(){
         System.out.println("here555555555555555");
        System.out.println("Selected Item Code: " + selectedItem.getItemCode());
        if(selectedItem.getItemCode().length()>0){
        hiYewICSSessionBean.deleteItem(selectedItem);
         FacesContext.getCurrentInstance().addMessage("upperMessages", new FacesMessage("Item " +selectedItem.getItemCode() +"'s quantity deleted successfully!"));
         return "viewInventory?faces-redirect=true"; 
        }else{
             FacesContext.getCurrentInstance().addMessage("upperMessages", new FacesMessage("Unable to delete!"));
             return "";
        }
    }

    /**
     * @return the showInPanel
     */
    public boolean isShowInPanel() {
        return showInPanel;
    }

    /**
     * @param showInPanel the showInPanel to set
     */
    public void setShowInPanel(boolean showInPanel) {
        this.showInPanel = showInPanel;
    }

    /**
     * @return the showOutPanel
     */
    public boolean isShowOutPanel() {
        return showOutPanel;
    }

    /**
     * @param showOutPanel the showOutPanel to set
     */
    public void setShowOutPanel(boolean showOutPanel) {
        this.showOutPanel = showOutPanel;
    }

    /**
     * @return the inCost
     */
    public double getInCost() {
        return inCost;
    }

    /**
     * @param inCost the inCost to set
     */
    public void setInCost(double inCost) {
        this.inCost = inCost;
    }

    /**
     * @return the totalWeight
     */
    public double getTotalWeight() {
        return totalWeight;
    }

    /**
     * @param totalWeight the totalWeight to set
     */
    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
    
    public void clickEventHandler(){
        showInOuttPanel =false;
        if(activityChoice.equalsIgnoreCase("stockIn")){
            showInPanel = true;
            showOutPanel = false;
            
             
        }else if (activityChoice.equalsIgnoreCase("stockOut")){
            showInPanel = false;
            showOutPanel = true;
        }
    }

    /**
     * @return the showInOuttPanel
     */
    public boolean isShowInOuttPanel() {
        return showInOuttPanel;
    }

    /**
     * @param showInOuttPanel the showInOuttPanel to set
     */
    public void setShowInOuttPanel(boolean showInOuttPanel) {
        this.showInOuttPanel = showInOuttPanel;
    }

    /**
     * @return the quantityIn
     */
    public int getQuantityIn() {
        return quantityIn;
    }

    /**
     * @param quantityIn the quantityIn to set
     */
    public void setQuantityIn(int quantityIn) {
        this.quantityIn = quantityIn;
    }

    /**
     * @return the quantityOut
     */
    public int getQuantityOut() {
        return quantityOut;
    }

    /**
     * @param quantityOut the quantityOut to set
     */
    public void setQuantityOut(int quantityOut) {
        this.quantityOut = quantityOut;
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

}
