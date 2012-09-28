package by.bsuir.samm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Historgam {
    private static final int PADDING = 10;
    private static final int RECT_PADDING = 30;

    private Point O;
    private JLabel dst;
    private BufferedImage mImage;
    private Set<Float> mData;

    public Historgam(JLabel label, Set<Float> data) {
        dst = label;
        mData = data;
        mImage = new BufferedImage(label.getWidth(), label.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    public void draw() {
        Graphics graph = mImage.getGraphics();
        graph.setColor(dst.getBackground());
        graph.fillRect(0, 0, mImage.getWidth(), mImage.getHeight());

        drawCoordinates(graph);
        drawHistogram(graph);

        dst.setIcon(new ImageIcon(mImage));
    }

    private void drawHistogram(Graphics graph) {
        int width = mImage.getWidth() - RECT_PADDING - O.x;
        int height = O.y - RECT_PADDING;

        int i = 0;
        float max = 0.05f;
        for (Float f : mData) {
            i++;
            if (f > max) {
                drawRect(graph, max, i, width, height);
                max += 0.05f;
                i = 0;
            }
        }
    }

    private void drawRect(Graphics graph, float max, int i, int width, int height) {
        graph.drawRect((int) (O.x + (max - 0.05f) * width), O.y - ((int) ((i / (float) mData.size()) * height * 15)),
                (int) ((0.05) * width), (int) ((i / (float) mData.size()) * height * 15));
    }

    private void drawCoordinates(Graphics graph) {
        graph.setColor(Color.RED);
        O = new Point(PADDING, mImage.getHeight() - PADDING);
        graph.drawLine(10, 0 + PADDING, 10, mImage.getHeight());
        graph.drawLine(10, 0 + PADDING, 0, PADDING * 2);
        graph.drawLine(10, 0 + PADDING, 20, PADDING * 2);
        graph.drawLine(0, mImage.getHeight() - PADDING, mImage.getWidth() - PADDING, mImage.getHeight() - PADDING);
        graph.drawLine(mImage.getWidth() - PADDING * 2, mImage.getHeight() - PADDING * 2, mImage.getWidth() - PADDING,
                mImage.getHeight() - PADDING);
        graph.drawLine(mImage.getWidth() - PADDING * 2, mImage.getHeight(), mImage.getWidth() - PADDING,
                mImage.getHeight() - PADDING);
    }
}
