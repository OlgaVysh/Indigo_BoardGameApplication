package service

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.KotlinModule
import entity.Coordinate
import java.io.IOException
import kotlin.jvm.Throws
/**
 * Custom key serializer for serializing Coordinate objects into a JSON-compatible String.
 *
 * This class uses Jackson ObjectMapper with KotlinModule for serialization.
 */
class CoordinateMapKeySerializer: JsonSerializer<Coordinate>() {
    private val kMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())


    //val serializedCoordinate = kMapper.writeValueAsString(coordinates)
    @Throws(IOException::class)
    override fun serialize(value: Coordinate?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.let { jGen ->
            value?.let { coordinate ->
                jGen.writeFieldName(kMapper.writeValueAsString(coordinate))
            } ?: jGen.writeNull()

        }
        //gen?.writeFieldName("${value?.row},${value?.column}")
    }

}