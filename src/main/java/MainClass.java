import com.github.sarxos.webcam.Webcam;
import net.d4.aiir.NormalizeInput;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by robotics on 2/3/2016.
 */
public class MainClass {


    static NormalizeInput ni = new NormalizeInput(new double[5], NormalizeInput.NormalizationType.MULTIPLICATIVE);

    public static void main(String[] args) throws IOException {




        Webcam webcam = Webcam.getWebcams().get(0);
        webcam.open();
        BufferedImage image = webcam.getImage();
        webcam.close();
        int[][] surroundingPixels = new int[5][5];

//        BufferedImage image = ImageIO.read(new File("rainbow1.jpg"));
//        BufferedImage image = ImageIO.read(new File("rainbow2.jpg"));
//        BufferedImage image = ImageIO.read(new File("rainbow3.png"));
        float[] hsb = new float[3];

//        BufferedImage bufferedImage = image;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                int rgbData = image.getRGB(x, y);
                int alpha = (rgbData & 0xFF000000) >> 24;
                int red = (rgbData & 0x00FF0000) >> 16;
                int green = (rgbData & 0x0000FF00) >> 8;
                int blue = (rgbData & 0x000000FF);

                Color.RGBtoHSB(red, green, blue, hsb);


//                Pixel pix = new Pixel(image, x, y);

                System.out.println(hsb[0] * 360 + " | " + hsb[1] + " | " + hsb[2]);
//                System.out.print(pix.getHue() + " | " + pix.getSaturation() + " | " + pix.getValue());

                float hue = hsb[0] * 360;
                float sat = hsb[1];
                float val = hsb[2];

                if (hue >= 80 && hue <= 150 && sat >= 0.15 && val >= 0.25) {
                    System.out.println("    -   YAY!!!");
                    //pix.setRed(0);
                    //pix.setBlue(0);
                } else {
                    System.out.println();
//                    image.setRGB(x, y, 0x00000000);
//                    pix.setValue(pix.getValue() / 7);
                    image.setRGB(x, y, Color.HSBtoRGB(hue, sat, val / 9));
                }

//                System.out.println();

            }
        }
        ImageIO.write(image, "PNG", new File("test.png"));


    }
}



