package by.bsuir.avdb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class MulticastClient {
    //private static final String ADDR = "10.41.0.251";
    private static final String ADDR = "230.0.0.1";
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        MulticastSocket socket = new MulticastSocket(5558);

        InetAddress addr = InetAddress.getByName(ADDR);
        socket.joinGroup(addr);
        DatagramPacket dp;

        for (int i = 0; i < 10; i++) {
            byte buf[] = new byte[256];
            dp = new DatagramPacket(buf, buf.length);
            socket.receive(dp);
            int in = ByteBuffer.wrap(buf).getInt();
            System.out.println("! " + in);
        }
        
        socket.leaveGroup(addr);
        socket.close();
    }

}
