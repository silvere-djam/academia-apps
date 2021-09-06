package cm.deepdream.academia.programmation.serializers;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.EntiteGenerique;

public class EntiteGeneriqueSerializer extends JsonSerializer<EntiteGenerique>{

	@Override
	public void serialize(EntiteGenerique value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
        	jsonGenerator.writeStringField("num", Long.toString(value.getNum()));
        	jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
		}
        jsonGenerator.writeEndObject();
	}

}
