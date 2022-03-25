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

import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Logo;
import cm.deepdream.academia.souscription.service.EtablissementService;
import cm.deepdream.academia.souscription.util.FileStore;
import cm.deepdream.academia.souscription.util.LocalFileStore;

@RestController
@RequestMapping("/ws/etablissement")
public class EtablissementWS {
	private Logger logger = Logger.getLogger(EtablissementWS.class.getName()) ;
	@Autowired
	private EtablissementService etablissementService ;

	
	
	@PostMapping("/ajout")
	public Etablissement ajouter (@RequestBody  Etablissement etablissement) {
		return etablissementService.creer(etablissement) ; 
	}
	
	@PutMapping("/modification")
	@ResponseStatus(code =  HttpStatus.OK)
	public Etablissement modifier (@RequestBody Etablissement etablissement) {
		try {
			Etablissement etablissementMaj = etablissementService.modifier(etablissement) ;
			return etablissementMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody Etablissement etablissement) {
		etablissementService.supprimer(etablissement) ;
	}
	
	@GetMapping("/id/{id}")
	public Etablissement getById (@PathVariable("id") long id) {
		return etablissementService.rechercher(id) ;
	}
	
	@GetMapping("/logo/id/{id}")
	public Logo getLogo (@PathVariable("id") Long id) {
		return null ;
	}
	
	
	@GetMapping("/all")
	public List<Etablissement> getAll () {
		return etablissementService.rechercher(new Etablissement()) ; 
	}
	
}
