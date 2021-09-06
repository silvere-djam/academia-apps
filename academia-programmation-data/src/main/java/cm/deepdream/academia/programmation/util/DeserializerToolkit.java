package cm.deepdream.academia.programmation.util;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.EntiteGenerique;

import cm.deepdream.academia.programmation.data.Filiere;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Localite;
import cm.deepdream.academia.programmation.deserializers.AnneeScolaireDeserializer;
import cm.deepdream.academia.programmation.deserializers.ClasseDeserializer;
import cm.deepdream.academia.programmation.deserializers.DetailEmploiTempsDeserializer;
import cm.deepdream.academia.programmation.deserializers.DomaineDeserializer;
import cm.deepdream.academia.programmation.deserializers.EleveDeserializer;
import cm.deepdream.academia.programmation.deserializers.EmploiTempsDeserializer;
import cm.deepdream.academia.programmation.deserializers.EnseignantDeserializer;
import cm.deepdream.academia.programmation.deserializers.EntiteGeneriqueDeserializer;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.programmation.deserializers.FiliereDeserializer;
import cm.deepdream.academia.programmation.deserializers.FonctionDeserializer;
import cm.deepdream.academia.programmation.deserializers.GroupeDeserializer;
import cm.deepdream.academia.programmation.deserializers.NiveauDeserializer;
import cm.deepdream.academia.souscription.deserializers.PaysDeserializer;
import cm.deepdream.academia.programmation.deserializers.TrimestreDeserializer;
import cm.deepdream.academia.programmation.deserializers.UEDeserializer;
import cm.deepdream.academia.souscription.deserializers.LocaliteDeserializer;

public class DeserializerToolkit {
	private Logger logger = Logger.getLogger(DeserializerToolkit.class.getName()) ;
	private static DeserializerToolkit toolkit ;
	private ObjectMapper mapper = new ObjectMapper();
	
	private DeserializerToolkit() {
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Pays.class, new PaysDeserializer());
        module.addDeserializer(Localite.class, new LocaliteDeserializer());
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        module.addDeserializer(AnneeScolaire.class, new AnneeScolaireDeserializer());
        module.addDeserializer(EntiteGenerique.class, new EntiteGeneriqueDeserializer());
        module.addDeserializer(Trimestre.class, new TrimestreDeserializer());
        module.addDeserializer(Niveau.class, new NiveauDeserializer());
        module.addDeserializer(Filiere.class, new FiliereDeserializer());
        module.addDeserializer(Classe.class, new ClasseDeserializer());
        module.addDeserializer(UE.class, new UEDeserializer());
        module.addDeserializer(Classe.class, new ClasseDeserializer());
        module.addDeserializer(EmploiTemps.class, new EmploiTempsDeserializer());
        module.addDeserializer(DetailEmploiTemps.class, new DetailEmploiTempsDeserializer());
        module.addDeserializer(Enseignant.class, new EnseignantDeserializer());
        module.addDeserializer(Eleve.class, new EleveDeserializer());
        module.addDeserializer(Groupe.class, new GroupeDeserializer());
        module.addDeserializer(Domaine.class, new DomaineDeserializer());
        module.addDeserializer(Fonction.class, new FonctionDeserializer());
        mapper.registerModule(module);
	}
	
	public static DeserializerToolkit getToolkit() {
		if(toolkit == null) {
			toolkit = new DeserializerToolkit() ;
		}
		return toolkit ;
	}
	
	public synchronized Object deserialize(String stringObject, Class classType) throws Exception {
		logger.log(Level.INFO, "Content to deserialize "+stringObject);
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		Object object =  mapper.readValue(new StringReader(stringObject), classType); 
		return object ;
	}

}
