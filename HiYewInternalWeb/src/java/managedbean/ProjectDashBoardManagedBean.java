/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Project;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

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

        ArrayList <Project> startedProjects = new ArrayList<>(projectSessionBean.getUncompletedStartedProjects());

        horizontalBarModel = new HorizontalBarChartModel();

        ChartSeries completed = new ChartSeries();
        ChartSeries uncompleted = new ChartSeries();

        for (Project p: startedProjects) {
            completed.setLabel("Completed");
            completed.set(p.getProjectNo(), 43);

            uncompleted.setLabel("Uncompleted");
            uncompleted.set(p.getProjectNo(), 52);
        }

        horizontalBarModel.addSeries(completed);
        horizontalBarModel.addSeries(uncompleted);

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
    
     private void createPieModel1() {
        pieModel1 = new PieChartModel();
         
        pieModel1.set("Engineer Shortfall", 540);
        pieModel1.set("Delivery Delays", 325);
        pieModel1.set("Reworks", 702);
        pieModel1.set("Others", 421);
         
        pieModel1.setTitle("Project Reason for Delays");
        pieModel1.setLegendPosition("w");
    }



}
