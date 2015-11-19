@Grab(group='io.advantageous.boon', module='boon-json', version='0.5.6.RELEASE') 
import io.advantageous.boon.json.*

JsonParser parser = new JsonParserFactory().createLaxParser()
def json = parser.parse('{"num": 1a}')

println json.num.getClass()    //Integer
println json.num               //59
