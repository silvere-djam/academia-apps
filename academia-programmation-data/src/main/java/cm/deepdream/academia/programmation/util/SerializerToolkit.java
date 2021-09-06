package cm.deepdream.academia.programmation.util ;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Filiere;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.TypeExamen;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Localite;
import cm.deepdream.academia.programmation.serializers.AnneeScolaireSerializer;
import cm.deepdream.academia.programmation.serializers.CentreExamenSerializer;
import cm.deepdream.academia.programmation.serializers.ClasseSerializer;
import cm.deepdream.academia.programmation.serializers.DetailEmploiTempsSerializer;
import cm.deepdream.academia.programmation.serializers.DomaineSerializer;
import cm.deepdream.academia.programmation.serializers.EleveSerializer;
import cm.deepdream.academia.programmation.serializers.EmploiTempsSerializer;
import cm.deepdream.academia.programmation.serializers.EnseignantSerializer;
import cm.deepdream.academia.programmation.serializers.ExamenSerializer;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.programmation.serializers.FiliereSerializer;
import cm.deepdream.academia.programmation.serializers.FonctionSerializer;
import cm.deepdream.academia.programmation.serializers.GroupeSerializer;
import cm.deepdream.academia.programmation.serializers.NiveauSerializer;
import cm.deepdream.academia.programmation.serializers.SalleExamenSerializer;
import cm.deepdream.academia.souscription.serializers.PaysSerializer;
import cm.deepdream.academia.programmation.serializers.TrimestreSerializer;
import cm.deepdream.academia.programmation.serializers.TypeExamenSerializer;
import cm.deepdream.academia.programmation.serializers.UESerializer;
import cm.deepdream.academia.souscription.serializers.LocaliteSerializer;

public class SerializerToolkit {
	private static SerializerToolkit toolkit ;
	private ObjectMapper mapper = new ObjectMapper();
	
	private SerializerToolkit() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Pays.class, new PaysSerializer());
        module.addSerializer(Localite.class, new LocaliteSerializer());
        module.addSerializer(Etablissement.class, new EtablissementSerializer());
        module.addSerializer(AnneeScolaire.class, new AnneeScolaireSerializer());
        module.addSerializer(Trimestre.class, new TrimestreSerializer());
        module.addSerializer(Niveau.class, new NiveauSerializer());
        module.addSerializer(Filiere.class, new FiliereSerializer());
        module.addSerializer(UE.class, new UESerializer());
        module.addSerializer(Enseignant.class, new EnseignantSerializer());
        module.addSerializer(Eleve.class, new EleveSerializer());
        module.addSerializer(EmploiTemps.class, new EmploiTempsSerializer());
        module.addSerializer(DetailEmploiTemps.class, new DetailEmploiTempsSerializer());
        module.addSerializer(Classe.class, new ClasseSerializer());
        module.addSerializer(Groupe.class, new GroupeSerializer());
        module.addSerializer(Domaine.class, new DomaineSerializer());
        module.addSerializer(Fonction.class, new FonctionSerializer());
        module.addSerializer(TypeExamen.class, new TypeExamenSerializer());
        module.addSerializer(Examen.class, new ExamenSerializer());
        module.addSerializer(CentreExamen.class, new CentreExamenSerializer());
        module.addSerializer(SalleExamen.class, new SalleExamenSerializer());
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
