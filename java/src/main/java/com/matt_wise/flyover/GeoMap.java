package com.matt_wise.flyover;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GeoMap implements FlyoverMap {
    //assume colormap at 1px = 1m resolution
    //assume heightMap is at lower resolution, scale is number of color pixels per height pixel
    private int heightMapScale;
    private BufferedImage colorMapImage = null;
    private BufferedImage heightMapImage = null;
    private int mapWidth;
    private int mapHeight;

    public GeoMap(String colorMapFile, String heightMapFile, int heightMapScale) throws IOException {
        colorMapImage = ImageIO.read(new File(colorMapFile));
        int colorWide = colorMapImage.getWidth();
        int colorHigh = colorMapImage.getHeight();
        heightMapImage = ImageIO.read(new File(heightMapFile));
        int heightWide = heightMapImage.getWidth();
        int heightHigh = heightMapImage.getHeight();
        this.heightMapScale = heightMapScale;
        if (heightWide * heightMapScale < colorWide){
            mapWidth = heightWide *heightMapScale;
        } else {
            mapWidth = colorWide;
        }
        if (heightHigh *heightMapScale < colorHigh){
            mapHeight = heightHigh *heightMapScale;
        } else {
            mapHeight = colorHigh;
        }
    }


    private int lastHeightXx=0;
    private int lastHeightYy=0;
    private int lastHeight=0;

    @Override
    public int getHeight(int x, int y) {
        int xx = x/heightMapScale;
        int yy = y/heightMapScale;
        if (xx < 0 || yy < 0 || xx >= heightMapImage.getWidth() || yy >= heightMapImage.getHeight()){
            return 0;
        }
        if (xx==lastHeightXx && yy==lastHeightYy){
            return lastHeight*3;
        } else {
            lastHeightXx = xx;
            lastHeightYy = yy;
            Color color = new Color(heightMapImage.getRGB(xx, yy));
            lastHeight = heightFromColor(color);
            return lastHeight*3;
        }
    }

    @Override
    public Color getColor(int x, int y) {
        double xx = x;
        double yy = y;
        if (x<0 || y<0 || x >= colorMapImage.getWidth() || y >= colorMapImage.getWidth()){
            return Color.BLACK;
        }
        return new Color(colorMapImage.getRGB((int) xx, (int) yy));
    }

    //This is my personal heightmap construction--don't know if it's used elsewhere
    private int heightFromColor(Color color){
        int r=color.getRed();
        int g=color.getGreen();
        int b=color.getBlue();
        return r + (255*g) + (255*255*b);
    }
}
