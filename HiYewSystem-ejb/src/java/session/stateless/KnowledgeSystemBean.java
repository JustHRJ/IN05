/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerComposition;
import entity.Metal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JustHRJ
 */
@Stateless
public class KnowledgeSystemBean implements KnowledgeSystemBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    public void addMetal(List<Vector> metals) {
        Query q = em.createQuery("Select c from Metal c");
        for (Object o : q.getResultList()) {
            Metal m = (Metal) o;
            m.setFillers(null);
            em.merge(m);
            em.remove(m);
            em.flush();
        }

        if (metals != null) {
            for (Object o : metals) {
                Vector im = (Vector) o;
                Metal f = new Metal();
                f.setMetalName(im.get(0).toString());
                f.setCopper(Integer.parseInt(im.get(1).toString()));
                f.setZinc(Integer.parseInt(im.get(2).toString()));
                f.setIron(Integer.parseInt(im.get(3).toString()));
                f.setLead(Integer.parseInt(im.get(4).toString()));
                f.setAluminium(Integer.parseInt(im.get(5).toString()));
                f.setCarbon(Integer.parseInt(im.get(6).toString()));
                f.setNickel(Integer.parseInt(im.get(7).toString()));
                f.setManganese(Integer.parseInt(im.get(8).toString()));
                f.setSilicon(Integer.parseInt(im.get(9).toString()));
                f.setChromium(Integer.parseInt(im.get(10).toString()));
                em.persist(f);
            }
        }
        System.out.println("metal added");
    }

    public List<Metal> metalRecords() {
        List<Metal> results = new ArrayList<Metal>();
        Query q = em.createQuery("select c from Metal c");

        for (Object o : q.getResultList()) {
            Metal f = (Metal) o;

            results.add(f);
        }
        if (results.isEmpty()) {
            System.out.println("no metal records found");
            return null;
        }

        return results;
    }

    public void editMetal(Metal metal) {
        if (metal == null) {
            System.out.println("no metal info present");
        } else {
            em.merge(metal);
        }
    }

    public void deleteMetal(Metal metal) {
        if (metal == null) {
            System.out.println("no metal to delete");
        } else {
            String id = metal.getMetalName();
            Metal m = em.find(Metal.class, id);
            if (m == null) {
                System.out.println("entity failed to find id of metal");
            } else {
                em.remove(m);
                em.flush();
            }
        }
    }

    public List<String> retrieveFillerNames() {
        Query q = em.createQuery("select c from FillerComposition c");
        List<String> results = new ArrayList<String>();
        for (Object o : q.getResultList()) {
            FillerComposition f = (FillerComposition) o;
            results.add(f.getName());
        }
        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    public void addNewFiller(FillerComposition filler) {
        try {
            if (filler != null) {
                em.persist(filler);
            }
        } catch (Exception ex) {
            System.out.println("Filler cannot persist");
        }
    }

    public void addNewMetal(Metal metal) {
        try {
            if (metal != null) {
                Metal m = em.find(Metal.class, metal.getMetalName());
                if (m == null) {
                    em.persist(metal);
                }
            }
        } catch (Exception ex) {
            System.out.println("metal cannot persist");
        }
    }

    public List<String> metalNames() {
        List<String> result = new ArrayList<String>();
        Query q = em.createQuery("select c from Metal c");
        for (Object o : q.getResultList()) {
            Metal m = (Metal) o;
            result.add(m.getMetalName());
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public void createPairings(String metal, List<String> fillerChosen) {
        Metal m = new Metal();
        Collection<FillerComposition> fillers = new ArrayList<FillerComposition>();
        try {
            Query q = em.createQuery("select m from Metal m where m.metalName = :id");
            q.setParameter("id", metal);
            m = (Metal) q.getSingleResult();
            System.out.println("error did not start here");
            if (fillerChosen.isEmpty()) {
                System.out.println("no fillerchosen");
            }

            for (Object o : fillerChosen) {
                String fn = (String) o;
                System.out.println(fn);
                FillerComposition f = new FillerComposition();
                q = em.createQuery("select f from FillerComposition f where f.name =:id");
                q.setParameter("id", fn);
                f = (FillerComposition) q.getSingleResult();
                fillers.add(f);
            }
            System.out.println("error here3");
            m.setFillers(fillers);
            System.out.println("error here4");
            em.merge(m);
            System.out.println("Pairing created");

        } catch (Exception ex) {
            System.out.println("error occured");
        }
    }

    public List<String> FillersNotAssociated(String metalName) {
        Metal m = new Metal();
        List<String> result = new ArrayList<String>();
        try {
            Query q = em.createQuery("select c from FillerComposition c");
            Query z = em.createQuery("select m from Metal m where m.metalName = :id");
            z.setParameter("id", metalName);
            m = (Metal) z.getSingleResult();
            for (Object o : q.getResultList()) {
                FillerComposition f = (FillerComposition) o;
                if (m.getFillers().contains(f)) {
                    System.out.println("should not add: not associated");
                } else {
                    result.add(f.getName());
                }
            }
            if (result.isEmpty()) {
                return new ArrayList<String>();
            } else {
                return result;
            }

        } catch (Exception ex) {
            return new ArrayList<String>();
        }

    }

    public List<String> FillersAssociated(String metalName) {
        Metal m = new Metal();
        List<String> result = new ArrayList<String>();
        try {
            Query q = em.createQuery("select c from FillerComposition c");
            Query z = em.createQuery("select m from Metal m where m.metalName = :id");
            z.setParameter("id", metalName);
            m = (Metal) z.getSingleResult();
            for (Object o : q.getResultList()) {
                FillerComposition f = (FillerComposition) o;
                if (m.getFillers().contains(f)) {
                    result.add(f.getName());
                } else {
                    System.out.println("should not add: associated");

                }
            }
            if (result.isEmpty()) {
                return new ArrayList<String>();
            } else {
                return result;
            }

        } catch (Exception ex) {
            return new ArrayList<String>();
        }

    }

    public void addFillers(List<Vector> fillers) {
        Query z = em.createQuery("select c from Metal c");
        for (Object o : z.getResultList()) {
            Metal m = (Metal) o;
            m.setFillers(null);
            em.merge(m);
        }

        Query q = em.createQuery("select c from FillerComposition c");
        for (Object o : q.getResultList()) {
            FillerComposition f = (FillerComposition) o;
            em.remove(f);
        }
        if (fillers != null) {
            for (Object o : fillers) {
                Vector im = (Vector) o;
                FillerComposition f = new FillerComposition();
                f.setName((String) im.get(1));
                f.setCopper(Integer.parseInt(im.get(2).toString()));
                f.setZinc(Integer.parseInt(im.get(3).toString()));
                f.setIron(Integer.parseInt(im.get(4).toString()));
                f.setLead(Integer.parseInt(im.get(5).toString()));
                f.setAluminium(Integer.parseInt(im.get(6).toString()));
                f.setCarbon(Integer.parseInt(im.get(7).toString()));
                f.setNickel(Integer.parseInt(im.get(8).toString()));
                f.setManganese(Integer.parseInt(im.get(9).toString()));
                f.setSilicon(Integer.parseInt(im.get(10).toString()));
                f.setChromium(Integer.parseInt(im.get(11).toString()));
                em.persist(f);
            }
            System.out.println("ompleted");
        }
    }

    public List<Vector> transferFillerInfo() {
        List<Vector> results = new ArrayList<Vector>();
        Query q = em.createQuery("select c from FillerComposition c");
        System.out.println("shit");
        for (Object o : q.getResultList()) {
            FillerComposition f = (FillerComposition) o;
            Vector im = new Vector();
            im.add(f.getId());
            im.add(f.getName());
            im.add(f.getCopper());
            im.add(f.getZinc());
            im.add(f.getIron());
            im.add(f.getLead());
            im.add(f.getAluminium());
            im.add(f.getCarbon());
            im.add(f.getNickel());
            im.add(f.getManganese());
            im.add(f.getSilicon());
            im.add(f.getChromium());

            results.add(im);
        }
        if (results.isEmpty()) {
            System.out.println("here");
            return null;
        }
        System.out.println("collected");
        System.out.println(results.size());
        return results;
    }

    public List<Vector> transferMetalInfo() {
        List<Vector> results = new ArrayList<Vector>();
        Query q = em.createQuery("select c from Metal c");
        System.out.println("shit");
        for (Object o : q.getResultList()) {
            Metal f = (Metal) o;
            Vector im = new Vector();

            im.add(f.getMetalName());

            im.add(f.getCopper());
            im.add(f.getZinc());
            im.add(f.getIron());
            im.add(f.getLead());
            im.add(f.getAluminium());
            im.add(f.getCarbon());
            im.add(f.getNickel());
            im.add(f.getManganese());
            im.add(f.getSilicon());
            im.add(f.getChromium());

            results.add(im);
        }
        if (results.isEmpty()) {
            System.out.println("here");
            return null;
        }
        System.out.println("collected");
        System.out.println(results.size());
        return results;
    }

    public List<FillerComposition> fillerRecords() {

        List<FillerComposition> results = new ArrayList<FillerComposition>();
        Query q = em.createQuery("select c from FillerComposition c");

        for (Object o : q.getResultList()) {
            FillerComposition f = (FillerComposition) o;

            results.add(f);
        }
        if (results.isEmpty()) {
            System.out.println("here");
            return null;
        }

        return results;

    }

    public void deleteFiller(FillerComposition filler) {
        if (filler != null) {
            Long id = filler.getId();
            FillerComposition f = em.find(FillerComposition.class, id);
            if (f
                    == null) {
                System.out.println("no filler to delete");
            } else {
                em.remove(f);
            }
        }
    }

    public void editFiller(FillerComposition filler) {
        if (filler == null) {
            System.out.println("filler info is missing");
        } else {
            em.merge(filler);
        }
    }

    public void addMatch(List<Vector> results) {
        try {

            Metal m = new Metal();
            FillerComposition f = new FillerComposition();

            for (Object o : results) {
                Vector im = (Vector) o;
                String metalN = im.get(0).toString();
                Long id = Long.parseLong(im.get(1).toString());
                m
                        = em.find(Metal.class, metalN);
                if (m
                        == null) {
                    System.out.println("metal not found");
                } else {
                    f = em.find(FillerComposition.class, id);
                    if (f == null) {
                        System.out.println("filler not found");
                    } else {
                        m.getFillers().add(f);

                    }
                }

                em.merge(m);
            }
            System.out.println("fillers added to metals");

        } catch (Exception ex) {
            System.out.println("matching went wrong");
        }
    }

    public List<Vector> transferMatchingInfo() {
        try {
            List<Vector> result = new ArrayList<Vector>();
            Query q = em.createQuery("select c from Metal c");
            for (Object o : q.getResultList()) {
                Metal m = (Metal) o;
                if (m.getFillers().isEmpty()) {

                } else {
                    for (Object p : m.getFillers()) {
                        FillerComposition f = (FillerComposition) p;
                        Vector im = new Vector();
                        im.add(m.getMetalName());
                        im.add(f.getId());
                        result.add(im);
                    }
                }
            }

            if (result.isEmpty()) {
                return null;
            } else {
                return result;
            }

        } catch (Exception ex) {
            return null;
        }
    }

}
