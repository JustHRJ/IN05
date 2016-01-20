/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Customer;
import entity.FillerEntity;
import entity.Metal;
import entity.Project;
import entity.ShelveEntity;
import entity.WeldJob;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.HiYewDSSSessionBeanLocal;
import session.stateless.HiYewICSSessionBeanLocal;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author User
 */
@Named(value = "projManagedBean")
@ViewScoped
public class ProjectsManagedBean implements Serializable {

    @EJB
    private HiYewDSSSessionBeanLocal hiYewDSSSessionBean;
    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    private ArrayList<Project> projects;
    private Map<String, String> projectStatuses;
    private ArrayList<Project> receivedProjects;
    private ArrayList<Project> filteredProj;

    private ArrayList<WeldJob> receivedWeldJobs;

    //for project initiation and closure
    private ArrayList<Project> receivedProjectByProjectNo;
    private Map<String, String> ongoingProjectNos;
    private String selectedProjectNo = "";
    private Project selectedProject;
    private Boolean actualStartVisibility;
    private Boolean actualEndVisibility;
    private Boolean updateBtnVisibility;
    private Date today = new Date();
    private Date actualStart;
    private Date actualEnd;

    private String fillerCode = "";
    private HashMap map;
    private ArrayList<String> toPickFrom;

    /**
     * Creates a new instance of ProjectsManagedBean
     */
    public ProjectsManagedBean() {
        projects = new ArrayList<>();
        projectStatuses = new HashMap<>();
        ongoingProjectNos = new HashMap<>();
        receivedProjects = new ArrayList<>();
        receivedWeldJobs = new ArrayList<>();
        selectedProject = new Project();
        map = new HashMap();
    }

    @PostConstruct
    public void init() {
        projectStatuses.put("Starting", "Starting");
        projectStatuses.put("Ongoing", "Ongoing");
        projectStatuses.put("Completed", "Completed");

        receivedProjects = new ArrayList<>(projectSessionBean.getAllProjects());
        //for project initiation and closure
        for (Project p : projectSessionBean.getUncompletedProjects()) {
            ongoingProjectNos.put(p.getProjectNo(), p.getProjectNo());
        }
        actualStartVisibility = false;
        actualEndVisibility = false;
        updateBtnVisibility = (actualEndVisibility == false) ? actualStartVisibility : actualEndVisibility;

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("resourceAvailabilityMessage");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fillerPickUpGuideMessage");

    }

