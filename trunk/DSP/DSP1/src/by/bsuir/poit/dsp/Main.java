package by.bsuir.poit.dsp;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String HARMONIC = "h 8 2 1 6 4";
    private static final String POLIHARMONIC = "p 3 2 1 1 4 3 4 2 3 1 2 1 3";
    private static final Pattern HARMONIC_COMMAND = Pattern.compile("[hH].*");
    
    private static final Pattern HARMONIC_PATTERN = Pattern
            .compile("[Hh] ([-+]?[0-9]*\\.?[0-9]*) ([0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*\\.?[0-9]*)");

    private static final Pattern POLYHARMONIC_COMMAND = Pattern.compile("[pP].*");
    
    private static final Pattern POLYHARMONIC_PATTERN = Pattern
            .compile("[Pp] ([-+]?[0-9]*\\.?[0-9]*) ([0-9]*) ([0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*)");

    private static final Pattern VARIABLE_POLYHARMONIC_PATTERN = Pattern
            .compile("[Vv] ([-+]?[0-9]*\\.?[0-9]*) ([0-9]*) ([0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*) ([-+]?[0-9]*)");
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        mainCycle();
    }
    private static void mainCycle() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            Matcher m = HARMONIC_COMMAND.matcher(command);
            if (m.matches()) {
                m = HARMONIC_PATTERN.matcher(command);
                if (m.matches()) {
                    makeHarmonicSignal(m, scanner);
                }else{
              //      command = HARMONIC;
             //       makeHarmonicSignal(HARMONIC_PATTERN.matcher(command), scanner);
                }
                
            } else if ((m = POLYHARMONIC_PATTERN.matcher(command)).matches()) {
                Polyharmonic p = new Polyharmonic(Double.parseDouble(m.group(1)), Integer.parseInt(m.group(2)),
                        parseGroup(m, 3), new double[] { getAngle(m, 4), getAngle(m, 6), getAngle(m, 8),
                                getAngle(m, 10), getAngle(m, 12), });
                p.makeChart();
            } else if ((m = VARIABLE_POLYHARMONIC_PATTERN.matcher(command)).matches()) {
                VariableChart p = new VariableChart(Double.parseDouble(m.group(1)), Integer.parseInt(m.group(2)),
                        parseGroup(m, 3), new double[] { getAngle(m, 4), getAngle(m, 6), getAngle(m, 8),
                                getAngle(m, 10), getAngle(m, 12), });
                p.makeChart();
            } else{
                System.out.println("Check parameters!");
            }
        }
    }

    private static final void makeHarmonicSignal(Matcher m, Scanner scanner){
        /*System.out.println("Gr "+m.groupCount());
        System.out.println("Gr "+m.group());
        for (int i = 0; i < m.groupCount(); i++) {
            System.out.println(m.group(i));
        }*/
        Harmonic h = new Harmonic(Double.parseDouble(m.group(1)), Integer.parseInt(m.group(2)),
                Double.parseDouble(m.group(3)) * Math.PI / Double.parseDouble(m.group(4)), Double.parseDouble(m
                        .group(5)));
        h.makeChart();
        System.out.print("Variabale parameter: ");
        while (true) {
            String com = scanner.next();
            char mode = com.toLowerCase().charAt(0);
            for (int i = 0; i < 4; i++) {
                System.out.print("Next " + mode + " value: ");
                com = scanner.next();
                System.out.println("! " + com);
                switch (mode) {
                case 'a':
                    h.setA(Double.parseDouble(com));
                    break;
                case 'f':
                    h.setF(Double.parseDouble(com));
                    break;
                case 'p':
                    h.setPhi(Integer.parseInt(com) * Math.PI / Integer.parseInt(scanner.next()));
                    break;
                default:
                    break;
                }
                h.makeChart();
            }
            System.out.println("Awaiting orders!");
            break;
        }
    }
    
    private static double parseGroup(Matcher m, int n) {
        return Double.parseDouble(m.group(n));
    }

    private static double getAngle(Matcher m, int n) {
        String str1 = m.group(n);
        String str2 = m.group(n + 1);
        return Double.parseDouble(str1) * Math.PI / Double.parseDouble(str2);
    }

}
