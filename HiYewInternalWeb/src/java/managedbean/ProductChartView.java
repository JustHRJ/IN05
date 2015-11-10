package managedbean;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import session.stateless.ProductQuotationSessionBeanLocal;

@ManagedBean
public class ProductChartView implements Serializable {

    @EJB
    private ProductQuotationSessionBeanLocal productQuotationSessionBean;

    // main left
    private BarChartModel barChart_AnnualRevPLPerYear;
    // main right
    private LineChartModel lineChart_AnnualRevPLPerYearByProduct; // total profit/loss per year by machines

    // main left -> left
    private PieChartModel pieChartQuarter_all;
    private PieChartModel pieChartQuarter_PL_all;
    private CartesianChartModel combinedModel_all;
    // main left -> right
    private BarChartModel barChartMonth_all;

    // main right -> left
    private PieChartModel pieChartQuarter;
    private PieChartModel pieChartQuarter_PL;
    private CartesianChartModel combinedModel;
    // main right -> right
    private BarChartModel barChartMonth;

    private boolean visibility_First = true;
    private boolean visibility_FirstRight = false;
    private boolean visibility_FirstLeft = false;

    @PostConstruct
    public void init() {
        createAnimatedModel_AnnualRevPLPerYear();
        createAnimatedModel_AnnualRevPLPerYearByProduct();
    }

    // main left
    public BarChartModel getBarChart_AnnualRevPLPerYear() {
        return barChart_AnnualRevPLPerYear;
    }

    // main right
    public LineChartModel getLineChart_AnnualRevPLPerYearByProduct() {
        return lineChart_AnnualRevPLPerYearByProduct;
    }

    // main left -> right
    public BarChartModel getBarChartMonth_all() {
        return barChartMonth_all;
    }

    // main left -> left
    public PieChartModel getPieChartQuarter_all() {
        return pieChartQuarter_all;
    }

    // main left -> left
    public PieChartModel getPieChartQuarter_PL_all() {
        return pieChartQuarter_PL_all;
    }

    // main left -> left
    public CartesianChartModel getCombinedModel_all() {
        return combinedModel_all;
    }

    // main right -> right
    public BarChartModel getBarChartMonth() {
        return barChartMonth;
    }

    // main right -> left
    public PieChartModel getPieChartQuarter() {
        return pieChartQuarter;
    }

    // main right -> left
    public PieChartModel getPieChartQuarter_PL() {
        return pieChartQuarter_PL;
    }

    // main right -> left
    public CartesianChartModel getCombinedModel() {
        return combinedModel;
    }

    private void createAnimatedModel_AnnualRevPLPerYear() {
        barChart_AnnualRevPLPerYear = initBarModel_AnnualRevPLPerYear();
        barChart_AnnualRevPLPerYear.setTitle("Annual Revenue & Profit/Loss");
        barChart_AnnualRevPLPerYear.setShadow(true);
        barChart_AnnualRevPLPerYear.setNegativeSeriesColors("red");
        barChart_AnnualRevPLPerYear.setExtender("pieExtender");
        barChart_AnnualRevPLPerYear.setAnimate(true);
        barChart_AnnualRevPLPerYear.setLegendPosition("e");
        barChart_AnnualRevPLPerYear.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

        Axis yAxisYear = barChart_AnnualRevPLPerYear.getAxis(AxisType.Y);
        yAxisYear.setLabel("Number of thousands (Revenue $)");
        yAxisYear.setMin(-500000);
        yAxisYear.setMax(500000);
        yAxisYear.setTickInterval("100000");

        Axis xAxisYear = barChart_AnnualRevPLPerYear.getAxis(AxisType.X);
        xAxisYear.setLabel("Years");
        xAxisYear.setMin(2010);
        xAxisYear.setMax(2015);
        xAxisYear.setTickInterval("1");
    }

    private void createAnimatedModel_AnnualRevPLPerYearByProduct() {
        lineChart_AnnualRevPLPerYearByProduct = initLinearModel_AnnualRevPLPerYearByProduct();
        lineChart_AnnualRevPLPerYearByProduct.setTitle("Annual Profit/Loss by Product");
        lineChart_AnnualRevPLPerYearByProduct.setAnimate(true);
        lineChart_AnnualRevPLPerYearByProduct.setLegendPosition("e");
        lineChart_AnnualRevPLPerYearByProduct.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

        Axis yAxisYear = lineChart_AnnualRevPLPerYearByProduct.getAxis(AxisType.Y);
        yAxisYear.setLabel("Number of thousands (Revenue $)");
        yAxisYear.setMin(-200000);
        yAxisYear.setMax(200000);
        yAxisYear.setTickInterval("50000");

        Axis xAxisYear = lineChart_AnnualRevPLPerYearByProduct.getAxis(AxisType.X);
        xAxisYear.setLabel("Years");
        xAxisYear.setMin(2010);
        xAxisYear.setMax(2015);
        xAxisYear.setTickInterval("1");
    }

    private void createAnimatedModelMonth_all(Integer item, Integer series) {
        String year = "";
        if (item == 0) {
            year = "2010";
        } else if (item == 1) {
            year = "2011";
        } else if (item == 2) {
            year = "2012";
        } else if (item == 3) {
            year = "2013";
        } else if (item == 4) {
            year = "2014";
        } else if (item == 5) {
            year = "2015";
        }

        barChartMonth_all = initBarModelMonth_all(item, series);
        barChartMonth_all.setTitle("Monthly Profit/Loss Breakdown by Product for Year \"" + year + "\"");
        barChartMonth_all.setAnimate(true);
        barChartMonth_all.setLegendPosition("e");
        barChartMonth_all.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        barChartMonth_all.setExtender("pieExtender");

        Axis yAxisMonth = barChartMonth_all.getAxis(AxisType.Y);
        yAxisMonth.setLabel("Number of thousands (Revenue $)");
        yAxisMonth.setMin(0);
        yAxisMonth.setMax(100000);
        yAxisMonth.setTickInterval("10000");

        Axis xAxisMonth = barChartMonth_all.getAxis(AxisType.X);
        xAxisMonth.setLabel("Months");
    }

