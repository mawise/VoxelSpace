package com.matt_wise.flyover;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class App extends JPanel {
    private FlyoverMap map;
    private Point camera = new Point(0,0);
    private int height = 200;
    private int horizon = 120;
    private int scaleHeight = 120;
    private int distance = 300;


    public App(FlyoverMap map){
        super();
        this.map = map;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame frame = new JFrame("Flyover");
        FlyoverMap fMap = new CommacheMap("/Users/matt/git/VoxelSpace/maps/C1W.png",
                "/Users/matt/git/VoxelSpace/maps/D1.png");
        App app = new App(fMap);
        app.setBackground(new Color(0x84c1f8)); //sky blue
        frame.add(app);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
            app.camera.y -= 1;
            app.repaint();
            Thread.sleep(16);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        renderFrame(g,camera, height, horizon, scaleHeight, distance, this.getWidth(), this.getHeight());
    }

    private void renderFrame(Graphics g, Point p, int height, int horizon, int scaleHeight, int distance, int screenWidth, int screenHeight){
        int[] drawnHeightsOnScreen = new int[screenWidth];
        Arrays.fill(drawnHeightsOnScreen, screenHeight);
        //for (int z = distance; z>1; z--){
        for (int z=1; z < distance; z++){
            //int z = distance;
            //line on map
            Point pLeft  = new Point(p.x - z, p.y - z);
            Point pRight = new Point(p.x + z, p.y - z);
            //segment line
            double dx = (pRight.x - pLeft.x)/screenWidth;
            //raster line, drawing a vertical column on screen for each segment
            for (int i=0; i<screenWidth; i++){
                int heightOnScreen = (int)((height - map.getHeight((int)pLeft.x, (int)pLeft.y)) / ((float)z) * (float)scaleHeight + horizon);
                if (heightOnScreen < drawnHeightsOnScreen[i]) {
                    drawVerticalLine(g, i, heightOnScreen, drawnHeightsOnScreen[i], map.getColor((int) pLeft.x, (int) pLeft.y));
                    drawnHeightsOnScreen[i] = heightOnScreen;
                }
                pLeft.x += dx;
            }
        }
    }
    private void drawVerticalLine(Graphics g, int x, int y1, int y2, Color color){
        g.setColor(color);
        int yy = y1;
        if (y1 < 0) {
            yy=0;
        }
        g.drawLine(x, yy, x, y2);
    }

}
