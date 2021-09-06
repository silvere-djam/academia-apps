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
import cm.deepdream.academia.programmation.data.Candidat;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;

public class CandidatSerializer extends JsonSerializer<Candidat>{

	@Override
	public void serialize(Candidat value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(AnneeScolaire.class, new AnneeScolaireSerializer());
			module.addSerializer(Examen.class, new ExamenSerializer());
			module.addSerializer(CentreExamen.class, new CentreExamenSerializer());
			module.addSerializer(SalleExamen.class, new SalleExamenSerializer());
			module.addSerializer(Eleve.class, new EleveSerializer());
			mapper.registerModule(module);
			
			Etablissement etablissement = value.getEtablissement() ;
			Examen examen = value.getExamen() ;
			CentreExamen centreExamen = value.getCentreExamen() ;
			SalleExamen salleExamen = value.getSalleExamen() ;
			Eleve eleve = value.getEleve() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	
        	jsonGenerator.writeStringField("numero", value.getNumero());
        	jsonGenerator.writeStringField("type", value.getType());
        	
            jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
            	
            jsonGenerator.writeObjectField("etablissement", etablissement);
            jsonGenerator.writeObjectField("examen", examen);
            jsonGenerator.writeObjectField("centreExamen", centreExamen);
            jsonGenerator.writeObjectField("salleExamen", salleExamen);
            jsonGenerator.writeObjectField("eleve", eleve);

		}
		jsonGenerator.writeEndObject();
		
	}

}
