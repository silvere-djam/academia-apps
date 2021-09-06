package cm.deepdream.academia.programmation.deserializers;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.EntiteGenerique;

public class EntiteGeneriqueDeserializer extends JsonDeserializer<EntiteGenerique>{
	
	public EntiteGeneriqueDeserializer() {
		super() ;
	}
	
	public EntiteGenerique deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		EntiteGenerique entiteGenerique = new EntiteGenerique() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		Long id = (Long) ((LongNode) node.get("id")).numberValue() ;
		Long num = (Long) ((LongNode) node.get("num")).numberValue();
        String createur = node.get("createur").asText();
        String modificateur = node.get("modificateur").asText();
        LocalDateTime dateCreation = LocalDateTime.parse(node.get("dateCreation").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        LocalDateTime dateDernMaj = LocalDateTime.parse(node.get("dateDernMaj").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        
        entiteGenerique.setNum(num);
        entiteGenerique.setCreateur(createur);
        entiteGenerique.setModificateur(modificateur);
        entiteGenerique.setDateCreation(dateCreation);
        entiteGenerique.setDateDernMaj(dateDernMaj);
        
        return entiteGenerique ;
	}

}
