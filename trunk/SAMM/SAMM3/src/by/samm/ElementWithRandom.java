package by.samm;

import java.util.Random;

public class ElementWithRandom extends Element{
    private final float P;
    private final LemerRandom mRandom;
    private Random mRnd;
    public ElementWithRandom(LemerRandom random, float p) {
        mRandom = random;
        P = p;
        mRnd = new Random();
    }
    


    private float nextRandom() {
        //return mRandom.nextFloat();
        return mRnd.nextFloat();
    }
    

    
    @Override
    public Request nextTurn() {
        float nxt = nextRandom();
        //System.out.println(getClass().getSimpleName() + " "+nxt);
        if (nxt > P) {
            Request r = mRequest;
            mRequest = null;
            return r;
        }else{
            return null;
        }
    }
}
