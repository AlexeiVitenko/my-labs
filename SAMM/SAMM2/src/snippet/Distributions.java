package snippet;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Distributions {

    private static SortedSet<Float> mData = new TreeSet<Float>();

    public static void uniform(float a, float b, Set<Float> source) {
        prepare();

        for (Float float1 : source) {
            mData.add(a + (b - a) * float1);
        }
        calculateParameters("Uniform distribution:");
        //dumpParameters((a + b) / 2, Math.pow(b - a, 2) / 12, "Uniform distribution:");
    }

    private static void prepare() {
        mData.clear();
    }

    public static void exponential(float lambda, Set<Float> source) {
        prepare();
        for (Float float1 : source) {
            mData.add((float) (-1f / lambda * Math.log(float1)));
        }
        calculateParameters("Exponential distribution:");
        //dumpParameters(1 / lambda, 1 / lambda / lambda, "Exponential distribution:");
    }

    public static void gamma(float lambda, int n, Set<Float> source) {
        prepare();
        ArrayList<Float> src = new ArrayList<Float>(source);
        Random r = new Random();
        for (int j = 0; j < src.size(); j++){
            /*mData.add((float) (Math.pow(lambda, n) / Gamma.gamma(n - 1) * Math.pow(float1, n - 1) * Math.exp(-2
                    * float1)));*/
            float c = 0;
            for (int i = 0; i < n; i++) {
                c += Math.log(src.get(r.nextInt(src.size())));
            }
            mData.add(-1f/lambda*c);
        }
        calculateParameters("Gamma-distribution");
        //dumpParameters(n / lambda, n / lambda / lambda, "Gamma-distribution");
    }

    public static void thriangular(float a, float b, Set<Float> source) {
        prepare();
        ArrayList<Float> src = new ArrayList<Float>(source);
        Random r = new Random();

        for (int i = 0; i < source.size(); i++) {
            mData.add(a + (b - a) * Math.max(src.get(r.nextInt(src.size())), src.get(r.nextInt(src.size()))));
        }

        calculateParameters("Thriangular distribution:");
        //dumpParameters((a + b) / 2, (b - a) * (b - a) / 24, "Thriangular distribution:");
    }

    public static void gauss(float m, float d, int n, Set<Float> source) {
        prepare();
        ArrayList<Float> src = new ArrayList<Float>(source);
        Random r = new Random();

        for (int i = 0; i < src.size(); i++) {
            float c = 0;
            for (int j = 0; j < n; j++) {
                c += (src.get(r.nextInt(src.size())));
            }
            c -= n / 2f;
            mData.add((float) (m + d * Math.sqrt(12f / n) * c));
        }

        calculateParameters("Gaussian distribution:");
    }

    public static void simpson(float a, float b, Set<Float> source) {
        prepare();
        uniform(a / 2, b / 2, source);

        ArrayList<Float> src = new ArrayList<Float>(mData);
        Random r = new Random();
        mData.clear();

        for (int i = 0; i < src.size(); i++) {
            mData.add(src.get(r.nextInt(src.size())) + src.get(r.nextInt(src.size())));
        }

        calculateParameters("Simpson distribution:");
    }

    private static void calculateParameters(String name) {
        float m = 0;
        for (Float float1 : mData) {
            m += float1;
        }
        m /= mData.size();

        float d = 0;
        for (Float float1 : mData) {
            d += (float1 - m) * (float1 - m);
        }

        d /= mData.size();
        dumpParameters(m, d, name);
    }

    private static void dumpParameters(double e, double v, String name) {
        System.out.println(name);
        System.out.println("Expected value: " + e);
        System.out.println("Variance: " + v);
        System.out.println("Standard deviation: " + Math.sqrt(v));
        Distributions.makeChart(name);
    }

    public static void makeChart(String name) {
        XYSeries xy = new XYSeries("Count");
        int i = 0;
        float delta = (mData.last() - mData.first()) / 20;
        float max = mData.first() + delta;
        xy.add(mData.first() - delta / 2, -1);
        for (Float f : mData) {
            while (f >= max + delta) {
                max += delta;
            }
            i++;
            if (f > max) {
                xy.add(max - delta / 2, i);
                max += delta;
                i = 0;
            }
        }
        JFreeChart chart = ChartFactory.createXYBarChart(name, "Range", false, "Count", new XYBarDataset(
                new XYSeriesCollection(xy), delta), PlotOrientation.VERTICAL, true, true, false);
        XYBarRenderer br = (XYBarRenderer) chart.getXYPlot().getRenderer();
        ChartFrame ch = new ChartFrame(name, chart);
        ch.setBounds(0, 0, 750, 500);
        ch.setVisible(true);
    }
}
