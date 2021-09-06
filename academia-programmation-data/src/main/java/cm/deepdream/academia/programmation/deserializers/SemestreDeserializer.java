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
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.data.Semestre;

public class SemestreDeserializer extends JsonDeserializer<Semestre>{
	private Logger logger = Logger.getLogger(SemestreDeserializer.class.getName()) ;
	
	
	public SemestreDeserializer() {
		super() ;
	}
	
	public Semestre deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		
		Semestre semestre = new Semestre() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		String libelle = node.get("libelle").asText();
		logger.info("libelle="+node.get("libelle").asText());
        LocalDate dateDebut = LocalDate.parse(node.get("dateDebut").asText(), DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        logger.info("dateDebut="+node.get("dateDebut").asText());
        LocalDate dateFin = LocalDate.parse(node.get("dateFin").asText(), DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        logger.info("dateFin="+node.get("dateFin").asText());
        String numero = node.get("numero").asText();
        logger.info("numero="+node.get("numero").asText());
        int courant = node.get("courant").asInt();
        logger.info("courant="+node.get("courant").asInt());
        
        Long id = node.get("id").asLong() ;
        logger.info("id="+node.get("id").asLong());
		Long num = node.get("num").asLong();
		logger.info("num="+node.get("num").asLong());
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
        module.addDeserializer(AnneeScolaire.class, new AnneeScolaireDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
        
        AnneeScolaire anneeScolaire = mapper.readValue(node.get("anneeScolaire").toPrettyString(), AnneeScolaire.class);
        logger.info("anneeScolaire="+anneeScolaire);
		
        semestre.setId(id);
        semestre.setNum(num);
        semestre.setCreateur(createur);
        semestre.setModificateur(modificateur);
        semestre.setDateCreation(dateCreation);
        semestre.setDateDernMaj(dateDernMaj);
        semestre.setLibelle(libelle);
        semestre.setDateDebut(dateDebut);
        semestre.setDateFin(dateFin);
        semestre.setNumero(numero);
        semestre.setCourant(courant);
        semestre.setEtablissement(etablissement);
        semestre.setAnneeScolaire(anneeScolaire);
        logger.info("Fin de la deserialization");
        return semestre ;
	}

}
