package cm.deepdream.academia.programmation.serializers;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.programmation.constants.DateConstants;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.souscription.data.Localite;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.souscription.serializers.PaysSerializer;
import cm.deepdream.academia.souscription.serializers.LocaliteSerializer;
public class AnneeScolaireSerializer extends JsonSerializer<AnneeScolaire>{

	@Override
	public void serialize(AnneeScolaire value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(Localite.class, new LocaliteSerializer());
			module.addSerializer(Pays.class, new PaysSerializer());
			mapper.registerModule(module);
			Etablissement etablissement = value.getEtablissement() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	jsonGenerator.writeStringField("libelle", value.getLibelle());
        	jsonGenerator.writeNumberField("courante", value.getCourante());
        	jsonGenerator.writeStringField("dateDebut", value.getDateDebut().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
            jsonGenerator.writeStringField("dateFin", value.getDateFin() == null ? "":value.getDateFin().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
            
            jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
            	
            jsonGenerator.writeObjectField("etablissement", etablissement);
		}
		jsonGenerator.writeEndObject();
		
	}

}
