package cm.deepdream.academia.souscription.deserializers;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.souscription.constants.DateConstants;
import cm.deepdream.academia.souscription.data.Abonnement;
import cm.deepdream.academia.souscription.data.Etablissement;


public class AbonnementDeserializer extends StdDeserializer<Abonnement>{
	
	public AbonnementDeserializer() {
		super(Abonnement.class) ;
	}
	
	public Abonnement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	
		Abonnement abonnement = new Abonnement() ;
		
		ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        mapper.registerModule(module);
		JsonNode node = jp.getCodec().readTree(jp);
		
		Long id = node.get("id").asLong();
        String createur = node.get("createur") == null ? "":node.get("createur").asText();
        String modificateur = node.get("modificateur") == null ? "":node.get("modificateur").asText();
        LocalDateTime dateCreation = LocalDateTime.parse(node.get("dateCreation").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        LocalDateTime dateDernMaj = LocalDateTime.parse(node.get("dateDernMaj").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        
        Integer nbEleves = node.get("nbEleves") == null ? null:node.get("nbEleves").asInt();
        Integer duree = node.get("duree") == null ? null:node.get("duree").asInt();
        String statut = node.get("statut").asText();
        Boolean evaluation = node.get("evaluation") == null ? null:node.get("evaluation").asBoolean() ;

        LocalDate dateDebut = LocalDate.parse(node.get("dateDebut").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        LocalDate dateFin = LocalDate.parse(node.get("dateFin").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        
        abonnement.setId(id);
        abonnement.setCreateur(createur);
        abonnement.setModificateur(modificateur);
        abonnement.setDateCreation(dateCreation);
        abonnement.setDateDernMaj(dateDernMaj);
        
        abonnement.setNbEleves(nbEleves);
        abonnement.setDuree(duree);
        abonnement.setStatut(statut);
        abonnement.setDateDebut(dateDebut);
        abonnement.setDateFin(dateFin);
        abonnement.setEvaluation(evaluation);

        abonnement.setEtablissement(etablissement);
        
        return abonnement ;
	}

}
