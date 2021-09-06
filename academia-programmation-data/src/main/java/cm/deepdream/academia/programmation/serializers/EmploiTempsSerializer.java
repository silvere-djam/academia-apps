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
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;

public class EmploiTempsSerializer extends JsonSerializer<EmploiTemps>{

	@Override
	public void serialize(EmploiTemps value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(AnneeScolaire.class, new AnneeScolaireSerializer());
			module.addSerializer(Enseignant.class, new EnseignantSerializer());
			module.addSerializer(Classe.class, new ClasseSerializer());
			module.addSerializer(Eleve.class, new EleveSerializer()) ;
			mapper.registerModule(module);
			Etablissement etablissement = value.getEtablissement() ;
			Classe classe = value.getClasse() ;
			Enseignant enseignantPrincipal = value.getEnseignantPrincipal() ;
			AnneeScolaire anneeScolaire = value.getAnneeScolaire() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());     
        	jsonGenerator.writeNumberField("num", value.getNum());  
        	jsonGenerator.writeObjectField("classe", classe);
        	jsonGenerator.writeObjectField("enseignantPrincipal", enseignantPrincipal);
        	jsonGenerator.writeObjectField("anneeScolaire", anneeScolaire);
        	jsonGenerator.writeObjectField("etablissement", etablissement);
        	
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
