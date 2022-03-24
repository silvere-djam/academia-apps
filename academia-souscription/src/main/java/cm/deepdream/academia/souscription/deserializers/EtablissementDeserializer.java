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
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Localite;
import cm.deepdream.academia.souscription.model.Logo;
import cm.deepdream.academia.souscription.model.Pays;

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
        mapper.registerModule(module);
		JsonNode node = jp.getCodec().readTree(jp);
		
		Long id = node.get("id").asLong();

        String libelle = node.get("libelle").asText();
        String cycle = node.get("cycle").asText();
        String boitePostale = node.get("boitePostale").asText();
        String telephone = node.get("telephone") == null ? null:node.get("telephone").asText();
        String email = node.get("email") == null ? null:node.get("email").asText();
        
        Integer nbElevesApprox = node.get("nbElevesApprox").asInt();
        
        
        etablissement.setId(id);
        etablissement.setLibelle(libelle);
        etablissement.setBoitePostale(boitePostale);
        etablissement.setTelephone(telephone);
        etablissement.setEmail(email);   
        etablissement.setCycle(cycle);
        etablissement.setNbElevesApprox(nbElevesApprox);
     
        return etablissement ;
	}

}
