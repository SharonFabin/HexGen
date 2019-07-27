package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

public class HexGen{

    private File file;
    private String hexPath;
    private BufferedImage originalImage, scaledImage;
    private PrintWriter writer;
    private int width,height;
    public DoubleProperty doubleProperty;

    public HexGen(){
        this.file=null;
        this.hexPath="";
        this.width=128;
        this.height=128;
        this.doubleProperty = new SimpleDoubleProperty(0);
    }

    public void process() throws Exception{
        doubleProperty.setValue(0);
        writer = new PrintWriter(hexPath, "UTF-8");
        originalImage = ImageIO.read(file);
        scaledImage = new BufferedImage(width,height,originalImage.getType());

        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();

        writer.println("v2.0 raw");
        run();
    }

    public void run() {
        int p,r,g,b;
        long r5,g5,b5,p5;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.doubleProperty.setValue((double)(i*width+j)/(double)(width*height));
                p = scaledImage.getRGB(i, j);
                r = (p >> 16) & 0xff;
                g = (p >> 8) & 0xff;
                b = p & 0xff;

                r5 = Math.round((31 * (r + 4)) / 255.0);
                g5 = Math.round((31 * (g + 4)) / 255.0);
                b5 = Math.round((31 * (b + 4)) / 255.0);
                p5 = (b5 | g5 << 5 | r5 << 10);
                System.out.print(Long.toHexString(p5) + " ");
                writer.print(Long.toHexString(p5)+" ");
            }
            System.out.println();
        }
        writer.close();

    }

    public void setImageFile(File file){
        this.file=file;
    }

    public void setHexPath(String hexPath){
        this.hexPath=hexPath;
    }

    public void setWidth(int width){
        this.width=width;
    }

    public void setHeight(int height){
        this.height=height;
    }

    public String getHexPath(){
        return this.hexPath;
    }
}
