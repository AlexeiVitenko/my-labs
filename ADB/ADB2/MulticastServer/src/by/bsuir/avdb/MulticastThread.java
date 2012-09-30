package by.bsuir.avdb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Random;

public class MulticastThread extends Thread {
//    private static final String ADDR = "10.41.0.251";
    private static final String ADDR = "230.0.0.1";
    private static final int PORT = 5558;
    private Random mRandom;
    private MulticastSocket mSocket;
    public MulticastThread() throws UnknownHostException, IOException{
        mRandom = new Random();
        mSocket = new MulticastSocket();// new DatagramSocket(PORT);
        InetAddress group = InetAddress.getByName(ADDR);
        mSocket.joinGroup(group);
    }
    
    @Override
    public void run() {
        while (true) {
            int rate = mRandom.nextInt(100); 
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(rate);
            System.out.println(""+rate);
            /*byte buf [] = new byte[256];
            buf = (""+rate).getBytes();*/
            try {
                InetAddress group = InetAddress.getByName(ADDR);
                DatagramPacket dp = new DatagramPacket(bb.array(), bb.array().length, group, PORT);
                //DatagramPacket dp = new DatagramPacket(buf, buf.length, group, PORT);
                mSocket.send(dp);
                Thread.sleep(1000);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                break;
            }
        }
        mSocket.close();
    }
}
