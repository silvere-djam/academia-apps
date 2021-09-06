package cm.deepdream.academia.viescolaire.deserializers;
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
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.deserializers.AnneeScolaireDeserializer;
import cm.deepdream.academia.programmation.deserializers.ClasseDeserializer;
import cm.deepdream.academia.programmation.deserializers.EnseignantDeserializer;
import cm.deepdream.academia.programmation.deserializers.SemestreDeserializer;
import cm.deepdream.academia.programmation.deserializers.TrimestreDeserializer;
import cm.deepdream.academia.programmation.deserializers.UEDeserializer;

public class CoursDeserializer extends JsonDeserializer<Cours>{
	private Logger logger = Logger.getLogger(CoursDeserializer.class.getName()) ;
	
	
	public CoursDeserializer() {
		super() ;
	}
	
	public Cours deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		Cours cours = new Cours() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		Integer absents = node.get("absents").asInt();
		logger.info("absents="+absents);
		
		Integer presents = node.get("presents").asInt();
		logger.info("presents="+presents);
		
		LocalDate date = LocalDate.parse(node.get("date").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT)) ;
        logger.info("date="+node.get("date").asText());
        
        LocalTime heureDebut = LocalTime.parse(node.get("heureDebut").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT)) ;
        logger.info("heureDebut="+node.get("heureDebut").asText());
        
        LocalTime heureFin = LocalTime.parse(node.get("heureFin").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT)) ;
        logger.info("heureFin="+node.get("heureFin").asText());
		
		String statut = node.get("statut").asText();
		logger.info("statut="+statut);
		
		String type = node.get("type").asText();
		logger.info("type="+type);
		
		String typeCours = node.get("typeCours").asText();
		logger.info("typeCours="+typeCours);
        
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
        module.addDeserializer(UE.class, new UEDeserializer());
        module.addDeserializer(Enseignant.class, new EnseignantDeserializer());
        module.addDeserializer(AnneeScolaire.class, new AnneeScolaireDeserializer());
        module.addDeserializer(Trimestre.class, new TrimestreDeserializer());
        module.addDeserializer(Semestre.class, new SemestreDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
        
        Classe classe = mapper.readValue(node.get("classe").toPrettyString(), Classe.class);
        logger.info("classe"+classe);
        
        UE ue = mapper.readValue(node.get("ue").toPrettyString(), UE.class);
        logger.info("ue="+ue);
        
        Enseignant enseignant = mapper.readValue(node.get("enseignant").toPrettyString(), Enseignant.class);
        logger.info("enseignant="+enseignant);
        
        AnneeScolaire anneeScolaire = mapper.readValue(node.get("anneeScolaire").toPrettyString(), AnneeScolaire.class);
        logger.info("anneeScolaire="+anneeScolaire);
        
        Trimestre trimestre = mapper.readValue(node.get("trimestre").toPrettyString(), Trimestre.class);
        logger.info("trimestre="+trimestre);
        
        Semestre semestre = mapper.readValue(node.get("semestre").toPrettyString(), Semestre.class);
        logger.info("semestre="+semestre);
		
        cours.setId(id);
        cours.setNum(num);
        cours.setAbsents(absents);
        cours.setPresents(presents);
        cours.setDate(date);
        cours.setHeureDebut(heureDebut);
        cours.setHeureFin(heureFin);
        cours.setType(type);
        cours.setStatut(statut);
        cours.setTypeCours(typeCours);
        
        cours.setCreateur(createur);
        cours.setModificateur(modificateur);
        cours.setDateCreation(dateCreation);
        cours.setDateDernMaj(dateDernMaj);

        cours.setEtablissement(etablissement);
        cours.setClasse(classe);
        cours.setUe(ue);
        cours.setEnseignant(enseignant);
        cours.setAnneeScolaire(anneeScolaire);
        cours.setTrimestre(trimestre);
        cours.setSemestre(semestre);
    
        logger.info("Fin de la deserialization");
        return cours ;
	}

}
