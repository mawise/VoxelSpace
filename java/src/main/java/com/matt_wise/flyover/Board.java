package com.matt_wise.flyover;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Board extends JPanel {
    private BufferedImage colorMapImage = null;
    private BufferedImage heightMapImage = null;
    private int mapWidth;
    private int mapHeight;

    public Board(String colorMapFile, String heightMapFile){
        super();
        try {
            colorMapImage = ImageIO.read(new File(colorMapFile));
        } catch (IOException e) {
        }
        try {
            heightMapImage = ImageIO.read(new File(heightMapFile));
        } catch (IOException e) {
        }
        if (colorMapImage.getHeight() != heightMapImage.getHeight()){
            throw new RuntimeException("Color map and height map have different Y dimensions");
        }
        if (colorMapImage.getWidth() != heightMapImage.getWidth()){
            throw new RuntimeException("Color map and height map have different X dimensions");
        }
        mapWidth = colorMapImage.getWidth();
        mapHeight = colorMapImage.getHeight();
    }

    private int heightMap(double x, double y){
        double xx = ((x % mapWidth) + mapWidth) % mapWidth;
        double yy = ((y % mapHeight) + mapHeight) % mapHeight;
        logCoordinates("height", xx, yy);
        try {
            Color color = new Color(heightMapImage.getRGB((int) xx, (int) yy));
            return color.getRed();//heightmap is greyscale, all colors should be the same
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Asked for X, Y: " + x + ", " + y);
            System.err.println("Translated to X, Y: " + xx + ", " + yy);
            System.err.println("Min X, Y: " + heightMapImage.getMinX() + ", " + heightMapImage.getMinY());
            System.err.println("X-width, Y-height: " + heightMapImage.getWidth() + ", " + heightMapImage.getHeight());
            throw e;
        }
    }

    private Color colorMap(double x, double y){
        double xx = ((x % mapWidth) + mapWidth) % mapWidth;
        double yy = ((y % mapHeight) + mapHeight) % mapHeight;
        logCoordinates("color", xx, yy);
        return new Color(colorMapImage.getRGB((int) xx, (int) yy));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFrame(g);
    }

    private void drawFrame(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        Dimension size = getSize();
        double w = size.getWidth();
        double h = size.getHeight();

        render(g, new Point(0,0), 200, 120, 120, 300, 800, 600);
    }

    /**
     *
     * @param p camera's x and y coordinate
     * @param height camera's z coordinate
     * @param horizon
     * @param scaleHeight
     * @param distance max distance from camera to render
     * @param screenWidth
     * @param screenHeight
     */
    private void render(Graphics g, Point p, int height, int horizon, int scaleHeight, int distance, double screenWidth, double screenHeight){

        for (int z = distance; z>1; z--){
        //int z = distance;
            //line on map
            Point pLeft  = new Point(p.x - z, p.y - z);
            Point pRight = new Point(p.x + z, p.y - z);
            //segment line
            double dx = (pRight.x - pLeft.x)/screenWidth;
            //raster line, drawing a vertical column on screen for each segment
            for (int i=0; i<screenWidth; i++){
                float heightOnScreen = (height - heightMap(pLeft.x, pLeft.y)) / ((float)z) * (float)scaleHeight + horizon;
                //System.out.println("heightOnScreen: " + heightOnScreen);
                drawVerticalLine(g, i, (int) heightOnScreen, (int) screenHeight, colorMap(pLeft.x, pLeft.y));
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

    private void logCoordinates(String resource, double x, double y){
        //System.out.println("Request for " + resource + " at " + Double.toString(x) + ", " + Double.toString(y));
    }

}