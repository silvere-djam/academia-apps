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
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.TypeExamen;

public class ExamenSerializer extends JsonSerializer<Examen>{

	@Override
	public void serialize(Examen value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(AnneeScolaire.class, new AnneeScolaireSerializer());
			module.addSerializer(Trimestre.class, new TrimestreSerializer());
			module.addSerializer(Semestre.class, new SemestreSerializer());
			module.addSerializer(TypeExamen.class, new TypeExamenSerializer());
			mapper.registerModule(module);
			
			Etablissement etablissement = value.getEtablissement() ;
			AnneeScolaire anneeScolaire = value.getAnneeScolaire() ;
			Trimestre trimestre = value.getTrimestre() ;
			Semestre semestre = value.getSemestre() ;
			TypeExamen typeExamen = value.getTypeExamen() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	jsonGenerator.writeStringField("libelle", value.getLibelle());
        	jsonGenerator.writeNumberField("nbreCentres", value.getNbreCentres());
        	jsonGenerator.writeNumberField("nbreCandidats", value.getNbreCandidats());
        	jsonGenerator.writeStringField("dateDebut", 
        			value.getDateDebut().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
        	jsonGenerator.writeStringField("dateFin", 
        			value.getDateFin().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));

            jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
            	
            jsonGenerator.writeObjectField("etablissement", etablissement);
            jsonGenerator.writeObjectField("trimestre", trimestre);
            jsonGenerator.writeObjectField("semestre", semestre);
            jsonGenerator.writeObjectField("anneeScolaire", anneeScolaire);
            jsonGenerator.writeObjectField("typeExamen", typeExamen);
		}
		jsonGenerator.writeEndObject();
		
	}

}
