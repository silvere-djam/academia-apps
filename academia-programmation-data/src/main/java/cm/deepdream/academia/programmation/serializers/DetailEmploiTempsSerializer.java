package cm.deepdream.academia.programmation.serializers;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;

public class DetailEmploiTempsSerializer extends JsonSerializer<DetailEmploiTemps>{

	@Override
	public void serialize(DetailEmploiTemps value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(UE.class, new UESerializer());
			module.addSerializer(EmploiTemps.class, new EmploiTempsSerializer());
			module.addSerializer(Enseignant.class, new EnseignantSerializer());
			mapper.registerModule(module);
			Etablissement etablissement = value.getEtablissement() ;
			UE ue = value.getUe() ;
			Enseignant enseignant = value.getEnseignant() ;
			EmploiTemps emploiTemps = value.getEmploiTemps() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());    
        	jsonGenerator.writeNumberField("num", value.getNum());   
        	jsonGenerator.writeObjectField("emploiTemps", emploiTemps);
        	jsonGenerator.writeObjectField("ue", ue);
        	jsonGenerator.writeObjectField("enseignant", enseignant);
        	jsonGenerator.writeObjectField("etablissement", etablissement);
        	
        	jsonGenerator.writeObjectField("jourSemaine", value.getJourSemaine());
        	jsonGenerator.writeObjectField("libelleJour", value.getLibelleJour());
        	
        	jsonGenerator.writeStringField("heureDebut", 
            		value.getHeureDebut().format(DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT)));
        	jsonGenerator.writeStringField("heureFin", 
            		value.getHeureFin().format(DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT)));
        	
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
