package cm.deepdream.academia.souscription.webservice;
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

import cm.deepdream.academia.souscription.model.Localite;
import cm.deepdream.academia.souscription.service.LocaliteService;

@RestController
@RequestMapping("/ws/localite")
public class LocaliteWS {
	private Logger logger = Logger.getLogger(LocaliteWS.class.getName()) ;
	@Autowired
	private LocaliteService localiteService ;
	
	@PostMapping("/ajout")
	public Localite ajout (@RequestBody  Localite localite) {
		try {
			Localite villeCree = localiteService.creer(localite) ;
			return villeCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	@ResponseStatus(code =  HttpStatus.OK)
	public Localite maj (@RequestBody Localite localite) {
		try {
			Localite villeMaj = localiteService.modifier(localite) ;
			return villeMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody Localite localite) {
		try {
			localiteService.supprimer(localite) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Localite getById (@PathVariable("id") Long id) {
		try {
			Localite ville = localiteService.rechercher(id) ;
			return ville  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Localite> getAll () {
		try {
			List<Localite> liste = localiteService.rechercherTout(new Localite()) ;
			return liste  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Localite>() ;
		}
	}
	
}
