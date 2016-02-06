import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by robotics on 2/3/2016.
 */
public class MainClass {

    public static void main(String[] args) throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        BufferedImage image = webcam.getImage();

        float[] hsb = new float[3];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                Pixel pix = new Pixel(image, x, y);
                System.out.print(pix.getHue() + " | " + pix.getSaturation() + " | " + pix.getValue());

                if (pix.getHue() >= 100 && pix.getHue() <= 140 && pix.getSaturation() >= 3/4 && pix.getValue() >= 3/4) {
                    System.out.println("    -   YAY!!!");
                    pix.setRed(0);
                    pix.setBlue(0);
//                    image.setRGB(x, y, 0x0000FF00);
                } else {
                    System.out.println();
                    image.setRGB(x, y, 0x00000000);
                }

//                int rgbData = image.getRGB(x, y);
//                int alpha = (rgbData & 0xFF000000) >> 24;
//                int red = (int) (((rgbData & 0x00FF0000) >> 16) * .2);
//                int green = (rgbData & 0x0000FF00) >> 8;
//                int blue = (int) ((rgbData & 0x000000FF) * .2);
//                System.out.println((alpha << 24) | (red << 16) | (green << 8) | blue);
//                image.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);

//                System.out.println(green);

//                Color.RGBtoHSB(red, green, blue, hsb);

//                if (hsb[0] >= 0 && hsb[1] >= .8 && hsb[2] >= .4)
//                    System.out.println((rgbData & 0x00FFFFFF) + " | " + red + " | " + green + " | " + blue + " | " + Arrays.toString(hsb));
            }
        }
        ImageIO.write(image, "JPG", new File("test.jpg"));
    }
}
