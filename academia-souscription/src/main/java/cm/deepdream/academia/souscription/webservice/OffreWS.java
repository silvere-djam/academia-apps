package cm.deepdream.academia.souscription.webservice;
import java.math.BigDecimal;
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

import cm.deepdream.academia.souscription.model.Offre;
import cm.deepdream.academia.souscription.service.OffreService;

@RestController
@RequestMapping("/ws/offre")
public class OffreWS {
	private Logger logger = Logger.getLogger(OffreWS.class.getName()) ;
	@Autowired
	private OffreService offreService ;
	
	@PostMapping("/ajout")
	public Offre ajout (@RequestBody  Offre offre) {
		try {
			Offre offreCree = offreService.creer(offre) ;
			return offreCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	@ResponseStatus(code =  HttpStatus.OK)
	public Offre maj (@RequestBody Offre offre) {
		try {
			Offre offreMaj = offreService.modifier(offre) ;
			return offreMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody Offre offre) {
		try {
			offreService.supprimer(offre) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Offre getById (@PathVariable("id") long id) {
		try {
			Offre offre = offreService.rechercher(id) ;
			return offre  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Offre> getAll () {
		try {
			List<Offre> liste = offreService.rechercherTout(new Offre()) ;
			return liste  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Offre>() ;
		}
	}
	
	@GetMapping("/cout/{nbEleves}")
	public BigDecimal getPrice (@PathVariable("nbEleves") Integer nbEleves) {
		try {
			BigDecimal cout = offreService.rechercherCout(nbEleves) ;
			return cout  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return BigDecimal.ZERO ;
		}
	}
	
	@GetMapping("/utilisateurs/{nbEleves}")
	public Long getUtilisateurs(@PathVariable("nbEleves") Integer nbEleves) {
		try {
			Long nbUtilisateurs = offreService.rechercherNbUtilisateurs(nbEleves) ;
			return nbUtilisateurs  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0L ;
		}
	}
}
