package managedBean;

import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.ProductQuotationSessionBeanLocal;

@Named(value = "productQuotationManagedBean")
@SessionScoped
public class ProductQuotationManagedBean implements Serializable {

    @EJB
    private ProductQuotationSessionBeanLocal productQuotationSessionBean;
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private String username = "";
    private String date = "";
    private String productQuotationNo = "";
    private Integer count;

    private String selectedMachine = "";

    private ArrayList<ProductQuotationDescription> cacheList = new ArrayList<>();
    ;
    private ProductQuotation newProductQuotation;
    private ProductQuotationDescription newProductQuotationDescription;

    private ArrayList<ProductQuotation> filteredProductList;
    private ArrayList<ProductQuotation> filteredProductList2;
    
    private ArrayList<ProductQuotation> receivedProductQuotationList;
    private ArrayList<ProductQuotationDescription> displayProductQuotationDescriptionList;

    // compare
    private Integer selectedCount = 0;
    private Boolean selectedMachine1 = false;
    private Boolean selectedMachine2 = false;
    private Boolean selectedMachine3 = false;
    private Boolean selectedMachine4 = false;
    private Boolean selectedMachine5 = false;
    private Boolean selectedMachine6 = false;
    private Boolean selectedMachine7 = false;
    // compare end

    public ProductQuotationManagedBean() {
        System.out.println("ProductQuotationManagedBean.java ProductQuotationManagedBean()");
        newProductQuotation = new ProductQuotation();
        newProductQuotationDescription = new ProductQuotationDescription();
        receivedProductQuotationList = new ArrayList<>();
        displayProductQuotationDescriptionList = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        count = 1;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            System.out.println("ProductQuotationManagedBean.java: Username is " + getUsername());
        }
        date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

