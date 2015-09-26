/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.CustomerPO;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author User
 */
@Stateless
public class CustomerPOSessionBean implements CustomerPOSessionBeanLocal {

   @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;
   
   @Override
   public void createPO(CustomerPO customerPO){
       em.persist(customerPO);
   }
   
   @Override
   public void conductMerge(CustomerPO customerPO){
        em.merge(customerPO);
    }
   
   @Override
   public List<CustomerPO> receivedCustomerPO(String username) {

        Customer c = em.find(Customer.class, username);
        
        return c.getCustomerPOs();
    }
}
