package cm.deepdream.academia.souscription.deserializers;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import cm.deepdream.academia.souscription.data.Pays;

public class PaysDeserializer extends JsonDeserializer<Pays>{
	
	public PaysDeserializer() {
		super() ;
	}
	
	public Pays deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	
		Pays pays = new Pays() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		Long id = node.get("id").asLong() ;
		String code = node.get("code").asText();
        String libelle = node.get("libelle").asText();
        String codeTel = node.get("codeTel").asText();
        String monnaie = node.get("monnaie").asText() ;
       
        pays.setId(id);
        pays.setCode(code);
        pays.setLibelle(libelle);
        pays.setCodeTel(codeTel);
        pays.setMonnaie(monnaie);
        
        return pays ;
	}

}
