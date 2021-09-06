package cm.deepdream.academia.viescolaire.listener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Candidat;
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
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.util.DeserializerToolkit;
import cm.deepdream.academia.viescolaire.repository.AnneeScolaireRepository;
import cm.deepdream.academia.viescolaire.repository.CandidatRepository;
import cm.deepdream.academia.viescolaire.repository.CentreExamenRepository;
import cm.deepdream.academia.viescolaire.repository.ClasseRepository;
import cm.deepdream.academia.viescolaire.repository.DetailEmploiTempsRepository;
import cm.deepdream.academia.viescolaire.repository.DomaineRepository;
import cm.deepdream.academia.viescolaire.repository.EleveRepository;
import cm.deepdream.academia.viescolaire.repository.EmploiTempsRepository;
import cm.deepdream.academia.viescolaire.repository.EnseignantRepository;
import cm.deepdream.academia.viescolaire.repository.EtablissementRepository;
import cm.deepdream.academia.viescolaire.repository.ExamenRepository;
import cm.deepdream.academia.viescolaire.repository.FiliereRepository;
import cm.deepdream.academia.viescolaire.repository.FonctionRepository;
import cm.deepdream.academia.viescolaire.repository.GroupeRepository;
import cm.deepdream.academia.viescolaire.repository.NiveauRepository;
import cm.deepdream.academia.viescolaire.repository.PaysRepository;
import cm.deepdream.academia.viescolaire.repository.SalleExamenRepository;
import cm.deepdream.academia.viescolaire.repository.TrimestreRepository;
import cm.deepdream.academia.viescolaire.repository.TypeExamenRepository;
import cm.deepdream.academia.viescolaire.repository.UERepository;
import cm.deepdream.academia.viescolaire.repository.VilleRepository;

@EnableBinding(Sink.class)
public class DocumentConsumer {
	private Logger logger = Logger.getLogger(DocumentConsumer.class.getName()) ;
	@Autowired
	private EtablissementRepository etablissementRepository ;
	@Autowired
	private AnneeScolaireRepository anneeScolaireRepository ;
	@Autowired
	private TrimestreRepository trimestreRepository ;
	@Autowired
	private ClasseRepository classeRepository ;
	@Autowired
	private UERepository ueRepository ;
	@Autowired
	private EnseignantRepository enseignantRepository ;
	@Autowired
	private EleveRepository eleveRepository ;
	@Autowired
	private NiveauRepository niveauRepository ;
	@Autowired
	private FiliereRepository filiereRepository ;
	@Autowired
	private EmploiTempsRepository emploiTempsRepository ;
	@Autowired
	private DetailEmploiTempsRepository detailETRepository ;
	@Autowired
	private PaysRepository paysRepository ;
	@Autowired
	private VilleRepository villeRepository ;
	@Autowired
	private DomaineRepository domaineRepository ;
	@Autowired
	private GroupeRepository groupeRepository ;
	@Autowired
	private FonctionRepository fonctionRepository ;
	@Autowired
	private ExamenRepository examenRepository ;
	@Autowired
	private CentreExamenRepository centreExamenRepository ;
	@Autowired
	private SalleExamenRepository salleExamenRepository ;
	@Autowired
	private CandidatRepository candidatRepository ;
	@Autowired
	private TypeExamenRepository typeExamenRepository ;
	
	public DocumentConsumer() {

	}
	
