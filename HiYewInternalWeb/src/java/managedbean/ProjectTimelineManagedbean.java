/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Project;
import java.io.Serializable;
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
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author Jitcheong
 */
@Named(value = "projectManagedbean")
@ViewScoped
public class ProjectTimelineManagedbean implements Serializable {

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

        curStartedProjects = new ArrayList<>(projectSessionBean.getAllCurrentStartedProjects());

        if (curStartedProjects != null) {

            // create timeline model  
            model = new TimelineModel();

            for (Project p : curStartedProjects) {
                
                Date actualStart = new Date(p.getActualStart().getTime());
                Date plannedEnd = new Date(p.getPlannedEnd().getTime());

                //long r = Math.round(Math.random() * 2);
                long r = 0;
                try {
                    Date actualEnd = new Date(p.getActualEnd().getTime());
                    if (actualEnd.before(plannedEnd)) {
                        r = 1;
                    }
                } catch (NullPointerException e) {
                    r = 0;
                }

                String availability = (r == 0 ? "Unavailable" : "Available");

                // create an event with content, actualStart / PlannedEnd dates, editable flag, group name and custom style class  
                TimelineEvent event = new TimelineEvent(availability, actualStart, plannedEnd, false, p.getProjectNo(), availability.toLowerCase());
                model.add(event);

            }
        }
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