    private void createAnimatedModelQuarter_all(Integer item, Integer series) {
        String year = "";
        if (item == 0) {
            year = "2010";
        } else if (item == 1) {
            year = "2011";
        } else if (item == 2) {
            year = "2012";
        } else if (item == 3) {
            year = "2013";
        } else if (item == 4) {
            year = "2014";
        } else if (item == 5) {
            year = "2015";
        }

        pieChartQuarter_all = initPieModelQuarter_all(item, series);
        pieChartQuarter_all.setTitle("Quarterly Revenue Breakdown for Year " + year + "");
        pieChartQuarter_all.setLegendPosition("e");
        pieChartQuarter_all.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        pieChartQuarter_all.setFill(false);
        pieChartQuarter_all.setMouseoverHighlight(true);
        pieChartQuarter_all.setShowDataLabels(true);
        pieChartQuarter_all.setDiameter(140);
        pieChartQuarter_all.setExtender("pieExtender_pie");
        pieChartQuarter_all.setDataLabelFormatString("%.2f%%");
    }

    private void createAnimatedModelQuarter_PL_all(Integer item, Integer series) {
        String year = "";
        if (item == 0) {
            year = "2010";
        } else if (item == 1) {
            year = "2011";
        } else if (item == 2) {
            year = "2012";
        } else if (item == 3) {
            year = "2013";
        } else if (item == 4) {
            year = "2014";
        } else if (item == 5) {
            year = "2015";
        }

        pieChartQuarter_PL_all = initPieModelQuarter_PL_all(item, series);
        pieChartQuarter_PL_all.setTitle("Quarterly Profit/Loss Breakdown for Year " + year + "");
        pieChartQuarter_PL_all.setLegendPosition("e");
        pieChartQuarter_PL_all.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        pieChartQuarter_PL_all.setFill(false);
        pieChartQuarter_PL_all.setMouseoverHighlight(true);
        pieChartQuarter_PL_all.setShowDataLabels(true);
        pieChartQuarter_PL_all.setDiameter(140);
        pieChartQuarter_PL_all.setExtender("pieExtender_pie");
        pieChartQuarter_PL_all.setDataLabelFormatString("%.2f%%");
    }

    private void createCombinedModel_all(Integer item, Integer series) {
        String year = "";
        if (item == 0) {
            year = "2010";
        } else if (item == 1) {
            year = "2011";
        } else if (item == 2) {
            year = "2012";
        } else if (item == 3) {
            year = "2013";
        } else if (item == 4) {
            year = "2014";
        } else if (item == 5) {
            year = "2015";
        }

        combinedModel_all = initBarModelCombined_all(item, series);

        combinedModel_all.setTitle("Quarterly Revenue & Profit/Loss Breakdown for Year " + year + "");
        combinedModel_all.setLegendPosition("e");
        combinedModel_all.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        combinedModel_all.setAnimate(true);
        combinedModel_all.setMouseoverHighlight(false);
        combinedModel_all.setShowDatatip(false);
        combinedModel_all.setShowPointLabels(true);

        Axis yAxis = combinedModel_all.getAxis(AxisType.Y);
        yAxis.setLabel("Number of thousands (Revenue $)");
        yAxis.setMin(0);
        yAxis.setMax(200000);
    }

    private void createCombinedModel(Integer item, Integer series) {
        String machineStr = "";
        if (series == 0) {
            machineStr = "Small Chamber";
        } else if (series == 1) {
            machineStr = "V Flexx";
        } else if (series == 2) {
            machineStr = "V ERGO";
        } else if (series == 3) {
            machineStr = "V T-BaseV3";
        } else if (series == 4) {
            machineStr = "V MobileFlexx";
        } else if (series == 5) {
            machineStr = "V Unixx III";
        } else if (series == 6) {
            machineStr = "V UltraFlexx";
        }

        String year = "";
        if (item == 0) {
            year = "2010";
        } else if (item == 1) {
            year = "2011";
        } else if (item == 2) {
            year = "2012";
        } else if (item == 3) {
            year = "2013";
        } else if (item == 4) {
            year = "2014";
        } else if (item == 5) {
            year = "2015";
        }

        combinedModel = initBarModelCombined(item, series);

        combinedModel.setTitle("Quarterly Revenue & Profit/Loss Breakdown for Year " + year + " for Product \"" + machineStr + "\"");
        combinedModel.setLegendPosition("e");
        combinedModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        combinedModel.setAnimate(true);
        combinedModel.setMouseoverHighlight(false);
        combinedModel.setShowDatatip(false);
        combinedModel.setShowPointLabels(true);

        Axis yAxis = combinedModel.getAxis(AxisType.Y);
        yAxis.setLabel("Number of thousands (Revenue $)");
        yAxis.setMin(0);
        yAxis.setMax(200000);
    }

    private void createAnimatedModelMonth(Integer item, Integer series) {
        String machine = "";
        if (series == 0) {
            machine = "Small Chamber";
        } else if (series == 1) {
            machine = "V Flexx";
        } else if (series == 2) {
            machine = "V ERGO";
        } else if (series == 3) {
            machine = "V T-BaseV3";
        } else if (series == 4) {
            machine = "V MobileFlexx";
        } else if (series == 5) {
            machine = "V Unixx III";
        } else if (series == 6) {
            machine = "V UltraFlexx";
        }

        String year = "";
        if (item == 0) {
            year = "2010";
        } else if (item == 1) {
            year = "2011";
        } else if (item == 2) {
            year = "2012";
        } else if (item == 3) {
            year = "2013";
        } else if (item == 4) {
            year = "2014";
        } else if (item == 5) {
            year = "2015";
        }

        barChartMonth = initBarModelMonth(item, series);
        barChartMonth.setTitle("Monthly Profit/Loss Breakdown for Year " + year + " for Product \"" + machine + "\"");
        barChartMonth.setAnimate(true);
        barChartMonth.setLegendPosition("e");
        barChartMonth.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        barChartMonth.setExtender("pieExtender");

        Axis yAxisMonth = barChartMonth.getAxis(AxisType.Y);
        yAxisMonth.setLabel("Number of thousands (Revenue $)");
        yAxisMonth.setMin(0);
        yAxisMonth.setMax(100000);
        yAxisMonth.setTickInterval("10000");

        Axis xAxisMonth = barChartMonth.getAxis(AxisType.X);
        xAxisMonth.setLabel("Months");
    }