    public void filterByProjectNo() {
        toPickFrom = new ArrayList<String>();
        receivedProjectByProjectNo = new ArrayList<>(projectSessionBean.getProjectByProjectNo(selectedProjectNo));
        if (!receivedProjectByProjectNo.isEmpty()) {
            selectedProject = receivedProjectByProjectNo.get(0);
            receivedWeldJobs = new ArrayList<>(selectedProject.getWeldJobs());

            //////do here
            if (selectedProject.getActualStart() == null) {
                System.out.print("hereeeee1######################numOfWeldJobs: " + receivedWeldJobs.size());
                for (int i = 0; i < receivedWeldJobs.size(); i++) {

                    WeldJob wj = receivedWeldJobs.get(i);

                    double surfaceAreaToWeld = wj.getSurfaceArea();
                    int qtyToweld = wj.getTotalQuantity();
                    Metal m = hiYewDSSSessionBean.getExistingMetal(wj.getMetal1());
                    Metal m2 = hiYewDSSSessionBean.getExistingMetal(wj.getMetal2());
                    System.out.print("hereeeee1######################SA: " + surfaceAreaToWeld);
                    System.out.print("hereeeee1######################QtyToWeld: " + qtyToweld);

                    FillerEntity recommendedFiller = new FillerEntity();
                    double lowestPrice = 9999999;
                    if ((m != null) || (m2 != null)) {
                        System.out.print("hereeeee1######################");
                        //System.out.print("hereeeee1######################metal 1: " + m.getMetalName());
                        //System.out.print("hereeeee1######################metal 2: " + m2.getMetalName());
                        ArrayList<FillerEntity> listOfMatchingFillers = new ArrayList<FillerEntity>();

                        if ((m != null) && (m.equals(m2))) {
                            System.out.print("hereeeee1######################both metal same");
                            listOfMatchingFillers.addAll(hiYewDSSSessionBean.getListOfMatchedFillers(m));
                        } else if ((m != null) && (m2 == null)) {
                            System.out.print("hereeeee1######################no metal 2");
                            listOfMatchingFillers.addAll(hiYewDSSSessionBean.getListOfMatchedFillers(m));
                        } else if ((m == null) && (m2 != null)) {
                            System.out.print("hereeeee1######################no metal 1");
                            listOfMatchingFillers.addAll(hiYewDSSSessionBean.getListOfMatchedFillers(m2));
                        } else {
                            System.out.print("hereeeee1######################metal not same");
                            listOfMatchingFillers.addAll(hiYewDSSSessionBean.getListOfMatchedFillers(m));
                            listOfMatchingFillers.addAll(hiYewDSSSessionBean.getListOfMatchedFillers(m2));
                        }
                        System.out.print("hereeeee1######################matching fillers size " + listOfMatchingFillers.size());
                        for (int j = 0; j < listOfMatchingFillers.size(); j++) {
                            FillerEntity f = listOfMatchingFillers.get(j);
                            System.out.print("hereeeee1######################now the filler is " + f.getFillerCode());
                            int numNeeded = hiYewDSSSessionBean.quantityNeeded(f, surfaceAreaToWeld, qtyToweld);
                            System.out.print("hereeeee1######################num needed " + numNeeded);
                            double fillerPrice = numNeeded * f.getCost();
                            int qtyLeft = (f.getQuantity() - f.getBookedQuantity()) - numNeeded;
                            if (qtyLeft >= 0) {
                                System.out.println("*************************enuff qty");
                                //check whish has the lowest price...
                                if (fillerPrice < lowestPrice) {
                                    System.out.println("*************************price lower");
                                    lowestPrice = fillerPrice;
                                    recommendedFiller = f;
                                }

                            }

                        }
                        System.out.print("hereeeee1######################recommended filler is " + recommendedFiller.getFillerCode());
                        if (recommendedFiller.getFillerCode() != null) {
                            System.out.print("hereeeee1######################set filler");
                            wj.setFiller(recommendedFiller);
                            projectSessionBean.conductWeldJobMerge(wj);
                        }
                    } else {
                        System.out.print("hereeeee1######################both metal null");
                    }

                    //else if either 1 metal is null
                }

            }

            //set visibility
            if (selectedProject.getActualStart() == null) {
                actualStartVisibility = true;
                actualEndVisibility = false;
            } else {
                actualStartVisibility = false;
                actualEndVisibility = true;
            }

        } else {
            actualStartVisibility = false;
            actualEndVisibility = false;
        }
        updateBtnVisibility = (actualEndVisibility == false) ? actualStartVisibility : actualEndVisibility;

    }

