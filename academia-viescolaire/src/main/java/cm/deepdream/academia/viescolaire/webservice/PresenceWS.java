package cm.deepdream.academia.viescolaire.webservice;
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
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.viescolaire.data.Presence;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.service.CoursService;
import cm.deepdream.academia.viescolaire.service.PresenceService;
import cm.deepdream.academia.viescolaire.service.EtablissementService;
import cm.deepdream.academia.viescolaire.service.EvaluationService;
@RestController
@RequestMapping("/ws/presence")
public class PresenceWS {
	private Logger logger = Logger.getLogger(PresenceWS.class.getName()) ;
	@Autowired
	private PresenceService presenceService ;
	@Autowired
	private CoursService coursService ;
	@Autowired
	private EvaluationService evaluationService ;
	@Autowired
	private EtablissementService etablissementService ;


	
	@PostMapping("/ajout")
	public Presence ajouter (@RequestBody  Presence presence) {
		try {
			Presence presenceCree = presenceService.creer(presence) ;
			return presenceCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@PutMapping("/modification")
	@ResponseStatus(code =  HttpStatus.OK)
	public Presence modifier (@RequestBody Presence presence) {
		try {
			Presence presenceMaj = presenceService.modifier(presence) ;
			return presenceMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@RequestBody Presence presence) {
		try {
			presenceService.supprimer(presence) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	

	@GetMapping("/etablissement/{idEtablissement}/cours/{idCours}/id/{id}")
	public Presence getPresenceCoursId (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idEtablissement") Long idCours, 
			@PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Cours cours = coursService.rechercher(idCours) ;
			Presence presence = presenceService.rechercher(cours, id) ;
			return presence  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/cours/{idCours}")
	public List<Presence> getPresencesCours (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idCours") long idCours) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Cours cours = coursService.rechercher(idCours) ;
			List<Presence> liste = presenceService.rechercher(cours) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Presence>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/evaluation/{idEvaluation}/id/{id}")
	public Presence getByPresenceEval (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idEvaluation") Long idEval, 
			@PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Evaluation evaluation = evaluationService.rechercher(idEval) ;
			Presence presence = presenceService.rechercher(evaluation, id) ;
			return presence  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/evaluation/{idEval}")
	public List<Presence> getPresencesEval (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idEval") Long idEval) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Evaluation evaluation = evaluationService.rechercher(idEval) ;
			List<Presence> liste = presenceService.rechercher(evaluation) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Presence>() ;
		}
	}
	
	
	
}
