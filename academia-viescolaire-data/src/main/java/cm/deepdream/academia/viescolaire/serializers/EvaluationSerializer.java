package cm.deepdream.academia.viescolaire.serializers;
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
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.serializers.AnneeScolaireSerializer;
import cm.deepdream.academia.programmation.serializers.ClasseSerializer;
import cm.deepdream.academia.programmation.serializers.EnseignantSerializer;
import cm.deepdream.academia.programmation.serializers.ExamenSerializer;
import cm.deepdream.academia.programmation.serializers.SemestreSerializer;
import cm.deepdream.academia.programmation.serializers.TrimestreSerializer;
import cm.deepdream.academia.programmation.serializers.UESerializer;

public class EvaluationSerializer extends JsonSerializer<Evaluation>{

	@Override
	public void serialize(Evaluation value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		jsonGenerator.writeStartObject();
		if(value != null) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(Etablissement.class, new EtablissementSerializer());
			module.addSerializer(Examen.class, new ExamenSerializer());
			module.addSerializer(UE.class, new UESerializer());
			module.addSerializer(Enseignant.class, new EnseignantSerializer());
			module.addSerializer(Classe.class, new ClasseSerializer());
			module.addSerializer(AnneeScolaire.class, new AnneeScolaireSerializer());
			module.addSerializer(Trimestre.class, new TrimestreSerializer());
			module.addSerializer(Semestre.class, new SemestreSerializer());
			mapper.registerModule(module);
			
			Etablissement etablissement = value.getEtablissement() ;
			UE ue = value.getUe() ;
			Classe classe = value.getClasse() ;
			Enseignant enseignant = value.getEnseignant() ;
			AnneeScolaire anneeScolaire = value.getAnneeScolaire() ;
			Trimestre trimestre = value.getTrimestre() ;
			Semestre semestre = value.getSemestre() ;
			Examen examen = value.getExamen() ;
			
        	jsonGenerator.writeNumberField("id", value.getId());
        	jsonGenerator.writeNumberField("num", value.getNum());
        	jsonGenerator.writeStringField("date", 
        			value.getDate().format(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT_2)));
        	jsonGenerator.writeStringField("heureDebut", 
        			value.getHeureDebut().format(DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT)));
        	try {
        		jsonGenerator.writeStringField("heureFin", 
        			value.getHeureFin().format(DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT)));
        	}catch (Exception ex) {}
        	jsonGenerator.writeNumberField("presents", value.getPresents() == null ? 0:value.getPresents());
        	jsonGenerator.writeNumberField("absents", value.getAbsents() == null ? 0:value.getAbsents());
        	jsonGenerator.writeStringField("statut", value.getStatut());
        	jsonGenerator.writeStringField("type", value.getType());
        	jsonGenerator.writeNumberField("tauxReussite", value.getTauxReussite() == null ? 0 : value.getTauxReussite());
        	jsonGenerator.writeNumberField("moyenneGenerale", value.getMoyenneGenerale() == null ? 0 : value.getMoyenneGenerale());
        	
            jsonGenerator.writeStringField("createur", value.getCreateur());
        	jsonGenerator.writeStringField("modificateur", value.getModificateur());
        	jsonGenerator.writeStringField("dateCreation", 
        		value.getDateCreation().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
        	jsonGenerator.writeStringField("dateDernMaj", 
        		value.getDateDernMaj().format(DateTimeFormatter.ofPattern(DateConstants.DATETIME_FORMAT_2)));
            	
            jsonGenerator.writeObjectField("etablissement", etablissement);
            jsonGenerator.writeObjectField("ue", ue);
            jsonGenerator.writeObjectField("classe", classe);
            jsonGenerator.writeObjectField("enseignant", enseignant);
            jsonGenerator.writeObjectField("trimestre", trimestre);
            jsonGenerator.writeObjectField("semestre", semestre);
            jsonGenerator.writeObjectField("anneeScolaire", anneeScolaire);
            jsonGenerator.writeObjectField("examen", examen);
		}
		jsonGenerator.writeEndObject();
		
	}

}
