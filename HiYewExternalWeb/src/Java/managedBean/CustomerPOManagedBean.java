package managedBean;

import entity.CustomerPO;
import entity.EmployeeEntity;
import entity.FillerEntity;
import entity.MachineEntity;
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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import session.stateless.CustomerPOSessionBeanLocal;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.MetalSessionBeanLocal;
import session.stateless.ProjectSessionBeanLocal;
import session.stateless.QuotationSessionBeanLocal;

@Named(value = "pOManagedBean")
@ViewScoped
public class CustomerPOManagedBean implements Serializable {

    @EJB
    private MetalSessionBeanLocal metalSessionBean;

    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    @EJB
    private CustomerPOSessionBeanLocal customerPOSessionBean;
    @EJB
    private QuotationSessionBeanLocal quotationSessionBean;
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private CustomerPO newPurOrder;
    //private Quotation quotation;

    private String username = "";

    //Attributes binded to view
    private String purOrderNo = "";

    private String attn = "";
    private String paymentTerms = "";
    private String orderDate = "";
    //private String expectedStart = "";
    //private String expectedEnd = "";
    private String totalPrice = "";

    private String metalName = "";
    private String descriptions;
    private Double total = 0.0;
    //private Timestamp expectedStartDate;
    //private Timestamp expectedEndDate;

    private ArrayList<CustomerPO> receivedCustomerPO;
    private ArrayList<WeldJob> receivedCustomerWJ;

    /**
     * Creates a new instance of POManagedBean
     */
    public CustomerPOManagedBean() {
        newPurOrder = new CustomerPO();
        receivedCustomerPO = new ArrayList<>();
        receivedCustomerWJ = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            System.out.println("Q: Username is " + username);
            
            
        }

        orderDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        descriptions = "";
        if(customerPOSessionBean.receivedCustomerPO(username) != null){
            receivedCustomerPO = new ArrayList<>(customerPOSessionBean.receivedCustomerPO(username));
        }
        if(projectSessionBean.receivedWeldJobs(username) != null){
            receivedCustomerWJ = new ArrayList<>(projectSessionBean.receivedWeldJobs(username));
        }
        
