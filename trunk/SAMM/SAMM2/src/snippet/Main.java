package snippet;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {
    static LemerRandom lr;

    public static void main(String[] args) throws UnsupportedEncodingException {
        lr = new LemerRandom(22695477, 1, (1L << 32), 50000);
        try {
            System.out.println(System.getProperty("user.dir"));
            lr.doInBackground();
            System.out.println();
            mainCommandCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mainCommandCycle() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String[] params = sc.nextLine().split(" ");
            switch (params[0].toLowerCase().charAt(0)) {
            case 'u':
                Distributions.uniform(Float.parseFloat(params[1]), Float.parseFloat(params[2]), lr.getResult());
                break;
            case 'e':
                Distributions.exponential(Float.parseFloat(params[1]), lr.getResult());
                break;
            case 'g':
                Distributions.uniform(Float.parseFloat(params[1]), Float.parseFloat(params[2]), lr.getResult());
                break;
            default:
                break;
            }
            Distributions.makeChart();
        }
    }
}
