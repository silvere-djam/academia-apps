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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;

public class EmploiTempsDeserializer extends JsonDeserializer<EmploiTemps>{
	private Logger logger = Logger.getLogger(EmploiTempsDeserializer.class.getName()) ;
	
	
	public EmploiTempsDeserializer() {
		super() ;
	}
	
	public EmploiTemps deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		EmploiTemps emploiTemps = new EmploiTemps() ;
		
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
        
        ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        module.addDeserializer(Enseignant.class, new EnseignantDeserializer());
		module.addDeserializer(Classe.class, new ClasseDeserializer());
		module.addDeserializer(Eleve.class, new EleveDeserializer());
		module.addDeserializer(AnneeScolaire.class, new AnneeScolaireDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
        
        AnneeScolaire anneeScolaire = mapper.readValue(node.get("anneeScolaire").toPrettyString(), AnneeScolaire.class);
        logger.info("anneeScolaire="+anneeScolaire);
        
        Classe classe = mapper.readValue(node.get("classe").toPrettyString(), Classe.class);
        logger.info("classe="+classe);
        
        Enseignant enseignantPrincipal = mapper.readValue(node.get("enseignantPrincipal").toPrettyString(), Enseignant.class);
        logger.info("enseignantPrincipal="+enseignantPrincipal);
		
        emploiTemps.setId(id);
        emploiTemps.setNum(num);
        
        emploiTemps.setCreateur(createur);
        emploiTemps.setModificateur(modificateur);
        emploiTemps.setDateCreation(dateCreation);
        emploiTemps.setDateDernMaj(dateDernMaj);

        emploiTemps.setEtablissement(etablissement);
        emploiTemps.setAnneeScolaire(anneeScolaire);
        emploiTemps.setClasse(classe);
        emploiTemps.setEnseignantPrincipal(enseignantPrincipal);
        logger.info("Fin de la deserialization");
        return emploiTemps ;
	}

}