    private void createAnimatedModelQuarter(Integer item, Integer series) {
        String machine = "";
        if (item == 0) {
            machine = "Small Chamber";
        } else if (item == 1) {
            machine = "V Flexx";
        } else if (item == 2) {
            machine = "V ERGO";
        } else if (item == 3) {
            machine = "V T-BaseV3";
        } else if (item == 4) {
            machine = "V MobileFlexx";
        } else if (item == 5) {
            machine = "V Unixx III";
        } else if (item == 6) {
            machine = "V UltraFlexx";
        }

        pieChartQuarter = initPieModelQuarter(item, series);
        pieChartQuarter.setTitle("Quarterly Revenue Breakdown for Product \"" + machine + "\"");
        pieChartQuarter.setLegendPosition("e");
        pieChartQuarter.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        pieChartQuarter.setFill(false);
        pieChartQuarter.setMouseoverHighlight(true);
        pieChartQuarter.setShowDataLabels(true);
        pieChartQuarter.setDiameter(140);
        pieChartQuarter.setExtender("pieExtender_pie");
        pieChartQuarter.setDataLabelFormatString("%.2f%%");
    }

    private void createAnimatedModelQuarter_PL(Integer item, Integer series) {
        String machine = "";
        if (item == 0) {
            machine = "Small Chamber";
        } else if (item == 1) {
            machine = "V Flexx";
        } else if (item == 2) {
            machine = "V ERGO";
        } else if (item == 3) {
            machine = "V T-BaseV3";
        } else if (item == 4) {
            machine = "V MobileFlexx";
        } else if (item == 5) {
            machine = "V Unixx III";
        } else if (item == 6) {
            machine = "V UltraFlexx";
        }

        pieChartQuarter_PL = initPieModelQuarter_PL(item, series);
        pieChartQuarter_PL.setTitle("Quarterly Profit/Loss Breakdown for Product \"" + machine + "\"");
        pieChartQuarter_PL.setLegendPosition("e");
        pieChartQuarter_PL.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        pieChartQuarter_PL.setFill(false);
        pieChartQuarter_PL.setMouseoverHighlight(true);
        pieChartQuarter_PL.setShowDataLabels(true);
        pieChartQuarter_PL.setDiameter(140);
        pieChartQuarter_PL.setExtender("pieExtender_pie");
        pieChartQuarter_PL.setDataLabelFormatString("%.2f%%");
    }

    public void itemSelect_FirstLeft(ItemSelectEvent event) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected", "Item Index: " + event.getItemIndex() + ", Series Index: " + event.getSeriesIndex());
//
//        FacesContext.getCurrentInstance().addMessage("msgs", msg);

        createCombinedModel_all(event.getItemIndex(), event.getSeriesIndex());
        createAnimatedModelMonth_all(event.getItemIndex(), event.getSeriesIndex());
        createAnimatedModelQuarter_all(event.getItemIndex(), event.getSeriesIndex());
        createAnimatedModelQuarter_PL_all(event.getItemIndex(), event.getSeriesIndex());
        visibility_First = false;
        visibility_FirstLeft = true;
        visibility_FirstRight = false;
    }

    public void itemSelect_FirstRight(ItemSelectEvent event) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected", "Item Index: " + event.getItemIndex() + ", Series Index: " + event.getSeriesIndex());
