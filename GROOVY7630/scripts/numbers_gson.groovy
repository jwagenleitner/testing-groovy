@Grab(group='com.google.code.gson', module='gson', version='2.4') 
import com.google.gson.*
import com.google.gson.stream.*

Gson gson = new GsonBuilder().create()
JsonReader reader = new JsonReader(new StringReader('{num: 1a}'))
reader.setLenient(true) // this is actually the default
def result = gson.fromJson(reader, Map)

println result.num.getClass() //String
println result.num    //"1a"
