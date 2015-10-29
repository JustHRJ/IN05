/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Project;
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
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author Jitcheong
 */
@Named(value = "projectManagedbean")
@ViewScoped
public class ProjectTimelineManagedbean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    private ArrayList<Project> curStartedProjects;
    private TimelineModel model;
    private String locale; // current locale as String, java.util.Locale is possible too.  
    private Date start;
    private Date end;

    @PostConstruct
    protected void initialize() {

        // set initial actualStart / PlannedEnd dates for the axis of the timeline  
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date now = new Date();

        cal.setTimeInMillis(now.getTime() - 4 * 60 * 60 * 1000);
        start = cal.getTime();

        cal.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);
        end = cal.getTime();

        curStartedProjects = new ArrayList<>(projectSessionBean.getUncompletedProjects());

        if (curStartedProjects != null) {

            // create timeline model  
            model = new TimelineModel();
            long r = 0;
            Date actualStart = new Date();
            Date plannedEnd = new Date();
            for (Project p : curStartedProjects) {

                if (p.getActualStart() != null) {
                    actualStart = new Date(p.getActualStart().getTime());

                    if (p.getProjectProgress() != null) {
                        if (p.getProjectProgress() >= 90 && p.getProjectProgress() <= 110) {
                            r = 2;
                        } else if (p.getProjectProgress() > 110) {
                            r = 1;
                        } else if (p.getProjectProgress() < 90) {
                            r = 0;
                        }
                    }

                } else {
                    actualStart = new Date(p.getPlannedStart().getTime());
                    r = 3;
                }
                plannedEnd = new Date(p.getPlannedEnd().getTime());

                //long r = Math.round(Math.random() * 2);
                String availability = (r == 0 ? "behindSchedule" : (r == 1 ? "aheadSchedule" : (r == 2 ? "scheduleOnTrack" : "notStarted")));
                String status = "";
                if (r == 0) {
                    status = "Behind Schedule";
                } else if (r == 1) {
                    status = "Ahead Schedule";
                } else if (r == 2) {
                    status = "Schedule On Track";
                } else if (r == 3) {
                    status = "Not Started";
                }
                System.out.println("User name " + p.getCustomerKey());
                // create an event with content, actualStart / PlannedEnd dates, editable flag, group name and custom style class  
                TimelineEvent event = new TimelineEvent(status, actualStart, plannedEnd, false, customerSessionBean.getCustomerByUsername(p.getCustomerKey()).getName(), availability.toLowerCase());
                model.add(event);

            }
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
