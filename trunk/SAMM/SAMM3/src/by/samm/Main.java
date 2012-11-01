package by.samm;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        QueueingModel qm = new QueueingModel();
        if (args.length>0) {
            qm.start(Integer.parseInt(args[0]));
        } else {

            qm.start(50000);
        }
    }

}
