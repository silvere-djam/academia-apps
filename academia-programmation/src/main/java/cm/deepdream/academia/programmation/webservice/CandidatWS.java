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
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.programmation.data.Candidat;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.service.CandidatService;
import cm.deepdream.academia.programmation.service.CentreExamenService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.ExamenService;
import cm.deepdream.academia.programmation.service.SalleExamenService;
@RestController
@RequestMapping("/ws/candidat")
public class CandidatWS {
	private Logger logger = Logger.getLogger(CandidatWS.class.getName()) ;
	@Autowired
	private CandidatService candidatService ;
	@Autowired
	private ExamenService examenService ;
	@Autowired
	private CentreExamenService centreExamenService ;
	@Autowired
	private SalleExamenService salleExamenService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	
	
	@PostMapping("/ajout")
	public ResponseEntity<Candidat> ajout (@RequestBody  Candidat candidat) {
		try {
			Candidat candidatCree = candidatService.creer(candidat) ;
			return ResponseEntity.ok(candidatCree) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Candidat>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@PostMapping("/modification")
	public ResponseEntity<Candidat> modifier (@RequestBody Candidat candidat) {
		try {
			Candidat candidatModifie = candidatService.modifier(candidat) ;
			return ResponseEntity.ok(candidatModifie) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Candidat>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@DeleteMapping("/suppr")
	public ResponseEntity<Integer> suppr (@RequestBody Candidat candidat) {
		try {
			candidatService.supprimer(candidat) ;
			return ResponseEntity.ok(1) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Candidat getById (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Candidat candidat = candidatService.rechercher(etablissement, id) ;
			return candidat  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  null ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/examen/{idExamen}")
	public List<Candidat> getCandidats0 (@PathVariable("idEtablissement") Long idEtablissement,
			@PathVariable("idExamen") Long idExamen) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Examen examen = examenService.rechercher(etablissement, idExamen) ;
			List<Candidat> liste = candidatService.rechercher(etablissement, examen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Candidat>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}")
	public List<Candidat> getCandidats_1 (@PathVariable("idEtablissement") Long idEtablissement,
			@PathVariable("idCentreExamen") Long idCentreExamen) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			CentreExamen centreExamen = centreExamenService.rechercher(etablissement, idCentreExamen) ;
			List<Candidat> liste = candidatService.rechercher(etablissement, centreExamen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Candidat>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/salleexamen/{idSalleExamen}")
	public List<Candidat> getCandidats_2 (@PathVariable("idEtablissement") Long idEtablissement,
			@PathVariable("idSalleExamen") Long idSalleExamen) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			SalleExamen salleExamen = salleExamenService.rechercher(etablissement, idSalleExamen) ;
			List<Candidat> liste = candidatService.rechercher(etablissement, salleExamen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Candidat>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Candidat> getByEcole (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Candidat> liste = candidatService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
}
