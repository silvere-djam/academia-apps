package cm.deepdream.academia.programmation.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.SemestreService;

@RestController
@RequestMapping("/ws/semestre")
public class SemestreWS {
	private Logger logger = Logger.getLogger(SemestreWS.class.getName()) ;
	@Autowired
	private SemestreService semestreService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	
	@PostMapping("/ajout")
	public Semestre ajout (@RequestBody  Semestre semestre) {
		try {
			Semestre semestreCree = semestreService.creer(semestre) ;
			return semestreCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public Semestre maj (@RequestBody Semestre semestre) {
		try {
			Semestre semestreMaj = semestreService.modifier(semestre) ;
			return semestreMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@RequestBody Semestre semestre) {
		try {
			semestreService.supprimer(semestre) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Semestre getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			
			Semestre semestre = semestreService.rechercher(etablissement, id) ;
			return semestre  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<Semestre> getByEcole (@PathVariable("idEtablissement") long idEtablissement,
			@PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Semestre> liste = semestreService.rechercher(etablissement, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Semestre>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}/courant")
	public Semestre getByEtablisement (@PathVariable("idEtablissement") Long idEtablissement,
			@PathVariable("idAnneeScolaire") Long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			Semestre semestre = semestreService.rechercherCourant(etablissement, anneeScolaire) ;
			return semestre ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
}
