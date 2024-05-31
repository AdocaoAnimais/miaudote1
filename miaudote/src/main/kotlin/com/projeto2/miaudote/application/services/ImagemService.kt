package com.projeto2.miaudote.application.services

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class ImagemService(){
    private val fileStorageLocation: Path = Paths.get("src/main/resources/templates/pet")
        .toAbsolutePath().normalize()

    init {
        Files.createDirectories(this.fileStorageLocation)
    }

    fun salvarImagem(file: MultipartFile): String {
        val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
        val targetLocation = this.fileStorageLocation.resolve(fileName)
        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        return targetLocation.toString() // Return the path or URL of the saved file
    }
}
