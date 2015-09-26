/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.CustomerPO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface CustomerPOSessionBeanLocal {

    public void createPO(CustomerPO customerPO);

    public void conductMerge(CustomerPO customerPO);

    public List<CustomerPO> receivedCustomerPO(String username);
    
}
