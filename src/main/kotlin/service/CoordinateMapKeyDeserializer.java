package service;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import entity.Coordinate;
import kotlin.jvm.Throws;
/*
class CoordinateMapKeyDeserializer: DeserializeKey() {
   // val kMapper = ObjectMapper().registerModules(KotlinModule())
   //
    //    return key?.let {kMapper.readValue<Coordinate>(key)}
   // @Throws(IOException::class,JsonProcessingException::class)
    //override fun deserializeKey(key:String?, ctxt: DeserializationContext?):Coordinate? {
    //    return key?.let {kMapper.readValue<Coordinate>(key)}

    //or this ??
    @Throws(IOException::class)
    override fun deserializeKey(key:String?, ctxt: DeserializationContext?):Coordinate? {
        return key?.split(",")?.let{
            val(row,column) = it
                    Coordinate(row.toInt(),column.toInt())
        }
    }

}
*/