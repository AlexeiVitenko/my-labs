package by.samm;

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

    public QueueingModel() {
        mRandom = new LemerRandom(22695477, 1, 2L << 32, 50000);
        mRandom.generate();
        mSource = new Source(mRandom, 0.5f);
        mFirstChannel = new Channel(mRandom, 0.6f);
        mSecondChannel = new Channel(mRandom, 0.8f);
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
            mBuffer.putRequest(bufRequest);
        }
        Request srcRequest = mSource.nextTurn();
        if (mFirstChannel.putRequest(srcRequest)) {

        } else if (mSecondChannel.putRequest(srcRequest)) {

        } else if (mBuffer.putRequest(srcRequest)) {
            mKzan++;
        } else if (srcRequest != null) {
            mPotk++;
        }
        String str = toString();
        if (mResults.containsKey(str)) {
            mResults.put(str, mResults.get(str) + 1);
        } else {
            mResults.put(str, 1);
        }
        // System.out.println(str);
    }

    private void showResults(int turns) {
        for (String str : mResults.keySet()) {
            System.out.println("State == " + str + ": " + ((float) mResults.get(str)) / turns);
        }
        System.out.println("Potk == "+(((float)mPotk)/turns));
        System.out.println("Kzan == "+(((float)mKzan)/turns));
    }

    @Override
    public String toString() {
        return "" + mBuffer.stateToString() + mFirstChannel.stateToString() + mSecondChannel.stateToString();
    }
}
