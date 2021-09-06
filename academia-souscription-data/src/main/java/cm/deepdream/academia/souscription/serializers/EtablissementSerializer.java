package cm.deepdream.academia.souscription.serializers;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.souscription.constants.DateConstants;
import cm.deepdream.academia.souscription.data.Contact;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Logo;
import cm.deepdream.academia.souscription.data.Localite;

public class EtablissementSerializer extends JsonSerializer<Etablissement>{

	@Override
	public void serialize(Etablissement value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Localite.class, new LocaliteSerializer());
			module.addSerializer(Logo.class, new LogoSerializer());
			module.addSerializer(Contact.class, new ContactSerializer());
			mapper.registerModule(module);
			Localite localite = value.getLocalite() ;
			Logo logo = value.getLogo() ;
			Contact contactChef = value.getContactChef() ;
			Contact contactInformaticien = value.getContactInformaticien() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	
        	jsonGenerator.writeStringField("libelle", value.getLibelle());
        	jsonGenerator.writeStringField("cycle", value.getCycle());
        	jsonGenerator.writeStringField("email", value.getEmail());
        	jsonGenerator.writeStringField("telephone", value.getTelephone());
        	jsonGenerator.writeStringField("boitePostale", value.getBoitePostale());
        	jsonGenerator.writeNumberField("nbElevesApprox", value.getNbElevesApprox() == null ? 0L:value.getNbElevesApprox());
        	
        	jsonGenerator.writeObjectField("logo", logo);
        	jsonGenerator.writeObjectField("localite", localite);
        	jsonGenerator.writeObjectField("contactChef", contactChef);
        	jsonGenerator.writeObjectField("contactInformaticien", contactInformaticien);
		}
		jsonGenerator.writeEndObject();
	}

}
