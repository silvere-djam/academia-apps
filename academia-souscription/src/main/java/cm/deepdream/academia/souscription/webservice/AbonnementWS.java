package cm.deepdream.academia.souscription.webservice;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.deepdream.academia.souscription.model.Abonnement;
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.service.AbonnementService;
import cm.deepdream.academia.souscription.service.EtablissementService;
@RestController
@RequestMapping("/ws/abonnement")
public class AbonnementWS {
	private Logger logger = Logger.getLogger(AbonnementWS.class.getName()) ;
	@Autowired
	private AbonnementService abonnementService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public Abonnement ajouter (@RequestBody  Abonnement abonnement) {
		return abonnementService.creer(abonnement) ;
	}
	
	@PostMapping("/etablissement/ajout")
	public Abonnement ajouter (@RequestBody  Etablissement etablissement) {
		try {
			Abonnement abonnementCree = abonnementService.creer(etablissement) ;
			return abonnementCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public Abonnement modifier (@RequestBody Abonnement abonnement) {
		try {
			Abonnement abonnementMaj = abonnementService.modifier(abonnement) ;
			return abonnementMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppression")
	public int suppr (@RequestBody Abonnement abonnement) {
		try {
			abonnementService.supprimer(abonnement) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Abonnement getById (@PathVariable("id") long id) {
		try {
			Abonnement abonnement = abonnementService.rechercher(id) ;
			return abonnement  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Abonnement> getByEcole (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Abonnement> liste = abonnementService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/annee/{annee}")
	public List<Abonnement> getByYear (@PathVariable("annee") Integer annee) {
		try {
			LocalDate dateDebut = LocalDate.of(annee, Month.JANUARY, 1) ;
			LocalDate dateFin = LocalDate.of(annee, Month.DECEMBER, 31) ;
			List<Abonnement> liste = abonnementService.rechercher(dateDebut, dateFin) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/statut/{statut}")
	public List<Abonnement> getByStatut (@PathVariable("statut") String statut) {
		try {
			List<Abonnement> liste = abonnementService.rechercher(statut) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Abonnement> getAll () {
		try {
			List<Abonnement> liste = abonnementService.rechercherTout(new Abonnement()) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
}
