package json

import groovy.json.JsonParserType
import groovy.json.JsonSlurper

class IndexOverlyJsonParserTest extends DefaultJsonParserTest {

    @Override
    void setUp() {
        parser = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
    }

}
