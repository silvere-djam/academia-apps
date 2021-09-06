package cm.deepdream.academia.viescolaire.webservice;
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
import cm.deepdream.academia.viescolaire.data.Activite;
import cm.deepdream.academia.viescolaire.service.EtablissementService;
import cm.deepdream.academia.viescolaire.service.ActiviteService;

@RestController
@RequestMapping("/ws/activite")
public class ActiviteWS {
	private Logger logger = Logger.getLogger(ActiviteWS.class.getName()) ;
	@Autowired
	private ActiviteService activiteService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public Activite ajout (@RequestBody  Activite activite) {
		try {
			Activite activiteCree = activiteService.creer(activite) ;
			return activiteCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	public Activite maj ( @RequestBody Activite activite) {
		try {
			Activite activiteMaj = activiteService.modifier(activite) ;
			return activiteMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody Activite activite) {
		try {
			activiteService.supprimer(activite) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Activite getById (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Activite activite = activiteService.rechercher(etablissement, id) ;
			return activite  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Activite> getByEcole (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Activite> liste = activiteService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Activite>() ;
		}
	}
	
}
