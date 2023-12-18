package service

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
class IOService(private val root: RootService) {

    /**
     * function for saving a game of Indigo to a file
     */
    private val objectMapper = jacksonObjectMapper()
    fun saveGameToFile(indigo: Indigo, filePath: String){
        val gameStates = mutableListOf<Indigo>()
        var currentState: Indigo? = root.currentGame

        while (currentState != null) {
            gameStates.add(0, currentState)
            currentState = currentState.previousGameState
        }
        val json = objectMapper.writeValueAsString(indigo)
        File(filePath).writeText(json)

    }

    /**
     * function for reading a saved game from a file
     */
    fun readGameFromFile(){

    }
}