        receivedProductQuotationList = new ArrayList<>(productQuotationSessionBean.receivedProductQuotationList(username));
    }

    public Integer doCount() throws IOException {
        selectedCount = 0;
        if (selectedMachine1) {
            selectedCount = selectedCount + 1;
        }
        if (selectedMachine2) {
            selectedCount = selectedCount + 1;
        }
        if (selectedMachine3) {
            selectedCount = selectedCount + 1;
        }
        if (selectedMachine4) {
            selectedCount = selectedCount + 1;
        }
        if (selectedMachine5) {
            selectedCount = selectedCount + 1;
        }
        if (selectedMachine6) {
            selectedCount = selectedCount + 1;
        }
        if (selectedMachine7) {
            selectedCount = selectedCount + 1;
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("compareMsg", selectedCount.toString());
        FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-compare.xhtml");
        return selectedCount;
    }

    public void redirectToSelectedMachineCheck() {
        selectedMachine = "";
    }

    public void redirectToSelectedMachine() throws IOException {
        if (selectedMachine != null) {
            if (!selectedMachine.equals("")) {
                System.out.println("selectedMachine ===== " + selectedMachine);
                if (selectedMachine.equals("LWI Small Chamber")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-SmallChamber.xhtml");
                } else if (selectedMachine.equals("LWI V Flexx")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-Flexx.xhtml");
                } else if (selectedMachine.equals("LWI V ERGO")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-ERGO.xhtml");
                } else if (selectedMachine.equals("LWI V T-BaseV3")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-TBaseV3.xhtml");
                } else if (selectedMachine.equals("LWI V MobileFlexx")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-MobileFlexx.xhtml");
                } else if (selectedMachine.equals("LWI V Unixx III")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-UnixxIII.xhtml");
                } else if (selectedMachine.equals("LWI V UltraFlexx")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-UltraFlexx.xhtml");
                }
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines.xhtml");
        }
    }

    public void redirectToSelectedMachine(String str) throws IOException {
        if (str != null) {
            if (!str.equals("")) {
                System.out.println("str ===== " + str);
                selectedMachine = str;
                if (str.equals("LWI Small Chamber")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-SmallChamber.xhtml");
                } else if (str.equals("LWI V Flexx")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-Flexx.xhtml");
                } else if (str.equals("LWI V ERGO")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-ERGO.xhtml");
                } else if (str.equals("LWI V T-BaseV3")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-TBaseV3.xhtml");
                } else if (str.equals("LWI V MobileFlexx")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-MobileFlexx.xhtml");
                } else if (str.equals("LWI V Unixx III")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-UnixxIII.xhtml");
                } else if (str.equals("LWI V UltraFlexx")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines-UltraFlexx.xhtml");
                }
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products-machines.xhtml");
        }
    }

    public String formatPrice(Double input) {
        DecimalFormat df = new DecimalFormat("0.00");
        // System.out.println("formatPrice() ===== " + df.format(input));
        try {
            return df.format(input);
        } catch (Exception ex) {
            return "";
        }
    }

    public void checkToReset() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString() == null) {

        } else if (!username.equals(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString())) {
            System.out.println("ProductQuotationManagedBean.java receivedProductQuotations() ===== call method reset()");
            reset();
        }
    }

    public void reset() {
        System.out.println("ProductQuotationManagedBean.java reset()");
        newProductQuotation = new ProductQuotation();
        newProductQuotationDescription = new ProductQuotationDescription();
        receivedProductQuotationList = new ArrayList<>();
        displayProductQuotationDescriptionList = new ArrayList<>();
        count = 1;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            System.out.println("ProductQuotationManagedBean.java: Username is " + getUsername());
        }
        date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

    public void receivedProductQuotations() {
        System.out.println("ProductQuotationManagedBean.java receivedProductQuotations() ===== " + username);

        if (!username.equals(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString())) {
            System.out.println("ProductQuotationManagedBean.java receivedProductQuotations() ===== call method reset()");
            reset();
        }

        receivedProductQuotationList = new ArrayList<>(productQuotationSessionBean.receivedProductQuotationList(username));

        FacesContext.getCurrentInstance().addMessage("qMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Current list of quotations are up to date.", ""));
    }

    public String formatDate(Timestamp t) {
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        return sd.format(t.getTime());
    }

    public void selectProductQuotation(ProductQuotation productQuotation) {
        System.out.println("productQuotation.getProductQuotationNo() ===== " + productQuotation.getProductQuotationNo());
        if (productQuotation != null) {
            displayProductQuotationDescriptionList = new ArrayList<>(productQuotation.getProductQuotationDescriptionList());
        }

        // System.out.println("ProductQuotationDescriptionList size is " + productQuotation.getProductQuotationDescriptionList().size());
    }

    public void addToCacheList(String productType, String itemName, Integer quantity) {
        Boolean noSuchItem = true;
        System.out.println("cacheList.size() === " + cacheList.size());
        System.out.println("count === " + count);
        if (cacheList.size() > 0) {
            System.out.println("inside for loop");

            Iterator<ProductQuotationDescription> it = cacheList.iterator();

            while (it.hasNext()) {
                ProductQuotationDescription pqd = it.next();
                if (pqd.getItemName().equals(itemName)) {
                    // same item
                    noSuchItem = false;
                    System.out.println("pqd.getItemName() === " + pqd.getItemName());
                    System.out.println("itemName === " + itemName);
                    System.out.println("inside for loop cacheList.size() === " + cacheList.size());
                    FacesContext.getCurrentInstance().addMessage("growlMsgs", new FacesMessage(FacesMessage.SEVERITY_WARN, "Item has been added to RFQ list!", ""));
                } else {
                    // no such item

                }
            }

            if (noSuchItem) {
                newProductQuotationDescription.setProductQuotationDescNo(count);
                newProductQuotationDescription.setProductType(productType);
                newProductQuotationDescription.setItemName(itemName);
                newProductQuotationDescription.setQuantity(quantity);

                cacheList.add(newProductQuotationDescription);

                count += 1;
                newProductQuotationDescription = new ProductQuotationDescription();

                FacesContext.getCurrentInstance().addMessage("growlMsgs", new FacesMessage("Item is added to RFQ list!", ""));
            }

        } else {
            newProductQuotationDescription.setProductQuotationDescNo(count);
            newProductQuotationDescription.setProductType(productType);
            newProductQuotationDescription.setItemName(itemName);
            newProductQuotationDescription.setQuantity(quantity);

            cacheList.add(newProductQuotationDescription);

            count += 1;
            newProductQuotationDescription = new ProductQuotationDescription();

            FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Item is added to RFQ list!", ""));
        }
    }

    public void deleteProductQuotationDescription(ProductQuotationDescription productQuotationDescription) {
        if (productQuotationDescription != null) {
            getCacheList().remove(productQuotationDescription.getProductQuotationDescNo() - 1);
            reassignQuotationNo();
            FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Item is removed from RFQ list!", ""));
        }
    }

    public void editRFQ() {
        FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Item has been updated in RFQ list!", ""));
    }

    public void reassignQuotationNo() {
        for (int i = 0; i < getCacheList().size(); i++) {
            getCacheList().get(i).setProductQuotationDescNo(i + 1);
        }
        setCount((Integer) getCacheList().size() + 1);
    }

    public void createProductQuotation(ActionEvent event) throws IOException {
        System.out.println("getUsername() ==== " + getUsername());
        // generate product quotation number
        setProductQuotationNo(getProductQuotationSessionBean().getProductQuotationNo(getUsername()));
        // assign product quotation number
        getNewProductQuotation().setProductQuotationNo(getProductQuotationNo());
        getNewProductQuotation().setCustomer(getCustomerSessionBean().getCustomerByUsername(getUsername()));
        // persist product quotation
        getProductQuotationSessionBean().createProductQuotation(getNewProductQuotation());
        // add quotation description into its respective quotation
        for (ProductQuotationDescription pqd : getCacheList()) {
            pqd.setProductQuotationNo(getProductQuotationNo());
            pqd.setProductQuotation(getNewProductQuotation());
            getNewProductQuotation().addProductQuotationDescriptionList(pqd);
            getProductQuotationSessionBean().createProductQuotationDesciption(pqd);//persist qd
        }
        getProductQuotationSessionBean().conductMerge(getNewProductQuotation());
        // add quotation to customer
        getCustomerSessionBean().addProductQuotation(getUsername(), getNewProductQuotation());

        // clear cachelist
        getCacheList().clear();
        // set count back to 1
        setCount((Integer) 1);
        // reinitialise quotation
        setNewProductQuotation(new ProductQuotation());
        setNewProductQuotationDescription(new ProductQuotationDescription());
        // set quotation tab to be selected
        System.out.println("Your request for product price quotation has been sent successfully!");
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products.xhtml");
        FacesContext.getCurrentInstance().addMessage("rfqMsg", new FacesMessage("An RFQ has been sent successfully!", ""));
    }

    public void setRejectionStatus(ProductQuotation productQuotation) {
        System.out.println("Customer rejected projuct quotation!");
        productQuotation.setStatus("Rejected");
        getProductQuotationSessionBean().conductMerge(productQuotation);
        FacesContext.getCurrentInstance().addMessage("qMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "You have rejected Product Quotation No. " + productQuotation.getProductQuotationNo(), ""));
    }

    public boolean viewVisibility(ProductQuotation productQuotation) {
        if (productQuotation == null) {
            return false;
        }

        if (productQuotation.getStatus().equals("Pending") || productQuotation.getStatus().equals("Accepted") || productQuotation.getStatus().equals("Rejected")) {
            return false;
        }
        return true;
    }

    /**
     * @return the productQuotationSessionBean
     */
    public ProductQuotationSessionBeanLocal getProductQuotationSessionBean() {
        return productQuotationSessionBean;
    }

    /**
     * @param productQuotationSessionBean the productQuotationSessionBean to set
     */
    public void setProductQuotationSessionBean(ProductQuotationSessionBeanLocal productQuotationSessionBean) {
        this.productQuotationSessionBean = productQuotationSessionBean;
    }

    /**
     * @return the customerSessionBean
     */
    public CustomerSessionBeanLocal getCustomerSessionBean() {
        return customerSessionBean;
    }

    /**
     * @param customerSessionBean the customerSessionBean to set
     */
    public void setCustomerSessionBean(CustomerSessionBeanLocal customerSessionBean) {
        this.customerSessionBean = customerSessionBean;
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
     * @return the productQuotationNo
     */
    public String getProductQuotationNo() {
        return productQuotationNo;
    }

    /**
     * @param productQuotationNo the productQuotationNo to set
     */
    public void setProductQuotationNo(String productQuotationNo) {
        this.productQuotationNo = productQuotationNo;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the cacheList
     */
    public ArrayList<ProductQuotationDescription> getCacheList() {
        return cacheList;
    }

    /**
     * @param cacheList the cacheList to set
     */
    public void setCacheList(ArrayList<ProductQuotationDescription> cacheList) {
        this.cacheList = cacheList;
    }

    /**
     * @return the newProductQuotation
     */
    public ProductQuotation getNewProductQuotation() {
        return newProductQuotation;
    }

    /**
     * @param newProductQuotation the newProductQuotation to set
     */
    public void setNewProductQuotation(ProductQuotation newProductQuotation) {
        this.newProductQuotation = newProductQuotation;
    }

    /**
     * @return the newProductQuotationDescription
     */
    public ProductQuotationDescription getNewProductQuotationDescription() {
        return newProductQuotationDescription;
    }

    /**
     * @param newProductQuotationDescription the newProductQuotationDescription
     * to set
     */
    public void setNewProductQuotationDescription(ProductQuotationDescription newProductQuotationDescription) {
        this.newProductQuotationDescription = newProductQuotationDescription;
    }

    /**
     * @return the receivedProductQuotationList
     */
    public ArrayList<ProductQuotation> getReceivedProductQuotationList() {
        return receivedProductQuotationList;
    }

    /**
     * @param receivedProductQuotationList the receivedProductQuotationList to
     * set
     */
    public void setReceivedProductQuotationList(ArrayList<ProductQuotation> receivedProductQuotationList) {
        this.receivedProductQuotationList = receivedProductQuotationList;
    }

    /**
     * @return the displayProductQuotationDescriptionList
     */
    public ArrayList<ProductQuotationDescription> getDisplayProductQuotationDescriptionList() {
        return displayProductQuotationDescriptionList;
    }

    /**
     * @param displayProductQuotationDescriptionList the
     * displayProductQuotationDescriptionList to set
     */
    public void setDisplayProductQuotationDescriptionList(ArrayList<ProductQuotationDescription> displayProductQuotationDescriptionList) {
        this.displayProductQuotationDescriptionList = displayProductQuotationDescriptionList;
    }

    /**
     * @return the selectedMachine
     */
    public String getSelectedMachine() {
        return selectedMachine;
    }

    /**
     * @param selectedMachine the selectedMachine to set
     */
    public void setSelectedMachine(String selectedMachine) {
        this.selectedMachine = selectedMachine;
    }

    /**
     * @return the selectedCount
     */
    public Integer getSelectedCount() {
        return selectedCount;
    }

    /**
     * @param selectedCount the selectedCount to set
     */
    public void setSelectedCount(Integer selectedCount) {
        this.selectedCount = selectedCount;
    }

    /**
     * @return the selectedMachine1
     */
    public Boolean getSelectedMachine1() {
        return selectedMachine1;
    }

    /**
     * @param selectedMachine1 the selectedMachine1 to set
     */
    public void setSelectedMachine1(Boolean selectedMachine1) {
        this.selectedMachine1 = selectedMachine1;
    }

    /**
     * @return the selectedMachine2
     */
    public Boolean getSelectedMachine2() {
        return selectedMachine2;
    }

    /**
     * @param selectedMachine2 the selectedMachine2 to set
     */
    public void setSelectedMachine2(Boolean selectedMachine2) {
        this.selectedMachine2 = selectedMachine2;
    }

    /**
     * @return the selectedMachine3
     */
    public Boolean getSelectedMachine3() {
        return selectedMachine3;
    }

    /**
     * @param selectedMachine3 the selectedMachine3 to set
     */
    public void setSelectedMachine3(Boolean selectedMachine3) {
        this.selectedMachine3 = selectedMachine3;
    }

    /**
     * @return the selectedMachine4
     */
    public Boolean getSelectedMachine4() {
        return selectedMachine4;
    }

    /**
     * @param selectedMachine4 the selectedMachine4 to set
     */
    public void setSelectedMachine4(Boolean selectedMachine4) {
        this.selectedMachine4 = selectedMachine4;
    }

    /**
     * @return the selectedMachine5
     */
    public Boolean getSelectedMachine5() {
        return selectedMachine5;
    }

    /**
     * @param selectedMachine5 the selectedMachine5 to set
     */
    public void setSelectedMachine5(Boolean selectedMachine5) {
        this.selectedMachine5 = selectedMachine5;
    }

    /**
     * @return the selectedMachine6
     */
    public Boolean getSelectedMachine6() {
        return selectedMachine6;
    }

    /**
     * @param selectedMachine6 the selectedMachine6 to set
     */
    public void setSelectedMachine6(Boolean selectedMachine6) {
        this.selectedMachine6 = selectedMachine6;
    }

    /**
     * @return the selectedMachine7
     */
    public Boolean getSelectedMachine7() {
        return selectedMachine7;
    }

    /**
     * @param selectedMachine7 the selectedMachine7 to set
     */
    public void setSelectedMachine7(Boolean selectedMachine7) {
        this.selectedMachine7 = selectedMachine7;
    }

    /**
     * @return the filteredProductList
     */
    public ArrayList<ProductQuotation> getFilteredProductList() {
        return filteredProductList;
    }

    /**
     * @param filteredProductList the filteredProductList to set
     */
    public void setFilteredProductList(ArrayList<ProductQuotation> filteredProductList) {
        this.filteredProductList = filteredProductList;
    }

    /**
     * @return the filteredProductList2
     */
    public ArrayList<ProductQuotation> getFilteredProductList2() {
        return filteredProductList2;
    }

    /**
     * @param filteredProductList2 the filteredProductList2 to set
     */
    public void setFilteredProductList2(ArrayList<ProductQuotation> filteredProductList2) {
        this.filteredProductList2 = filteredProductList2;
    }
}
