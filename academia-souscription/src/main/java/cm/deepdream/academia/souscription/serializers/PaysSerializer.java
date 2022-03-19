package cm.deepdream.academia.souscription.serializers;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import cm.deepdream.academia.souscription.model.Pays;

public class PaysSerializer extends JsonSerializer<Pays>{

	@Override
	public void serialize(Pays value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			jsonGenerator.writeNumberField("id", value.getId());
			jsonGenerator.writeStringField("code", value.getCode());
			jsonGenerator.writeStringField("libelle", value.getLibelle());
			jsonGenerator.writeStringField("codeTel", value.getCodeTel());
			jsonGenerator.writeStringField("monnaie", value.getMonnaie());
		}
        jsonGenerator.writeEndObject();
        
	}

}
