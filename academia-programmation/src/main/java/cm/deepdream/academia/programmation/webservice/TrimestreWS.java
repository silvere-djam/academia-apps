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
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.TrimestreService;
@RestController
@RequestMapping("/ws/trimestre")
public class TrimestreWS {
	private Logger logger = Logger.getLogger(TrimestreWS.class.getName()) ;
	@Autowired
	private TrimestreService trimestreService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	
	@PostMapping("/ajout")
	public Trimestre ajout (@RequestBody  Trimestre trimestre) {
		try {
			Trimestre trimestreCree = trimestreService.creer(trimestre) ;
			return trimestreCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public Trimestre maj (@RequestBody Trimestre trimestre) {
		try {
			Trimestre trimestreMaj = trimestreService.modifier(trimestre) ;
			return trimestreMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/etablissement/{idEtablissement}/id/{id}")
	public int suppr (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Trimestre trimestre = trimestreService.rechercher(etablissement, id) ;
			trimestreService.supprimer(trimestre) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Trimestre getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			
			Trimestre trimestre = trimestreService.rechercher(etablissement, id) ;
			return trimestre  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<Trimestre> getByEcole (@PathVariable("idEtablissement") long idEtablissement,
			@PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Trimestre> liste = trimestreService.rechercher(etablissement, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Trimestre>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}/courant")
	public Trimestre getByEtablisement (@PathVariable("idEtablissement") Long idEtablissement,
			@PathVariable("idAnneeScolaire") Long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			Trimestre trimestre = trimestreService.rechercherCourant(etablissement, anneeScolaire) ;
			return trimestre ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@PutMapping("/definition-defaut")
	public void definirDefaut (@RequestBody Trimestre trimestre) {
		try {
			trimestreService.definirDefaut(trimestre) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
		}
	}
	
}
