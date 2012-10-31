package by.samm;

public class Buffer extends Element {
    
    
    @Override
    public Request nextTurn() {
        Request r = mRequest;
        mRequest = null;
        return r;
    }
}
