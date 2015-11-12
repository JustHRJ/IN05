/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.DocumentControlEntity;
import entity.Project;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JustHRJ
 */
@Stateless
public class DocumentSystemBean implements DocumentSystemBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    public DocumentControlEntity retrieveDocument(String projID) {
        Project project = em.find(Project.class, projID);
        if (project == null) {
            return null;
        } else {
            return project.getDocuments();
        }
    }

    public void updatePODestination(String destination, String projID) {
        if (projID.isEmpty() || destination.isEmpty()) {

        } else {
            Project p = em.find(Project.class, projID);
            if (p == null) {

            } else {
                DocumentControlEntity d = p.getDocuments();
                d.setPurchaseOrder(destination);
                em.merge(d);
            }
        }
    }

    public Customer customerInfo(String CustomerKey) {
        Customer cust = em.find(Customer.class, CustomerKey);
        return cust;

    }

    public void updateCustDODestination(String destination, String projID) {
        if (projID.isEmpty() || destination.isEmpty()) {

        } else {
            Project p = em.find(Project.class, projID);
            if (p == null) {

            } else {
                DocumentControlEntity d = p.getDocuments();
                d.setCustDeliveryOrder(destination);
                em.merge(d);
            }
        }
    }

    public void updateRequestForm(String destination, String projID) {
        if (projID.isEmpty() || destination.isEmpty()) {

        } else {
            Project p = em.find(Project.class, projID);
            if (p == null) {

            } else {
                DocumentControlEntity d = p.getDocuments();
                d.setRequestForm(destination);
                em.merge(d);
            }
        }
    }

    public void updatePWS(String destination, String projID) {
        if (projID.isEmpty() || destination.isEmpty()) {

        } else {
            Project p = em.find(Project.class, projID);
            if (p == null) {

            } else {
                DocumentControlEntity d = p.getDocuments();
                d.setProductWeldSheet(destination);
                em.merge(d);
            }
        }
    }

    public void updateInvoice(String destination, String projID) {
        if (projID.isEmpty() || destination.isEmpty()) {

        } else {
            Project p = em.find(Project.class, projID);
            if (p == null) {

            } else {
                DocumentControlEntity d = p.getDocuments();
                d.setInvoice(destination);
                em.merge(d);
            }
        }
    }

    public void updateServiceReport(String destination, String projID) {
        if (projID.isEmpty() || destination.isEmpty()) {

        } else {
            Project p = em.find(Project.class, projID);
            if (p == null) {

            } else {
                DocumentControlEntity d = p.getDocuments();
                d.setServiceReport(destination);
                em.merge(d);
            }
        }
    }

    public void updateComDO(String destination, String projID) {
        if (projID.isEmpty() || destination.isEmpty()) {

        } else {
            Project p = em.find(Project.class, projID);
            if (p == null) {

            } else {
                DocumentControlEntity d = p.getDocuments();
                d.setComDeliveyOrder(destination);
                em.merge(d);
            }
        }
    }

    public List<String> retrieveProjectList() {
        Query q = em.createQuery("select c from Project c");
        List<String> result = new ArrayList<String>();
        for (Object o : q.getResultList()) {
            Project p = (Project) o;
            result.add(p.getProjectNo());
        }

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }
}
