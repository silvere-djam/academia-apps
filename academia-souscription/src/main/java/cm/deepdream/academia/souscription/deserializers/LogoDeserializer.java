package cm.deepdream.academia.souscription.deserializers;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;

import cm.deepdream.academia.souscription.model.Logo;

public class LogoDeserializer extends JsonDeserializer<Logo>{
	
	public LogoDeserializer() {
		super() ;
	}
	
	public Logo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	
		Logo logo = new Logo() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		String path = node.get("path").asText();
        String fileName = node.get("fileName").asText();
        String contentType = node.get("contentType").asText();
        byte[] bytes = node.get("bytes") ==  null ? null:node.get("bytes").binaryValue() ;
        Long size = node.get("size").asLong() ;
       
        logo.setPath(path);
        logo.setFileName(fileName);
        logo.setContentType(contentType);
        logo.setBytes(bytes);
        logo.setSize(size);
        
        return logo ;
	}

}
