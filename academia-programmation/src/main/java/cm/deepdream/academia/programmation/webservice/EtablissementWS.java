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
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.service.EtablissementService;
@RestController
@RequestMapping("/ws/etablissement")
public class EtablissementWS {
	private Logger logger = Logger.getLogger(EtablissementWS.class.getName()) ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Etablissement ajout (@RequestBody  Etablissement etablissement) {
		try {
			Etablissement etablissementCree = etablissementService.creer(etablissement) ;
			return etablissementCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	@ResponseStatus(code =  HttpStatus.OK)
	public Etablissement maj (@RequestBody Etablissement etablissement) {
		try {
			Etablissement etablissementMaj = etablissementService.modifier(etablissement) ;
			return etablissementMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@RequestBody Etablissement etablissement) {
		try {
			etablissementService.supprimer(etablissement) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Etablissement getById (@PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(id) ;
			return etablissement  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Etablissement> getAll () {
		try {
			List<Etablissement> listeEtablissements = etablissementService.rechercher(new Etablissement()) ;
			return listeEtablissements  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Etablissement>() ;
		}
	}
}
