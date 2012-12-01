import java.util.Random;


public class SMOObject {
    private float mNextEventDelta;
    private Random mRandom = new Random();
    private float mLambda;
    
    public SMOObject(float lambda) {
        mLambda = lambda;
    }
    
    public float getNextEventDelta() {
        return mNextEventDelta;
    }
    
    public void setNextEventDelta(float nextEventDelta) {
        mNextEventDelta = nextEventDelta;
    }
    
    public float someEventHappens(float delta){
        mNextEventDelta -= delta;
        return mNextEventDelta;
    }
    
    private float getTimeInterval(float divisor)
    {
        float value;
        do
        {
            value = mRandom.nextFloat();
        }
        while (value == 0 || value == 1);
        do
        {
            value = (float) Math.abs(-Math.log(value) / divisor);
        }
        while (value == 0);
        return value;
    }
}
