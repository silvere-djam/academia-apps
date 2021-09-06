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
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;

public class CentreExamenSerializer extends JsonSerializer<CentreExamen>{

	@Override
	public void serialize(CentreExamen value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(AnneeScolaire.class, new AnneeScolaireSerializer());
			module.addSerializer(Examen.class, new ExamenSerializer());
			mapper.registerModule(module);
			
			Etablissement etablissement = value.getEtablissement() ;
			AnneeScolaire anneeScolaire = value.getAnneeScolaire() ;
			Examen examen = value.getExamen() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	jsonGenerator.writeStringField("numero", value.getNumero());
        	jsonGenerator.writeStringField("libelle", value.getLibelle());
        	jsonGenerator.writeNumberField("nbreSalles", value.getNbreSalles());
        	jsonGenerator.writeNumberField("nbreCandidats", value.getNbreCandidats());
        	
            jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
            	
            jsonGenerator.writeObjectField("etablissement", etablissement);
            jsonGenerator.writeObjectField("examen", examen);
            jsonGenerator.writeObjectField("anneeScolaire", anneeScolaire);
		}
		jsonGenerator.writeEndObject();
		
	}

}
