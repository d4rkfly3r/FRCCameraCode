/**
 * Created by Joshua on 2/6/2016.
 */
import com.github.sarxos.webcam.Webcam
import net.d4.aiir.SelfOrganizingMap
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

/**
 * A data class that stores lower and upper bounds
 */
data class Range(val lower: Float, val upper: Float)

data class Result(val object1: Double, val object2: Double)

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
val satRange: Range = Range(0.15f, 1f)

/**
 * The range of values that value can be.
 * @see Range
 */
val valRange: Range = Range(0.25f, 1f)



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
            image.filterPixel(x, y, hueRange, satRange, valRange)
        }
    }

    image.filterByDensity(4);

    // Write [modified] BufferedImage to a file
    ImageIO.write(image, "PNG", File("test.png"))
}

fun BufferedImage.compareTo(other: BufferedImage): Result {
    var success: Int = 0
    for (x in 0..width - 1) {
        for (y in 0..height - 1) {
            if (getRGB(x, y) == other.getRGB(x, y)) {
                success++
            }
        }
    }
    return Result(((success.toDouble() / (width * height)) * 100), (success.toDouble() / (width * height)))
}

fun BufferedImage.filterByDensity(density: Int) {
    val surroundingPixels = IntArray(Math.pow((1 + (2 * density)).toDouble(), 2.0).toInt())
    for (x in 0..width - 1) {
        for (y in 0..height - 1) {
            getRGBPixelsAroundPoint(x, y, density, surroundingPixels);
            for (pixel in surroundingPixels) {
                //                println((pixel))
                if (pixel != 0x00000000) {
                    println((pixel and 0x0000FF00) shl 8)
                }
            }
        }
    }
}

fun BufferedImage.getRGBPixelsAroundPoint(x: Int, y: Int, density: Int, surroundingPixels: IntArray) {
    val surroundingPixelsList: ArrayList<Int> = ArrayList(surroundingPixels.size + 2)
    for (x1 in x - density..x + density) {
        for (y1 in y - density..y + density) {
            if (x1 > width - 1 || y1 > height - 1 || x1 < 0 || y1 < 0)
                continue;

            surroundingPixelsList.add(getRGB(x1, y1))
        }
    }
}

/**
 * A function that takes a pixel and passes it through a HSV filter, set by the Ranges above
 * @see MainClass.Range
 * @see BufferedImage
 */
fun BufferedImage.filterPixel(x: Int, y: Int, hueRange: Range, satRange: Range, valRange: Range) {
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
    val sat: Float = hsvArray[1]
    // Set VALUE value from hsv data array
    val `val`: Float = hsvArray[2]

    // This IF checks if all HSV data values are within the set ranges
    if ((hue >= hueRange.lower && hue <= hueRange.upper ) && (sat >= satRange.lower && sat <= satRange.upper) && (`val` >= valRange.lower && `val` <= valRange.upper)) {
        println("Pixel at ($x, $y) matches the criteria given!")
    } else {
        // If not, sets pixels to nothing...
        setRGB(x, y, 0x00000000) // 0 shl 24 or (0 shl 16) or (green shl 8) or 0)
    }
}

