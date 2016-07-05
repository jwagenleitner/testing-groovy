package bugreport;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

/**
  From users mailing list
  http://mail-archives.apache.org/mod_mbox/groovy-users/201606.mbox/%3C1466835140469-5733514.post@n5.nabble.com%3E
**/
public class TemplateEngineTest {

    @Test
    public void testLeak() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("groovy"); 
		
		String template = "step-${i}";
		
		String groovy = 
			"def engine = new groovy.text.GStringTemplateEngine();\n" +
			"def res = engine.createTemplate(template).make(bindings);\n" + 
			"return res.toString();";

		for (int i = 0; i < (10000000); i++) {
			Bindings vars = new SimpleBindings();
			vars.put("template", template);
			
			Map<String, Object> templateObjects = new HashMap<>();
			vars.put("bindings", templateObjects);
			templateObjects.put("i", i);

			Object res = engine.eval(groovy, vars);
			
			if (i % 100 == 0) {
				System.out.println("->" + res);
			}
		}

    }

}
