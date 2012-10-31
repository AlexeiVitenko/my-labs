package by.samm;

public abstract class Element {
    protected Request mRequest;

    public boolean putRequest(Request request) {
        if (mRequest != null) {
            return false;
        }
        mRequest = request;
        return true;
    }

    public abstract Request nextTurn();

    public boolean getState() {
        return mRequest != null;
    }

    public String stateToString() {
        return getState() ? "1" : "0";
    }
}
