package by.bsuir.poit.dsp;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class Polyharmonic {

    private double A;
    private double[] f;
    private double[] phi;
    private int N;

    public Polyharmonic(double _a, int _n, double _f, double[] _phi) {
        N = Harmonic.N_VALUES[_n];
        A = _a;
        f = new double[5];
        for (int i = 0; i < 5; i++) {
            f[i] = _f + i;
        }
        phi = _phi;
    }

    public void makeChart() {
        double results[][] = new double[2][N];
        for (int i = 0; i < N; i++) {
            float result = 0;
            for (int j = 0; j < phi.length; j++) {
                result += A * Math.sin(2 * Math.PI * f[j] * i / N + phi[j]);
            }
            results[1][i] = result;
            results[0][i] = i;
        }
        DefaultXYDataset xy = new DefaultXYDataset();
        xy.addSeries("bla-bla", results);
        JFreeChart chart = ChartFactory.createXYLineChart("Chart", "", "", xy, PlotOrientation.VERTICAL, false, false,
                false);
        ChartFrame cf = new ChartFrame("frame", chart);
        cf.setBounds(0, 0, 750, 500);
        cf.setVisible(true);
    }
}
