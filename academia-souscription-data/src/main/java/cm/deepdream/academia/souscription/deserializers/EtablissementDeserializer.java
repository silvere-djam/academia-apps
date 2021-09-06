package cm.deepdream.academia.souscription.deserializers;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.souscription.constants.DateConstants;
import cm.deepdream.academia.souscription.data.Contact;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Logo;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.souscription.data.Localite;

public class EtablissementDeserializer extends StdDeserializer<Etablissement>{
	
	public EtablissementDeserializer() {
		super(Etablissement.class) ;
	}
	
	public Etablissement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	
		Etablissement etablissement = new Etablissement() ;
		
		ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Pays.class, new PaysDeserializer());
        module.addDeserializer(Localite.class, new LocaliteDeserializer()) ;
        module.addDeserializer(Logo.class, new LogoDeserializer()) ;
        module.addDeserializer(Contact.class, new ContactDeserializer()) ;
        mapper.registerModule(module);
		JsonNode node = jp.getCodec().readTree(jp);
		
		Long id = node.get("id").asLong();
        String createur = node.get("createur").asText();
        String modificateur = node.get("modificateur").asText();
        LocalDateTime dateCreation = LocalDateTime.parse(node.get("dateCreation").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        LocalDateTime dateDernMaj = LocalDateTime.parse(node.get("dateDernMaj").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        
        String libelle = node.get("libelle").asText();
        String cycle = node.get("cycle").asText();
        String boitePostale = node.get("boitePostale").asText();
        String telephone = node.get("telephone") == null ? null:node.get("telephone").asText();
        String email = node.get("email") == null ? null:node.get("email").asText();
        
        Integer nbElevesApprox = node.get("nbElevesApprox").asInt();
        
        try {
        	Logo logo = mapper.readValue(node.get("logo").toPrettyString(), Logo.class);
        	etablissement.setLogo(logo);
        }catch(Exception ex) {}
        
        try {
        	Contact contactChef = mapper.readValue(node.get("contactChef").toPrettyString(), Contact.class);
        	etablissement.setContactChef(contactChef);
        }catch(Exception ex) {}
        
        try {
        	Contact contactInformaticien = mapper.readValue(node.get("contactInformaticien").toPrettyString(), Contact.class);
        	etablissement.setContactInformaticien(contactInformaticien);
        }catch(Exception ex) {}
        
        try {
        	Localite localite = mapper.readValue(node.get("localite").toPrettyString(), Localite.class);
        	etablissement.setLocalite(localite);
        }catch(Exception ex) {}
        
        etablissement.setId(id);
        etablissement.setCreateur(createur);
        etablissement.setModificateur(modificateur);
        etablissement.setDateCreation(dateCreation);
        etablissement.setDateDernMaj(dateDernMaj);
        
        etablissement.setLibelle(libelle);
        etablissement.setBoitePostale(boitePostale);
        etablissement.setTelephone(telephone);
        etablissement.setEmail(email);
        
        etablissement.setCycle(cycle);
        etablissement.setNbElevesApprox(nbElevesApprox);
     
        
        return etablissement ;
	}

}
