package cm.deepdream.academia.souscription.serializers;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.souscription.constants.DateConstants;
import cm.deepdream.academia.souscription.model.Localite;
import cm.deepdream.academia.souscription.model.Pays;

public class LocaliteSerializer extends JsonSerializer<Localite>{

	@Override
	public void serialize(Localite value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Pays.class, new PaysSerializer());
			mapper.registerModule(module);
			Pays pays = value.getPays() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeStringField("libelle", value.getLibelle());
        	try {
        		jsonGenerator.writeObjectField("pays", pays);
        	}catch(Exception ex) {}
		}
		jsonGenerator.writeEndObject();
		
	}

}
