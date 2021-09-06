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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.programmation.service.CentreExamenService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.ExamenService;
import cm.deepdream.academia.programmation.service.SalleExamenService;

@RestController
@RequestMapping("/ws/salleexamen")
public class SalleExamenWS {
	private Logger logger = Logger.getLogger(SalleExamenWS.class.getName()) ;
	@Autowired
	private SalleExamenService salleExamenService ;
	@Autowired
	private ExamenService examenService ;
	@Autowired
	private CentreExamenService centreExamenService ;
	@Autowired
	private EtablissementService etablissementService ;
	

	
	@PostMapping("/ajout")
	public ResponseEntity<SalleExamen> ajouter (@RequestBody  SalleExamen salleExamen) {
		try {
			SalleExamen salleExamenCree = salleExamenService.creer(salleExamen) ;
			return ResponseEntity.ok(salleExamenCree) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<SalleExamen>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	@PostMapping("/modification")
	public ResponseEntity<SalleExamen> modifier (@RequestBody SalleExamen salleExamen) {
		try {
			SalleExamen salleExamenModifiee = salleExamenService.modifier(salleExamen) ;
			return ResponseEntity.ok(salleExamenModifiee) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<SalleExamen>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@DeleteMapping("/suppr")
	public ResponseEntity<Integer> suppr (@RequestBody SalleExamen salleExamen) {
		try {
			salleExamenService.supprimer(salleExamen) ;
			return ResponseEntity.ok(1) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public SalleExamen getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			SalleExamen salleExamen = salleExamenService.rechercher(etablissement, id) ;
			return salleExamen  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<SalleExamen> getCours1 (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idCentreExamen") Long idCentreExamen) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			CentreExamen centreExamen = centreExamenService.rechercher(etablissement, idCentreExamen) ;
			List<SalleExamen> liste = salleExamenService.rechercher(etablissement, centreExamen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<SalleExamen>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/examen/{idExamen}")
	public List<SalleExamen> getSalleExamensTrimestre (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idExamen") Long idExamen) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Examen examen = examenService.rechercher(etablissement, idExamen) ;
			List<SalleExamen> liste = salleExamenService.rechercher(etablissement, examen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<SalleExamen>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}")
	public List<SalleExamen> getSalleExamens_2 (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idCentreExamen") Long idCentreExamen) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			CentreExamen centreExamen = centreExamenService.rechercher(etablissement, idCentreExamen) ;
			List<SalleExamen> liste = salleExamenService.rechercher(etablissement, centreExamen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<SalleExamen>() ;
		}
	}
	
}
