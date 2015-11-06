/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.DocumentControlEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
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
    private DocumentControlEntity document;
    private boolean check = false;
    private String projID;
    private String destination = "C:\\Users\\JustHRJ\\Desktop\\IN05\\HiYewInternalWeb\\web\\projectDocuments\\";

    /**
     * Creates a new instance of DocumentManageBean
     */
    public DocumentManageBean() {
    }

    @PostConstruct
    public void init() {

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
        if (documentLink.isEmpty() || ("").equals(documentLink)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkForNull2(String documentLink) {
        if (documentLink.isEmpty() || ("").equals(documentLink) || documentLink == null) {
            return true;
        } else {
            return false;
        }
    }

    public void uploadPO(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updatePODestination(destination + event.getFile().getFileName(), projID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadCustDO(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateCustDODestination(destination + event.getFile().getFileName(), projID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadRequestForm(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateRequestForm(destination + event.getFile().getFileName(), projID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadPWS(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updatePWS(destination + event.getFile().getFileName(), projID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadServiceReport(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateServiceReport(destination + event.getFile().getFileName(), projID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadComDO(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateComDO(destination + event.getFile().getFileName(), projID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadInvoice(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            documentSystemBean.updateInvoice(destination + event.getFile().getFileName(), projID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFile(String fileName, InputStream in) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination + fileName));

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

}
