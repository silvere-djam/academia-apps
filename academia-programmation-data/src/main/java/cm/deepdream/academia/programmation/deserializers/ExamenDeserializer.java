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
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Niveau;
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

public class ExamenDeserializer extends JsonDeserializer<Examen>{
	private Logger logger = Logger.getLogger(ExamenDeserializer.class.getName()) ;
	
	
	public ExamenDeserializer() {
		super() ;
	}
	
	public Examen deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		Examen examen = new Examen() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		LocalDate dateDebut = LocalDate.parse(node.get("dateDebut").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT)) ;
        logger.info("dateDebut="+node.get("dateDebut").asText());
        
        LocalDate dateFin = LocalDate.parse(node.get("dateFin").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT)) ;
        logger.info("dateFin="+node.get("dateFin").asText());
		
		String libelle = node.get("libelle").asText();
		logger.info("libelle="+libelle);
		
        Long id = node.get("id").asLong() ;
        logger.info("id="+node.get("id").asLong());
        
		Long num = node.get("num").asLong();
		logger.info("num="+node.get("num").asLong());
		
		Long nbreCentres = node.get("nbreCentres").asLong();
		logger.info("nbreCentres"+node.get("nbreCentres").asLong());
		
		Long nbreCandidats = node.get("nbreCandidats").asLong();
		logger.info("nbreCandidats"+node.get("nbreCandidats").asLong());
		
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
        module.addDeserializer(Trimestre.class, new TrimestreDeserializer());
        module.addDeserializer(Semestre.class, new SemestreDeserializer());
        module.addDeserializer(TypeExamen.class, new TypeExamenDeserializer());
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);

        AnneeScolaire anneeScolaire = mapper.readValue(node.get("anneeScolaire").toPrettyString(), AnneeScolaire.class);
        logger.info("anneeScolaire="+anneeScolaire);
        
        Trimestre trimestre = mapper.readValue(node.get("trimestre").toPrettyString(), Trimestre.class);
        logger.info("trimestre="+trimestre);
        
        Semestre semestre = mapper.readValue(node.get("semestre").toPrettyString(), Semestre.class);
        logger.info("semestre="+semestre);
        
        TypeExamen typeExamen = mapper.readValue(node.get("typeExamen").toPrettyString(), TypeExamen.class);
        logger.info("typeExamen="+typeExamen);
		
        examen.setId(id);
        examen.setNum(num);
        examen.setLibelle(libelle);
        examen.setNbreCentres(nbreCentres) ;
        examen.setNbreCandidats(nbreCandidats);
        examen.setTypeExamen(typeExamen);
        examen.setDateDebut(dateDebut);
        examen.setDateFin(dateFin);
        
        examen.setCreateur(createur);
        examen.setModificateur(modificateur);
        examen.setDateCreation(dateCreation);
        examen.setDateDernMaj(dateDernMaj);

        examen.setEtablissement(etablissement);
        examen.setAnneeScolaire(anneeScolaire);
        examen.setTrimestre(trimestre);
        examen.setSemestre(semestre);
    
        logger.info("Fin de la deserialization");
        return examen ;
	}

}
