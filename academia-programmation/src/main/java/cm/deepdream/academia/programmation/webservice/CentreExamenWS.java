package cm.deepdream.academia.programmation.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.ExamenService;
import cm.deepdream.academia.programmation.service.CentreExamenService;

@RestController
@RequestMapping("/ws/centreexamen")
public class CentreExamenWS {
	private Logger logger = Logger.getLogger(CentreExamenWS.class.getName()) ;
	@Autowired
	private CentreExamenService centreExamenService ;
	@Autowired
	private ExamenService examenService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	@Autowired
	private EtablissementService etablissementService ;
	

	
	@PostMapping("/ajout")
	public ResponseEntity<CentreExamen> ajouter (@RequestBody  CentreExamen centreExamen) {
		try {
			CentreExamen centreExamenCree = centreExamenService.creer(centreExamen) ;
			return ResponseEntity.ok(centreExamenCree) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<CentreExamen>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	@PostMapping("/modification")
	public ResponseEntity<CentreExamen> modifier (@RequestBody CentreExamen centreExamen) {
		try {
			CentreExamen centreExamenModifie = centreExamenService.modifier(centreExamen) ;
			return ResponseEntity.ok(centreExamenModifie) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<CentreExamen>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@DeleteMapping("/suppr")
	public ResponseEntity<Integer> suppr (@RequestBody CentreExamen centreExamen) {
		try {
			centreExamenService.supprimer(centreExamen) ;
			return ResponseEntity.ok(1) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public CentreExamen getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			CentreExamen centreExamen = centreExamenService.rechercher(etablissement, id) ;
			return centreExamen  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<CentreExamen> getCours1 (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idAnneeScolaire") Long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<CentreExamen> liste = centreExamenService.rechercher(etablissement, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<CentreExamen>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/examen/{idExamen}")
	public List<CentreExamen> getCentreExamensTrimestre (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idExamen") Long idExamen) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Examen examen = examenService.rechercher(etablissement, idExamen) ;
			List<CentreExamen> liste = centreExamenService.rechercher(etablissement, examen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<CentreExamen>() ;
		}
	}
	
	
}
