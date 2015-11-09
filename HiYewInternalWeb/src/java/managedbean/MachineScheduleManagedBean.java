/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.MachineEntity;
import entity.Project;
import entity.WeldJob;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.extensions.model.timeline.TimelineEvent;

import org.primefaces.extensions.model.timeline.TimelineModel;
import session.stateless.MachineSystemBeanLocal;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author User
 */
@Named(value = "machineScheduleManagedBean")
@ViewScoped
public class MachineScheduleManagedBean implements Serializable{

    @EJB
    private MachineSystemBeanLocal machineSystemBean;
    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    private ArrayList<Project> curStartedProjects;
    private TimelineModel model;
    private String locale; // current locale as String, java.util.Locale is possible too.  
    private Date start;
    private Date end;

    @PostConstruct
    protected void initialize() {

        ArrayList<MachineEntity> machines = new ArrayList<>(machineSystemBean.getAllMachine());

        // set initial actualStart / PlannedEnd dates for the axis of the timeline  
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date now = new Date();

        cal.setTimeInMillis(now.getTime() - 4 * 60 * 60 * 1000);
        start = cal.getTime();

        cal.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);
        end = cal.getTime();

        // create timeline model
        model = new TimelineModel();

        Date actualStart = new Date();
        Date plannedEnd = new Date();

        long r = -1;
        Boolean hasJob;
        curStartedProjects = new ArrayList<>(projectSessionBean.getUncompletedProjects());

        for (int i = 0; i < machines.size(); i++) {
            hasJob = false;
            if (curStartedProjects != null) {

                for (Project p : curStartedProjects) {
                    for (WeldJob w : p.getWeldJobs()) {
                        if (w.getMachine().getId().equals(machines.get(i).getId())) {
                            if (p.getActualStart() != null) {
                                r = 1;
                                actualStart = new Date(p.getActualStart().getTime());
                                //System.out.println("--");
                            } else {
                                actualStart = new Date(p.getPlannedStart().getTime());
                                r = 0;
                                //System.out.println("!!");
                            }
                            plannedEnd = new Date(p.getPlannedEnd().getTime());

                            String availability = (r == 0 ? "notStarted" : (r == 1 ? "started" : "noJob"));
                            String status = "";
                            if (r == 0) {
                                status = "Not Started";
                            } else if (r == 1) {
                                status = "Started";
                            } else {
                                status = " ";
                            }

                            //  create an event with content, actualStart / PlannedEnd dates, editable flag, group name and custom style class  
                            TimelineEvent event = new TimelineEvent(status, actualStart, plannedEnd, false, machines.get(i).getMachine_number(), availability.toLowerCase());
                            model.add(event);
                            //reintialise date
                            actualStart = new Date();
                            plannedEnd = new Date();
                            r = -1;
                            hasJob = true;
                        }
                    }
                }

            }
            if (hasJob == true) {
                continue;
            }
            //System.out.println("Count is " + i);
            //System.out.println("Actual date is " + actualStart);
            //System.out.println("Plan End is " + plannedEnd);
            String availability = (r == 0 ? "notStarted" : (r == 1 ? "started" : "noJob"));
            String status = "";
            if (r == 0) {
                status = "Not Started";
            } else if (r == 1) {
                status = "Started";
            } else {
                status = " ";
            }

            //  create an event with content, actualStart / PlannedEnd dates, editable flag, group name and custom style class  
            TimelineEvent event = new TimelineEvent(status, actualStart, plannedEnd, false, machines.get(i).getMachine_number(), availability.toLowerCase());
            model.add(event);
            //reintialise date
            actualStart = new Date();
            plannedEnd = new Date();
            r = -1;
        }
    }

    public Timestamp convertDateToTimestamp(Date date) {
        Timestamp t = new Timestamp(date.getTime());
        return t;
    }

    public TimelineModel getModel() {
        return model;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

}
