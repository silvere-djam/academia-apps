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
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.service.DomaineService;
import cm.deepdream.academia.programmation.service.EtablissementService;
@RestController
@RequestMapping("/ws/domaine")
public class DomaineWS {
	private Logger logger = Logger.getLogger(DomaineWS.class.getName()) ;
	@Autowired
	private DomaineService domaineService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public Domaine ajout (@RequestBody  Domaine domaine) {
		try {
			Domaine domaineCree = domaineService.creer(domaine) ;
			return domaineCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	public Domaine maj (@RequestBody Domaine domaine) {
		try {
			Domaine domaineMaj = domaineService.modifier(domaine) ;
			return domaineMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody Domaine domaine) {
		try {
			domaineService.supprimer(domaine) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Domaine getById (@PathVariable("id") long id) {
		try {
			Domaine domaine = domaineService.rechercher(id) ;
			return domaine  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Domaine getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Domaine domaine = domaineService.rechercher(etablissement, id) ;
			return domaine  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Domaine> getByEcole (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Domaine> liste = domaineService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Domaine>() ;
		}
	}
	
}
