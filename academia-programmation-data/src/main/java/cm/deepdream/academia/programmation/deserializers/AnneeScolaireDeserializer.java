package cm.deepdream.academia.programmation.deserializers;
import java.io.IOException;
import java.time.LocalDate;
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
import cm.deepdream.academia.souscription.constants.DateConstants;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;

public class AnneeScolaireDeserializer extends JsonDeserializer<AnneeScolaire>{
	private Logger logger = Logger.getLogger(AnneeScolaireDeserializer.class.getName()) ;
	
	
	public AnneeScolaireDeserializer() {
		super() ;
	}
	
	public AnneeScolaire deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		AnneeScolaire anneeScolaire = new AnneeScolaire() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		String libelle = node.get("libelle").asText();
		logger.info("libelle="+node.get("libelle").asText());
        LocalDate dateDebut = LocalDate.parse(node.get("dateDebut").asText(), DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        logger.info("dateDebut="+node.get("dateDebut").asText());
        LocalDate dateFin = LocalDate.parse(node.get("dateFin").asText(), DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        logger.info("dateFin="+node.get("dateFin").asText());
        Integer courante = node.get("courante").asInt();
        logger.info("courante="+node.get("courante").asText());
        
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
		
        anneeScolaire.setId(id);
        anneeScolaire.setNum(num);
        anneeScolaire.setCreateur(createur);
        anneeScolaire.setModificateur(modificateur);
        anneeScolaire.setDateCreation(dateCreation);
        anneeScolaire.setDateDernMaj(dateDernMaj);
        anneeScolaire.setLibelle(libelle);
        anneeScolaire.setDateDebut(dateDebut);
        anneeScolaire.setDateFin(dateFin);
        anneeScolaire.setCourante(courante);
        anneeScolaire.setEtablissement(etablissement);
        logger.info("Fin de la deserialization");
        return anneeScolaire ;
	}

}
