package cm.deepdream.academia.programmation.deserializers;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
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
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.souscription.deserializers.PaysDeserializer;
import cm.deepdream.academia.programmation.data.Filiere;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.serializers.ClasseSerializer;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;

public class EleveDeserializer extends JsonDeserializer<Eleve>{
	private Logger logger = Logger.getLogger(EleveDeserializer.class.getName()) ;
	
	
	public EleveDeserializer() {
		super() ;
	}
	
	public Eleve deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		logger.info("Debut de la deserialization");
		Eleve eleve = new Eleve() ;
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		String matricule = node.get("matricule").asText();
		logger.info("matricule="+matricule);
		String nom = node.get("nom").asText();
		logger.info("nom="+nom);
		String prenom = node.get("prenom").asText();
		logger.info("prenom="+prenom);
		String sexe = node.get("sexe").asText();
		logger.info("sexe="+sexe);
		String lieuNaissance = node.get("lieuNaissance").asText();
		logger.info("lieuNaissance="+lieuNaissance);
		LocalDate dateNaissance = LocalDate.parse(node.get("dateNaissance").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        logger.info("dateNaissance="+dateNaissance);
        LocalDate dateAdmission = node.get("dateAdmission") == null ? null:LocalDate.parse(node.get("dateAdmission").asText(), 
        		DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)) ;
        logger.info("dateAdmission="+dateAdmission);
        String statut = node.get("statut").asText();
		logger.info("statut="+statut);
        String nomParent = node.get("nomParent").asText();
		logger.info("nomParent="+nomParent);
		String adresseParent = node.get("adresseParent").asText();
		logger.info("adresseParent="+adresseParent);
		String adresse = node.get("adresse")==null ?"":node.get("adresse").asText();
		logger.info("adresse="+adresse);
		String telephone = node.get("telephone").asText();
		logger.info("telephone="+telephone);
		String email = node.get("email").asText();
		logger.info("email="+email);
		
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
        module.addDeserializer(Pays.class, new PaysDeserializer());
        module.addDeserializer(Photo.class, new PhotoDeserializer());
		module.addDeserializer(Classe.class, new ClasseDeserializer());
        mapper.registerModule(module);
        
        logger.info("etablissement="+node.get("etablissement"));
        
        Etablissement etablissement = mapper.readValue(node.get("etablissement").toPrettyString(), Etablissement.class);
        logger.info("etablissement="+etablissement);
        try {
        	Classe classe = mapper.readValue(node.get("classe").toPrettyString(), Classe.class);
        	logger.info("classe"+classe);
        	eleve.setClasse(classe);
        }catch(Exception ex) {
        	logger.log(Level.SEVERE, ex.getMessage());
        }
        try {
        	Pays pays = mapper.readValue(node.get("pays").toPrettyString(), Pays.class);
        	logger.info("pays"+pays);
        	eleve.setPays(pays);
        }catch(Exception ex) {}
        try {
        	Photo photo = mapper.readValue(node.get("photo").toPrettyString(), Photo.class);
        	logger.info("photo="+photo);
        	eleve.setPhoto(photo);
        }catch(Exception ex) {}
		
        eleve.setId(id);
        eleve.setNum(num);
        eleve.setMatricule(matricule);
        eleve.setNom(nom);
        eleve.setPrenom(prenom);
        eleve.setSexe(sexe);
        eleve.setDateNaissance(dateNaissance);
        eleve.setLieuNaissance(lieuNaissance);
        eleve.setDateAdmission(dateAdmission);
        eleve.setNomParent(nomParent);
        eleve.setAdresseParent(adresseParent);
        eleve.setTelephone(telephone);
        eleve.setEmail(email);
        eleve.setStatut(statut);
        
        eleve.setCreateur(createur);
        eleve.setModificateur(modificateur);
        eleve.setDateCreation(dateCreation);
        eleve.setDateDernMaj(dateDernMaj);

        eleve.setEtablissement(etablissement);
      
        logger.info("Fin de la deserialization");
        return eleve ;
	}

}