        //System.out.println(receivedCustomerPO.size());
        
    }

    public void receivedCustomerPO() {
        receivedCustomerPO = new ArrayList<>(customerPOSessionBean.receivedCustomerPO(username));
        FacesContext.getCurrentInstance().addMessage("poMsg",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Current purchase order are up to date.", ""));
    }
    
    public void receivedCustomerWJ(){
        receivedCustomerWJ = new ArrayList<>(projectSessionBean.receivedWeldJobs(username));
        FacesContext.getCurrentInstance().addMessage("wjMsg",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Current weld jobs are up to date.", ""));
    }

    public void createPurchaseOrder(Quotation q) {
        q.setStatus("Accepted");
        quotationSessionBean.conductMerge(q);

        newPurOrder.setPoId(q.getQuotationNo());
        //newPurOrder.setExpectedStartDate(expectedStartDate);
        //newPurOrder.setExpectedEndDate(expectedEndDate);
        newPurOrder.setTotalPrice(total);

        if (newPurOrder.getMailingAddr1().equals("") && newPurOrder.getMailingAddr2().equals("")) {
            newPurOrder.setMailingAddr1(q.getCustomer().getAddress1());
            newPurOrder.setMailingAddr2(q.getCustomer().getAddress2());
        }

        newPurOrder.setCustomer(q.getCustomer());
        newPurOrder.setQuotation(q);
        customerPOSessionBean.createPO(newPurOrder);//persist
        //add into customer purchase order collection in persistence context
        customerSessionBean.addPurchaseOrder(q.getCustomer().getUserName(), newPurOrder);
        System.out.println("Test 1");
        //create a new project
        Project project = new Project();
        project.setProjectNo(newPurOrder.getPoId());
        project.setProjectCompletion(false);
        project.setProjectManager("Han Kiat");
        //set customer key to project
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            String username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            project.setCustomerKey(username);
        }
        System.out.println("Test 2");
        //set latest End date for project
        if (q.getCompanyEarliestEnd() == null) {
            project.setLatestEnd(q.getCustomerLatestEnd());
        } else {
            project.setLatestEnd(q.getCompanyEarliestEnd());
        }
        System.out.println("Test 3");
        //duration
        Integer days = 0;
        //create new weldJobs
        ArrayList<WeldJob> weldJobs = new ArrayList<>();
        WeldJob newWeldJob = new WeldJob();
        System.out.println("Size of qd: " + q.getQuotationDescriptions().size());
        for (int i = 0; i < q.getQuotationDescriptions().size(); i++) {
            System.out.println("Test 4");
            newWeldJob.setProjectNo(newPurOrder.getPoId());
            newWeldJob.setMetal1(q.getQuotationDescriptions().get(i).getMetalName());
            newWeldJob.setMetal2(q.getQuotationDescriptions().get(i).getMetalName());
            //set filler
            Metal m = metalSessionBean.getMetalByName(q.getQuotationDescriptions().get(i).getMetalName());
            if (m != null && !m.getFillers().isEmpty()) {
                newWeldJob.setFiller((FillerEntity) new ArrayList(m.getFillers()).get(0));
            }
            //check which staff is available
            EmployeeEntity e = projectSessionBean.getAvailableEmployee();
            if (e != null) {
                newWeldJob.setEmpName(e.getEmployee_name());
            }

            //check which machine can be used for welding type
            MachineEntity machine = projectSessionBean.getAvailableMachine(q.getQuotationDescriptions().get(i).getWeldingType());
            if (machine != null) {
                newWeldJob.setMachine(machine);
            }

            //check duration(days) from project of same nature
            String mName = q.getQuotationDescriptions().get(0).getMetalName();
            if (projectSessionBean.getSimilarPastProjectDuration(mName, mName) > days) {
                days = projectSessionBean.getSimilarPastProjectDuration(mName, mName); //get the longest duration among diff welding job
            }
            System.out.println("days taken: " + days);
            weldJobs.add(newWeldJob);
            newWeldJob = new WeldJob();
        }
        System.out.println("Test 5");
        if (days != -1) {
            //check for any project slack which can accomodate the new project duration
            Project p = projectSessionBean.getProjectDurationWithSlack(days);
            if (p != null) {
                project.setPlannedStart(p.getActualEnd());
            } else {
                //if there is no project slack equals new proj dur, then check and take the earliest project expected completion date
                p = projectSessionBean.getProjectWithEarliestCompletionDate();
                if (p != null) {
                    project.setPlannedStart(p.getPlannedEnd());
                }
            }
            //assign plannedEnd
            if (project.getPlannedStart() != null) {
                project.setPlannedEnd(projectSessionBean.addDays(project.getPlannedStart(), days));
            }
        }
        System.out.println("Test 6");
        projectSessionBean.createProject(project);
        for (WeldJob w : weldJobs) {
            w.setProject(project);
            projectSessionBean.createWeldJob(w);
        }
        project.setWeldJobs(weldJobs);
        projectSessionBean.conductMerge(project);
        

        newPurOrder = new CustomerPO();//reinitialise
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "RFQ creation must have at least one item job!", ""));
    }

    public void generatePurchaseOrder(Quotation q) {
        System.out.println("Customer sent purchase order!");
        //purOrderNo = q.getQuotationNo();
        attn = q.getCustomer().getName();
        paymentTerms = q.getTermsOfPayment();
        //Will update again when DSS is up. For now, we assume work starts 3 days later and ends in 1 month(30 days)
        //Date now = addDays(new Date(), 3);
        //Timestamp threeDaysLater = new Timestamp(now.getTime());
        //expectedStartDate = threeDaysLater;
        //expectedStart = formatDate(threeDaysLater);
        //now = addDays(new Date(), 30);
        //Timestamp thirtyDaysLater = new Timestamp(now.getTime());
        //expectedEndDate = thirtyDaysLater;
       // expectedEnd = formatDate(thirtyDaysLater);

        for (QuotationDescription qd : q.getQuotationDescriptions()) {

            descriptions += qd.getQuotationDescNo().toString() + ". " + qd.getMetalName() + "\r\n   "
                    + "Welding Type: " + qd.getWeldingType() + "\r\n   "
                    + "Instn: " + qd.getItemDesc() + "\r\n   " + "Price: SGD "
                    + String.format("%.2f", qd.getPrice() * qd.getQty()) + "\r\n";
            //compute total price
            total += qd.getPrice() * qd.getQty();
        }
        //System.out.println("total is " + total);
        totalPrice = String.format("%.2f", total);
        //Pop up
        showDialog();
    }

    public Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public String formatDate(Timestamp t) {

        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        if (t != null) {
            return sd.format(t.getTime());
        }
        return "";
    }

    public void showDialog() {
        System.out.println("Show Dialog - AFTER Customer sent purchase order!");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDialogVar').show();");
    }

    /**
     * @return the newPurOrder
     */
    public CustomerPO getNewPurOrder() {
        return newPurOrder;
    }

    /**
     * @param newPurOrder the newPurOrder to set
     */
    public void setNewPurOrder(CustomerPO newPurOrder) {
        this.newPurOrder = newPurOrder;
    }

    /**
     * @return the purOrderNo
     */
    public String getPurOrderNo() {
        return purOrderNo;
    }

    /**
     * @param purOrderNo the purOrderNo to set
     */
    public void setPurOrderNo(String purOrderNo) {
        this.purOrderNo = purOrderNo;
    }

    /**
     * @return the attn
     */
    public String getAttn() {
        return attn;
    }

    /**
     * @param attn the attn to set
     */
    public void setAttn(String attn) {
        this.attn = attn;
    }

    /**
     * @return the paymentTerms
     */
    public String getPaymentTerms() {
        return paymentTerms;
    }

    /**
     * @param paymentTerms the paymentTerms to set
     */
    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

   
    /**
     * @return the totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }


    /**
     * @return the descriptions
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * @param descriptions the descriptions to set
     */
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
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
     * @return the receivedCustomerPO
     */
    public ArrayList<CustomerPO> getReceivedCustomerPO() {
        return receivedCustomerPO;
    }

    /**
     * @param receivedCustomerPO the receivedCustomerPO to set
     */
    public void setReceivedCustomerPO(ArrayList<CustomerPO> receivedCustomerPO) {
        this.receivedCustomerPO = receivedCustomerPO;
    }

    /**
     * @return the metalName
     */
    public String getMetalName() {
        return metalName;
    }

    /**
     * @param metalName the metalName to set
     */
    public void setMetalName(String metalName) {
        this.metalName = metalName;
    }

    /**
     * @return the receivedCustomerWJ
     */
    public ArrayList<WeldJob> getReceivedCustomerWJ() {
        return receivedCustomerWJ;
    }

    /**
     * @param receivedCustomerWJ the receivedCustomerWJ to set
     */
    public void setReceivedCustomerWJ(ArrayList<WeldJob> receivedCustomerWJ) {
        this.receivedCustomerWJ = receivedCustomerWJ;
    }

}
