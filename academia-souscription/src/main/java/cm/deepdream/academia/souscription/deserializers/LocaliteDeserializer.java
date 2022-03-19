package cm.deepdream.academia.souscription.deserializers;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import cm.deepdream.academia.souscription.model.Localite;
import cm.deepdream.academia.souscription.model.Pays;

public class LocaliteDeserializer extends JsonDeserializer<Localite>{
	
	public LocaliteDeserializer() {
		super() ;
	}
	
	public Localite deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	
		Localite ville = new Localite() ;
		
		ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Pays.class, new PaysDeserializer());
        mapper.registerModule(module);
		JsonNode node = jp.getCodec().readTree(jp);
		Long id = node.get("id").asLong() ;
        String libelle = node.get("libelle").asText(); 
        try {
        	Pays pays = mapper.readValue(node.get("pays").toPrettyString(), Pays.class);
        	ville.setPays(pays);
        }catch(Exception ex) {}
        ville.setId(id);
        ville.setLibelle(libelle);
        
        return ville ;
	}

}
