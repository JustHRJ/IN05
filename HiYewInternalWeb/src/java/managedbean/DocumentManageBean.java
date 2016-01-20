/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Customer;
import entity.DocumentControlEntity;
import entity.Project;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import manager.EmailManager;
import org.primefaces.event.FileUploadEvent;
import session.stateless.DocumentSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "documentManageBean")
@ViewScoped
public class DocumentManageBean implements Serializable {

    @EJB
    private DocumentSystemBeanLocal documentSystemBean;
    private Project selectedProject;
    private DocumentControlEntity document;
    private boolean check = false;
    private boolean checkPDFRender = false;
    private String projID;
    private String destination = "C:\\Users\\JustHRJ\\Desktop\\Final Presentation\\IN05\\HiYewInternalWeb\\web\\projectDocuments\\";
    private String destinations = "/projectDocuments/";
    private String pdfURL = "";

    /**
     * Creates a new instance of DocumentManageBean
     */
    public DocumentManageBean() {
    }

    @PostConstruct
    public void init() {

    }

    public void sendURL(String url) {
        checkPDFRender = true;
        pdfURL = url;
    }

    public boolean checkEmail(Project proj) {
        if (proj != null) {
            DocumentControlEntity document = proj.getDocuments();
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 5);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            Timestamp timeNow = new Timestamp(c.getTime().getTime());
            Timestamp timeExpected = null;
            if (!proj.getProjectCompletion()) {
                timeExpected = proj.getPlannedEnd();
            }
            if (proj.getProjectCompletion()) {

                if (document.getCustDeliveryOrder() == null) {
                    return true;
                }

                if (document.getPurchaseOrder() == null) {
                    return true;
                }
                return false;
            } else if (!(proj.getProjectCompletion()) && timeNow.after(timeExpected)) {
                if (document.getCustDeliveryOrder() == null) {
                    return true;
                }

                if (document.getPurchaseOrder() == null) {
                    return true;
                }
                return false;
            }
            return false;
        } else {
            return false;
        }
    }

    public String checkFullSubmit(Project proj) {
        if (proj == null) {
            return "ui-icon-alert";
        }
        DocumentControlEntity document = proj.getDocuments();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 5);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Timestamp timeNow = new Timestamp(c.getTime().getTime());
        Timestamp timeExpected = null;
        if (!proj.getProjectCompletion()) {
            timeExpected = proj.getPlannedEnd();
        }

        if (proj.getProjectCompletion()) {
            if (document.getComDeliveyOrder() == null) {
                return "ui-icon-alert";
            }
            if (document.getCustDeliveryOrder() == null) {
                return "ui-icon-alert";
            }
            if (document.getInvoice() == null) {
                return "ui-icon-alert";
            }
            if (document.getProductWeldSheet() == null) {
                return "ui-icon-alert";
            }
            if (document.getPurchaseOrder() == null) {
                return "ui-icon-alert";
            }
            if (document.getRequestForm() == null) {
                return "ui-icon-alert";
            }
            if (document.getServiceReport() == null) {
                return "ui-icon-alert";
            }
            return "ui-icon-check";
//        } else if (!(proj.getProjectCompletion()) && timeNow.after(timeExpected)) {
//            if (document.getComDeliveyOrder() == null) {
//                return "ui-icon-notice";
//            }
//            if (document.getCustDeliveryOrder() == null) {
//                return "ui-icon-notice";
//            }
//            if (document.getProductWeldSheet() == null) {
//                return "ui-icon-notice";
//            }
//            if (document.getPurchaseOrder() == null) {
//                return "ui-icon-notice";
//            }
//            if (document.getRequestForm() == null) {
//                return "ui-icon-notice";
//            }
//            if (document.getServiceReport() == null) {
//                return "ui-icon-notice";
//            }
//            return "ui-icon-script";
        } else {
            if (document.getComDeliveyOrder() == null) {
                return "ui-icon-script";
            }
            if (document.getCustDeliveryOrder() == null) {
                return "ui-icon-script";
            }
            if (document.getProductWeldSheet() == null) {
                return "ui-icon-script";
            }
            if (document.getPurchaseOrder() == null) {
                return "ui-icon-script";
            }
            if (document.getRequestForm() == null) {
                return "ui-icon-script";
            }
            if (document.getServiceReport() == null) {
                return "ui-icon-script";
            }
            if (document.getInvoice() == null) {
                return "ui-icon-script";
            }
            return "ui-icon-check";
        }
    }

    public String retrievePDF() {
        return pdfURL;
    }

    /**
     * @return the projID
     */
    public String getProjID() {
        return projID;
    }

    /**
     * @param projID the projID to set
     */
    public void setProjID(String projID) {
        this.projID = projID;
    }

    public List<String> retrieveProjList() {
        return documentSystemBean.retrieveProjectList();
    }

    public void processList() {
        if (projID != null) {
            document = documentSystemBean.retrieveDocument(projID);
            check = true;
        }
    }

    public boolean checkForNull3(String documentLink) {
        System.out.println(documentLink);
        if (documentLink == null || documentLink.isEmpty()) {
            return false;
        } else {
            if (documentLink.equals("Received")) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean checkForNull(String documentLink) {
        System.out.println(documentLink);
        if (documentLink == null || documentLink.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkForNull2(String documentLink) {

        if (documentLink == null || documentLink.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String retrieveProjectName() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("projectToEdit") != null) {
            Project proj = (Project) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("projectToEdit");
            document = proj.getDocuments();
            projID = proj.getProjectNo();
            return proj.getProjectNo();
        } else {
            return "";
        }
    }

    public void sendEmail(Project proj) {
        Customer cust = documentSystemBean.customerInfo(proj.getCustomerKey());
        DocumentControlEntity doc = proj.getDocuments();
        if (cust != null) {
            String email = cust.getEmail();
            EmailManager emailManager = new EmailManager();

            Vector im = new Vector();

            if (doc.getCustDeliveryOrder() == null) {
                im.add("Customer Delivery Order");
            }

            if (doc.getPurchaseOrder() == null) {
                im.add("Customer Purchase Order");
            }

            if (im != null) {
                emailManager.emailReminder(email, im, proj.getProjectNo(), cust.getName());
            }

            FacesMessage msg = new FacesMessage("Email sent");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }

    public void redirectToEdit() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("projectToEdit", selectedProject);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/dcms-edit-project-documents.xhtml");
    }

    public void uploadPO(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updatePODestination(destinations + projID + "/" + event.getFile().getFileName(), projID);
            document.setPurchaseOrder(destinations + projID + "/" + event.getFile().getFileName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadCustDO(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateCustDODestination(destinations + projID + "/" + event.getFile().getFileName(), projID);
            document.setCustDeliveryOrder(destinations + projID + "/" + event.getFile().getFileName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadRequestForm(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateRequestForm(destinations + projID + "/" + event.getFile().getFileName(), projID);
            document.setRequestForm(destinations + projID + "/" + event.getFile().getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadPWS(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updatePWS(destinations + projID + "/" + event.getFile().getFileName(), projID);
            document.setProductWeldSheet(destinations + projID + "/" + event.getFile().getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadServiceReport(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateServiceReport(destinations + projID + "/" + event.getFile().getFileName(), projID);
            document.setServiceReport(destinations + projID + "/" + event.getFile().getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadComDO(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateComDO(destinations + projID + "/" + event.getFile().getFileName(), projID);
            document.setComDeliveyOrder(destinations + projID + "/" + event.getFile().getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadInvoice(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateInvoice(destinations + projID + "/" + event.getFile().getFileName(), projID);
            document.setInvoice(destinations + projID + "/" + event.getFile().getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFile(String fileName, InputStream in) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination + projID + "\\" + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @return the document
     */
    public DocumentControlEntity getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(DocumentControlEntity document) {
        this.document = document;
    }

    /**
     * @return the check
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * @param check the check to set
     */
    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * @return the selectedProject
     */
    public Project getSelectedProject() {
        return selectedProject;
    }

    /**
     * @param selectedProject the selectedProject to set
     */
    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    /**
     * @return the pdfURL
     */
    public String getPdfURL() {
        return pdfURL;
    }

    /**
     * @param pdfURL the pdfURL to set
     */
    public void setPdfURL(String pdfURL) {
        this.pdfURL = pdfURL;
    }

    /**
     * @return the checkPDFRender
     */
    public boolean isCheckPDFRender() {
        return checkPDFRender;
    }

    /**
     * @param checkPDFRender the checkPDFRender to set
     */
    public void setCheckPDFRender(boolean checkPDFRender) {
        this.checkPDFRender = checkPDFRender;
    }

    public void changeRequestForm() {
        documentSystemBean.changeStatus(projID, "RequestForm", "Received");
        FacesMessage msg = new FacesMessage("Receive Request Form Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void changePO() {
        documentSystemBean.changeStatus(projID, "PurchaseOrder", "Received");
        FacesMessage msg = new FacesMessage("Receive Purchase Order Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void changeCustDO() {
        documentSystemBean.changeStatus(projID, "CustDeliveryOrder", "Received");
        FacesMessage msg = new FacesMessage("Receive Customer Delivery Order Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void changePWS() {
        documentSystemBean.changeStatus(projID, "ProductWeldSheet", "Received");
        FacesMessage msg = new FacesMessage("Receive Product Weld Sheet Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void changeServiceReport() {
        documentSystemBean.changeStatus(projID, "ServiceReport", "Received");
        FacesMessage msg = new FacesMessage("Receive Service Report Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void changeComDO() {
        documentSystemBean.changeStatus(projID, "ComDeliveryOrder", "Received");
        FacesMessage msg = new FacesMessage("Receive Company Delivery Order Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void changeInvoice() {
        documentSystemBean.changeStatus(projID, "Invoice", "Received");
        FacesMessage msg = new FacesMessage("Receive Invoice Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void revertRequestForm() {
        documentSystemBean.changeStatus(projID, "RequestForm", null);
        FacesMessage msg = new FacesMessage("Revert Request Form Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void revertPO() {
        documentSystemBean.changeStatus(projID, "PurchaseOrder", null);
        FacesMessage msg = new FacesMessage("Revert Purchase Order Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void revertCustDO() {
        documentSystemBean.changeStatus(projID, "CustDeliveryOrder", null);
        FacesMessage msg = new FacesMessage("Revert Customer Delivery Order Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void revertPWS() {
        documentSystemBean.changeStatus(projID, "ProductWeldSheet", null);
        FacesMessage msg = new FacesMessage("Revert Product Weld Sheet Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void revertServiceReport() {
        documentSystemBean.changeStatus(projID, "ServiceReport", null);
        FacesMessage msg = new FacesMessage("Revert Service Reports Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void revertComDO() {
        documentSystemBean.changeStatus(projID, "ComDeliveryOrder", null);
        FacesMessage msg = new FacesMessage("Revert Company Delivery Order Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void revertInvoice() {
        documentSystemBean.changeStatus(projID, "Invoice", null);
        FacesMessage msg = new FacesMessage("Revert Invoice Success");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
