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
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.serializers.ClasseSerializer;
import cm.deepdream.academia.programmation.serializers.DomaineSerializer;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.souscription.serializers.PaysSerializer;

public class EnseignantDeserializer extends JsonDeserializer<Enseignant>{
	private Logger logger = Logger.getLogger(EnseignantDeserializer.class.getName()) ;
	
	
	public EnseignantDeserializer() {
		super() ;
	}
	
	public Enseignant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		Enseignant enseignant = new Enseignant() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		String civilite = node.get("civilite").asText();
		logger.info("civilite="+civilite);
		String matricule = node.get("matricule").asText();
		logger.info("matricule="+matricule);
		String nom = node.get("nom").asText();
		logger.info("nom="+nom);
		String prenom = node.get("prenom").asText();
		logger.info("prenom="+prenom);
		String sexe = node.get("sexe").asText();
		logger.info("sexe="+sexe);
		String lieuNaissance = node.get("lieuNaissance")==null?null:node.get("lieuNaissance").asText();
		logger.info("lieuNaissance="+lieuNaissance);
		LocalDate dateNaissance = LocalDate.parse(node.get("dateNaissance").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        logger.info("dateNaissance="+dateNaissance);
        LocalDate datePriseService = node.get("datePriseService") == null ? null:LocalDate.parse(node.get("datePriseService").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        logger.info("datePriseService="+datePriseService);
        String situation = node.get("situation").asText();
		logger.info("situation="+situation);
		String telephone = node.get("telephone").asText();
		logger.info("telephone="+telephone);
		String email = node.get("email").asText();
		logger.info("email="+email);
        
        Long id = node.get("id").asLong() ;
        logger.info("id="+id);
		Long num = node.get("num").asLong();
		logger.info("num="+num);
		
        String createur = node.get("createur").asText();
        logger.info("createur="+createur);
        
        String modificateur = node.get("modificateur").asText();
        logger.info("modificateur="+modificateur);
        
        LocalDateTime dateCreation = LocalDateTime.parse(node.get("dateCreation").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        logger.info("dateCreation="+dateCreation);
        
        LocalDateTime dateDernMaj = LocalDateTime.parse(node.get("dateDernMaj").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)) ;
        logger.info("dateDernMaj="+dateDernMaj);
        
        ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        module.addDeserializer(Domaine.class, new DomaineDeserializer());
		module.addDeserializer(Fonction.class, new FonctionDeserializer());
		module.addDeserializer(Photo.class, new PhotoDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
        
        Fonction fonction = mapper.readValue(node.get("fonction").toPrettyString(), Fonction.class);
        logger.info("fonction="+fonction);
        
        Domaine domaine = mapper.readValue(node.get("domaine").toPrettyString(), Domaine.class);
        logger.info("domaine="+domaine);
        
        Photo photo = mapper.readValue(node.get("photo").toPrettyString(), Photo.class);
        logger.info("photo="+photo);
		
        enseignant.setId(id);
        enseignant.setNum(num);
        enseignant.setCivilite(civilite);
        enseignant.setMatricule(matricule);
        enseignant.setNom(nom);
        enseignant.setPrenom(prenom);
        enseignant.setSexe(sexe);
        enseignant.setDateNaissance(dateNaissance);
        enseignant.setLieuNaissance(lieuNaissance);
        enseignant.setDatePriseService(datePriseService);
        enseignant.setTelephone(telephone);
        enseignant.setEmail(email);
        enseignant.setSituation(situation);
        
        enseignant.setCreateur(createur);
        enseignant.setModificateur(modificateur);
        enseignant.setDateCreation(dateCreation);
        enseignant.setDateDernMaj(dateDernMaj);

        enseignant.setEtablissement(etablissement);
        enseignant.setDomaine(domaine);
        enseignant.setFonction(fonction);
        enseignant.setPhoto(photo);
        logger.info("Fin de la deserialization");
        return enseignant ;
	}

}
