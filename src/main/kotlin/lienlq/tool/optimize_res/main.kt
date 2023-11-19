package lienlq.tool.optimize_res

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception
import javax.imageio.ImageIO


private fun loadConfigFromFile(configPath: String) : ConfigDto {
    val mapper = ObjectMapper(YAMLFactory()).apply {
        registerModule(KotlinModule())
    }

    return BufferedReader(FileReader(configPath)).use {
        mapper.readValue(it, ConfigDto::class.java)
    }
}



fun main2(args:Array<String>) {

    var configPath = "config.yaml"

    if (args.size > 1) {
        configPath = args[0]
    }

    val configDto = loadConfigFromFile(configPath)


    val testFolder = configDto.inputFolder

    File(configDto.outFolder).apply {
        if (exists()) deleteRecursively()
    }


    File(configDto.outFolder).apply {
        if (!exists()) mkdirs()

    }



    val resizeWidth = configDto.maxWidth

    val outFolder = File(configDto.outFolder)

    var orgSize = 0L
    var reducedSize = 0L
    var count = 0


    for ( file in File(testFolder).listFiles()) {

        if (file.isDirectory || file.isHidden) continue

        var orgImage : BufferedImage?= null

        try {
            orgImage = ImageIO.read(file)
        } catch (e: Exception) {
            println("error => ${e.toString()}")
        }

        if (orgImage == null) continue


        orgSize += file.length()

        var f = file
        if (configDto.isPngToJpg && file.extension == "png") {
            f = png2Jpg(file, outFolder, outFileName = if (configDto.startWithName  == null) null else configDto.startWithName + count)
        }

        if (resizeWidth > 0 &&  orgImage.width > resizeWidth) {
            f = resize(f.absoluteFile, outFolder, resizeWidth,  false, outFileName = if (configDto.startWithName  == null) null else configDto.startWithName + count)
        }

        reducedSize += f.length()
        count++
    }


    println("Count: $count, Org: $orgSize, Reduced Size = ${orgSize - reducedSize}")
}