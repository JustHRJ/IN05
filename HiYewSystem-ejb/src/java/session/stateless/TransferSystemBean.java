/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.DocumentControlEntity;
import entity.FillerEntity;
import entity.MachineEntity;
import entity.Project;
import entity.WeldJob;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author JustHRJ
 */
@Stateless
public class TransferSystemBean implements TransferSystemBeanLocal {

    @PersistenceContext
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void addProjects(List<Vector> result) {
        if (result != null) {
            for (Object o : result) {
                Vector im = (Vector) o;
                Project p = new Project();
                p.setProjectNo(im.get(0).toString());
                String record1 = im.get(1).toString();
                System.out.println(record1);
                if (record1.equals("null")) {
                    p.setActualEnd(null);
                } else {
                    Calendar c = Calendar.getInstance();
                    int year = Integer.parseInt(record1.substring(0, 4));
                    System.out.println(year);
                    int month = Integer.parseInt(record1.substring(5, 7)) - 1;
                    System.out.println(month);
                    int date = Integer.parseInt(record1.substring(8));
                    System.out.println(date);
                    c.set(year, month, date);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    System.out.println(c);

                    p.setActualEnd(new Timestamp(c.getTime().getTime()));
                }
                record1 = im.get(2).toString();
                System.out.println(record1);
                if (record1.equals("null")) {
                    p.setActualEnd(null);
                } else {
                    Calendar c = Calendar.getInstance();
                    int year = Integer.parseInt(record1.substring(0, 4));
                    System.out.println(year);
                    int month = Integer.parseInt(record1.substring(5, 7)) - 1;
                    System.out.println(month);
                    int date = Integer.parseInt(record1.substring(8));
                    System.out.println(date);
                    c.set(year, month, date);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    System.out.println(c);

                    p.setActualStart(new Timestamp(c.getTime().getTime()));
                }

                p.setCustomerKey(im.get(3).toString());

                record1 = im.get(4).toString();
                Calendar c = Calendar.getInstance();
                int year = Integer.parseInt(record1.substring(0, 4));
                System.out.println(year);
                int month = Integer.parseInt(record1.substring(5, 7)) - 1;
                System.out.println(month);
                int date = Integer.parseInt(record1.substring(8));
                System.out.println(date);
                c.set(year, month, date);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.MILLISECOND, 0);
                System.out.println(c);

                p.setLatestEnd(new Timestamp(c.getTime().getTime()));

                record1 = im.get(5).toString();
                c = Calendar.getInstance();
                year = Integer.parseInt(record1.substring(0, 4));
                System.out.println(year);
                month = Integer.parseInt(record1.substring(5, 7)) - 1;
                System.out.println(month);
                date = Integer.parseInt(record1.substring(8));
                System.out.println(date);
                c.set(year, month, date);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.MILLISECOND, 0);
                System.out.println(c);

                p.setLatestStart(new Timestamp(c.getTime().getTime()));

                record1 = im.get(6).toString();
                c = Calendar.getInstance();
                year = Integer.parseInt(record1.substring(0, 4));
                System.out.println(year);
                month = Integer.parseInt(record1.substring(5, 7)) - 1;
                System.out.println(month);
                date = Integer.parseInt(record1.substring(8));
                System.out.println(date);
                c.set(year, month, date);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.MILLISECOND, 0);
                System.out.println(c);

                p.setPlannedEnd(new Timestamp(c.getTime().getTime()));

                record1 = im.get(7).toString();
                c = Calendar.getInstance();
                year = Integer.parseInt(record1.substring(0, 4));
                System.out.println(year);
                month = Integer.parseInt(record1.substring(5, 7)) - 1;
                System.out.println(month);
                date = Integer.parseInt(record1.substring(8));
                System.out.println(date);
                c.set(year, month, date);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.MILLISECOND, 0);
                System.out.println(c);

                p.setPlannedStart(new Timestamp(c.getTime().getTime()));

                String record = im.get(8).toString();
                boolean check;
                if (record.equals("true")) {
                    check = true;
                } else {
                    check = false;
                }
                String folderName = p.getProjectNo();
                new File("C:\\Users\\User\\Desktop\\SouceTreeRepo\\IN05\\HiYewInternalWeb\\web\\projectDocuments\\" + folderName).mkdir();

                DocumentControlEntity d = new DocumentControlEntity();
                em.persist(d);
                p.setDocuments(d);
                p.setProjectCompletion(check);
                p.setProjectManager(im.get(9).toString());
                p.setProjectProgress(Integer.parseInt(im.get(10).toString()));
                String checkBoolean = im.get(11).toString();
                if (checkBoolean.equals("true")) {
                    p.setProjectOverrun(true);
                } else {
                    p.setProjectOverrun(false);
                }
                p.setProjectDaysExceed(Integer.parseInt(im.get(12).toString()));
                if (im.get(13).toString().equals("0")) {
                    p.setCauseOfDelay(null);
                } else {
                    p.setCauseOfDelay(im.get(13).toString());
                }
                em.persist(p);
            }
        }
    }

    public void addWelds(List<Vector> result) {
        if (result != null) {
            for (Object o : result) {
                Vector im = (Vector) o;
                WeldJob w = new WeldJob();
                w.setWeldJobId(Integer.parseInt(im.get(0).toString()));
                w.setEmpName(im.get(1).toString());
                w.setMetal1(im.get(2).toString());
                w.setMetal2(im.get(3).toString());
                w.setProjectNo(im.get(4).toString());
                w.setSurfaceArea(Double.parseDouble(im.get(5).toString()));
                String fillerCode = im.get(6).toString();
                FillerEntity f = em.find(FillerEntity.class, fillerCode);
                if (f == null) {
                    w.setFiller(null);
                } else {
                    w.setFiller(f);
                }
                Long machineId = Long.parseLong(im.get(7).toString());
                MachineEntity m = em.find(MachineEntity.class, machineId);
                if (m == null) {
                    w.setMachine(null);
                } else {
                    w.setMachine(m);
                }
                w.setTotalQuantity(Integer.parseInt(im.get(8).toString()));
                w.setQuantityWelded(Integer.parseInt(im.get(9).toString()));
                w.setWeldingType(im.get(10).toString());
                if (im.get(11).toString().equals("null")) {

                } else {
                    w.setDuration(Integer.parseInt(im.get(11).toString()));
                }
                String projectID = w.getProjectNo();
                Project p = em.find(Project.class, projectID);
                if (p == null) {
                    w.setProject(null);
                } else {
                    w.setProject(p);
                    p.getWeldJobs().add(w);
                    em.merge(p);
                }
                em.persist(w);
            }
        }

    }

    public void connectProjectWelds(List<Vector> result) {

    }

}
