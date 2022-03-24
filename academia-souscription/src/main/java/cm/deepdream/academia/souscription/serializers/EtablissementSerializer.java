package cm.deepdream.academia.souscription.serializers;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Localite;
import cm.deepdream.academia.souscription.model.Logo;

public class EtablissementSerializer extends JsonSerializer<Etablissement>{

	@Override
	public void serialize(Etablissement value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Localite.class, new LocaliteSerializer());
			module.addSerializer(Logo.class, new LogoSerializer());
			mapper.registerModule(module);
			Localite localite = value.getLocalite() ;

        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeStringField("libelle", value.getLibelle());
        	jsonGenerator.writeStringField("cycle", value.getCycle());
        	jsonGenerator.writeStringField("email", value.getEmail());
        	jsonGenerator.writeStringField("telephone", value.getTelephone());
        	jsonGenerator.writeStringField("boitePostale", value.getBoitePostale());
        	jsonGenerator.writeNumberField("nbElevesApprox", value.getNbElevesApprox() == null ? 0L:value.getNbElevesApprox());
        	jsonGenerator.writeObjectField("localite", localite);
		}
		jsonGenerator.writeEndObject();
	}

}
