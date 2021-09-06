package cm.deepdream.academia.viescolaire.util;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.viescolaire.deserializers.CoursDeserializer;
import cm.deepdream.academia.viescolaire.deserializers.EvaluationDeserializer;
import cm.deepdream.academia.programmation.deserializers.ExamenDeserializer;

public class DeserializerToolkit {
	private Logger logger = Logger.getLogger(DeserializerToolkit.class.getName()) ;
	private static DeserializerToolkit toolkit ;
	private ObjectMapper mapper = new ObjectMapper();
	
	private DeserializerToolkit() {
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Cours.class, new CoursDeserializer());
        module.addDeserializer(Evaluation.class, new EvaluationDeserializer());
        module.addDeserializer(Examen.class, new ExamenDeserializer());
     
        mapper.registerModule(module);
	}
	
	public static DeserializerToolkit getToolkit() {
		if(toolkit == null) {
			toolkit = new DeserializerToolkit() ;
		}
		return toolkit ;
	}
	
	public synchronized Object deserialize(String stringObject, Class classType) throws Exception {
		logger.log(Level.INFO, "Content to deserialize "+stringObject);
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		Object object =  mapper.readValue(new StringReader(stringObject), classType); 
		return object ;
	}

}
