package cm.deepdream.academia.souscription.deserializers;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import cm.deepdream.academia.souscription.model.Contact;

public class ContactDeserializer extends JsonDeserializer<Contact>{
	
	public ContactDeserializer() {
		super() ;
	}
	
	public Contact deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	
		Contact contact = new Contact() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		String nom = node.get("nom").asText();
        String email = node.get("email").asText();
        String telephone = node.get("telephone").asText();
       
        contact.setNom(nom);
        contact.setEmail(email);
        contact.setTelephone(telephone);
        
        return contact ;
	}

}
