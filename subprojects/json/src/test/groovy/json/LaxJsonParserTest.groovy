package json

import groovy.json.JsonParserType
import groovy.json.JsonSlurper

class LaxJsonParserTest extends DefaultJsonParserTest {

    @Override
    void setUp() {
        parser = new JsonSlurper().setType(JsonParserType.LAX)
    }

}