    public void updateProjectActualDate() {
        if (actualStart != null) {
            System.out.println("For haven started project........");
            selectedProject.setActualStart(new Timestamp(actualStart.getTime()));
            selectedProject.setProjectProgress(100);
            actualStart = null;
            actualStartVisibility = false;

            //should be do here
            ArrayList<WeldJob> jobList = new ArrayList<WeldJob>();
            jobList.addAll(selectedProject.getWeldJobs());
            for (int i = 0; i < jobList.size(); i++) {
                System.out.println("++++++++++++++++++++++++++++jobList.size() " + jobList.size());
                if (jobList.get(i).getFiller() != null) {
                    FillerEntity f = jobList.get(i).getFiller();
                    int qtyNeeded = hiYewDSSSessionBean.quantityNeeded(f, jobList.get(i).getSurfaceArea(), jobList.get(i).getTotalQuantity());
                    System.out.println("++++++++++++++++++++++++++++filler to reduce " + f.getFillerCode());
                    System.out.println("++++++++++++++++++++++++++++qty needed " + qtyNeeded);
                    map = hiYewDSSSessionBean.whichShelveToTake(f, qtyNeeded);
                    // Get a set of the entries
                    Set set = map.entrySet();
                    // Get an iterator
                    Iterator it = set.iterator();
                    // Display elements
                    while (it.hasNext()) {
                        Map.Entry me = (Map.Entry) it.next();
                        ShelveEntity shelveToTakeFrom = (ShelveEntity) me.getKey();
                        int qtyToTake = (int) me.getValue();
                        System.out.println("++++++++++++++++++++++++++++shelve to take from " + shelveToTakeFrom.getShelveID());
                        System.out.println("++++++++++++++++++++++++++++qty to take " + qtyToTake);
                        hiYewICSSessionBean.reduceStorageQty(f, shelveToTakeFrom, qtyToTake);
                        toPickFrom.add("Please retrieve " + qtyToTake + " of " + f.getFillerCode() + " from Shelf " + shelveToTakeFrom.getShelveID());
                    }
                    System.out.println("++++++++++++++++++++++++++++toPickFrom size " + toPickFrom.size());
                    System.out.println("++++++++++++++++++++++++++++before stock down filler " + f.getFillerCode());
                    System.out.println("++++++++++++++++++++++++++++qty to reduce " + qtyNeeded);
                    hiYewICSSessionBean.stockDown(f, qtyNeeded);
                }
            }
            String pickGuide = "";
            for (int i = 0; i < toPickFrom.size(); i++) {
                pickGuide = pickGuide + "" + String.format(toPickFrom.get(i) + "%n");
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fillerPickUpGuideMessage", pickGuide);
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("icsPickingGuide") != null) {
                String existing = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("icsPickingGuide").toString();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("icsPickingGuide", existing + "" + pickGuide);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("icsPickingGuide", pickGuide);
            }
        }
        if (actualEnd != null) {
            selectedProject.setActualEnd(new Timestamp(actualEnd.getTime()));
            selectedProject.setProjectCompletion(true);
            actualEnd = null;
            actualEndVisibility = false;
        }
        updateBtnVisibility = false;
        projectSessionBean.conductProjectMerge(selectedProject);

        for (int i = 0; i < receivedWeldJobs.size(); i++) {
            projectSessionBean.conductWeldJobMerge(receivedWeldJobs.get(i));
        }

        String str = "";
        if (selectedProject.getActualEnd() == null) { //after setting start date
            for (int i = 0; i < selectedProject.getWeldJobs().size(); i++) {
                str += selectedProject.getWeldJobs().get(i).getEmpName() + " has been attached to a welding job using Machine ";
                str += selectedProject.getWeldJobs().get(i).getMachine().getMachine_number() + "\r\n";

                projectSessionBean.setEmployeeAvailability(selectedProject.getWeldJobs().get(i).getEmpName(), false);
                projectSessionBean.setMachineAvailability(selectedProject.getWeldJobs().get(i).getMachine().getMachine_number(), "In use");

            }

        } else { // after setting ending date
            for (int i = 0; i < selectedProject.getWeldJobs().size(); i++) {
                str += selectedProject.getWeldJobs().get(i).getEmpName() + " are currently ready for new welding job assignment.\r\n";
                str += "Machine " + selectedProject.getWeldJobs().get(i).getMachine().getMachine_number() + " are currently available for use.\r\n";

                projectSessionBean.setEmployeeAvailability(selectedProject.getWeldJobs().get(i).getEmpName(), true);
                projectSessionBean.setMachineAvailability(selectedProject.getWeldJobs().get(i).getMachine().getMachine_number(), "Available");
                System.out.println("employee and machine released");

                WeldJob job = selectedProject.getWeldJobs().get(i);
                job.setDuration(calculateDuration(selectedProject.getActualEnd(), selectedProject.getActualStart()));
                projectSessionBean.saveWeldJob(job);
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("resourceAvailabilityMessage", str);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "orange");
    }

    private Integer calculateDuration(Timestamp end, Timestamp start) {
        Date end1 = new Date(end.getTime());
        Date start1 = new Date(start.getTime());
        int diffInDays = (int) ((end1.getTime() - start1.getTime())
                / (1000 * 60 * 60 * 24));
        return (Integer) diffInDays + 1;
    }

    public void onEditRow(RowEditEvent event) {
        WeldJob w = (WeldJob) event.getObject();//gives me unedited value
        FillerEntity f = hiYewICSSessionBean.getExistingItem(fillerCode);

        w.setFiller(f);
        projectSessionBean.conductWeldJobMerge(w);
    }

    public void getAllProjects() {
        receivedProjects = new ArrayList<>(projectSessionBean.getAllProjects());
    }

    /**
     * @return the receivedProjects
     */
    public ArrayList<Project> getReceivedProjects() {
        return receivedProjects;
    }

    /**
     * @param receivedProjects the receivedProjects to set
     */
    public void setReceivedProjects(ArrayList<Project> receivedProjects) {
        this.receivedProjects = receivedProjects;
    }

    public String formatDate(Timestamp t) {

        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        if (t != null) {
            return sd.format(t.getTime());
        }
        return "";
    }

    public String getDurationForCompletedProjects(Boolean completion, Timestamp start, Timestamp end) {
        String dur = "";
        if (completion == true) {
            dur = projectSessionBean.getDifferenceDays(start, end).toString();
        }
        return dur;
    }

    public String getProjectStatus(Boolean completion) {
        String status = "Y";
        if (completion == false) {
            status = "N";
        }
        return status;
    }

    public String getCustomerName(String username) {
        Customer c = customerSessionBean.getCustomerByUsername(username);
        if (c != null) {
            return c.getName();
        }
        return "";
    }

    /**
     * @return the filteredProj
     */
    public ArrayList<Project> getFilteredProj() {
        return filteredProj;
    }

    /**
     * @param filteredProj the filteredProj to set
     */
    public void setFilteredProj(ArrayList<Project> filteredProj) {
        this.filteredProj = filteredProj;
    }

    public Integer compareActualEndAndPlannedEnd(Boolean completion, Timestamp actualEnd, Timestamp plannedEnd) {
        if (completion == true) {
            Integer value = projectSessionBean.getDifferenceDays(actualEnd, plannedEnd);
            return value;
        }
        return 1;
    }

    public void selectProject(Project p) {
        receivedWeldJobs = new ArrayList<>(projectSessionBean.getWeldJobs(p));
    }

    /**
     * @return the receivedWeldJobs
     */
    public ArrayList<WeldJob> getReceivedWeldJobs() {
        return receivedWeldJobs;
    }

    /**
     * @param receivedWeldJobs the receivedWeldJobs to set
     */
    public void setReceivedWeldJobs(ArrayList<WeldJob> receivedWeldJobs) {
        this.receivedWeldJobs = receivedWeldJobs;
    }

    /**
     * @return the ongoingProjectNos
     */
    public Map<String, String> getOngoingProjectNos() {
        return ongoingProjectNos;
    }

    /**
     * @param ongoingProjectNos the ongoingProjectNos to set
     */
    public void setOngoingProjectNos(Map<String, String> ongoingProjectNos) {
        this.ongoingProjectNos = ongoingProjectNos;
    }

    /**
     * @return the selectedProjectNo
     */
    public String getSelectedProjectNo() {
        return selectedProjectNo;
    }

    /**
     * @param selectedProjectNo the selectedProjectNo to set
     */
    public void setSelectedProjectNo(String selectedProjectNo) {
        this.selectedProjectNo = selectedProjectNo;
    }

    /**
     * @return the receivedProjectByProjectNo
     */
    public ArrayList<Project> getReceivedProjectByProjectNo() {
        return receivedProjectByProjectNo;
    }

    /**
     * @param receivedProjectByProjectNo the receivedProjectByProjectNo to set
     */
    public void setReceivedProjectByProjectNo(ArrayList<Project> receivedProjectByProjectNo) {
        this.receivedProjectByProjectNo = receivedProjectByProjectNo;
    }

    /**
     * @return the actualStartVisibility
     */
    public Boolean getActualStartVisibility() {
        return actualStartVisibility;
    }

    /**
     * @param actualStartVisibility the actualStartVisibility to set
     */
    public void setActualStartVisibility(Boolean actualStartVisibility) {
        this.actualStartVisibility = actualStartVisibility;
    }

    /**
     * @return the actualEndVisibility
     */
    public Boolean getActualEndVisibility() {
        return actualEndVisibility;
    }

    /**
     * @param actualEndVisibility the actualEndVisibility to set
     */
    public void setActualEndVisibility(Boolean actualEndVisibility) {
        this.actualEndVisibility = actualEndVisibility;
    }

    /**
     * @return the selectedProject
     */
    public Project getSelectedProject() {
        return selectedProject;
    }

    /**
     * @param selectedProject the selectedProject to set
     */
    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    /**
     * @return the today
     */
    public Date getToday() {
        return today;
    }

    /**
     * @param today the today to set
     */
    public void setToday(Date today) {
        this.today = today;
    }

    /**
     * @return the updateBtnVisibility
     */
    public Boolean getUpdateBtnVisibility() {
        return updateBtnVisibility;
    }

    /**
     * @param updateBtnVisibility the updateBtnVisibility to set
     */
    public void setUpdateBtnVisibility(Boolean updateBtnVisibility) {
        this.updateBtnVisibility = updateBtnVisibility;
    }

    /**
     * @return the actualStart
     */
    public Date getActualStart() {
        return actualStart;
    }

    /**
     * @param actualStart the actualStart to set
     */
    public void setActualStart(Date actualStart) {
        this.actualStart = actualStart;
    }

    /**
     * @return the actualEnd
     */
    public Date getActualEnd() {
        return actualEnd;
    }

    /**
     * @param actualEnd the actualEnd to set
     */
    public void setActualEnd(Date actualEnd) {
        this.actualEnd = actualEnd;
    }

    /**
     * @return the fillerCode
     */
    public String getFillerCode() {
        return fillerCode;
    }

    /**
     * @param fillerCode the fillerCode to set
     */
    public void setFillerCode(String fillerCode) {
        this.fillerCode = fillerCode;
    }

    /**
     * @return the toPickFrom
     */
    public ArrayList<String> getToPickFrom() {
        return toPickFrom;
    }

    /**
     * @param toPickFrom the toPickFrom to set
     */
    public void setToPickFrom(ArrayList<String> toPickFrom) {
        this.toPickFrom = toPickFrom;
    }

}
