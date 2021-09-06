package cm.deepdream.academia.programmation.deserializers;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;

public class DetailEmploiTempsDeserializer extends JsonDeserializer<DetailEmploiTemps>{
	private Logger logger = Logger.getLogger(DetailEmploiTempsDeserializer.class.getName()) ;
	
	
	public DetailEmploiTempsDeserializer() {
		super() ;
	}
	
	public DetailEmploiTemps deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		DetailEmploiTemps detailET = new DetailEmploiTemps() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
        Long id = node.get("id").asLong() ;
        logger.info("id="+id);
        
		Long num = node.get("num").asLong();
		logger.info("num="+num);
		
        String createur = node.get("createur").asText();
        logger.info("createur="+node.get("createur").asText());
        String modificateur = node.get("modificateur").asText();
        LocalDateTime dateCreation = LocalDateTime.parse(node.get("dateCreation").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        logger.info("dateCreation="+node.get("dateCreation").asText());
        LocalDateTime dateDernMaj = LocalDateTime.parse(node.get("dateDernMaj").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        logger.info("dateDernMaj="+node.get("dateDernMaj").asText());
        
        Integer jourSemaine = node.get("jourSemaine").asInt();
        logger.info("jourSemaine="+jourSemaine);
        String libelleJour = node.get("libelleJour").asText();
        logger.info("libelleJour="+libelleJour);
        LocalTime heureDebut = LocalTime.parse(node.get("heureDebut").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT)) ;
        logger.info("heureDebut="+heureDebut);
        LocalTime heureFin = LocalTime.parse(node.get("heureFin").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT)) ;
        logger.info("heureFin="+heureFin);
        
        ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        module.addDeserializer(Enseignant.class, new EnseignantDeserializer());
		module.addDeserializer(UE.class, new UEDeserializer());
		module.addDeserializer(EmploiTemps.class, new EmploiTempsDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
        
        UE ue = mapper.readValue(node.get("ue").toPrettyString(), UE.class);
        logger.info("ue="+ue);
        
        EmploiTemps emploiTemps = mapper.readValue(node.get("emploiTemps").toPrettyString(), EmploiTemps.class);
        logger.info("emploiTemps="+emploiTemps);
        
        Enseignant enseignant = mapper.readValue(node.get("enseignant").toPrettyString(), Enseignant.class);
        logger.info("enseignant="+enseignant);
		
        detailET.setId(id);
        detailET.setNum(num);
        
        detailET.setCreateur(createur);
        detailET.setModificateur(modificateur);
        detailET.setDateCreation(dateCreation);
        detailET.setDateDernMaj(dateDernMaj);
        
        detailET.setHeureDebut(heureDebut);
        detailET.setHeureFin(heureFin);
        detailET.setJourSemaine(jourSemaine);
        detailET.setLibelleJour(libelleJour);

        detailET.setEtablissement(etablissement);
        detailET.setEmploiTemps(emploiTemps);
        detailET.setUe(ue);
        detailET.setEnseignant(enseignant);
        logger.info("Fin de la deserialization");
        return detailET ;
	}

}
