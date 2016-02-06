import com.github.sarxos.webcam.Webcam
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

data class Range(val lower: Float, val upper: Float)


val imageTest: Int = 2
val webcam: Webcam = Webcam.getDefault()
val hueRange: Range = Range(80f, 150f)
val saturationRange: Range = Range(0.15f, 1f)
val valueRange: Range = Range(0.25f, 1f)


/**
 * Created by Joshua on 2/6/2016.
 */
fun main(args: Array<String>) {
    val image: BufferedImage
    when (imageTest) {
        0 -> {
            image = ImageIO.read(File("rainbow1.jpg"))
        }
        1 -> {
            image = ImageIO.read(File("rainbow2.jpg"))
        }
        2 -> {
            image = ImageIO.read(File("rainbow3.png"))
        }
        else -> {
            webcam.open()
            image = webcam.image
            webcam.close()
        }
    }

    for (x in 0..image.width - 1) {
        for (y in 0..image.height - 1) {
            filterPixel(image, x, y)
        }
    }

    ImageIO.write(image, "PNG", File("test.png"))
}

fun filterPixel(image: BufferedImage, x: Int, y: Int) {
    val rgbData: Int = image.getRGB(x, y)
    val red: Int = rgbData and 0x00FF0000 shr 16
    val green: Int = rgbData and 0x0000FF00 shr 8
    val blue: Int = rgbData and 0x000000FF
    val hsbA: FloatArray = FloatArray(3)

    Color.RGBtoHSB(red, green, blue, hsbA)

    val hue: Float = hsbA[0] * 360
    val saturation: Float = hsbA[1]
    val value: Float = hsbA[2]

    if ((hue >= hueRange.lower && hue <= hueRange.upper ) && (saturation >= saturationRange.lower && saturation <= saturationRange.upper) && (value >= valueRange.lower && value <= valueRange.upper)) {
        println("Pixel at ($x, $y) matches the criteria given!")
    } else {
        image.setRGB(x, y, 0 shl 24 or (0 shl 16) or (green shl 8) or 0)
    }

}

