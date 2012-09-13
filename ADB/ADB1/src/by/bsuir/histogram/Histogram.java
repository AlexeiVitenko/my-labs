package by.bsuir.histogram;

import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;

public class Histogram {
	private List<Point> mValues;
	private float mScaleX;
	private float mScaleY;
	private int mDelta;
	private int mPixelsForDelta;
	private Rect mDisplayRect;

	public Histogram(Rect displayRect, List<Point> values) {
		mValues = values;
	}

	// TODO add check for empty list
	private void calculateDeltas() {
		Collections.sort(mValues, new Comparator<Point>() {

			@Override
			public int compare(Point lhs, Point rhs) {
				return new Integer(lhs.y).compareTo(rhs.y);
			}
		});
		int nextDelta = Integer.MAX_VALUE;
		int minDelta = Integer.MAX_VALUE;
		for (int i = 1; i < mValues.size(); i++) {
			int temp = Math.abs(mValues.get(i).y - mValues.get(i - 1).y);
			if (temp < minDelta) {
				nextDelta = minDelta;
				minDelta = temp;
			}
		}
		int height = mValues.get(mValues.size() - 1).y - mValues.get(0).y;
		while ((height) * 2 + 50 < mDisplayRect.bottom) {
			height *= 2;
		}
	}
}
