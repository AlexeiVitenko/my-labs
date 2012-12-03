public class Worker extends SMOObject {

    private final int mQueueMaxLength;
    private int mQueueLength;
    private int mFailCount;

    public Worker(float lambda, Worker obj, int len, String name) {
        super(lambda, obj, name);
        mQueueMaxLength = len + 1;
    }

    @Override
    public void doWork() {
        super.doWork();
        if (mQueueLength > 0) {
            mQueueLength--;
            if (mNextChannnel!=null) {
                mNextChannnel.putRequest();
            }
        }
    }

    public void putRequest() {
        mRequestsCount++;
        if (mQueueLength < mQueueMaxLength) {
            mQueueLength++;
        } else {
            mFailCount++;
        }
    }

    @Override
    public void dumpResults() {
        log("Fail count: "+((float)mFailCount/mRequestsCount));
    }

    @Override
    public int getFailsCount() {
        return mFailCount;
    }
}
