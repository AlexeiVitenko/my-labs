package by.bsuir.avdb;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MainWindow implements PropertyChangeListener {

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainWindow() {
        initialize();
    }

    private Historgam mHistorgam;
    private LimitedQueue<Integer> mData = new LimitedQueue<Integer>(10);

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label = new JLabel("");
        label.setBounds(10, 11, 414, 240);
        frame.getContentPane().add(label);
        mHistorgam = new Historgam(label, mData);
        new MulticastListiner().start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
        }
    }

    private class MulticastListiner extends Thread {
        private static final String ADDR = "230.0.0.1";
        private boolean flag;

        @Override
        public void run() {
            MulticastSocket socket;
            try {
                socket = new MulticastSocket(5558);

                InetAddress addr = InetAddress.getByName(ADDR);
                socket.joinGroup(addr);
                DatagramPacket dp;

                while (true) {
                    byte buf[] = new byte[256];
                    dp = new DatagramPacket(buf, buf.length);
                    socket.receive(dp);
                    final int in = ByteBuffer.wrap(buf).getInt();
                    if (flag) {
                        break;
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        
                        @Override
                        public void run() {
                            mData.offer(in);
                            mHistorgam.draw(); 
                        }
                    });
                }

                socket.leaveGroup(addr);
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
