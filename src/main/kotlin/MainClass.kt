/**
 * Created by Joshua on 2/6/2016.
 */
import com.github.sarxos.webcam.Webcam
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * A data class that stores lower and upper bounds
 */
data class Range(val lower: Float, val upper: Float)

/**
 * Selects which test image to use
 */
val imageTest: Int = 2

/**
 * Reference to the webcam object
 */
val webcam: Webcam = Webcam.getDefault()

/**
 * The range of values that hue can be.
 * @see Range
 */
val hueRange: Range = Range(80f, 150f)

/**
 * The range of values that saturation can be.
 * @see Range
 */
val saturationRange: Range = Range(0.15f, 1f)

/**
 * The range of values that value can be.
 * @see Range
 */
val valueRange: Range = Range(0.25f, 1f)


fun main(args: Array<String>) {
    // The BufferedImage object that will contain all the data
    val image: BufferedImage

    // A When statement that loads either a preset or a live photo
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

    // Iterate over every pixel
    for (x in 0..image.width - 1) {
        for (y in 0..image.height - 1) {
            // Runs pixel through filter algorithm
            image.filterPixel(x, y)
        }
    }

    // Write [modified] BufferedImage to a file
    ImageIO.write(image, "PNG", File("test.png"))
}

/**
 * A function that takes a pixel and passes it through a HSV filter, set by the Ranges above
 * @see Range
 * @see BufferedImage
 *
 */
fun BufferedImage.filterPixel(x: Int, y: Int) {
    // RGB raw data (Int)
    val rgbData: Int = getRGB(x, y)
    // Red value from RGB data (Int)
    val red: Int = rgbData and 0x00FF0000 shr 16
    // Green value from RGB data (Int)
    val green: Int = rgbData and 0x0000FF00 shr 8
    // Blue value from RGB data (Int)
    val blue: Int = rgbData and 0x000000FF
    // Array to store hsb return data
    val hsvArray: FloatArray = FloatArray(3)

    // Convert RGB data into HSV data
    Color.RGBtoHSB(red, green, blue, hsvArray)


    // Set HUE value from hsv data array (mult by 360 to get degrees)
    val hue: Float = hsvArray[0] * 360
    // Set SATURATION value from hsv data array
    val saturation: Float = hsvArray[1]
    // Set VALUE value from hsv data array
    val value: Float = hsvArray[2]

    // This IF checks if all HSV data values are within the set ranges
    if ((hue >= hueRange.lower && hue <= hueRange.upper ) && (saturation >= saturationRange.lower && saturation <= saturationRange.upper) && (value >= valueRange.lower && value <= valueRange.upper)) {
        println("Pixel at ($x, $y) matches the criteria given!")
    } else {
        // If not, sets pixels to nothing...
        setRGB(x, y, 0x00000000) // 0 shl 24 or (0 shl 16) or (green shl 8) or 0)
    }

}

