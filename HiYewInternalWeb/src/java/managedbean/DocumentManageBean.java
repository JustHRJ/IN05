/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
    private String destination = "D:\\Users\\K.guoxiang\\Documents\\NetBeansProjects\\IN05\\HiYewInternalWeb\\web\\projectDocuments\\";
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

    public String checkFullSubmit(Project proj) {
        DocumentControlEntity document = proj.getDocuments();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -5);
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
        } else if (!(proj.getProjectCompletion()) && timeNow.after(timeExpected)) {
            if (document.getComDeliveyOrder() == null) {
                return "ui-icon-notice";
            }
            if (document.getCustDeliveryOrder() == null) {
                return "ui-icon-notice";
            }
            if (document.getProductWeldSheet() == null) {
                return "ui-icon-notice";
            }
            if (document.getPurchaseOrder() == null) {
                return "ui-icon-notice";
            }
            if (document.getRequestForm() == null) {
                return "ui-icon-notice";
            }
            if (document.getServiceReport() == null) {
                return "ui-icon-notice";
            }
            return "ui-icon-script";
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
        try {
            System.out.println("pdfURl =" + pdfURL);
            if (pdfURL.equals("")) {
                return "";
            } else {
                return pdfURL;
            }
        } catch (Exception ex) {
            return "";
        }
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

}
