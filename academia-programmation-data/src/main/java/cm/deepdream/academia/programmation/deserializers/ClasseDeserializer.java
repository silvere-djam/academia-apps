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
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.data.Filiere;
import cm.deepdream.academia.programmation.data.Niveau;

public class ClasseDeserializer extends JsonDeserializer<Classe>{
	private Logger logger = Logger.getLogger(ClasseDeserializer.class.getName()) ;
	
	
	public ClasseDeserializer() {
		super() ;
	}
	
	public Classe deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		Classe classe = new Classe() ;
		
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
        module.addDeserializer(Niveau.class, new NiveauDeserializer()) ;
        module.addDeserializer(Filiere.class, new FiliereDeserializer()) ;
        mapper.registerModule(module);
        
        Niveau niveau = mapper.readValue(node.get("niveau").toPrettyString(), Niveau.class);
        logger.info("niveau="+niveau);
        
        Filiere filiere = mapper.readValue(node.get("filiere").toPrettyString(), Filiere.class);
        logger.info("filiere="+filiere);
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
		
        classe.setId(id);
        classe.setNum(num);
        classe.setCreateur(createur);
        classe.setModificateur(modificateur);
        classe.setDateCreation(dateCreation);
        classe.setDateDernMaj(dateDernMaj);
        classe.setLibelle(libelle);
        classe.setNiveau(niveau);
        classe.setFiliere(filiere);
        classe.setEtablissement(etablissement);
        logger.info("Fin de la deserialization");
        return classe ;
	}

}
