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
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.data.Fonction;

public class FonctionDeserializer extends JsonDeserializer<Fonction>{
	private Logger logger = Logger.getLogger(FonctionDeserializer.class.getName()) ;
	
	
	public FonctionDeserializer() {
		super() ;
	}
	
	public Fonction deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		Fonction fonction = new Fonction() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		String libelle = node.get("libelle").asText();
		logger.info("libelle="+node.get("libelle").asText());
        
        Long id = node.get("id").asLong() ;
        logger.info("id="+node.get("id").asText());
		Long num = node.get("num").asLong();
		logger.info("num="+node.get("num").asText());
        String createur = node.get("createur").asText();
        logger.info("createur="+node.get("createur").asText());
        String modificateur = node.get("modificateur").asText();
        LocalDateTime dateCreation = LocalDateTime.parse(node.get("dateCreation").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        logger.info("dateCreation="+node.get("dateCreation").asText());
        LocalDateTime dateDernMaj = LocalDateTime.parse(node.get("dateDernMaj").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        logger.info("dateDernMaj="+node.get("dateDernMaj").asText());
        
        ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
		
        fonction.setId(id);
        fonction.setNum(num);
        fonction.setCreateur(createur);
        fonction.setModificateur(modificateur);
        fonction.setDateCreation(dateCreation);
        fonction.setDateDernMaj(dateDernMaj);
        fonction.setLibelle(libelle);

        fonction.setEtablissement(etablissement);
        logger.info("Fin de la deserialization");
        return fonction ;
	}

}
