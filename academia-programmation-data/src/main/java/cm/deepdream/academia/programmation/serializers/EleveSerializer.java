package cm.deepdream.academia.programmation.serializers;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.souscription.serializers.PaysSerializer;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.souscription.data.Localite;

public class EleveSerializer extends JsonSerializer<Eleve>{
	private Logger logger = Logger.getLogger(EleveSerializer.class.getName()) ;

	@Override
	public void serialize(Eleve value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			logger.log(Level.INFO, "Eleve="+value.toString());
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Pays.class, new PaysSerializer());
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(Classe.class, new ClasseSerializer());
			module.addSerializer(Pays.class, new PaysSerializer());
			module.addSerializer(Photo.class, new PhotoSerializer());
			mapper.registerModule(module);
			
			Etablissement etablissement = value.getEtablissement() ;
			Classe classe = value.getClasse() ;
			Pays pays = value.getPays() ;
			Photo photo = value.getPhoto() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	jsonGenerator.writeStringField("matricule", value.getMatricule());
        	jsonGenerator.writeStringField("nom", value.getNom());
        	jsonGenerator.writeStringField("prenom", value.getPrenom());
        	jsonGenerator.writeStringField("sexe", value.getSexe());
        	jsonGenerator.writeStringField("dateNaissance", 
            		value.getDateNaissance().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
        	jsonGenerator.writeStringField("lieuNaissance", value.getLieuNaissance());
        	jsonGenerator.writeStringField("dateAdmission", value.getDateAdmission() == null?null:value.getDateAdmission().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
        	jsonGenerator.writeStringField("statut", value.getStatut());
        	jsonGenerator.writeStringField("nomParent", value.getNomParent());
        	jsonGenerator.writeStringField("adresseParent", value.getAdresseParent());
        	jsonGenerator.writeStringField("adresse", value.getAdresse());
        	jsonGenerator.writeStringField("telephone", value.getTelephone());
        	jsonGenerator.writeStringField("email", value.getEmail());
   
        	try {
        		jsonGenerator.writeObjectField("photo", photo);
        	}catch(Exception ex) {}
        	try {
        		jsonGenerator.writeObjectField("pays", pays);
        	}catch(Exception ex) {}
        	try {
        		if(classe != null)
        		jsonGenerator.writeObjectField("classe", classe);
        	}catch(Exception ex) {}
        	try {
        		jsonGenerator.writeObjectField("etablissement", etablissement);
        	}catch(Exception ex) {}
        	
        	jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
		}
		jsonGenerator.writeEndObject();
		
	}

}