//
//        FacesContext.getCurrentInstance().addMessage("msgs", msg);

        createCombinedModel(event.getItemIndex(), event.getSeriesIndex());
        createAnimatedModelMonth(event.getItemIndex(), event.getSeriesIndex());
        createAnimatedModelQuarter(event.getItemIndex(), event.getSeriesIndex());
        createAnimatedModelQuarter_PL(event.getItemIndex(), event.getSeriesIndex());
        visibility_First = false;
        visibility_FirstLeft = false;
        visibility_FirstRight = true;
    }

    public void itemSelectBack_First() {
        visibility_First = true;
        visibility_FirstLeft = false;
        visibility_FirstRight = false;
    }

    private BarChartModel initBarModel_AnnualRevPLPerYear() {
        BarChartModel model = new BarChartModel();

        List<List<List<String>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Year();
        int machineIndex, yearIndex;

        ChartSeries series1 = new ChartSeries();
        series1.setLabel("Revenue");
        ChartSeries series2 = new ChartSeries();
        series2.setLabel("Profit/Loss");

        Integer[] revenue_year = new Integer[6];
        revenue_year[0] = 0;
        revenue_year[1] = 0;
        revenue_year[2] = 0;
        revenue_year[3] = 0;
        revenue_year[4] = 0;
        revenue_year[5] = 0;

        Integer[] profitLoss_year = new Integer[6];
        profitLoss_year[0] = 0;
        profitLoss_year[1] = 0;
        profitLoss_year[2] = 0;
        profitLoss_year[3] = 0;
        profitLoss_year[4] = 0;
        profitLoss_year[5] = 0;

        // 7 machines, 6 years
        for (machineIndex = 0; machineIndex < allMachines.size(); machineIndex++) {
            for (yearIndex = 0; yearIndex < allMachines.get(machineIndex).size(); yearIndex++) {
                revenue_year[yearIndex] = revenue_year[yearIndex] + ((int) round(Double.parseDouble(allMachines.get(machineIndex).get(yearIndex).get(1)), 0));
                profitLoss_year[yearIndex] = profitLoss_year[yearIndex] + ((int) round(Double.parseDouble(allMachines.get(machineIndex).get(yearIndex).get(2)), 0));
            }
        }

        series1.set(2010, revenue_year[0]);
        series1.set(2011, revenue_year[1]);
        series1.set(2012, revenue_year[2]);
        series1.set(2013, revenue_year[3]);
        series1.set(2014, revenue_year[4]);
        series1.set(2015, revenue_year[5]);

        series2.set(2010, profitLoss_year[0]);
        series2.set(2011, profitLoss_year[1]);
        series2.set(2012, profitLoss_year[2]);
        series2.set(2013, profitLoss_year[3]);
        series2.set(2014, profitLoss_year[4]);
        series2.set(2015, profitLoss_year[5]);

        System.out.println("revenue_year[0] ==== " + revenue_year[0]);
        System.out.println("revenue_year[1] ==== " + revenue_year[1]);
        System.out.println("revenue_year[2] ==== " + revenue_year[2]);
        System.out.println("revenue_year[3] ==== " + revenue_year[3]);
        System.out.println("revenue_year[4] ==== " + revenue_year[4]);
        System.out.println("revenue_year[5] ==== " + revenue_year[5]);

        System.out.println("profitLoss_year[0] ==== " + profitLoss_year[0]);
        System.out.println("profitLoss_year[1] ==== " + profitLoss_year[1]);
        System.out.println("profitLoss_year[2] ==== " + profitLoss_year[2]);
        System.out.println("profitLoss_year[3] ==== " + profitLoss_year[3]);
        System.out.println("profitLoss_year[4] ==== " + profitLoss_year[4]);
        System.out.println("profitLoss_year[5] ==== " + profitLoss_year[5]);

        model.addSeries(series1);
        model.addSeries(series2);

        return model;
    }

    private LineChartModel initLinearModel_AnnualRevPLPerYearByProduct() {
        LineChartModel model = new LineChartModel();

        List<List<List<String>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Year();
        int i;

        // Small Chamber
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Small Chamber");

        List<List<String>> machine1 = allMachines.get(0);
        for (i = 0; i < machine1.size(); i++) {
            System.out.println("Small Chamber Month Revenue,Profit/Loss = " + machine1.get(i).get(0) + " " + (int) round(Double.parseDouble(machine1.get(i).get(2)), 0));
            series1.set(machine1.get(i).get(0), (int) round(Double.parseDouble(machine1.get(i).get(2)), 0));
        }

        // V Flexx
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("V Flexx");

        List<List<String>> machine2 = allMachines.get(1);
        for (i = 0; i < machine2.size(); i++) {
            System.out.println("V Flexx Month Revenue,Profit/Loss = " + machine2.get(i).get(0) + " " + (int) round(Double.parseDouble(machine2.get(i).get(2)), 0));
            series2.set(machine2.get(i).get(0), (int) round(Double.parseDouble(machine2.get(i).get(2)), 0));
        }

        // V ERGO
        LineChartSeries series3 = new LineChartSeries();
        series3.setLabel("V ERGO");

        List<List<String>> machine3 = allMachines.get(2);
        for (i = 0; i < machine3.size(); i++) {
            System.out.println("V ERGO Month Revenue,Profit/Loss = " + machine3.get(i).get(0) + " " + (int) round(Double.parseDouble(machine3.get(i).get(2)), 0));
            series3.set(machine3.get(i).get(0), (int) round(Double.parseDouble(machine3.get(i).get(2)), 0));
        }

        // V T-BaseV3
        LineChartSeries series4 = new LineChartSeries();
        series4.setLabel("V T-BaseV3");

        List<List<String>> machine4 = allMachines.get(3);
        for (i = 0; i < machine4.size(); i++) {
            System.out.println("V T-BaseV3 Month Revenue,Profit/Loss = " + machine4.get(i).get(0) + " " + (int) round(Double.parseDouble(machine4.get(i).get(2)), 0));
            series4.set(machine4.get(i).get(0), (int) round(Double.parseDouble(machine4.get(i).get(2)), 0));
        }

        // V MobileFlexx
        LineChartSeries series5 = new LineChartSeries();
        series5.setLabel("V MobileFlexx");

        List<List<String>> machine5 = allMachines.get(4);
        for (i = 0; i < machine5.size(); i++) {
            System.out.println("V MobileFlexx Month Revenue,Profit/Loss = " + machine5.get(i).get(0) + " " + (int) round(Double.parseDouble(machine5.get(i).get(2)), 0));
            series5.set(machine5.get(i).get(0), (int) round(Double.parseDouble(machine5.get(i).get(2)), 0));
        }

        // V Unixx III
        LineChartSeries series6 = new LineChartSeries();
        series6.setLabel("V Unixx III");

        List<List<String>> machine6 = allMachines.get(5);
        for (i = 0; i < machine6.size(); i++) {
            System.out.println("V Unixx III Month Revenue,Profit/Loss = " + machine6.get(i).get(0) + " " + (int) round(Double.parseDouble(machine6.get(i).get(2)), 0));
            series6.set(machine6.get(i).get(0), (int) round(Double.parseDouble(machine6.get(i).get(2)), 0));
        }

        // V UltraFlexx
        LineChartSeries series7 = new LineChartSeries();
        series7.setLabel("V UltraFlexx");

        List<List<String>> machine7 = allMachines.get(6);
        for (i = 0; i < machine7.size(); i++) {
            System.out.println("V Flexx Month Revenue,Profit/Loss = " + machine7.get(i).get(0) + " " + (int) round(Double.parseDouble(machine7.get(i).get(2)), 0));
            series7.set(machine7.get(i).get(0), (int) round(Double.parseDouble(machine7.get(i).get(2)), 0));
        }

        model.addSeries(series1);
        model.addSeries(series2);
        model.addSeries(series3);
        model.addSeries(series4);
        model.addSeries(series5);
        model.addSeries(series6);
        model.addSeries(series7);

        return model;
    }

    private BarChartModel initBarModelMonth_all(Integer item, Integer series) {
        BarChartModel model = new BarChartModel();

        List<List<List<List<String>>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Month();
        int i; // year
        int j; // month

        // Small Chamber
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Small Chamber");

        Integer[] pl = new Integer[12];
        pl[0] = 0;
        pl[1] = 0;
        pl[2] = 0;
        pl[3] = 0;
        pl[4] = 0;
        pl[5] = 0;
        pl[6] = 0;
        pl[7] = 0;
        pl[8] = 0;
        pl[9] = 0;
        pl[10] = 0;
        pl[11] = 0;

        List<List<List<String>>> machine1 = allMachines.get(0);
        //System.out.println("machine1.size() expect 6 === " + machine1.size());
        for (i = 0; i < machine1.size(); i++) { // 6 years
            List<List<String>> machine_year = machine1.get(i);
            //System.out.println("machine_year.get(i).size() expect 12 === " + machine_year.size());
            for (j = 0; j < machine_year.size(); j++) { // 12 months
                //System.out.println("Small Chamber Month Profit/Loss = " + machine_year.get(j).get(0) + " " + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0));
                pl[j] = pl[j] + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0);
            }
        }
        series1.set("Jan", pl[0]);
        series1.set("Feb", pl[1]);
        series1.set("Mar", pl[2]);
        series1.set("Apr", pl[3]);
        series1.set("May", pl[4]);
        series1.set("Jun", pl[5]);
        series1.set("Jul", pl[6]);
        series1.set("Aug", pl[7]);
        series1.set("Sep", pl[8]);
        series1.set("Oct", pl[9]);
        series1.set("Nov", pl[10]);
        series1.set("Dec", pl[11]);

        // V Flexx
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("V Flexx");

        pl[0] = 0;
        pl[1] = 0;
        pl[2] = 0;
        pl[3] = 0;
        pl[4] = 0;
        pl[5] = 0;
        pl[6] = 0;
        pl[7] = 0;
        pl[8] = 0;
        pl[9] = 0;
        pl[10] = 0;
        pl[11] = 0;

        List<List<List<String>>> machine2 = allMachines.get(1);
        //System.out.println("machine1.size() expect 6 === " + machine1.size());
        for (i = 0; i < machine2.size(); i++) { // 6 years
            List<List<String>> machine_year = machine2.get(i);
            //System.out.println("machine_year.get(i).size() expect 12 === " + machine_year.size());
            for (j = 0; j < machine_year.size(); j++) { // 12 months
                //System.out.println("Small Chamber Month Profit/Loss = " + machine_year.get(j).get(0) + " " + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0));
                pl[j] = pl[j] + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0);
            }
        }
        series2.set("Jan", pl[0]);
        series2.set("Feb", pl[1]);
        series2.set("Mar", pl[2]);
        series2.set("Apr", pl[3]);
        series2.set("May", pl[4]);
        series2.set("Jun", pl[5]);
        series2.set("Jul", pl[6]);
        series2.set("Aug", pl[7]);
        series2.set("Sep", pl[8]);
        series2.set("Oct", pl[9]);
        series2.set("Nov", pl[10]);
        series2.set("Dec", pl[11]);

        // V ERGO
        LineChartSeries series3 = new LineChartSeries();
        series3.setLabel("V ERGO");

        pl[0] = 0;
        pl[1] = 0;
        pl[2] = 0;
        pl[3] = 0;
        pl[4] = 0;
        pl[5] = 0;
        pl[6] = 0;
        pl[7] = 0;
        pl[8] = 0;
        pl[9] = 0;
        pl[10] = 0;
        pl[11] = 0;

        List<List<List<String>>> machine3 = allMachines.get(2);
        //System.out.println("machine1.size() expect 6 === " + machine1.size());
        for (i = 0; i < machine3.size(); i++) { // 6 years
            List<List<String>> machine_year = machine3.get(i);
            //System.out.println("machine_year.get(i).size() expect 12 === " + machine_year.size());
            for (j = 0; j < machine_year.size(); j++) { // 12 months
                //System.out.println("Small Chamber Month Profit/Loss = " + machine_year.get(j).get(0) + " " + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0));
                pl[j] = pl[j] + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0);
            }
        }
        series3.set("Jan", pl[0]);
        series3.set("Feb", pl[1]);
        series3.set("Mar", pl[2]);
        series3.set("Apr", pl[3]);
        series3.set("May", pl[4]);
        series3.set("Jun", pl[5]);
        series3.set("Jul", pl[6]);
        series3.set("Aug", pl[7]);
        series3.set("Sep", pl[8]);
        series3.set("Oct", pl[9]);
        series3.set("Nov", pl[10]);
        series3.set("Dec", pl[11]);

        // V T-BaseV3
        LineChartSeries series4 = new LineChartSeries();
        series4.setLabel("V T-BaseV3");

        pl[0] = 0;
        pl[1] = 0;
        pl[2] = 0;
        pl[3] = 0;
        pl[4] = 0;
        pl[5] = 0;
        pl[6] = 0;
        pl[7] = 0;
        pl[8] = 0;
        pl[9] = 0;
        pl[10] = 0;
        pl[11] = 0;

        List<List<List<String>>> machine4 = allMachines.get(3);
        //System.out.println("machine1.size() expect 6 === " + machine1.size());
        for (i = 0; i < machine4.size(); i++) { // 6 years
            List<List<String>> machine_year = machine4.get(i);
            //System.out.println("machine_year.get(i).size() expect 12 === " + machine_year.size());
            for (j = 0; j < machine_year.size(); j++) { // 12 months
                //System.out.println("Small Chamber Month Profit/Loss = " + machine_year.get(j).get(0) + " " + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0));
                pl[j] = pl[j] + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0);
            }
        }
        series4.set("Jan", pl[0]);
        series4.set("Feb", pl[1]);
        series4.set("Mar", pl[2]);
        series4.set("Apr", pl[3]);
        series4.set("May", pl[4]);
        series4.set("Jun", pl[5]);
        series4.set("Jul", pl[6]);
        series4.set("Aug", pl[7]);
        series4.set("Sep", pl[8]);
        series4.set("Oct", pl[9]);
        series4.set("Nov", pl[10]);
        series4.set("Dec", pl[11]);

        // V MobileFlexx
        LineChartSeries series5 = new LineChartSeries();
        series5.setLabel("V MobileFlexx");

        pl[0] = 0;
        pl[1] = 0;
        pl[2] = 0;
        pl[3] = 0;
        pl[4] = 0;
        pl[5] = 0;
        pl[6] = 0;
        pl[7] = 0;
        pl[8] = 0;
        pl[9] = 0;
        pl[10] = 0;
        pl[11] = 0;

        List<List<List<String>>> machine5 = allMachines.get(4);
        //System.out.println("machine1.size() expect 6 === " + machine1.size());
        for (i = 0; i < machine5.size(); i++) { // 6 years
            List<List<String>> machine_year = machine5.get(i);
            //System.out.println("machine_year.get(i).size() expect 12 === " + machine_year.size());
            for (j = 0; j < machine_year.size(); j++) { // 12 months
                //System.out.println("Small Chamber Month Profit/Loss = " + machine_year.get(j).get(0) + " " + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0));
                pl[j] = pl[j] + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0);
            }
        }
        series5.set("Jan", pl[0]);
        series5.set("Feb", pl[1]);
        series5.set("Mar", pl[2]);
        series5.set("Apr", pl[3]);
        series5.set("May", pl[4]);
        series5.set("Jun", pl[5]);
        series5.set("Jul", pl[6]);
        series5.set("Aug", pl[7]);
        series5.set("Sep", pl[8]);
        series5.set("Oct", pl[9]);
        series5.set("Nov", pl[10]);
        series5.set("Dec", pl[11]);

        // V Unixx III
        LineChartSeries series6 = new LineChartSeries();
        series6.setLabel("V Unixx III");

        pl[0] = 0;
        pl[1] = 0;
        pl[2] = 0;
        pl[3] = 0;
        pl[4] = 0;
        pl[5] = 0;
        pl[6] = 0;
        pl[7] = 0;
        pl[8] = 0;
        pl[9] = 0;
        pl[10] = 0;
        pl[11] = 0;

        List<List<List<String>>> machine6 = allMachines.get(5);
        //System.out.println("machine1.size() expect 6 === " + machine1.size());
        for (i = 0; i < machine6.size(); i++) { // 6 years
            List<List<String>> machine_year = machine6.get(i);
            //System.out.println("machine_year.get(i).size() expect 12 === " + machine_year.size());
            for (j = 0; j < machine_year.size(); j++) { // 12 months
                //System.out.println("Small Chamber Month Profit/Loss = " + machine_year.get(j).get(0) + " " + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0));
                pl[j] = pl[j] + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0);
            }
        }
        series6.set("Jan", pl[0]);
        series6.set("Feb", pl[1]);
        series6.set("Mar", pl[2]);
        series6.set("Apr", pl[3]);
        series6.set("May", pl[4]);
        series6.set("Jun", pl[5]);
        series6.set("Jul", pl[6]);
        series6.set("Aug", pl[7]);
        series6.set("Sep", pl[8]);
        series6.set("Oct", pl[9]);
        series6.set("Nov", pl[10]);
        series6.set("Dec", pl[11]);

        // V UltraFlexx
        LineChartSeries series7 = new LineChartSeries();
        series7.setLabel("V UltraFlexx");

        pl[0] = 0;
        pl[1] = 0;
        pl[2] = 0;
        pl[3] = 0;
        pl[4] = 0;
        pl[5] = 0;
        pl[6] = 0;
        pl[7] = 0;
        pl[8] = 0;
        pl[9] = 0;
        pl[10] = 0;
        pl[11] = 0;

        List<List<List<String>>> machine7 = allMachines.get(6);
        //System.out.println("machine1.size() expect 6 === " + machine1.size());
        for (i = 0; i < machine7.size(); i++) { // 6 years
            List<List<String>> machine_year = machine7.get(i);
            //System.out.println("machine_year.get(i).size() expect 12 === " + machine_year.size());
            for (j = 0; j < machine_year.size(); j++) { // 12 months
                //System.out.println("Small Chamber Month Profit/Loss = " + machine_year.get(j).get(0) + " " + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0));
                pl[j] = pl[j] + (int) round(Double.parseDouble(machine_year.get(j).get(2)), 0);
            }
        }
        series7.set("Jan", pl[0]);
        series7.set("Feb", pl[1]);
        series7.set("Mar", pl[2]);
        series7.set("Apr", pl[3]);
        series7.set("May", pl[4]);
        series7.set("Jun", pl[5]);
        series7.set("Jul", pl[6]);
        series7.set("Aug", pl[7]);
        series7.set("Sep", pl[8]);
        series7.set("Oct", pl[9]);
        series7.set("Nov", pl[10]);
        series7.set("Dec", pl[11]);

        model.addSeries(series1);
        model.addSeries(series2);
        model.addSeries(series3);
        model.addSeries(series4);
        model.addSeries(series5);
        model.addSeries(series6);
        model.addSeries(series7);

        return model;
    }

    private PieChartModel initPieModelQuarter_all(Integer item, Integer series) {
        // series should be equal to zero
        PieChartModel model = new PieChartModel();

        List<List<List<List<String>>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Quarter();

        Double[] quarter = new Double[4];
        quarter[0] = 0.0;
        quarter[1] = 0.0;
        quarter[2] = 0.0;
        quarter[3] = 0.0;

        int i;
        for (i = 0; i < 7; i++) {
            List<List<List<String>>> machine = allMachines.get(i);
            // item: 0 = year 2010, 1 = year 2011, ...
            List<List<String>> machine_quarter = machine.get(item);
            int j;
            for (j = 0; j < 4; j++) {
                quarter[j] = quarter[j] + Double.parseDouble(machine_quarter.get(j).get(0));
            }
        }

        model.set("Q1", (int) round(quarter[0], 0));
        model.set("Q2", (int) round(quarter[1], 0));
        model.set("Q3", (int) round(quarter[2], 0));
        model.set("Q4", (int) round(quarter[3], 0));

        return model;
    }

    private PieChartModel initPieModelQuarter_PL_all(Integer item, Integer series) {
        // series should be equal to one
        PieChartModel model = new PieChartModel();

        List<List<List<List<String>>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Quarter();

        Double[] quarter = new Double[4];
        quarter[0] = 0.0;
        quarter[1] = 0.0;
        quarter[2] = 0.0;
        quarter[3] = 0.0;

        int i;
        for (i = 0; i < 7; i++) {
            List<List<List<String>>> machine = allMachines.get(i);
            // item: 0 = year 2010, 1 = year 2011, ...
            List<List<String>> machine_quarter = machine.get(item);
            int j;
            for (j = 0; j < 4; j++) {
                quarter[j] = quarter[j] + Double.parseDouble(machine_quarter.get(j).get(1));
            }
        }

        model.set("Q1", (int) round(quarter[0], 0));
        model.set("Q2", (int) round(quarter[1], 0));
        model.set("Q3", (int) round(quarter[2], 0));
        model.set("Q4", (int) round(quarter[3], 0));

        return model;
    }

    private BarChartModel initBarModelCombined_all(Integer item, Integer series) {
        BarChartModel model = new BarChartModel();

        List<List<List<List<String>>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Quarter();
        List<List<List<String>>> machine;
        List<List<String>> machine_quarter;
        int i;
        Integer[] revenueQuarter = new Integer[4];
        revenueQuarter[0] = 0;
        revenueQuarter[1] = 0;
        revenueQuarter[2] = 0;
        revenueQuarter[3] = 0;
        Integer[] plQuarter = new Integer[4];
        plQuarter[0] = 0;
        plQuarter[1] = 0;
        plQuarter[2] = 0;
        plQuarter[3] = 0;

        for (i = 0; i < 7; i++) {
            machine = allMachines.get(i);
            // item: 0 = year 2010, 1 = year 2011, ...
            machine_quarter = machine.get(item);
            revenueQuarter[0] = revenueQuarter[0] + (int) round(Double.parseDouble(machine_quarter.get(0).get(0)), 0);
            revenueQuarter[1] = revenueQuarter[1] + (int) round(Double.parseDouble(machine_quarter.get(1).get(0)), 0);
            revenueQuarter[2] = revenueQuarter[2] + (int) round(Double.parseDouble(machine_quarter.get(2).get(0)), 0);
            revenueQuarter[3] = revenueQuarter[3] + (int) round(Double.parseDouble(machine_quarter.get(3).get(0)), 0);
        }

        BarChartSeries revenue = new BarChartSeries();
        revenue.setLabel("Revenue");

        revenue.set("Q1", revenueQuarter[0]);
        revenue.set("Q2", revenueQuarter[1]);
        revenue.set("Q3", revenueQuarter[2]);
        revenue.set("Q4", revenueQuarter[3]);

        LineChartSeries profitLoss = new LineChartSeries();
        profitLoss.setLabel("Profit/Loss");

        for (i = 0; i < 7; i++) {
            machine = allMachines.get(i);
            // item: 0 = year 2010, 1 = year 2011, ...
            machine_quarter = machine.get(item);
            plQuarter[0] = plQuarter[0] + (int) round(Double.parseDouble(machine_quarter.get(0).get(1)), 0);
            plQuarter[1] = plQuarter[1] + (int) round(Double.parseDouble(machine_quarter.get(1).get(1)), 0);
            plQuarter[2] = plQuarter[2] + (int) round(Double.parseDouble(machine_quarter.get(2).get(1)), 0);
            plQuarter[3] = plQuarter[3] + (int) round(Double.parseDouble(machine_quarter.get(3).get(1)), 0);
        }

        profitLoss.set("Q1", plQuarter[0]);
        profitLoss.set("Q2", plQuarter[1]);
        profitLoss.set("Q3", plQuarter[2]);
        profitLoss.set("Q4", plQuarter[3]);

        model.addSeries(revenue);
        model.addSeries(profitLoss);

        return model;
    }

    private BarChartModel initBarModelMonth(Integer item, Integer series) {
        BarChartModel model = new BarChartModel();

        List<List<List<List<String>>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Month();
        int i;

        ChartSeries chartSeries = new ChartSeries();
        if (series == 0) { // Machine 1
            chartSeries.setLabel("Small Chamber");
        } else if (series == 1) { // Machine 2
            chartSeries.setLabel("V Flexx");
        } else if (series == 2) { // Machine 3
            chartSeries.setLabel("V ERGO");
        } else if (series == 3) { // Machine 4
            chartSeries.setLabel("V T-BaseV3");
        } else if (series == 4) { // Machine 5
            chartSeries.setLabel("V MobileFlexx");
        } else if (series == 5) { // Machine 6
            chartSeries.setLabel("V Unixx III");
        } else if (series == 6) { // Machine 7
            chartSeries.setLabel("V UltraFlexx");
        }

        List<List<List<String>>> machine = allMachines.get(series);

        // item: 0 = year 2010, 1 = year 2011, ...
        List<List<String>> machine_year = machine.get(item);
        for (i = 0; i < machine_year.size(); i++) { // get month, should loop 12 times only
            chartSeries.set(machine_year.get(i).get(0), (int) round(Double.parseDouble(machine_year.get(i).get(2)), 0));
        }
        model.addSeries(chartSeries);
        return model;
    }

    private PieChartModel initPieModelQuarter(Integer item, Integer series) {
        PieChartModel model = new PieChartModel();

        List<List<List<List<String>>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Quarter();
        List<List<List<String>>> machine = allMachines.get(series);
        // item: 0 = year 2010, 1 = year 2011, ...
        List<List<String>> machine_quarter = machine.get(item);

//        System.out.println("Revenue machine_quarter.size() expect size 4 = " + machine_quarter.size());
//        System.out.println("Revenue machine_quarter.get(0).size() expect size 1 = " + machine_quarter.get(0).size());
//        System.out.println("Revenue Quater 1 = " + machine_quarter.get(0).get(0));
//        System.out.println("Revenue Quater 2 = " + machine_quarter.get(1).get(0));
//        System.out.println("Revenue Quater 3 = " + machine_quarter.get(2).get(0));
//        System.out.println("Revenue Quater 4 = " + machine_quarter.get(3).get(0));
        model.set("Q1", (int) round(Double.parseDouble(machine_quarter.get(0).get(0)), 0));
        model.set("Q2", (int) round(Double.parseDouble(machine_quarter.get(1).get(0)), 0));
        model.set("Q3", (int) round(Double.parseDouble(machine_quarter.get(2).get(0)), 0));
        model.set("Q4", (int) round(Double.parseDouble(machine_quarter.get(3).get(0)), 0));

        int i;
        for (i = 0; i < machine_quarter.size(); i++) { // get quater, should loop 4 times only

        }
        return model;
    }

    private PieChartModel initPieModelQuarter_PL(Integer item, Integer series) {
        PieChartModel model = new PieChartModel();

        List<List<List<List<String>>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Quarter();
        List<List<List<String>>> machine = allMachines.get(series);
        // item: 0 = year 2010, 1 = year 2011, ...
        List<List<String>> machine_quarter = machine.get(item);

//        System.out.println("PL machine_quarter.size() expect size 4 = " + machine_quarter.size());
//        System.out.println("PL machine_quarter.get(0).size() expect size 1 = " + machine_quarter.get(0).size());
//        System.out.println("PL Quater 1 = " + machine_quarter.get(0).get(1));
//        System.out.println("PL Quater 2 = " + machine_quarter.get(1).get(1));
//        System.out.println("PL Quater 3 = " + machine_quarter.get(2).get(1));
//        System.out.println("PL Quater 4 = " + machine_quarter.get(3).get(1));
        model.set("Q1", (int) round(Double.parseDouble(machine_quarter.get(0).get(1)), 0));
        model.set("Q2", (int) round(Double.parseDouble(machine_quarter.get(1).get(1)), 0));
        model.set("Q3", (int) round(Double.parseDouble(machine_quarter.get(2).get(1)), 0));
        model.set("Q4", (int) round(Double.parseDouble(machine_quarter.get(3).get(1)), 0));

        int i;
        for (i = 0; i < machine_quarter.size(); i++) { // get quater, should loop 4 times only

        }
        return model;
    }

    private BarChartModel initBarModelCombined(Integer item, Integer series) {
        BarChartModel model = new BarChartModel();

        List<List<List<List<String>>>> allMachines = productQuotationSessionBean.deriveRevenueGraph_Quarter();
        List<List<List<String>>> machine = allMachines.get(series);
        // item: 0 = year 2010, 1 = year 2011, ...
        List<List<String>> machine_quarter = machine.get(item);

        BarChartSeries revenue = new BarChartSeries();
        revenue.setLabel("Revenue");

        revenue.set("Q1", (int) round(Double.parseDouble(machine_quarter.get(0).get(0)), 0));
        revenue.set("Q2", (int) round(Double.parseDouble(machine_quarter.get(1).get(0)), 0));
        revenue.set("Q3", (int) round(Double.parseDouble(machine_quarter.get(2).get(0)), 0));
        revenue.set("Q4", (int) round(Double.parseDouble(machine_quarter.get(3).get(0)), 0));

        LineChartSeries profitLoss = new LineChartSeries();
        profitLoss.setLabel("Profit/Loss");

        profitLoss.set("Q1", (int) round(Double.parseDouble(machine_quarter.get(0).get(1)), 0));
        profitLoss.set("Q2", (int) round(Double.parseDouble(machine_quarter.get(1).get(1)), 0));
        profitLoss.set("Q3", (int) round(Double.parseDouble(machine_quarter.get(2).get(1)), 0));
        profitLoss.set("Q4", (int) round(Double.parseDouble(machine_quarter.get(3).get(1)), 0));

        model.addSeries(revenue);
        model.addSeries(profitLoss);

        return model;
    }

    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * @return the visibility_First
     */
    public boolean isVisibility_First() {
        return visibility_First;
    }

    /**
     * @param visibility_First the visibility_First to set
     */
    public void setVisibility_First(boolean visibility_First) {
        this.visibility_First = visibility_First;
    }

    /**
     * @return the visibility_FirstRight
     */
    public boolean isVisibility_FirstRight() {
        return visibility_FirstRight;
    }

    /**
     * @param visibility_FirstRight the visibility_FirstRight to set
     */
    public void setVisibility_FirstRight(boolean visibility_FirstRight) {
        this.visibility_FirstRight = visibility_FirstRight;
    }

    /**
     * @return the visibility_FirstLeft
     */
    public boolean isVisibility_FirstLeft() {
        return visibility_FirstLeft;
    }

    /**
     * @param visibility_FirstLeft the visibility_FirstLeft to set
     */
    public void setVisibility_FirstLeft(boolean visibility_FirstLeft) {
        this.visibility_FirstLeft = visibility_FirstLeft;
    }
}
