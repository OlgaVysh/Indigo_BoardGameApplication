package service

import com.fasterxml.jackson.databind.module.SimpleModule
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
    private val mapper = jacksonObjectMapper().apply {
        val module = SimpleModule().apply {
            addKeySerializer(Coordinate::class.java ,CoordinateMapKeySerializer())
            addKeyDeserializer(Coordinate::class.java, CoordinateMapKeyDeserializer())
        }
        registerModules(module)
    }

    /*
    init {
        val module = SimpleModule()
        module.addKeyDeserializer(Coordinate::class.java,CoordinateMapKeyDeserializer())
        mapper.registerModules(SimpleModule().addKeyDeserializer(Coordinate::class.java,CoordinateMapKeyDeserializer()))
    }
*/



    /**
     * function for saving a game of Indigo to a file
     */
    fun saveGameToFile(gameStateList: List<Indigo>, path:String) {
        //changed the parameters from indigo: Indigo, path: String to gameStateList: List<Indigo>, path: String
        val json = mapper.writeValueAsString(gameStateList)
        File(path).writeText(json)

    }

    /**
     * function for reading a saved game from a file
     */
    fun readGameFromFile(path: String): List<Indigo> {
        val jsonString = File(path).readText()
        return mapper.readValue<List<Indigo>>(jsonString)
    }
}