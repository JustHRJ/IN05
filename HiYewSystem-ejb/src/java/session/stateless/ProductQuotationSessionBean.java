package session.stateless;

import entity.Customer;
import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProductQuotationSessionBean implements ProductQuotationSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    public String getProductQuotationNo(String username) {
        Customer customer = em.find(Customer.class, username);
        return generateProductQuotationNo(customer);
    }

    private String generateProductQuotationNo(Customer customer) {
        String customerUsername = customer.getName();
        String newProductQuotationNo = "";
        String[] splitArray = null;
        if (customerUsername.contains("-")) {
            splitArray = customerUsername.split("-");
            for (String s : splitArray) {
                newProductQuotationNo += s.substring(0, 1);
            }
        } else if (customerUsername.contains(" ")) {
            splitArray = customerUsername.split(" ");
            for (String s : splitArray) {
                newProductQuotationNo += s.substring(0, 1);
            }
        } else {
            newProductQuotationNo = customerUsername;
        }
        newProductQuotationNo += new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime());

        return newProductQuotationNo;
    }

    public void createProductQuotation(ProductQuotation productQuotation) {
        em.persist(productQuotation);
    }

    public void createProductQuotationDesciption(ProductQuotationDescription productQuotationDescription) {
        em.persist(productQuotationDescription);
    }

    public List<ProductQuotation> receivedProductQuotationList(String username) {
        System.out.println("productQuotationList username ===== " + username);
        Query query = em.createQuery("SELECT q FROM ProductQuotation AS q where q.customer.userName=:username ");

        query.setParameter("username", username);

        List<ProductQuotation> productQuotationList = query.getResultList();
        return productQuotationList;
    }

    public List<ProductQuotation> receivedCustomerNewProductQuotationList(String status) {
        Query query;
        if (status.equals("Settled")) {
            query = em.createQuery("SELECT q FROM ProductQuotation AS q where q.status='Accepted' or q.status='Rejected'");
        } else {
            query = em.createQuery("SELECT q FROM ProductQuotation AS q where q.status='Pending' or q.status='Processed' or q.status='Relayed'");
        }

//        Query query = em.createQuery("SELECT q FROM ProductQuotation AS q");
        List<ProductQuotation> productQuotationList = query.getResultList();
        //System.out.println("productQuotationList.size() ===== " + productQuotationList.size());
        return productQuotationList;
    }

    public List<ProductQuotationDescription> retrieveProductQuotationDescriptionList(String quotationNo) {
        //System.out.println("ProductPurchaseOrderSessionBean.java receivedProductQuotationList() quotationNo ===== " + quotationNo);

        Query query = em.createQuery("SELECT qd FROM ProductQuotationDescription AS qd WHERE qd.productQuotation.productQuotationNo=:quotationNo");

        query.setParameter("quotationNo", quotationNo);

        List<ProductQuotationDescription> productQuotationDescriptionList = query.getResultList();
        return productQuotationDescriptionList;
    }

    public void conductMerge(ProductQuotation productQuotation) {
        em.merge(productQuotation);
    }

    public void updateProductQuotationPrices(ArrayList<ProductQuotationDescription> list) {
        for (ProductQuotationDescription productQuotationDescription : list) {
            em.merge(productQuotationDescription);
        }
    }

    public void updateProductQuotationRelayedStatus(ProductQuotation inProductQuotation) {
        ProductQuotation productQuotation = em.find(ProductQuotation.class, inProductQuotation.getProductQuotationNo());
        productQuotation.setStatus("Relayed");
    }

    public void updateProductQuotationStatus(ProductQuotation inProductQuotation) {
        ProductQuotation productQuotation = em.find(ProductQuotation.class, inProductQuotation.getProductQuotationNo());
        productQuotation.setStatus("Processed");
    }

    public List<List<List<String>>> deriveRevenueGraph_Year() {
        List<List<String>> machine1 = new ArrayList<List<String>>();
        List<List<String>> machine2 = new ArrayList<List<String>>();
        List<List<String>> machine3 = new ArrayList<List<String>>();
        List<List<String>> machine4 = new ArrayList<List<String>>();
        List<List<String>> machine5 = new ArrayList<List<String>>();
        List<List<String>> machine6 = new ArrayList<List<String>>();
        List<List<String>> machine7 = new ArrayList<List<String>>();

        List<List<List<String>>> allMachines = new ArrayList<List<List<String>>>();
        allMachines.add(machine1);
        allMachines.add(machine2);
        allMachines.add(machine3);
        allMachines.add(machine4);
        allMachines.add(machine5);
        allMachines.add(machine6);
        allMachines.add(machine7);

        ArrayList<String> years = new ArrayList<String>();
        years.add("2010");
        years.add("2011");
        years.add("2012");
        years.add("2013");
        years.add("2014");
        years.add("2015");

        ArrayList<String> machines = new ArrayList<String>();
        machines.add("LWI Small Chamber");
        machines.add("LWI V Flexx");
        machines.add("LWI V ERGO");
        machines.add("LWI V T-BaseV3");
        machines.add("LWI V MobileFlexx");
        machines.add("LWI V Unixx III");
        machines.add("LWI V UltraFlexx");

        ArrayList<ProductQuotation> productQuotationList = new ArrayList<ProductQuotation>();
        Query query = em.createQuery("SELECT q FROM ProductQuotation AS q");
        productQuotationList.addAll(query.getResultList());

        int count = 0;
        // for each machine
        for (String machine : machines) {
            // for each year
            for (String year : years) {
                Double revenue = 0.0;
                Double profitLoss = 0.0;
                // for each pd
                for (ProductQuotation pq : productQuotationList) {
                    List<ProductQuotationDescription> pqdList = pq.getProductQuotationDescriptionList();
                    // for each pqd
                    for (ProductQuotationDescription pqd : pqdList) {
                        if (pqd.getItemName().equals(machine)) {
                            if (formatDate_toYear(pq.getDate()).equals(year)) {
                                revenue = revenue + ((pqd.getQuotedPrice() == null) ? 0 : pqd.getQuotedPrice());
                                profitLoss = profitLoss + ((pqd.getProfitMargin() == null) ? 0 : pqd.getProfitMargin());
                            }
                        }
                    } // end for each pqd
                } // each for each pd
                ArrayList<String> yearRevenue = new ArrayList<String>();
                yearRevenue.add(year);
                yearRevenue.add(revenue.toString());
                yearRevenue.add(profitLoss.toString());
                allMachines.get(count).add(yearRevenue);
            } // end for each year
            count = count + 1;
        } // end for each machine

        return allMachines;
    }

    public List<List<List<List<String>>>> deriveRevenueGraph_Quarter() {
        // machine -> years -> months -> revenue
        List<List<List<String>>> machine1 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine2 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine3 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine4 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine5 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine6 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine7 = new ArrayList<List<List<String>>>();

        // machines -> machine -> years -> months -> revenue
        List<List<List<List<String>>>> allMachines = new ArrayList<List<List<List<String>>>>();
        allMachines.add(machine1);
        allMachines.add(machine2);
        allMachines.add(machine3);
        allMachines.add(machine4);
        allMachines.add(machine5);
        allMachines.add(machine6);
        allMachines.add(machine7);

        ArrayList<String> years = new ArrayList<String>();
        years.add("2010");
        years.add("2011");
        years.add("2012");
        years.add("2013");
        years.add("2014");
        years.add("2015");

        ArrayList<String> months = new ArrayList<String>();
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

        ArrayList<String> machines = new ArrayList<String>();
        machines.add("LWI Small Chamber");
        machines.add("LWI V Flexx");
        machines.add("LWI V ERGO");
        machines.add("LWI V T-BaseV3");
        machines.add("LWI V MobileFlexx");
        machines.add("LWI V Unixx III");
        machines.add("LWI V UltraFlexx");

        ArrayList<ProductQuotation> productQuotationList = new ArrayList<ProductQuotation>();
        Query query = em.createQuery("SELECT q FROM ProductQuotation AS q");
        productQuotationList.addAll(query.getResultList());

        int countMachine = 0;
        // for each machine
        for (String machine : machines) {
            int countYear = 0;
            // for each year
            for (String year : years) {
                List<List<String>> yearRevenue = new ArrayList<List<String>>();
                int countMonth = 0;
                // for each month
                Double revenue = 0.0;
                Double profitLoss = 0.0;
                for (String month : months) {
                    ArrayList<String> quarterRevenue = new ArrayList<String>();
                    // for each pd
                    for (ProductQuotation pq : productQuotationList) {
                        List<ProductQuotationDescription> pqdList = pq.getProductQuotationDescriptionList();
                        // for each pqd
                        for (ProductQuotationDescription pqd : pqdList) {
                            if (pqd.getItemName().equals(machine)) {
                                if (formatDate_toYear(pq.getDate()).equals(year)) {
                                    if (formatDate_toMonth(pq.getDate()).equals(month)) {
                                        revenue = revenue + ((pqd.getQuotedPrice() == null) ? 0 : pqd.getQuotedPrice());
                                        profitLoss = profitLoss + ((pqd.getProfitMargin() == null) ? 0 : pqd.getProfitMargin());
                                    }
                                }
                            }
                        } // end for each pqd
                    } // end for each pd
                    if (countMonth == 2 || countMonth == 5 || countMonth == 8 || countMonth == 11) {
//                        System.err.println("quarter month == " + countMonth);
                        quarterRevenue.add(revenue.toString()); // index 0
                        quarterRevenue.add(profitLoss.toString()); // index 1
                        yearRevenue.add(quarterRevenue); // will add 4 times
                        revenue = 0.0;
                        profitLoss = 0.0;
                    }
                    countMonth++;
                } // end for each month
                // we need to add the whole 12 values of month revenue to allMachines.get(machineNumber).get(year)
                allMachines.get(countMachine).add(yearRevenue);
                countYear = countYear + 1;
            } // end for each year
            countMachine = countMachine + 1;
        } // end for each machine
//        System.out.println("expect size 7 machines ===== " + allMachines.size()); // expect size 7
//        System.out.println("expect size 6 years ===== " + allMachines.get(0).size()); // expect size 6
//        System.out.println("expect size 12 months ===== " + allMachines.get(0).get(0).size()); // expect size 12
//        System.out.println("expect size 2 ===== " + allMachines.get(0).get(0).get(0).size()); // expect size 2

        return allMachines;
    }

    public List<List<List<List<String>>>> deriveRevenueGraph_Month() {
        // machine -> years -> months -> revenue
        List<List<List<String>>> machine1 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine2 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine3 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine4 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine5 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine6 = new ArrayList<List<List<String>>>();
        List<List<List<String>>> machine7 = new ArrayList<List<List<String>>>();

        // machines -> machine -> years -> months -> revenue
        List<List<List<List<String>>>> allMachines = new ArrayList<List<List<List<String>>>>();
        allMachines.add(machine1);
        allMachines.add(machine2);
        allMachines.add(machine3);
        allMachines.add(machine4);
        allMachines.add(machine5);
        allMachines.add(machine6);
        allMachines.add(machine7);

        ArrayList<String> years = new ArrayList<String>();
        years.add("2010");
        years.add("2011");
        years.add("2012");
        years.add("2013");
        years.add("2014");
        years.add("2015");

        ArrayList<String> months = new ArrayList<String>();
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

        ArrayList<String> machines = new ArrayList<String>();
        machines.add("LWI Small Chamber");
        machines.add("LWI V Flexx");
        machines.add("LWI V ERGO");
        machines.add("LWI V T-BaseV3");
        machines.add("LWI V MobileFlexx");
        machines.add("LWI V Unixx III");
        machines.add("LWI V UltraFlexx");

        ArrayList<ProductQuotation> productQuotationList = new ArrayList<ProductQuotation>();
        Query query = em.createQuery("SELECT q FROM ProductQuotation AS q");
        productQuotationList.addAll(query.getResultList());

        int countMachine = 0;
        // for each machine
        for (String machine : machines) {
            int countYear = 0;
            // for each year
            for (String year : years) {
                List<List<String>> yearRevenue = new ArrayList<List<String>>();
                // for each month
                for (String month : months) {
                    Double revenue = 0.0;
                    Double profitLoss = 0.0;
                    ArrayList<String> monthRevenue = new ArrayList<String>();
                    // for each pd
                    for (ProductQuotation pq : productQuotationList) {
                        List<ProductQuotationDescription> pqdList = pq.getProductQuotationDescriptionList();
                        // for each pqd
                        for (ProductQuotationDescription pqd : pqdList) {
                            if (pqd.getItemName().equals(machine)) {
                                if (formatDate_toYear(pq.getDate()).equals(year)) {
                                    if (formatDate_toMonth(pq.getDate()).equals(month)) {
                                        revenue = revenue + ((pqd.getQuotedPrice() == null) ? 0 : pqd.getQuotedPrice());
                                        profitLoss = profitLoss + ((pqd.getProfitMargin() == null) ? 0 : pqd.getProfitMargin());
                                    }
                                }
                            }
                        } // end for each pqd
                    } // end for each pd
//                    System.err.println("month month == " + month);
                    monthRevenue.add(month); // index 0
                    monthRevenue.add(revenue.toString()); // index 1
                    monthRevenue.add(profitLoss.toString()); // index 2
                    // System.out.println("countMachine countYear machine year month.......................... " + countMachine + " " + countYear + " " + machine + " " + year + " " + month);
                    yearRevenue.add(monthRevenue);
                } // end for each month
                // we need to add the whole 12 values of month revenue to allMachines.get(machineNumber).get(year)
                //System.out.println("yearRevenue.size() === " + yearRevenue.size());
                allMachines.get(countMachine).add(yearRevenue);
                countYear = countYear + 1;
            } // end for each year
            countMachine = countMachine + 1;
        } // end for each machine
//        System.out.println("expect size 7 machines ===== " + allMachines.size()); // expect size 7
//        System.out.println("expect size 6 years ===== " + allMachines.get(0).size()); // expect size 6
//        System.out.println("expect size 12 months ===== " + allMachines.get(0).get(0).size()); // expect size 12
//        System.out.println("expect size 2 ===== " + allMachines.get(0).get(0).get(0).size()); // expect size 2

        return allMachines;
    }

    public String formatDate_toYear(Timestamp date) {
        if (date != null) {
            Date date1 = new Date(date.getTime());
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            return format.format(date1);
        } else {
            return "";
        }
    }

    public String formatDate_toMonth(Timestamp date) {
        if (date != null) {
            Date date1 = new Date(date.getTime());
            SimpleDateFormat format = new SimpleDateFormat("MMM");
            return format.format(date1);
        } else {
            return "";
        }
    }

}
