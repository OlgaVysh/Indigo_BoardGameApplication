package service

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.KotlinModule
import entity.Coordinate
import java.io.IOException
import kotlin.jvm.Throws

class CoordinateMapKeySerializer: JsonSerializer<Coordinate>() {
    val kMapper = ObjectMapper().registerModules(KotlinModule())


    //val serializedCoordinate = kMapper.writeValueAsString(coordinates)
    @Throws(IOException::class)
    override fun serialize(value: Coordinate?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.let { jGen ->
            value?.let { coordinate ->
                jGen.writeFieldName(kMapper.writeValueAsString(coordinate))
            } ?: jGen.writeNull()
            gen?.writeFieldName("${value?.row},${value?.column}")
        }
    }

}