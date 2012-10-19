package by.bsuir.poit.dsp;

import org.jfree.data.xy.XYDataItem;

public class VariableChart extends Polyharmonic {
	int[] mPeriod;

	public VariableChart(double _a, int _n, double _f, double[] _phi) {
		super(_a, _n, _f, _phi);
		calculatePeriod();
	}

	private void calculatePeriod() {
		mPeriod = new int[f.length];
		for (int i = 0; i < mPeriod.length; i++) {
			mPeriod[i] = (int) (N / f[i]);
		}
	}

	@Override
	protected XYDataItem getData(int index) {
		float result = 0;
		for (int j = 0; j < phi.length; j++) {
			float k = (1 + 0.2f * index / mPeriod[index]);
			result += k * A * Math.sin(2 * Math.PI * f[j] * k * index / N + phi[j] * k);
		}
		return new XYDataItem(index, result);
	}
}
