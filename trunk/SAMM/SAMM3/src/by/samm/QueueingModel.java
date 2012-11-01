package by.samm;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class QueueingModel {

    private final Source mSource;
    private final Channel mFirstChannel;
    private final Channel mSecondChannel;
    private final LemerRandom mRandom;
    private final Buffer mBuffer;
    // TODO to Int, Int -> SparseArray or bitmask;
    private final Map<String, Integer> mResults;
    private int mPotk;
    private int mKzan;
    private int mKzan1;
    private int mKzan2;
    private static final HashMap<String, String> sPMap = new HashMap<String, String>();
    static{
        sPMap.put("000", "P1");
        sPMap.put("010", "P2");
        sPMap.put("011", "P3");
        sPMap.put("111", "P4");
        sPMap.put("001", "P5");
    }
    public QueueingModel() {
        mRandom = new LemerRandom(22695477, 1, 2L << 32, 50000);
        mRandom.generate();
        System.out.println();
        mSource = new Source(mRandom, 0.5f);
        mFirstChannel = new Channel(mRandom, 0.8f);
        mSecondChannel = new Channel(mRandom, 1);
        mBuffer = new Buffer();
        mResults = new TreeMap<String, Integer>();
    }

    public void start(int turns) {
        for (int i = 0; i < turns; i++) {
            nextMove();
        }
        showResults(turns);
    }

    private void nextMove() {
        mFirstChannel.nextTurn();
        mSecondChannel.nextTurn();
        Request bufRequest = mBuffer.nextTurn();
        if (mFirstChannel.putRequest(bufRequest)) {

        } else if (mSecondChannel.putRequest(bufRequest)) {

        } else {
            if (bufRequest != null) {
                mKzan++;
            }
            mBuffer.putRequest(bufRequest);
        }
        Request srcRequest = mSource.nextTurn();
        if (mFirstChannel.putRequest(srcRequest)) {

        } else if (mSecondChannel.putRequest(srcRequest)) {

        } else if (mBuffer.putRequest(srcRequest)) {
        } else if (srcRequest != null) {
            mPotk++;
        }
        if (mFirstChannel.getState()) {
            mKzan1++;
        }
        if (mSecondChannel.getState()) {
            mKzan2++;
        }
        String str = toString();
        if (mResults.containsKey(str)) {
            mResults.put(str, mResults.get(str) + 1);
        } else {
            mResults.put(str, 1);
        }
    }

    private void showResults(int turns) {
        for (String str : mResults.keySet()) {
            System.out.println(sPMap.get(str)+" state == " + str + ": " + ((float) mResults.get(str)) / turns);
        }
        System.out.println("Potk == " + (((float) mPotk) / turns));
     //   System.out.println("Kzan == " + (((float) mKzan) / turns));
        System.out.println("Kzan1 == " + (((float) mKzan1) / turns));
        System.out.println("Kzan2 == " + (((float) mKzan2) / turns));
    }

    @Override
    public String toString() {
        return "" + mBuffer.stateToString() + mFirstChannel.stateToString() + mSecondChannel.stateToString();
    }
}
