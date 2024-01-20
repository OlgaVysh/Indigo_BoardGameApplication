package service
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.IOException
import entity.Coordinate
import kotlin.jvm.Throws


/**
 * Custom key deserializer for deserializing String keys into Coordinate objects.
 *
 * This class provides two different implementations for deserialization:
 * 1. Using Jackson ObjectMapper with KotlinModule.
 * 2. Using manual parsing of the key String.
 */
class CoordinateMapKeyDeserializer: KeyDeserializer() {
    private val kMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    //
    //    return key?.let {kMapper.readValue<Coordinate>(key)}

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserializeKey(key: String?, ctxt: DeserializationContext?): Coordinate? {
        return key?.let { kMapper.readValue<Coordinate>(key) }

        /*

    //or this ??
    @Throws(IOException::class)
    override fun deserializeKey(key:String?, ctxt: DeserializationContext?):Coordinate? {
        return key?.split(",")?.let{
            //val(row,column) = it.map { num ->num.toInt() }
              //      Coordinate(row,column)
            Coordinate(it[0].toInt(), it[1].toInt())
        }
    }
*/

    }
}