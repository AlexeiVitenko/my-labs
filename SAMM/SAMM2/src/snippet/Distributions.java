package snippet;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
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

    public static void exponential(float lambda, Set<Float> source) {
        mData.clear();

        for (Float float1 : source) {
            mData.add((float) (-1f / lambda * Math.log(float1)));
        }
        dumpParameters(1 / lambda, 1 / lambda / lambda);
    }

    public static void gamma(float lambda, float n, Set<Float> source) {
        mData.clear();

        for (Float float1 : source) {
            mData.add((float) (Math.pow(lambda, n) / Gamma.gamma(n - 1) * Math.pow(float1, n - 1) * Math.exp(-2
                    * float1)));
        }
        dumpParameters(n / lambda, n / lambda / lambda);
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
        float delta = (mData.last() - mData.first()) / 20;
        float max = delta;
        for (Float f : mData) {
            while (f >= max + delta) {
                max += delta;
            }
            i++;
            if (f > max) {
                System.out.println(": "+max);
                xy.add(max, maxCount);
                max += delta;
                if (i > maxCount) {
                    maxCount = i;
                }
                i = 0;
            }
        }
        JFreeChart chart = ChartFactory.createXYStepAreaChart("Distribution", "Range", "Count", new XYSeriesCollection(xy),
                PlotOrientation.VERTICAL, true, true, false);
        ChartFrame ch = new ChartFrame("blablabla", chart);
        ch.setBounds(0, 0, 750, 500);
        ch.setVisible(true);
    }
}
