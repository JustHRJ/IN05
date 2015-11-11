/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Project;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.PieChartModel;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author Jitcheong
 */
@Named(value = "projectDashBoardManagedBean")
@ViewScoped
public class ProjectDashBoardManagedBean implements Serializable {

    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    private HorizontalBarChartModel horizontalBarModel;
    private PieChartModel pieModel1;
    //private PieChartModel pieModel2;
    Boolean drillDown = true;

    /**
     * Creates a new instance of ProjectDashBoardManagedBean
     */
    public ProjectDashBoardManagedBean() {

    }

    @PostConstruct
    public void init() {
        createBarModels();
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    private void createBarModels() {
        createPieModel1();
        createHorizontalBarModel();
    }

    private void createHorizontalBarModel() {

        ArrayList<Project> startedProjects = new ArrayList<>(projectSessionBean.getUncompletedStartedProjects());

        horizontalBarModel = new HorizontalBarChartModel();

        ChartSeries workDone = new ChartSeries();
        ChartSeries excess = new ChartSeries();

        ChartSeries lag = new ChartSeries();

        for (Project p : startedProjects) {

            int totalDur = projectSessionBean.getDifferenceDays(p.getActualStart(), p.getPlannedEnd());

            double actualTotalSAWelded = 0;
            double totalSAToBeWelded = 0;
            for (int i = 0; i < p.getWeldJobs().size(); i++) {
                //totalDur += p.getWeldJobs().get(i).getDuration();

                actualTotalSAWelded += p.getWeldJobs().get(i).getSurfaceArea() * p.getWeldJobs().get(i).getQuantityWelded();
                totalSAToBeWelded += p.getWeldJobs().get(i).getSurfaceArea() * p.getWeldJobs().get(i).getTotalQuantity();
            }

            Date today = new Date();
            double planTotalSAWelded = (totalSAToBeWelded / totalDur) * projectSessionBean.getDifferenceDays(p.getActualStart(), new Timestamp(today.getTime()));

            double diff = actualTotalSAWelded - planTotalSAWelded;

            workDone.setLabel("Work Done");
            excess.setLabel("Extra Work completed as Scheduled");

            lag.setLabel("Incompleted work as Scheduled");
            if (diff >= 0) { //excess

                workDone.set(p.getProjectNo(), (int) round((planTotalSAWelded / totalSAToBeWelded) * 100, 0));
                excess.set(p.getProjectNo(), (int) round(((diff) / totalSAToBeWelded) * 100, 0));
                lag.set(p.getProjectNo(), 0);

            } else if (diff < 0) { //lagging 

                workDone.set(p.getProjectNo(), (int) round((actualTotalSAWelded / totalSAToBeWelded) * 100, 0));
                lag.set(p.getProjectNo(), (int) round(((diff * (-1)) / totalSAToBeWelded) * 100, 0));
                excess.set(p.getProjectNo(), 0);
            }

            //uncompleted.setLabel("Incompleted Work as Scheduled");
            //uncompleted.set(p.getProjectNo(), 52);
        }
        horizontalBarModel.addSeries(workDone);
        horizontalBarModel.addSeries(excess);
        horizontalBarModel.addSeries(lag);

        horizontalBarModel.setShadow(true);
        horizontalBarModel.setBarPadding(15);
        horizontalBarModel.setTitle("Project Progress");
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("% Project Completion");
        xAxis.setMin(0);
        xAxis.setMax(100);

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Project");
    }

    public void createPieModel1() {
        pieModel1 = new PieChartModel();

        double manpower = 0, machines = 0, fillers = 0, other = 0, reworks = 0;
        ArrayList<Project> completedProjects = new ArrayList<>();
        if (projectSessionBean.getCompletedProjects() != null) {
            completedProjects = new ArrayList<>(projectSessionBean.getCompletedProjects());
        }
        System.out.println(completedProjects.size());
        for (Project p : completedProjects) {

            if (p.getCauseOfDelay() != null) {
                if (p.getCauseOfDelay().equalsIgnoreCase("Manpower")) {
                    manpower += 1;
                } else if (p.getCauseOfDelay().equalsIgnoreCase("Machine")) {
                    machines += 1;
                } else if (p.getCauseOfDelay().equalsIgnoreCase("Filler")) {
                    fillers += 1;
                } else if (p.getCauseOfDelay().equalsIgnoreCase("Rework")) {
                    reworks += 1;
                } else {
                    other += 1;
                    //System.out.println(p.getCauseOfDelay() + "--");
                }
            }
        }
        double total = manpower + machines + fillers + reworks + other;

        //pieModel1.set("Manpower", (Number)(round((manpower / total) * 100, 2)));
        //pieModel1.set("Machine",(round((machines / total) * 100, 2)));
        //pieModel1.set("Filler",(round((fillers / total) * 100, 2)));
        //pieModel1.set("Rework", (round((reworks / total) * 100, 2)));
        //pieModel1.set("Others", (round((other / total) * 100, 2)));
        pieModel1.set("Manpower", manpower);
        pieModel1.set("Machine", machines);
        pieModel1.set("Filler", fillers);
        pieModel1.set("Rework", reworks);
        pieModel1.set("Others", other);

        pieModel1.setShowDataLabels(true);
        pieModel1.setDataLabelFormatString("%.2f%%");
        pieModel1.setTitle("Reasons for Project Delays");
        pieModel1.setLegendPosition("w");
        pieModel1.setExtender("pieExtender");
        
        drillDown = true;
    }

    private void createPieModel2(Integer index) {
        pieModel1 = new PieChartModel();
        String category = "";

        ArrayList<Project> completedProjects = new ArrayList<>();
        if (projectSessionBean.getCompletedProjects() != null) {
            completedProjects = new ArrayList<>(projectSessionBean.getCompletedProjects());
        }

        if (index == 0) {
            category = "Manpower";
        } else if (index == 1) {
            category = "Machine";
        } else if (index == 2) {
            category = "Filler";
        } else if (index == 3) {
            category = "Rework";
        } else {
            category = "Others";
        }

        //System.out.println(completedProjects.size());
        ArrayList<Project> filterProject = new ArrayList<>();
        for (Project p : completedProjects) {

            if (p.getCauseOfDelay() != null) {
                if (p.getCauseOfDelay().equalsIgnoreCase(category)) {
                    filterProject.add(p);
                }
            }

        }
        double tier1 = 0, tier2 = 0, tier3 = 0, tier4 = 0;
        for (Project p : filterProject) {
            double dur = projectSessionBean.getDifferenceDays(p.getPlannedStart(), p.getPlannedEnd());
            double days = projectSessionBean.getDifferenceDays(p.getPlannedEnd(), p.getActualEnd());
            //double percent = (days / dur) * 100;
            double percent = days;
            if (percent <= 5) {
                tier1++;
            } else if (percent > 5 && percent <= 10) {
                tier2++;
            } else if (percent > 10 && percent <= 15) {
                tier3++;
            } else if (percent > 15) {
                tier4++;
            }
        }

        pieModel1.set("&le; 5%", tier1);
        pieModel1.set("(6 - 10) %", tier2);
        pieModel1.set("11-15%", tier3);
        pieModel1.set("> 15%", tier4);

        pieModel1.setShowDataLabels(true);
        pieModel1.setDataLabelFormatString("%.2f%%");
        pieModel1.setTitle("Extent of Delays (" + category + ")");
        pieModel1.setLegendPosition("w");
        pieModel1.setExtender("pieExtender");
        
        
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
        if (drillDown == true) {
            createPieModel2(event.getItemIndex());
            drillDown = false;
        }

    }

    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
