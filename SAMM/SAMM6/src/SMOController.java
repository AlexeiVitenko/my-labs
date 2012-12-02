import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SMOController {
    private static int L1 = 5;
    private static int L2 = 2;
    private static float MU = 5f;
    private static float LAMBDA = 4.5f;
    private Source mSource;
    private List<SMOObject> mObjects = new ArrayList<SMOObject>();

    public SMOController() {
        Worker w2 = new Worker(MU, null, 2, "W2");
        Worker w1 = new Worker(MU, w2, L1, "W1");
        mSource = new Source(4.5f, w1, "S");
        mObjects.add(mSource);
        mObjects.add(w1);
        mObjects.add(w2);

        mainCycle();
    }

    private void mainCycle() {
        for (int i = 0; i < 10000000; i++) {
            Collections.sort(mObjects);
            mObjects.get(0).doWork();
        }
        analyticValues();
    }

    private void analyticValues() {
        float val = 0;
        float ro = LAMBDA / MU;
        for (int i = 0; i <= L1 + 1; i++) {
            val += Math.pow(ro, i);
        }
        float P0 = 1 / val;
        float lambda1 = MU * (1 - P0);
        float ro1 = lambda1/MU;
        val = 0;
        for (int i = 0; i <= L2 + 1; i++) {
            val += Math.pow(ro1, i);
        }
        float P0_2 = 1/val;
        float Potk1 = (float) (Math.pow(ro, L1 + 1) * P0);
        float Potk2 = (float) (Math.pow(ro1, L2 + 1) * P0_2);
        System.out.println("P0 = " + P0);
        System.out.println("ro = " + ro);
        System.out.println("P0_2 = " + P0_2);
        System.out.println("ro1 = " + ro1);
        System.out.println("lambda1 = "+lambda1);
        System.out.println("Potk1 = " + Potk1);
        System.out.println("Potk2 = " + Potk2);
        System.out.println("Potk = " + ((LAMBDA*Potk1+lambda1*Potk2)/LAMBDA));
        int fails = 0;
        for (SMOObject obj : mObjects) {
            obj.dumpResults();
            fails += obj.getFailsCount();
        }
        System.out.println(fails);
        System.out.println("Potk_found = "+((float)fails/mSource.getSentCount()));
    }
}
