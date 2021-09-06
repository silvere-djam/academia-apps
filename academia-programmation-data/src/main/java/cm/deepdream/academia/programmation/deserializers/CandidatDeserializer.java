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
import cm.deepdream.academia.programmation.data.Candidat;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.data.Examen;

public class CandidatDeserializer extends JsonDeserializer<Candidat>{
	private Logger logger = Logger.getLogger(CandidatDeserializer.class.getName()) ;
	
	
	public CandidatDeserializer() {
		super() ;
	}
	
	public Candidat deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		Candidat candidat = new Candidat() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		String libelle = node.get("libelle").asText();
		logger.info("libelle="+libelle);
		
        Long id = node.get("id").asLong() ;
        logger.info("id="+node.get("id").asText());
        
		Long num = node.get("num").asLong();
		logger.info("num="+node.get("num").asLong());
		
		String numero = node.get("numero").asText();
		logger.info("numero="+node.get("numero").asText());
		
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
        module.addDeserializer(CentreExamen.class, new CentreExamenDeserializer());
        module.addDeserializer(SalleExamen.class, new SalleExamenDeserializer());
        module.addDeserializer(Eleve.class, new EleveDeserializer());
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);

        Eleve eleve = mapper.readValue(node.get("eleve").toPrettyString(), Eleve.class);
        logger.info("eleve="+eleve);
        
        Examen examen = mapper.readValue(node.get("examen").toPrettyString(), Examen.class);
        logger.info("examen="+examen);
        
        CentreExamen centreExamen = mapper.readValue(node.get("centreExamen").toPrettyString(), CentreExamen.class);
        logger.info("centreExamen="+centreExamen);
        
        SalleExamen salleExamen = mapper.readValue(node.get("salleExamen").toPrettyString(), SalleExamen.class);
        logger.info("salleExamen="+salleExamen);
		
        candidat.setId(id);
        candidat.setNum(num);
        candidat.setNumero(numero);
        candidat.setEleve(eleve);
        
        candidat.setCreateur(createur);
        candidat.setModificateur(modificateur);
        candidat.setDateCreation(dateCreation);
        candidat.setDateDernMaj(dateDernMaj);

        candidat.setEtablissement(etablissement);
        candidat.setExamen(examen);
        candidat.setCentreExamen(centreExamen);
        candidat.setSalleExamen(salleExamen);
    
        logger.info("Fin de la deserialization");
        return candidat ;
	}

}
