package by.samm;

public class ElementWithRandom extends Element{
    private final float P;
    private final LemerRandom mRandom;

    public ElementWithRandom(LemerRandom random, float p) {
        mRandom = random;
        P = p;
    }
    


    private float nextRandom() {
        return mRandom.nextFloat();
    }
    

    
    @Override
    public Request nextTurn() {
        if (nextRandom() > P) {
            Request r = mRequest;
            mRequest = null;
            return r;
        }else{
            return null;
        }
    }
}
