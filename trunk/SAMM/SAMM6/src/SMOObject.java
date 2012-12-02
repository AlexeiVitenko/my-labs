import java.util.Random;


public abstract class SMOObject implements Comparable<SMOObject>{
    private float mNextEventTime;
    private Random mRandom = new Random();
    private float mLambda;
    protected Worker mNextChannnel;
    protected int mRequestsCount;
    protected String mName;
    
    public SMOObject(float lambda, Worker nextChannel, String name) {
        mLambda = lambda;
        mNextChannnel = nextChannel;
        getTimeInterval();
        mName = name;
    }
    
    public float getNextEventTime() {
        return mNextEventTime;
    }
    
    public void doWork(){
        getTimeInterval();
    }
    
    public abstract void dumpResults();
    public abstract int getFailsCount(); 
    public int getSentCount() {
        return mRequestsCount;
    }
    
    private void getTimeInterval()
    {
        float value;
        do
        {
            value = mRandom.nextFloat();
        }
        while (value == 0 || value == 1);
        do
        {
            value = (float) Math.abs(-Math.log(value) / mLambda);
        }
        while (value == 0);
        mNextEventTime += value;
      //  log("time: "+mNextEventTime);
    }

    @Override
    public int compareTo(SMOObject o) {
        return Double.compare(mNextEventTime, o.getNextEventTime());
    }
    
    protected void log(String msg) {
        System.out.println(mName+": "+msg);
    }
}
