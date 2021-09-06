package cm.deepdream.academia.programmation.deserializers;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;

public class DomaineDeserializer extends JsonDeserializer<Domaine>{
	private Logger logger = Logger.getLogger(DomaineDeserializer.class.getName()) ;
	
	
	public DomaineDeserializer() {
		super() ;
	}
	
	public Domaine deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		Domaine domaine = new Domaine() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		String libelle = node.get("libelle").asText();
		logger.info("libelle="+libelle);
        
        Long id = node.get("id").asLong() ;
        logger.info("id="+id);
        
		Long num = node.get("num").asLong();
		logger.info("num="+num);
		
        String createur = node.get("createur").asText();
        logger.info("createur="+createur);
        
        String modificateur = node.get("modificateur").asText();
        logger.info("modificateur="+modificateur);
        logger.info("dateCreation="+node.get("dateCreation").asText());
        
        LocalDateTime dateCreation = LocalDateTime.parse(node.get("dateCreation").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        logger.info("dateCreation="+dateCreation);
        
        LocalDateTime dateDernMaj = LocalDateTime.parse(node.get("dateDernMaj").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        logger.info("dateDernMaj="+node.get("dateDernMaj").asText());
        
        ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        mapper.registerModule(module);
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
		
        domaine.setId(id);
        domaine.setNum(num);
        domaine.setCreateur(createur);
        domaine.setModificateur(modificateur);
        domaine.setDateCreation(dateCreation);
        domaine.setDateDernMaj(dateDernMaj);
        domaine.setLibelle(libelle);

        domaine.setEtablissement(etablissement);
        logger.info("Fin de la deserialization");
        return domaine ;
	}

}
