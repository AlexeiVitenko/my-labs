package by.bsuir.samm;

import java.util.Set;

import javax.swing.SwingWorker;

public class LemerRandom extends SwingWorker<Boolean, Integer> {

	private int a;
	private int r;
	private int m;
	private int goal;
	private RandomListenet randomListener;
	private Set<Integer> numbers;

	public LemerRandom(int a, int r, int m, int goal, RandomListenet pl) {
		this.a = a;
		this.r = r;
		this.m = m;
		this.goal = goal;
		this.randomListener = pl;
	}

	public interface RandomListenet {
		void onProgress();

		void onFail();
	}

	@Override
	/**
	 * @return @true - success, false - otherwise. 
	 */
	protected Boolean doInBackground() throws Exception {
		int nextR = r;
		do {
			nextR = (a*nextR) % m;
			if (!numbers.add(nextR)) {
				return false;
			}
			setProgress(numbers.size());
		} while (numbers.size() < goal);
		return true;
	}

}
