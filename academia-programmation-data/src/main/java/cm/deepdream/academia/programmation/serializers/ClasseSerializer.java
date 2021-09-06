package cm.deepdream.academia.programmation.serializers;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule ;
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.programmation.data.Filiere;
import cm.deepdream.academia.programmation.data.Niveau;
public class ClasseSerializer extends JsonSerializer<Classe>{

	@Override
	public void serialize(Classe value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(Niveau.class, new NiveauSerializer());
			module.addSerializer(Filiere.class, new FiliereSerializer());
			mapper.registerModule(module);
			Etablissement etablissement = value.getEtablissement() ;
			Niveau niveau = value.getNiveau() ;
			Filiere filiere = value.getFiliere() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	jsonGenerator.writeStringField("libelle", value.getLibelle());
        	jsonGenerator.writeStringField("abreviation", value.getAbreviation());
        	
            jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	
        	jsonGenerator.writeObjectField("niveau", niveau);	
        	jsonGenerator.writeObjectField("filiere", filiere);
            jsonGenerator.writeObjectField("etablissement", etablissement);
		}
		jsonGenerator.writeEndObject();
		
	}

}
