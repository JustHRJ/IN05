/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.Quotation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public void addQuotation(String username, Quotation quotation) {
        Customer c = em.find(Customer.class, username);
        if (c == null) {
            System.out.println("Customer is null");
        } else {
            c.addQuotations(quotation);
        }
    }

    @Override
    public List<Customer> getAllCustomer() {
        Query query = em.createQuery("SELECT c FROM Customer c");
        return query.getResultList();
    }

    @Override
    public void updateCustomer(Customer c1) {
        Customer c = em.find(Customer.class, c1.getUserName());
        c.setName(c1.getName());
        c.setAddress1(c1.getAddress1());
        c.setPhone(c1.getPhone());
        c.setPw(c1.getPw());
        c.setAddress2(c1.getAddress2());
        c.setEmail(c1.getEmail());
        c.setPostalCode(c1.getPostalCode());
    }
}
