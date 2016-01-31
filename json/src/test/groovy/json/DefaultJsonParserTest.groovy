package json

import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import static json.ResourceHelper.*

class DefaultJsonParserTest extends GroovyTestCase {

    JsonSlurper parser

    @Override
    void setUp() {
        parser = new JsonSlurper().setType(JsonParserType.CHAR_BUFFER)
    }

    void testAllCards() {
        def json = parser.parse(getAsInputStream('AllCards.json'))
    }

    void testAllSets() {
        def json = parser.parse(getAsInputStream('AllSets-x.json'))
    }

    void testCities() {
        def json = parser.parse(getAsInputStream('citys.json'))
        int pop = 0
        for (o in json) {
            assert o.loc[0] instanceof BigDecimal
            assert o.loc[0].scale() > 0
            if (o.city == 'THAYNE') {
                pop = o.pop
            }
        }
        assert pop == 505
    }

    void testCitmCatalog() {
        def json = parser.parse(getAsInputStream('citm_catalog.json'))
        assert json.areaNames['205706006'] == '1er balcon cour'
    }

    void testEPAData() {
        def json = parser.parse(getAsInputStream('epa_data.json'))
    }

    void testUser() {
        def json = parser.parse(getAsInputStream('user.json'))
    }

    void testRepos() {
        def json = parser.parse(getAsInputStream('repos.json'))
    }

    void testUSDAData() {
        def json = parser.parse(getAsInputStream('usda_data.json'))
        assert json.dataset[2].title == 'Barge Grain Movements'
    }

    void testRequest() {
        def json = parser.parse(getAsInputStream('request.json'))
    }

}