	@StreamListener(Sink.INPUT)
	public void enregistrer (Document document) throws Exception{
		logger.log(Level.INFO, "Enregistrement du document "+document);

		if(document.getType().equals(Etablissement.class.getName())) {
			logger.info("Enregistrement de l'etablissement 0");
			Etablissement etablissement = (Etablissement) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Etablissement.class);
			logger.info("Enregistrement de l'etablissement "+etablissement);
			etablissementRepository.save(etablissement) ;
		} else if(document.getType().equals(AnneeScolaire.class.getName())) {
			logger.info("Enregistrement de l'année scolaire 0");
			AnneeScolaire anneeScolaire = (AnneeScolaire) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					AnneeScolaire.class);
			logger.info("Enregistrement de l'année scolaire "+anneeScolaire);
			anneeScolaireRepository.save(anneeScolaire) ;
		} else if(document.getType().equals(Trimestre.class.getName())) {
			logger.info("Enregistrement du trimestre 0");
			Trimestre trimestre = (Trimestre) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Trimestre.class);
			logger.info("Enregistrement du trimestre "+trimestre);
			trimestreRepository.save(trimestre) ;
		} else if(document.getType().equals(Niveau.class.getName())) {
			logger.info("Enregistrement du niveau 0");
			Niveau niveau = (Niveau) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Niveau.class);
			logger.info("Enregistrement du niveau "+niveau);
			niveauRepository.save(niveau) ;
		} else if(document.getType().equals(Filiere.class.getName())) {
			logger.info("Enregistrement de la filiere");
			Filiere filiere = (Filiere) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Filiere.class);
			logger.info("Enregistrement de la filiere "+filiere);
			filiereRepository.save(filiere) ;
		} else if(document.getType().equals(Classe.class.getName())) {
			logger.info("Enregistrement de la classe");
			Classe classe = (Classe) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Classe.class);
			logger.info("Enregistrement de la classe "+classe);
			classeRepository.save(classe) ;
		} else if(document.getType().equals(UE.class.getName())) {
			UE ue = (UE) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					UE.class);
			logger.info("Enregistrement de l'UE "+ue);
			ueRepository.save(ue) ;
		} else if(document.getType().equals(Enseignant.class.getName())) {
			Enseignant enseignant = (Enseignant) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Enseignant.class);
			logger.info("Enregistrement de l'enseignant "+enseignant);
			enseignantRepository.save(enseignant) ;
		} else if(document.getType().equals(Eleve.class.getName())) {
			Eleve eleve = (Eleve) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Eleve.class);
			logger.info("Enregistrement de l'eleve "+eleve);
			eleveRepository.save(eleve) ;
		} else if(document.getType().equals(EmploiTemps.class.getName())) {
			EmploiTemps emploiTemps = (EmploiTemps) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					EmploiTemps.class);
			logger.info("Enregistrement de l'emploi de temps "+emploiTemps);
			emploiTempsRepository.save(emploiTemps) ;
		}  else if(document.getType().equals(DetailEmploiTemps.class.getName())) {
			DetailEmploiTemps detailET = (DetailEmploiTemps) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					DetailEmploiTemps.class);
			logger.info("Enregistrement de détail de l'emploi de temps "+detailET);
			detailETRepository.save(detailET) ;
		}  else if(document.getType().equals(Pays.class.getName())) {
			Pays pays = (Pays) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Pays.class);
			logger.info("Enregistrement de détail du pays "+pays);
			paysRepository.save(pays) ;
		} else if(document.getType().equals(Localite.class.getName())) {
			Localite ville = (Localite) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Localite.class);
			logger.info("Enregistrement de détail de la ville "+ville);
			villeRepository.save(ville) ;
		} else if(document.getType().equals(Domaine.class.getName())) {
			Domaine domaine = (Domaine) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Domaine.class);
			logger.info("Enregistrement de détail du domaine "+domaine);
			domaineRepository.save(domaine) ;
		} else if(document.getType().equals(Groupe.class.getName())) {
			Groupe groupe = (Groupe) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Groupe.class);
			logger.info("Enregistrement de détail du groupe "+groupe);
			groupeRepository.save(groupe) ;
		} else if(document.getType().equals(Fonction.class.getName())) {
			Fonction fonction = (Fonction) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Fonction.class);
			logger.info("Enregistrement de détail de la fonction "+fonction);
			fonctionRepository.save(fonction) ;
		} else if(document.getType().equals(Candidat.class.getName())) {
			Candidat candidat = (Candidat) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Candidat.class);
			logger.info("Enregistrement de détail de l'examen "+candidat);
			candidatRepository.save(candidat) ;
		} else if(document.getType().equals(Examen.class.getName())) {
			TypeExamen typeExamen = (TypeExamen) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					TypeExamen.class);
			logger.info("Enregistrement de détail du type d'examen "+typeExamen);
			typeExamenRepository.save(typeExamen) ;
		} else if(document.getType().equals(SalleExamen.class.getName())) {
			SalleExamen salleExamen = (SalleExamen) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					SalleExamen.class);
			logger.info("Enregistrement de détail de la salle d'examen "+salleExamen);
			salleExamenRepository.save(salleExamen) ;
		} else if(document.getType().equals(CentreExamen.class.getName())) {
			CentreExamen centreExamen = (CentreExamen) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					CentreExamen.class);
			logger.info("Enregistrement de détail du centre d'examen "+centreExamen);
			centreExamenRepository.save(centreExamen) ;
		}  else if(document.getType().equals(Examen.class.getName())) {
			Examen examen = (Examen) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Examen.class);
			logger.info("Enregistrement de détail de l'examen "+examen);
			examenRepository.save(examen) ;
		} 
	}

}
