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
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.data.Filiere;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.UE;

public class UEDeserializer extends JsonDeserializer<UE>{
	private Logger logger = Logger.getLogger(UEDeserializer.class.getName()) ;
	
	
	public UEDeserializer() {
		super() ;
	}
	
	public UE deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		UE ue = new UE() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		String code = node.get("code").asText();
		logger.info("code="+code);
		Integer numero = node.get("numero").asInt();
		logger.info("numero="+numero);
		Double credits = node.get("credits")== null ?null: node.get("credits").asDouble();
		logger.info("credits="+credits);
		Double coefficient = node.get("coefficient") == null ?null:node.get("coefficient").asDouble();
		logger.info("coefficient="+coefficient);
        
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
        module.addDeserializer(Classe.class, new ClasseDeserializer());
        module.addDeserializer(Groupe.class, new GroupeDeserializer());
        module.addDeserializer(Domaine.class, new DomaineDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
        
        Classe classe = mapper.readValue(node.get("classe").toPrettyString(), Classe.class);
        logger.info("classe"+classe);
        
        Domaine domaine = mapper.readValue(node.get("domaine").toPrettyString(), Domaine.class);
        logger.info("domaine="+domaine);
        
        Groupe groupe = mapper.readValue(node.get("groupe").toPrettyString(), Groupe.class);
        logger.info("groupe="+groupe);
		
        ue.setId(id);
        ue.setNum(num);
        ue.setNumero(numero);
        ue.setCode(code);
        ue.setCredits(credits == null ? null : credits.floatValue());
        ue.setCoefficient(coefficient == null ? null : coefficient.floatValue());
        ue.setCreateur(createur);
        ue.setModificateur(modificateur);
        ue.setDateCreation(dateCreation);
        ue.setDateDernMaj(dateDernMaj);

        ue.setEtablissement(etablissement);
        ue.setClasse(classe);
        ue.setDomaine(domaine);
        ue.setGroupe(groupe);
        logger.info("Fin de la deserialization");
        return ue ;
	}

}
