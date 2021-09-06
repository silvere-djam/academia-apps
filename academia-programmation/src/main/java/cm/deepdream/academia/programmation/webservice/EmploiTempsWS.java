package cm.deepdream.academia.programmation.webservice;
import java.util.ArrayList;
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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.ClasseService;
import cm.deepdream.academia.programmation.service.EmploiTempsService;
import cm.deepdream.academia.programmation.service.EtablissementService;

@RestController
@RequestMapping("/ws/emploitemps")
public class EmploiTempsWS {
	private Logger logger = Logger.getLogger(EmploiTempsWS.class.getName()) ;
	@Autowired
	private EmploiTempsService emploiTempsService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	
	@PostMapping("/ajout")
	public EmploiTemps ajout (@RequestBody  EmploiTemps emploiTemps) {
		try {
			EmploiTemps emploiTempsCree = emploiTempsService.creer(emploiTemps) ;
			return emploiTempsCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public EmploiTemps modifier (@RequestBody EmploiTemps emploiTemps) {
		try {
			EmploiTemps emploiTempsMaj = emploiTempsService.modifier(emploiTemps) ;
			return emploiTempsMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody EmploiTemps emploiTemps) {
		try {
			emploiTempsService.supprimer(emploiTemps) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public EmploiTemps getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			EmploiTemps emploiTemps = emploiTempsService.rechercher(etablissement, id) ;
			return emploiTemps  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	

	@GetMapping("/details/etablissement/{idEtablissement}/id/{id}")
	public List<DetailEmploiTemps> getByDetails (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			EmploiTemps emploiTemps = emploiTempsService.rechercher(etablissement, id) ;
			List<DetailEmploiTemps> listeET = emploiTempsService.rechercherDetails(etablissement, emploiTemps) ;
			return listeET  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<EmploiTemps> getByEcole (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<EmploiTemps> liste = emploiTempsService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<EmploiTemps>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<EmploiTemps> getByEleves(@PathVariable("idEtablissement") long idEtablissement,
			 @PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<EmploiTemps> liste = emploiTempsService.rechercher(etablissement, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<EmploiTemps>() ;
		}
	}
	
}
