package by.bsuir.poit.dsp;

import org.jfree.data.xy.XYDataItem;

public class Polyharmonic extends Chart {
    protected double[] f;
    protected double[] phi;

    public Polyharmonic(double _a, int _n, double _f, double[] _phi) {
        super(_a, _n);
        f = new double[5];
        for (int i = 0; i < 5; i++) {
            f[i] = _f + i;
        }
        phi = _phi;
    }

    @Override
    protected String dumpParams() {
        return "";
    }

    @Override
    protected XYDataItem getData(int index) {
        float result = 0;
        for (int j = 0; j < phi.length; j++) {
            result += A * Math.sin(2 * Math.PI * f[j] * index / N + phi[j]);
        }
        return new XYDataItem(index, result);
    }
}
