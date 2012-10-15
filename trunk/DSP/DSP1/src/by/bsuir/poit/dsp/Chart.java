package by.bsuir.poit.dsp;

import java.awt.BasicStroke;
import java.awt.Frame;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public abstract class Chart {

    public static final int[] N_VALUES = { 512, 1024, 2048, 4096 };
    protected double A;
    protected int N;

    public Chart(double a, int n) {
        A = a;
        N = N_VALUES[n];
    }

    protected String dumpDouble(double arg) {
        return String.format("%.2f", arg);
    }

    protected abstract String dumpParams();

    protected abstract XYDataItem getData(int index);

    public void makeChart() {
        XYSeries xys = new XYSeries("key");
        for (int i = 0; i < N; i++) {
            xys.add(getData(i));
        }
        XYDataset xy = new XYSeriesCollection(xys);
        JFreeChart chart = ChartFactory.createXYLineChart(dumpParams(), "", "", xy, PlotOrientation.VERTICAL, false,
                true, false);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        renderer.setBaseCreateEntities(true);
        renderer.setSeriesStroke(0, new BasicStroke(5));
        ChartFrame cf = new ChartFrame("chart", chart);
        cf.setBounds(0, 0, 750, 500);
        cf.setVisible(true);
    }
}
