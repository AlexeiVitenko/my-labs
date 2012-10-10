package by.bsuir.poit.dsp;

import java.awt.Dimension;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class Harmonic {
    public static final int[] N_VALUES = { 512, 1024, 2048, 4096 };
    private double A;
    private int N;
    private double phi;
    private double f;
    private ArrayList<Double> mResults;

    public Harmonic(double a, int nIndex, double _phi, double _f) {
        A = a;
        N = N_VALUES[nIndex];
        phi = _phi;
        f = _f;
        mResults = new ArrayList<Double>();
    }

    public void setA(double a) {
        A = a;
    }

    public void setN(int n) {
        N = n;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public void setF(double f) {
        this.f = f;
    }

    private String dumpParams(){
        return "A = "+ dumpDouble(A) + " N = "+N + " φ = " + dumpDouble(phi) + " ƒ = " + dumpDouble(f);                
    }
    
    private String dumpDouble(double arg){
        return String.format("%.2f", arg);
    }
    
    public void makeChart() {
        double results[][] = new double[2][N];
        for (int i = 0; i < N; i++) {
            results[1][i] = A * Math.sin(2 * Math.PI * f * i / N + phi);
            results[0][i] = i;
        }
        DefaultXYDataset xy = new DefaultXYDataset();
        xy.addSeries("bla-bla", results);
        JFreeChart chart = ChartFactory.createXYLineChart(dumpParams(), "", "", xy, PlotOrientation.VERTICAL, false, false,
                false);
        ChartFrame cf = new ChartFrame("frame", chart);
        cf.setBounds(0, 0, 750, 500);
        cf.setVisible(true);
    }
}
