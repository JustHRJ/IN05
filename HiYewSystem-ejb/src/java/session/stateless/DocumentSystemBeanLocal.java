/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.DocumentControlEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JustHRJ
 */
@Local
public interface DocumentSystemBeanLocal {

    public DocumentControlEntity retrieveDocument(String projID);

    public List<String> retrieveProjectList();

    public void updatePODestination(String destination, String projID);

    public void updateCustDODestination(String destination, String projID);

    public void updateRequestForm(String destination, String projID);

    public void updatePWS(String destination, String projID);

    public void updateInvoice(String destination, String projID);

    public void updateServiceReport(String destination, String projID);

    public void updateComDO(String destination, String projID);

    public Customer customerInfo(String CustomerKey);
    
}
