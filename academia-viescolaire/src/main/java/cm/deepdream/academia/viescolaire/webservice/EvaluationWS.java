package cm.deepdream.academia.viescolaire.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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
import org.springframework.web.client.RestTemplate;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.viescolaire.service.AnneeScolaireService;
import cm.deepdream.academia.viescolaire.service.ClasseService;
import cm.deepdream.academia.viescolaire.service.EtablissementService;
import cm.deepdream.academia.viescolaire.service.EvaluationService;

@RestController
@RequestMapping("/ws/evaluation")
public class EvaluationWS {
	private Logger logger = Logger.getLogger(EvaluationWS.class.getName()) ;
	@Autowired
	private EvaluationService evaluationService ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	@Autowired
	private EtablissementService etablissementService ;
	

	
	@PostMapping("/ajout")
	public Evaluation ajouter (@RequestBody  Evaluation evaluation) {
		try {
			Evaluation evaluationCree = evaluationService.creer(evaluation) ;
			return evaluationCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public Evaluation modifier (@RequestBody Evaluation evaluation) {
		try {
			Evaluation evaluationMaj = evaluationService.modifier(evaluation) ;
			return evaluationMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@RequestBody Evaluation evaluation) {
		try {
			evaluationService.supprimer(evaluation) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Evaluation getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Evaluation evaluation = evaluationService.rechercher(etablissement, id) ;
			return evaluation  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}")
	public List<Evaluation> getCours1 (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idClasse") Long idClasse,
			@PathVariable("idAnneeScolaire") Long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, idClasse) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Evaluation> liste = evaluationService.rechercher(etablissement, classe, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Evaluation>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Evaluation> getByEcole (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Evaluation> liste = evaluationService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Evaluation>() ;
		}
	}
	
}
