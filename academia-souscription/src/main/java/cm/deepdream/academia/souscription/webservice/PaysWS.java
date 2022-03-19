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

import cm.deepdream.academia.souscription.model.Pays;
import cm.deepdream.academia.souscription.service.PaysService;

@RestController
@RequestMapping("/ws/pays")
public class PaysWS {
	private Logger logger = Logger.getLogger(PaysWS.class.getName()) ;
	@Autowired
	private PaysService paysService ;
	
	@PostMapping("/ajout")
	public Pays ajout (@RequestBody  Pays pays) {
		try {
			Pays paysCree = paysService.creer(pays) ;
			return paysCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Pays maj (@RequestBody Pays pays) {
		try {
			Pays paysMaj = paysService.modifier(pays) ;
			return paysMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody Pays pays) {
		try {
			paysService.supprimer(pays) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Pays getById (@PathVariable("id") long id) {
		try {
			Pays pays = paysService.rechercher(id) ;
			return pays  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Pays> getAll () {
		try {
			List<Pays> liste = paysService.rechercherTout(new Pays()) ;
			return liste  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Pays>() ;
		}
	}
	
}
