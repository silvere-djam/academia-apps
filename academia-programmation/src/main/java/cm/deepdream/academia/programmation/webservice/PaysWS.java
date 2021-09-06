package cm.deepdream.academia.programmation.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.programmation.service.PaysService;

@RestController
@RequestMapping("/ws/pays")
public class PaysWS {
	private Logger logger = Logger.getLogger(PaysWS.class.getName()) ;
	@Autowired
	private PaysService paysService ;
	
	
	
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
	
	
	@GetMapping("/libelle/{libelle}")
	public Pays getByLibelle (@PathVariable("libelle") String libelle) {
		try {
			Pays pays = paysService.rechercherLibelle(libelle) ;
			return pays  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Pays> getAll () {
		try {
			List<Pays> liste = paysService.rechercher(new Pays()) ;
			return liste  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Pays>() ;
		}
	}
	
}
