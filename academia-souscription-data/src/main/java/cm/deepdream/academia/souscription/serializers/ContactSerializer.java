package cm.deepdream.academia.souscription.serializers;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cm.deepdream.academia.souscription.data.Contact;

public class ContactSerializer extends JsonSerializer<Contact>{

	@Override
	public void serialize(Contact value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			jsonGenerator.writeStringField("nom", value.getNom());
			jsonGenerator.writeStringField("telephone", value.getTelephone());
			jsonGenerator.writeStringField("email", value.getEmail());
		}
        jsonGenerator.writeEndObject();
        
	}

}
