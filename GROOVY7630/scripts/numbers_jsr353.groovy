@Grab('org.glassfish:javax.json:1.0.4')
import javax.json.*
import javax.json.stream.*

// No lenient/relaxed mode that I could find, but thought I'd test it

JsonReader reader = Json.createReader(new StringReader('{"num": 1a}'))
JsonObject json = reader.readObject()  //JsonParsingException

println json.num.getClass()
println json.num
