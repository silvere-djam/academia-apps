package cm.deepdream.academia.programmation.deserializers;
import java.io.IOException;
import java.time.LocalDate;
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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.TypeExamen;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.deserializers.AnneeScolaireDeserializer;
import cm.deepdream.academia.programmation.deserializers.NiveauDeserializer;
import cm.deepdream.academia.programmation.deserializers.SemestreDeserializer;
import cm.deepdream.academia.programmation.deserializers.TrimestreDeserializer;

public class SalleExamenDeserializer extends JsonDeserializer<SalleExamen>{
	private Logger logger = Logger.getLogger(SalleExamenDeserializer.class.getName()) ;
	
	
	public SalleExamenDeserializer() {
		super() ;
	}
	
	public SalleExamen deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		SalleExamen salleExamen = new SalleExamen() ;
		
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
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);

        AnneeScolaire anneeScolaire = mapper.readValue(node.get("anneeScolaire").toPrettyString(), AnneeScolaire.class);
        logger.info("anneeScolaire="+anneeScolaire);
        
        Examen examen = mapper.readValue(node.get("examen").toPrettyString(), Examen.class);
        logger.info("examen="+examen);
        
        CentreExamen centreExamen = mapper.readValue(node.get("centreExamen").toPrettyString(), CentreExamen.class);
        logger.info("centreExamen="+centreExamen);

		
        salleExamen.setId(id);
        salleExamen.setNum(num);
        salleExamen.setNumero(numero);
        salleExamen.setLibelle(libelle);
        
        salleExamen.setCreateur(createur);
        salleExamen.setModificateur(modificateur);
        salleExamen.setDateCreation(dateCreation);
        salleExamen.setDateDernMaj(dateDernMaj);

        salleExamen.setEtablissement(etablissement);
        salleExamen.setExamen(examen);
        salleExamen.setCentreExamen(centreExamen);
    
        logger.info("Fin de la deserialization");
        return salleExamen ;
	}

}
