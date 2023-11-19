package lienlq.tool.optimize_res


import org.imgscalr.Scalr
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.lang.Exception
import javax.imageio.ImageIO

fun ratioHeight(orgWidth: Int, orgHeight: Int, newWidth: Int) : Int {

    // org width / org height
    // new w / new h
    // h = new w * org height / orgwight

    val ratio = orgWidth.toFloat() / orgHeight
    return newWidth * orgHeight / orgWidth

}


fun png2Jpg(path: File, outFolder: File, outFileName: String? = null): File {
    println("png2jpg: path=${path.absolutePath}")
    val orgImage = ImageIO.read(path)
    val convertedImage = BufferedImage(orgImage.width, orgImage.height, BufferedImage.TYPE_INT_RGB)
    convertedImage.createGraphics().drawImage(orgImage, 0, 0, Color.WHITE, null)





    val outfile = File(outFolder, (outFileName ?: path.nameWithoutExtension) + ".jpg")
    ImageIO.write(convertedImage, "jpg",outfile )

    return outfile
}

fun resize(path: File, outfile: File, width: Int, isCopyIfNoResize : Boolean = true, outFileName: String? = null) : File {
    print("resize: path=${path.absolutePath}")

    try {
        val orgImage = ImageIO.read(path)

        if (orgImage.width < width) {
            if (isCopyIfNoResize) {
                val outfile = File(outfile, (outFileName ?: path.nameWithoutExtension) + "." + path.extension)
                ImageIO.write(orgImage, path.extension, outfile)
            }

            println(" => NO RESIZE")
            return path
        }

        val newHeight = ratioHeight(orgImage.width, orgImage.height, width)

        val resized = Scalr.resize(orgImage, Scalr.Method.SPEED, Scalr.Mode.AUTOMATIC, width, newHeight)


//        val convertedImage = BufferedImage(width, newHeight, orgImage.type)
//
//        convertedImage.createGraphics().drawImage(resized, 0, 0,  null)

        val outfile = File(outfile,  (outFileName ?: path.nameWithoutExtension) + "." + path.extension)
        ImageIO.write(resized, path.extension, outfile)

        println(" ===> ${path.length()} -> ${outfile.length()}")

        return outfile
    } catch (e: Exception) {
        println("======> error : " + e.toString())
        return path;
    }
}
