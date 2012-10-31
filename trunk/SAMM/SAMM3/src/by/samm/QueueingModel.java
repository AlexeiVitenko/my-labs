package by.samm;

public class QueueingModel {

    private Source mSource;
    private Channel mFirstChannel;
    private Channel mSecondChannel;
    private LemerRandom mRandom;

    public QueueingModel() {
        mRandom = new LemerRandom(22695477, 1, 2L << 32, 50000);
        mRandom.generate();
        mSource = new Source(mRandom, 0.5f);
        mFirstChannel = new Channel(mRandom, 0.6f);
        mSecondChannel = new Channel(mRandom, 0.8f);
    }
    
    public void nextMove(){
        mFirstChannel.nextTurn();
        mSecondChannel.nextTurn();
        
    }
}
