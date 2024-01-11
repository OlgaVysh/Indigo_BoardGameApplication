package service

import com.fasterxml.jackson.core.JsonProcessingException
import entity.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

/**
 * class to read/write files for saving and loading games
 *
 * @constructor creates an Instance of the Service tied to a given [RootService]
 *
 * @param root [RootService] the IOService belongs to
 */
class IOService(private val root: RootService) : AbstractRefreshingService() {
    private val mapper = jacksonObjectMapper()

    /**
     * function for saving a game of Indigo to a file
     */
    fun saveGameToFile(indigo: Indigo, path: String) {
       try {
           val json = mapper.writeValueAsString(indigo)
           File(path).writeText(json)
       }catch (e: JsonProcessingException) {
        // Hier können Sie die Ausnahme behandeln oder einfach weitergeben
        throw e
    }
    }

    /**
     * function for reading a saved game from a file
     */
    fun readGameFromFile(path: String): Indigo {
        val jsonString = File(path).readText()
        return mapper.readValue<Indigo>(jsonString)
    }
}