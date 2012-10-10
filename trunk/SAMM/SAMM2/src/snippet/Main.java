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
        showHelp();
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
                Distributions.gamma(Float.parseFloat(params[1]), Integer.parseInt(params[2]), lr.getResult());
                break;
            case 't':
                Distributions.thriangular(Float.parseFloat(params[1]), Float.parseFloat(params[2]), lr.getResult());
                break;
            case 'x':
                Distributions.gauss(Float.parseFloat(params[1]), Float.parseFloat(params[2]), Integer.parseInt(params[3]), lr.getResult());
                break;
            case 's':
                Distributions.simpson(Float.parseFloat(params[1]), Float.parseFloat(params[2]), lr.getResult());
                break;
            case 'h':
                showHelp();
                break;
            default:
                break;
            }
            
        }
    }
    
    private static void showHelp(){
        System.out.println("/***********************************************\\");
        System.out.println("Uniform: u a-float b-float");
        System.out.println("Exponential: e λ-float");
        System.out.println("Gamma: g λ-float η - float");
        System.out.println("Thriangular: t a-float b-float");
        System.out.println("Gauss: x m-float, d-float, n-float");
        System.out.println("Simpson: s a-float b-float");
        System.out.println("\\***********************************************/");
    }
}
