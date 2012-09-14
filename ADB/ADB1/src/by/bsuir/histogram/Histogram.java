package by.bsuir.histogram;

import static by.bsuir.histogram.HistogramsConstants.LEFT_PADDING;
import static by.bsuir.histogram.HistogramsConstants.X_PADDING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Histogram {
	private Point mSize;
	private List<Point> mValues;
	private float mScaleX;
	private float mScaleY;
	private int mDelta;
	private int mPixelsForDelta;
	private Rect mDisplayRect;
	private int maxSize;
	private float mDensity;

	public Histogram(Rect displayRect, List<Point> values, float density) {
		mValues = values;
		maxSize = Math.max(displayRect.bottom, displayRect.right);
		mDensity = density;
		calculateSize();
	}

	// TODO add check for empty list
	private void calculateSize() {
		calculateWidth();
	}

	private void calculateHeight() {
		Collections.sort(mValues, new Comparator<Point>() {

			@Override
			public int compare(Point lhs, Point rhs) {
				return lhs.y > rhs.y ? 1 : lhs.y == rhs.y ? 0 : -1;
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
	}

	private void calculateWidth() {
		List<Point> values = new ArrayList<Point>(mValues);
		Collections.sort(values, new Comparator<Point>() {

			@Override
			public int compare(Point lhs, Point rhs) {
				return lhs.x > rhs.x ? 1 : lhs.x == rhs.x ? 0 : -1;
			}
		});

		SortedSet<Integer> si = new TreeSet<Integer>();
		for (Point p : values) {
			si.add(p.x);
		}

		int width = (int) (LEFT_PADDING * 2 * mDensity + (si.last() - si
				.first()) * X_PADDING * mDensity);
		Log.d("adb width", "" + width);
	}
}
