/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.Quotation;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
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
        //customer.setPw(encryptPassword(customer.getPw()));
        em.persist(customer);
    }
    
    @Override
    public String encryptPassword(String password) {

        String key = "Bar12345Bar12345"; // 128 bit key
        byte[] encrypted = null;
        try {
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            try {
                // encrypt the text
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
            }
            encrypted = cipher.doFinal(password.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {

        }
        
        System.err.println("Encrypted Password: " + new String(encrypted));
        return new String(encrypted);
    }
    
    @Override
    public String decryptPassword(String password) {
        String key = "Bar12345Bar12345";
        String decrypted = "";
        byte[] encrypted = password.getBytes();
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            try {
                cipher.init(Cipher.DECRYPT_MODE, aesKey);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
            }
            decrypted = new String(cipher.doFinal(encrypted));
            System.err.println("Decrypted password: " + decrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {

        }
        return decrypted;

    }
    

    @Override
    public Customer getCustomerByUsername(String username) {
        Customer c = em.find(Customer.class, username);
        if(c != null){
            //c.setPw(decryptPassword(c.getPw()));
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
    //decryption of password dont happen as admin should not be able to view
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
        //c.setPw(encryptPassword(c1.getPw()));
        c.setPw(c1.getPw());
        c.setAddress2(c1.getAddress2());
        c.setEmail(c1.getEmail());
        c.setPostalCode(c1.getPostalCode());
    }
}
