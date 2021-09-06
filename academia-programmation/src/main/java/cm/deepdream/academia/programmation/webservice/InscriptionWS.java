package cm.deepdream.academia.programmation.webservice;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Inscription;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.ClasseService;
import cm.deepdream.academia.programmation.service.EleveService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.InscriptionService;

@RestController
@RequestMapping("/ws/inscription")
public class InscriptionWS {
	private Logger logger = Logger.getLogger(InscriptionWS.class.getName()) ;
	@Autowired
	private InscriptionService inscriptionService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private EleveService eleveService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	
	@PostMapping("/ajout1")
	public Inscription ajout1 (@RequestBody  Inscription inscription) {
		try {
			Eleve eleve = inscription.getEleve() ;
			Eleve eleveCree = eleveService.creer(eleve) ;
			inscription.setEleve(eleveCree);
			
			Inscription inscriptionCree = inscriptionService.creer(inscription) ;
			return inscriptionCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@PostMapping("/ajout2")
	public Inscription ajout2 (@RequestBody  Inscription inscription) {
		try {
			Inscription inscriptionCree = inscriptionService.creer(inscription) ;
			return inscriptionCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/modification")
	public Inscription maj (@RequestBody Inscription inscription) {
		try {
			Inscription inscriptionMaj = inscriptionService.modifier(inscription) ;
			return inscriptionMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody Inscription inscription) {
		try {
			inscriptionService.supprimer(inscription) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Inscription getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Inscription inscription = inscriptionService.rechercher(etablissement, id) ;
			return inscription  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<Inscription> getInscriptions0 (@PathVariable("idEtablissement") long idEtablissement, 
			@PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Inscription> listeInscriptions = inscriptionService.rechercher(etablissement, anneeScolaire) ;
			return listeInscriptions  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}")
	public List<Inscription> getInscriptions1 (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("idClasse") long idClasse, 
			@PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, idClasse) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Inscription> listeInscriptions = inscriptionService.rechercher(etablissement, classe, anneeScolaire) ;
			return listeInscriptions  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/eleve/{idEleve}/anneescolaire/{idAnneeScolaire}")
	public List<Inscription> getInscriptions2 (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("idEleve") long idEleve, 
			@PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Eleve eleve = eleveService.rechercher(idEleve) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Inscription> listeInscriptions = inscriptionService.rechercher(etablissement, eleve, anneeScolaire) ;
			return listeInscriptions  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
}
