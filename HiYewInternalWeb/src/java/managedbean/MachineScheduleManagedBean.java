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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
public class MachineScheduleManagedBean implements Serializable {

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
        List<MachineEntity> mList = machineSystemBean.getAllMachine();

//        Collections.sort(mList, new Comparator<MachineEntity>() {
//            public int compare(MachineEntity m1, MachineEntity m2) {
//                return m1.getMachine_number().compareTo(m2.getMachine_number());
//            }
//        });

        
        ArrayList<MachineEntity> machines = new ArrayList<>(bubbleSort(mList));
        for(int i =0;i<machines.size();i++){
            //System.out.println(machines.get(i).getMachine_number());
        }
        

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
            System.out.println("#################"+ machines.get(i).getMachine_number());
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
        //Collections.sort(model.getEvents(),new TimelineEventCompare());

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

    private static List<MachineEntity> bubbleSort(List<MachineEntity> mList) {

        List<MachineEntity> result = new ArrayList<MachineEntity>();
        int n = mList.size();
        MachineEntity m = new MachineEntity();
        int counterToHit = n;
        
        while(mList.size()>0){
        for(int i = 0;i<mList.size();i++){
            m = mList.get(i);
            int numOfSmallThan = 0;
            for(int j = 0;j<mList.size();j++){
                int num1 = Integer.parseInt(m.getMachine_number());
                int num2 = Integer.parseInt(mList.get(j).getMachine_number());
                if(num1<=num2){
                    numOfSmallThan++;
                }
            }
            if(numOfSmallThan==counterToHit){
                counterToHit = counterToHit -1;
                result.add(m);
                mList.remove(i);
            }
        }
        }
        return result;

}



}

class TimelineEventCompare implements Comparator<TimelineEvent> {

    @Override
    public int compare(TimelineEvent o1, TimelineEvent o2) {
        // write comparison logic here like below , it's just a sample
        return o1.getGroup().compareTo(o2.getGroup());
    }
}

class MachineCompare implements Comparator<MachineEntity> {

    @Override
    public int compare(MachineEntity o1, MachineEntity o2) {
        // write comparison logic here like below , it's just a sample
        return o1.getMachine_number().compareTo(o2.getMachine_number());
    }
}
