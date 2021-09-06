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
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.data.Examen;

public class CentreExamenDeserializer extends JsonDeserializer<CentreExamen>{
	private Logger logger = Logger.getLogger(CentreExamenDeserializer.class.getName()) ;
	
	
	public CentreExamenDeserializer() {
		super() ;
	}
	
	public CentreExamen deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		CentreExamen centreExamen = new CentreExamen() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		LocalDate dateDebut = LocalDate.parse(node.get("dateDebut").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT)) ;
        logger.info("dateDebut="+node.get("dateDebut").asText());
        
        LocalDate dateFin = LocalDate.parse(node.get("dateFin").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT)) ;
        logger.info("dateFin="+node.get("dateFin").asText());
		
        String numero = node.get("numero").asText();
		logger.info("numero="+numero);
		
		String libelle = node.get("libelle").asText();
		logger.info("libelle="+libelle);
		
        Long id = node.get("id").asLong() ;
        logger.info("id="+node.get("id").asText());
        
		Long num = node.get("num").asLong();
		logger.info("num="+node.get("num").asLong());
		
		Long nbreSalles = node.get("nbreSalles").asLong();
		logger.info("nbreSalles="+node.get("nbreSalles").asLong());
		
		Long nbreCandidats = node.get("nbreCandidats").asLong();
		logger.info("nbreCandidats="+node.get("nbreCandidats").asLong());
		
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
        module.addDeserializer(AnneeScolaire.class, new AnneeScolaireDeserializer());
        module.addDeserializer(Examen.class, new ExamenDeserializer());
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);

        AnneeScolaire anneeScolaire = mapper.readValue(node.get("anneeScolaire").toPrettyString(), AnneeScolaire.class);
        logger.info("anneeScolaire="+anneeScolaire);
        
        Examen examen = mapper.readValue(node.get("examen").toPrettyString(), Examen.class);
        logger.info("examen="+examen);

		
        centreExamen.setId(id);
        centreExamen.setNum(num);
        centreExamen.setNumero(numero);
        centreExamen.setLibelle(libelle);
        centreExamen.setNbreSalles(nbreSalles);
        centreExamen.setNbreCandidats(nbreCandidats);
        
        centreExamen.setCreateur(createur);
        centreExamen.setModificateur(modificateur);
        centreExamen.setDateCreation(dateCreation);
        centreExamen.setDateDernMaj(dateDernMaj);

        centreExamen.setEtablissement(etablissement);
        centreExamen.setAnneeScolaire(anneeScolaire);
        centreExamen.setExamen(examen);
    
        logger.info("Fin de la deserialization");
        return centreExamen ;
	}

}
