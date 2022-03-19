package cm.deepdream.academia.souscription.serializers;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.souscription.constants.DateConstants;
import cm.deepdream.academia.souscription.model.Abonnement;
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Localite;

public class AbonnementSerializer extends JsonSerializer<Abonnement>{

	@Override
	public void serialize(Abonnement value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Localite.class, new LocaliteSerializer());
			mapper.registerModule(module);
			Etablissement etablissement = value.getEtablissement() ;
			
        	jsonGenerator.writeStringField("dateDebut", 
        		value.getDateDebut().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
        	jsonGenerator.writeStringField("dateFin", 
        		value.getDateFin().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
        	
        	jsonGenerator.writeNumberField("nbEleves", value.getNbEleves());
        	jsonGenerator.writeNumberField("duree", value.getDuree());
        	jsonGenerator.writeStringField("statut", value.getStatut());
        	jsonGenerator.writeBooleanField("evaluation", value.getEvaluation());
        	
        	jsonGenerator.writeObjectField("etablissement", etablissement);
		}
		jsonGenerator.writeEndObject();
	}

}
