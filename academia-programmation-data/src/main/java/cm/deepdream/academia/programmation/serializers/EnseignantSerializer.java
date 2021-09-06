package cm.deepdream.academia.programmation.serializers;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;

public class EnseignantSerializer extends JsonSerializer<Enseignant>{

	@Override
	public void serialize(Enseignant value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(Fonction.class, new FonctionSerializer());
			module.addSerializer(Domaine.class, new DomaineSerializer());
			mapper.registerModule(module);
			Etablissement etablissement = value.getEtablissement() ;
			Fonction fonction = value.getFonction() ;
			Domaine domaine = value.getDomaine() ;
			Photo photo = value.getPhoto() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	jsonGenerator.writeStringField("civilite", value.getCivilite());
        	jsonGenerator.writeStringField("matricule", value.getMatricule());
        	jsonGenerator.writeStringField("nom", value.getNom());
        	jsonGenerator.writeStringField("prenom", value.getPrenom());
        	jsonGenerator.writeStringField("sexe", value.getSexe());
        	jsonGenerator.writeStringField("dateNaissance", 
            		value.getDateNaissance().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
        	jsonGenerator.writeStringField("lieuNaissance", value.getLieuNaissance());
        	jsonGenerator.writeStringField("datePriseService", 
        			value.getDatePriseService().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
        	jsonGenerator.writeStringField("situation", value.getSituation());
        	jsonGenerator.writeStringField("telephone", value.getTelephone());
        	jsonGenerator.writeStringField("email", value.getEmail());
        	
        	jsonGenerator.writeObjectField("fonction", fonction);
        	jsonGenerator.writeObjectField("domaine", domaine);
        	jsonGenerator.writeObjectField("etablissement", etablissement);
        	jsonGenerator.writeObjectField("photo", photo);
        	
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
