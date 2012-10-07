package snippet;

import java.awt.Point;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Distributions {

    private static SortedSet<Float> mData = new TreeSet<Float>();

    public static void uniform(float a, float b, Set<Float> source) {
        mData.clear();

        for (Float float1 : source) {
            mData.add(a + (b - a) * float1);
        }
        dumpParameters((a + b) / 2, Math.pow(b - a, 2) / 12);
    }

    private static void dumpParameters(double e, double v) {
        System.out.println("Expected value: " + e);
        System.out.println("Variance: " + v);
        System.out.println("Standard deviation: " + Math.sqrt(v));
    }

    public static void makeChart() {
        XYSeries xy = new XYSeries("Distribution");
        int maxCount = -1;
        int i = 0;
        float max = 0.05f;
        xy.add(0, 0);
        for (Float f : mData) {
            while (f >= max + 0.05) {
                max += 0.05f;
            }
            i++;
            if (f > max) {
                // x.setValue(i, "Distr", "" + String.format("%.2f-%.2f",
                // max-0.05f, max));
                xy.add(max - 0.025, maxCount);
                max += 0.05f;
                if (i > maxCount) {
                    maxCount = i;
                }
                i = 0;
            }
        }
        JFreeChart chart = ChartFactory.createXYBarChart("Distribution", "Range", false, "Count",
                new XYSeriesCollection(xy), PlotOrientation.VERTICAL, true, true, false);
        ChartFrame ch = new ChartFrame("blablabla", chart);
        ch.setBounds(0, 0, 750, 500);
        ch.setVisible(true);
    }
}
