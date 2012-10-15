package by.bsuir.poit.dsp;

import org.jfree.data.xy.XYDataItem;

public class Harmonic extends Chart {
    private double phi;
    private double f;
    //private ArrayList<Double> mResults;

    public Harmonic(double a, int nIndex, double _phi, double _f) {
        super(a, nIndex);
        phi = _phi;
        f = _f;
      //  mResults = new ArrayList<Double>();
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

    @Override
    protected String dumpParams() {
        return "A = " + dumpDouble(A) + " N = " + N + " φ = " + dumpDouble(phi) + " ƒ = " + dumpDouble(f);
    }

    @Override
    protected XYDataItem getData(int index) {
        return new XYDataItem(index, A * Math.sin(2 * Math.PI * f * index / N + phi));
    }

}
