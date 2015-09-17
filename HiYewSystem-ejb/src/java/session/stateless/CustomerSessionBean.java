/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author: Jit Cheong
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createCustomer(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        Customer c = em.find(Customer.class, username);
        if (c == null) {
            return null;
        }
        return c;

    }

    @Override
    public List<Customer> getAllCustomer() {
        Query query = em.createQuery("SELECT c FROM Customer c");
        return query.getResultList();
    }

    @Override
    public void updateCustomer(Customer c1){
        Customer c = em.find(Customer.class, c1.getUserName());
        c.setName(c1.getName());
        c.setAddress(c1.getAddress());
        c.setPhone(c1.getPhone());
        c.setPw(c1.getPw());
    }
}
