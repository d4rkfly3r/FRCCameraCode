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
        ImageIO.write(image, "JPG", new File("test.jpg"));

        float[] hsb = new float[3];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgbData = image.getRGB(x, y);
                int red = (rgbData & 0x00FF0000) >> 16;
                int green = (rgbData & 0x0000FF00) >> 8;
                int blue = (rgbData & 0x000000FF);
                Color.RGBtoHSB(red, green, blue, hsb);

                if (hsb[0] > 120 && hsb[1] > 50 && hsb[2] > 60)
                    System.out.println((rgbData & 0x00FFFFFF) + " | " + red + " | " + green + " | " + blue + " | " + Arrays.toString(hsb));
            }
        }
    }
}
