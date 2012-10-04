package by.bsuir.samm;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.CardLayout;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.SwingWorker.StateValue;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main implements PropertyChangeListener {

    private JFrame frame;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JProgressBar progressBar;
    private LemerRandom lemer;
    private JButton btnRandomize;
    private JTextArea textArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
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
    public Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 750, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 184, 562);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(5, 5, 179, 37);
        panel.add(panel_1);
        panel_1.setLayout(null);

        JLabel lblA = new JLabel("a:");
        lblA.setBounds(12, 12, 14, 15);
        panel_1.add(lblA);

        textField = new JTextField();
        textField.setText("22695477");
        textField.setBounds(44, 10, 114, 19);
        panel_1.add(textField);
        textField.setColumns(10);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(5, 42, 179, 37);
        panel.add(panel_2);
        panel_2.setLayout(null);

        JLabel lblR = new JLabel("R:");
        lblR.setBounds(12, 12, 14, 15);
        panel_2.add(lblR);

        textField_1 = new JTextField();
        textField_1.setText("1");
        textField_1.setBounds(44, 10, 114, 19);
        panel_2.add(textField_1);
        textField_1.setColumns(10);

        JPanel panel_3 = new JPanel();
        panel_3.setBounds(5, 79, 179, 37);
        panel.add(panel_3);
        panel_3.setLayout(null);

        JLabel lblM = new JLabel("m:");
        lblM.setBounds(12, 12, 24, 15);
        panel_3.add(lblM);

        textField_2 = new JTextField();
        textField_2.setText("4294967296");
        textField_2.setBounds(44, 10, 114, 19);
        panel_3.add(textField_2);
        textField_2.setColumns(10);

        JPanel panel_5 = new JPanel();
        panel_5.setBounds(5, 114, 179, 37);
        panel.add(panel_5);
        panel_5.setLayout(null);

        JLabel lblGoal = new JLabel("goal:");
        lblGoal.setBounds(0, 12, 36, 15);
        panel_5.add(lblGoal);

        textField_3 = new JTextField();
        textField_3.setBounds(44, 10, 114, 19);
        textField_3.setText("50000");
        panel_5.add(textField_3);
        textField_3.setColumns(10);

        btnRandomize = new JButton("Randomize");
        btnRandomize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                start();
            }
        });
        btnRandomize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int i = 1 + 5;
            }
        });
        btnRandomize.setBounds(15, 163, 117, 25);
        panel.add(btnRandomize);
        
        JPanel panel_6 = new JPanel();
        panel_6.setBounds(5, 193, 169, 358);
        panel.add(panel_6);
        panel_6.setLayout(null);
        
        JLabel label_1 = new JLabel("Дисперсия:");
        label_1.setBounds(18, 11, 69, 14);
        panel_6.add(label_1);
        
        JLabel label_2 = new JLabel("Ожидание:");
        label_2.setBounds(18, 36, 69, 14);
        panel_6.add(label_2);
        
        JLabel lbln = new JLabel("Отклонение:");
        lbln.setBounds(10, 61, 77, 14);
        panel_6.add(lbln);
        
        JButton button = new JButton("Рассчитать");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                startCalcuclating();
            }
        });
        button.setBounds(10, 86, 149, 23);
        panel_6.add(button);
        
        lblDisp = new JLabel("");
        lblDisp.setBounds(94, 11, 65, 14);
        panel_6.add(lblDisp);
        
        lblMat = new JLabel("");
        lblMat.setBounds(94, 36, 65, 14);
        panel_6.add(lblMat);
        
        lblOtkl = new JLabel("");
        lblOtkl.setBounds(89, 61, 70, 14);
        panel_6.add(lblOtkl);
        
        lblShod = new JLabel("");
        lblShod.setBounds(10, 126, 149, 33);
        panel_6.add(lblShod);

        JPanel panel_4 = new JPanel();
        panel_4.setBounds(186, 0, 548, 263);
        frame.getContentPane().add(panel_4);
        panel_4.setLayout(null);

        lblHistogramlabel = new JLabel("");
        lblHistogramlabel.setBounds(0, 0, 548, 263);
        panel_4.add(lblHistogramlabel);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(186, 299, 548, 263);
        frame.getContentPane().add(scrollPane_1);

        textArea = new JTextArea();
        scrollPane_1.setViewportView(textArea);

        JLabel label = new JLabel("Результат:");
        label.setBounds(196, 274, 76, 14);
        frame.getContentPane().add(label);

        lblResult = new JLabel("");
        lblResult.setBounds(268, 274, 46, 14);
        frame.getContentPane().add(lblResult);

        progressBar = new JProgressBar();
        progressBar.setBounds(527, 274, 197, 19);
        frame.getContentPane().add(progressBar);
        progressBar.setStringPainted(true);
    }
    private JLabel lblDisp;
    private JLabel lblHistogramlabel;
    private JLabel lblResult;
    private JLabel lblMat;
    private JLabel lblOtkl;
    private JLabel lblShod;
    
    private void start() {
        try {
            long a = Long.parseLong(textField.getText());
            long m = Long.parseLong(textField_2.getText());
            long r = Long.parseLong(textField_1.getText());
            long g = Long.parseLong(textField_3.getText());
            runWorker(a, r, m, g);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Проверьте аргументы!");
        }
    }

    private void runWorker(long a, long r, long m, long g) {
        lemer = new LemerRandom(a, r, m, g);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        lemer.addPropertyChangeListener(this);
        lemer.execute();
        textArea.setText("");
        lblResult.setText("");
        lblHistogramlabel.setIcon(new ImageIcon());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            textArea.setText(lemer.getSb().toString());
        }
        if ((evt.getNewValue()).equals(StateValue.DONE)) {
            onComplete();
        }
    }

    private void onComplete(){
        try {
            lblResult.setText(""+lemer.getResult().size());
            
            StringBuilder sb = new StringBuilder();
            for (Float f : lemer.getResult()){
                sb.append(String.format("%.6f", f) + "\n");
            }
            textArea.setText(sb.toString());
            SwingUtilities.invokeLater(new Runnable() {
                
                @Override
                public void run() {
                    Historgam h = new Historgam(lblHistogramlabel, lemer.getResult());
                    h.draw();
                }
            });
            if (!lemer.get()) {
                JOptionPane.showMessageDialog(null, "FAIL!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    private void startCalcuclating(){
        SortedSet<Float> res = lemer.getResult();
        float value = 0;
        for (Float float1 : res) {
            value += float1;
        }
        value /= res.size();
        lblMat.setText(""+value);
        
        float disp = 0;
        for (Float float1 : res) {
            disp += float1*float1 - value * value;
        }
        disp /= res.size();
        disp *= (res.size()/((float)(res.size()-1)));
        lblDisp.setText(""+disp);
        lblOtkl.setText(""+Math.sqrt(disp));
        int k = 0;
        ArrayList<Float> result = new ArrayList<Float>(res);
        for (int i = 1; i < res.size(); i+=2) {
            float x1 = result.get(i-1);
            float x2 = result.get(i);
            if ((x1*x1+x2*x2)<1f) {
                k++;
            }
        }
        
        lblShod.setText(""+((2f*k/res.size())/(Math.PI/4f)));
    }
}
