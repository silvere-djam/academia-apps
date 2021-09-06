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
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.FonctionService;

@RestController
@RequestMapping("/ws/fonction")
public class FonctionWS {
	private Logger logger = Logger.getLogger(FonctionWS.class.getName()) ;
	@Autowired
	private FonctionService fonctionService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public Fonction ajout (@RequestBody  Fonction fonction) {
		try {
			Fonction fonctionCree = fonctionService.creer(fonction) ;
			return fonctionCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	public Fonction maj ( @RequestBody Fonction fonction) {
		try {
			Fonction fonctionMaj = fonctionService.modifier(fonction) ;
			return fonctionMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/etablissement/{idEtablissement}/id/{id}")
	public int suppr (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Fonction fonction = fonctionService.rechercher(etablissement, id) ;
			fonctionService.supprimer(fonction) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Fonction getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Fonction fonction = fonctionService.rechercher(etablissement, id) ;
			return fonction  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Fonction> getByEcole (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Fonction> liste = fonctionService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Fonction>() ;
		}
	}
	
}
