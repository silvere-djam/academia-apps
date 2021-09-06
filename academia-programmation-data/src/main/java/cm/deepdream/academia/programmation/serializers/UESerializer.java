package cm.deepdream.academia.programmation.serializers;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.UE;

public class UESerializer extends JsonSerializer<UE>{

	@Override
	public void serialize(UE value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(Groupe.class, new GroupeSerializer());
			module.addSerializer(Domaine.class, new DomaineSerializer());
			module.addSerializer(Classe.class, new ClasseSerializer());
			mapper.registerModule(module);
			Etablissement etablissement = value.getEtablissement() ;
			Domaine domaine = value.getDomaine() ;
			Classe classe = value.getClasse() ;
			Groupe groupe = value.getGroupe() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	jsonGenerator.writeStringField("code", value.getCode());
        	jsonGenerator.writeNumberField("coefficient", value.getCoefficient());
        	jsonGenerator.writeNumberField("numero", value.getNumero());
        	jsonGenerator.writeNumberField("credits", value.getCredits());
        	jsonGenerator.writeNumberField("numero", value.getNumero());
        	
            jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
            	
            jsonGenerator.writeObjectField("etablissement", etablissement);
            jsonGenerator.writeObjectField("domaine", domaine);
            jsonGenerator.writeObjectField("classe", classe);
            jsonGenerator.writeObjectField("groupe", groupe);
		}
		jsonGenerator.writeEndObject();
		
	}

}
