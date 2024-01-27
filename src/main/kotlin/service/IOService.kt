package service

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import entity.*
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectories


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
     * Saves a game state to a file in JSON format.
     *
     * @param indigoList The Indigo game state to be saved.
     * @param path The path to the file where the game state will be saved.
     */
    /*fun saveGameToFile(indigo: Indigo, path:String) {
        //changed the parameters from indigo: Indigo, path: String to gameStateList: List<Indigo>, path: String

        val json = mapper.writeValueAsString(indigo)
        File(path).writeText(json)

    }

     */
    fun saveGameToFile(indigoList: MutableList<Indigo>, path:String) {
        val dirPath = Path("./indigogames/")
        dirPath.createDirectories()
        val json = mapper.writeValueAsString(indigoList)
        val fileToWrite = File("$dirPath/$path")
        fileToWrite.createNewFile()
        fileToWrite.writeText(json)
    }


    /**
     * Reads a game state from a file in JSON format.
     *
     * @param path The path to the file from which the game state will be read.
     * @return The Indigo game state read from the specified file.
     */

    fun readGameFromFile(path: String): List<Indigo> {
        val jsonString = File(path).readText()
        val indigoList = mutableListOf<Indigo>()
        indigoList.add(mapper.readValue<Indigo>(jsonString))
        return indigoList
    }

    /*
    fun readGameFromFile(path: String): Indigo {
        val jsonString = File(path).readText()

        return mapper.readValue<Indigo>(jsonString)
    }

     */


}