package by.samm;

public class Source extends ElementWithRandom {

    public Source(LemerRandom random, float ro) {
        super(random, ro);
    }
    
    @Override
    public Request nextTurn() {
        mRequest = new Request();
        return super.nextTurn();
    }
}
