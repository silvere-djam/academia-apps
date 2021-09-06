package cm.deepdream.academia.programmation.deserializers;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import cm.deepdream.academia.programmation.data.Photo;

public class PhotoDeserializer extends JsonDeserializer<Photo>{
	
	public PhotoDeserializer() {
		super() ;
	}
	
	public Photo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	
		Photo photo = new Photo() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		String path = node.get("path").asText();
		String subPath1 = node.get("subPath1").asText();
		String subPath2 = node.get("subPath2").asText();
        String fileName = node.get("fileName").asText();
        String contentType = node.get("contentType").asText();
        
        String bytesStr = node.get("bytesStr") ==  null ? null:node.get("bytesStr").asText() ;
        Long size = node.get("size").asLong() ;
       
        photo.setPath(path);
        photo.setSubPath1(subPath1);
        photo.setSubPath2(subPath2);
        photo.setFileName(fileName);
        photo.setContentType(contentType);
        photo.setBytesStr(bytesStr);
        photo.setSize(size);
        
        return photo ;
	}

}
