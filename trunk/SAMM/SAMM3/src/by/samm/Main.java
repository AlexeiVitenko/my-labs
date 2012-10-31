package by.samm;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        QueueingModel qm = new QueueingModel();
        for (int i = 0; i < 10000; i++) {
            qm.nextMove();
        }
        qm.showResults();
    }

}
