package session.stateless;

import entity.SupplierEntity;
import entity.SupplierPurchaseOrder;
//import entity.Quotation;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SupplierSessionBean implements SupplierSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createSupplier(SupplierEntity supplier) {
        //supplier.setPw(encryptPassword(supplier.getPw()));
        em.persist(supplier);
    }

    @Override
    public String encryptPassword(String password) {
        String encrypted = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes(), 0, password.length());
            encrypted = new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encrypted;
    }

    @Override
    public SupplierEntity getSupplierByUsername(String username) {
        SupplierEntity c = em.find(SupplierEntity.class, username);
        if (c != null) {
            //c.setPw(decryptPassword(c.getPw()));
        }
        return c;
    }

    /*@Override
    public void addQuotation(String username, Quotation quotation) {
        SupplierEntity c = em.find(SupplierEntity.class, username);
        if (c == null) {
            System.out.println("Supplier is null");
        } else {
            c.addQuotations(quotation);
        }
    }*/
    
    /*@Override
    public void addPurchaseOrder(String username, SupplierPO cpo){
        SupplierEntity c = em.find(SupplierEntity.class, username);
        if (c == null) {
            System.out.println("Supplier is null");
        } else {
            c.addSupplierPO(cpo);
        }
    }*/

    //decryption of password dont happen as admin should not be able to view
    @Override
    public List<SupplierEntity> getAllSupplier() {
        Query query = em.createQuery("SELECT c FROM SupplierEntity c");
        return query.getResultList();
    }

    @Override
    public void updateSupplier(SupplierEntity s1) {
        SupplierEntity s = em.find(SupplierEntity.class, s1.getUserName());
        //s.setName(s1.getName());
        s.setAddress1(s1.getAddress1());
        s.setPhone(s1.getPhone());
        s.setPw(s1.getPw());
        s.setAddress2(s1.getAddress2());
        s.setEmail(s1.getEmail());
        s.setPostalCode(s1.getPostalCode());
        s.setCompanyName(s1.getCompanyName());
    }

    @Override
    public String resetSupplierPassword(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
