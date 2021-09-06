package cm.deepdream.academia.viescolaire.util ;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.viescolaire.serializers.CoursSerializer;
import cm.deepdream.academia.viescolaire.serializers.EvaluationSerializer;
import cm.deepdream.academia.programmation.serializers.ExamenSerializer;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.serializers.AnneeScolaireSerializer;
import cm.deepdream.academia.programmation.serializers.ClasseSerializer;
import cm.deepdream.academia.programmation.serializers.EnseignantSerializer;
import cm.deepdream.academia.programmation.serializers.SemestreSerializer;
import cm.deepdream.academia.programmation.serializers.TrimestreSerializer;
import cm.deepdream.academia.programmation.serializers.UESerializer;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.programmation.data.Examen;


public class SerializerToolkit {
	private static SerializerToolkit toolkit ;
	private ObjectMapper mapper = new ObjectMapper();
	
	private SerializerToolkit() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Cours.class, new CoursSerializer());
        module.addSerializer(Evaluation.class, new EvaluationSerializer());
        module.addSerializer(Examen.class, new ExamenSerializer());
        module.addSerializer(Etablissement.class, new EtablissementSerializer());
		module.addSerializer(UE.class, new UESerializer());
		module.addSerializer(Enseignant.class, new EnseignantSerializer());
		module.addSerializer(Classe.class, new ClasseSerializer());
		module.addSerializer(AnneeScolaire.class, new AnneeScolaireSerializer());
		module.addSerializer(Trimestre.class, new TrimestreSerializer());
		module.addSerializer(Semestre.class, new SemestreSerializer());
        mapper.registerModule(module);
	}
	
	public static SerializerToolkit getToolkit() {
		if(toolkit == null) {
			toolkit = new SerializerToolkit() ;
		}
		return toolkit ;
	}
	
	public synchronized String serialize(Object objet) throws Exception {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objet) ; 
	}
}
