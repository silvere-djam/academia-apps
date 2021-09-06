package cm.deepdream.academia.programmation.serializers;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cm.deepdream.academia.programmation.data.Photo;

public class PhotoSerializer extends JsonSerializer<Photo>{

	@Override
	public void serialize(Photo value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			jsonGenerator.writeNumberField("size", value.getSize());
			jsonGenerator.writeStringField("path", value.getPath());
			jsonGenerator.writeStringField("subPath1", value.getSubPath1());
			jsonGenerator.writeStringField("subPath2", value.getSubPath2());
			jsonGenerator.writeStringField("fileName", value.getFileName());
			jsonGenerator.writeStringField("bytesStr", value.getBytesStr());
			jsonGenerator.writeStringField("contentType", value.getContentType());
		}
        jsonGenerator.writeEndObject();
        
	}

}
