@Grab('com.fasterxml.jackson.core:jackson-databind:2.6.3')
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
       
ObjectMapper mapper = new ObjectMapper()
mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS)
mapper.enable(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS)
mapper.enable(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)
mapper.enable(JsonParser.Feature.IGNORE_UNDEFINED)

Map<String, Object> json = mapper.readValue('{"num": 1a}', Map.class)

// Fails on parse above, never gets here

println json.num.getClass()
println json.num
