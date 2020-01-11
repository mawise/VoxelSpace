package com.matt_wise.flyover;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CommacheMap implements FlyoverMap {
    private BufferedImage colorMapImage = null;
    private BufferedImage heightMapImage = null;
    private int mapWidth;
    private int mapHeight;

    public CommacheMap(String colorMapFile, String heightMapFile) throws IOException {
        super();
        colorMapImage = ImageIO.read(new File(colorMapFile));
        heightMapImage = ImageIO.read(new File(heightMapFile));
        if (colorMapImage.getHeight() != heightMapImage.getHeight()){
            throw new RuntimeException("Color map and height map have different Y dimensions");
        }
        if (colorMapImage.getWidth() != heightMapImage.getWidth()){
            throw new RuntimeException("Color map and height map have different X dimensions");
        }
        mapWidth = colorMapImage.getWidth();
        mapHeight = colorMapImage.getHeight();
    }

    @Override
    public int getHeight(int x, int y) {
        double xx = ((x % mapWidth) + mapWidth) % mapWidth;
        double yy = ((y % mapHeight) + mapHeight) % mapHeight;
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

    @Override
    public Color getColor(int x, int y) {
        double xx = ((x % mapWidth) + mapWidth) % mapWidth;
        double yy = ((y % mapHeight) + mapHeight) % mapHeight;
        return new Color(colorMapImage.getRGB((int) xx, (int) yy));
    }
}
