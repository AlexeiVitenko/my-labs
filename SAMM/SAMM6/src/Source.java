
public class Source extends SMOObject{

    public Source(float lambda, Worker obj, String name) {
        super(lambda, obj, name);
    }

    @Override
    public void doWork() {
        super.doWork();
        mRequestsCount++;
        if (mNextChannnel!=null) {
            mNextChannnel.putRequest();
        }
    }

    @Override
    public void dumpResults() {
        
    }

    @Override
    public int getFailsCount() {
        return 0;
    }
}
