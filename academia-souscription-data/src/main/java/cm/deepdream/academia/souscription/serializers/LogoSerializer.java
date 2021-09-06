package cm.deepdream.academia.souscription.serializers;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cm.deepdream.academia.souscription.data.Logo;

public class LogoSerializer extends JsonSerializer<Logo>{

	@Override
	public void serialize(Logo value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			jsonGenerator.writeNumberField("size", value.getSize());
			jsonGenerator.writeStringField("path", value.getPath());
			jsonGenerator.writeStringField("fileName", value.getFileName());
			jsonGenerator.writeBinaryField("bytes", value.getBytes());
			jsonGenerator.writeStringField("contentType", value.getContentType());
		}
        jsonGenerator.writeEndObject();
        
	}

}
