package json

import groovy.json.JsonParserType
import groovy.json.JsonSlurper

class CharSourceParserTest extends DefaultJsonParserTest {

    @Override
    void setUp() {
        parser = new JsonSlurper().setType(JsonParserType.CHARACTER_SOURCE)
    }

}
