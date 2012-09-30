package by.bsuir.avdb;

import java.io.IOException;
import java.net.UnknownHostException;

public class MulticastServer {
    public static void main(String ...strings ) {
        try {
            Thread t = new MulticastThread();
            t.run();
            System.in.read();
            t.interrupt();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
