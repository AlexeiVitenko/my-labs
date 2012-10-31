package by.samm;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class LemerRandom {

    private long a;
    private long r;
    private long m;
    private long goal;
    private Set<Long> numbers = new TreeSet<Long>();
    private SortedSet<Float> result = new TreeSet<Float>();
    private List<Float> listOfResults;
    private StringBuilder sb = new StringBuilder();

    public LemerRandom(long a, long r, long m, long goal) {
        this.a = a;
        this.r = r;
        this.m = m;
        this.goal = goal;
    }

    public StringBuilder getSb() {
        return sb;
    }

    public SortedSet<Float> getResult() {
        return result;
    }

    /**
     * @return @true - success, false - otherwise.
     */
    Boolean generate() {
        long nextR = r;
        do {
            nextR = (a * nextR) % m;
            if (!numbers.add(nextR)) {
                return false;
            }
            setProgress((int) (numbers.size() / (float) goal * 100));
            float res = (float) nextR / m;
            result.add(res);
            sb.append(res + "\n");
        } while (result.size() < goal);
        listOfResults = new ArrayList<Float>(result);
        Collections.shuffle(listOfResults);
        return true;
    }

    public float nextFloat() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return listOfResults.get((int) (System.currentTimeMillis() % listOfResults.size()));
    }

    private int progress = -1;

    private void setProgress(int p){
        if (p > progress) {
            System.out.print("Generate random numbers: " + p + "%              \r");
            progress = p;
        }
    }
}
