package com.matt_wise.flyover;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;

public class App extends JPanel {
    private FlyoverMap map;
    private Point camera = new Point(3000,3000);
    private double phi = 0;//angle of rotation in radians
    private int height = 8700;//geo
    //private int height = 200;//commache
    private int horizon = 120;
    private int scaleHeight = 120;
    private int distance = 3000;


    public App(FlyoverMap map, KeyListener listener){
        super();
        addKeyListener(listener);
        setFocusable(true);
        this.map = map;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        final JFrame frame = new JFrame("Flyover");
        //FlyoverMap fMap = new CommacheMap("/Users/matt/git/VoxelSpace/maps/C1W.png",
        //        "/Users/matt/git/VoxelSpace/maps/D1.png");
        FlyoverMap fMap = new GeoMap("/Users/matt/git/VoxelSpace/usmaps/yosemite/yosemite1-colors.png",
                "/Users/matt/git/VoxelSpace/usmaps/yosemite/yosemite1-height.png", 10);
        Listener listener = new Listener();
        App app = new App(fMap, listener);
        app.setBackground(new Color(0x84c1f8)); //sky blue
        frame.add(app);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (true) {
            app.camera.x -= listener.getSpeed() * Math.sin(app.phi);
            app.camera.y -= listener.getSpeed() * Math.cos(app.phi);
            app.phi += (listener.getRotation()/30);
            app.repaint();
            Thread.sleep(10);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        renderFrame(g,camera, phi, height, horizon, scaleHeight, distance, this.getWidth(), this.getHeight());
    }

    private void renderFrame(Graphics g, Point p, double phi, int height, int horizon, int scaleHeight, int distance, int screenWidth, int screenHeight){
        int[] drawnHeightsOnScreen = new int[screenWidth];
        Arrays.fill(drawnHeightsOnScreen, screenHeight);
        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        int z=0;
        while (z<distance){
            z=increment(z);
            //line on map
            Point pLeft  = new Point(p.x + (-cosPhi * z - sinPhi * z),
                                     p.y + (sinPhi * z - cosPhi * z));
            Point pRight = new Point(p.x + (cosPhi * z - sinPhi * z),
                                     p.y + (-sinPhi * z -cosPhi * z));
            //segment line
            double dx = (pRight.x - pLeft.x)/screenWidth;
            double dy = (pRight.y - pLeft.y)/screenWidth;
            //raster line, drawing a vertical column on screen for each segment
            for (int i=0; i<screenWidth; i++){
                int heightOnScreen = (int)((height - map.getHeight((int)pLeft.x, (int)pLeft.y)) / ((float)z) * (float)scaleHeight + horizon);
                if (heightOnScreen < drawnHeightsOnScreen[i]) {
                    drawVerticalLine(g, i, heightOnScreen, drawnHeightsOnScreen[i], map.getColor((int) pLeft.x, (int) pLeft.y));
                    drawnHeightsOnScreen[i] = heightOnScreen;
                }
                pLeft.x += dx;
                pLeft.y += dy;
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

    private int increment(int a){
        if (a<300){
            return a+1;
        } else {
            return (int)(a*1.005);
        }
    }
}
