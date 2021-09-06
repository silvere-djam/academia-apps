package cm.deepdream.academia.programmation.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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
import org.springframework.web.client.RestTemplate;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.serializers.SemestreSerializer;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.ClasseService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.ExamenService;
import cm.deepdream.academia.programmation.service.SemestreService;
import cm.deepdream.academia.programmation.service.TrimestreService;

@RestController
@RequestMapping("/ws/examen")
public class ExamenWS {
	private Logger logger = Logger.getLogger(ExamenWS.class.getName()) ;
	@Autowired
	private ExamenService examenService ;
	@Autowired
	private TrimestreService trimestreService ;
	@Autowired
	private SemestreService semestreService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	@Autowired
	private EtablissementService etablissementService ;
	

	
	@PostMapping("/ajout")
	public ResponseEntity<Examen> ajouter (@RequestBody  Examen examen) {
		try {
			Examen examenCree = examenService.creer(examen) ;
			return  ResponseEntity.ok(examenCree) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Examen>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	@PostMapping("/modification")
	public ResponseEntity<Examen> modifier (@RequestBody Examen examen) {
		try {
			Examen examenModifie = examenService.modifier(examen) ;
			return  ResponseEntity.ok(examenModifie) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Examen>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	@DeleteMapping("/suppr")
	public ResponseEntity<Integer> suppr (@RequestBody Examen examen) {
		try {
			examenService.supprimer(examen) ;
			return ResponseEntity.ok(1) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Examen getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Examen examen = examenService.rechercher(etablissement, id) ;
			return examen  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<Examen> getCours1 (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idAnneeScolaire") Long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Examen> liste = examenService.rechercher(etablissement, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Examen>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/trimestre/{idTrimestre}")
	public List<Examen> getExamensTrimestre (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idTrimestre") Long idTrimestre) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Trimestre trimestre = trimestreService.rechercher(etablissement, idTrimestre) ;
			List<Examen> liste = examenService.rechercher(etablissement, trimestre) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Examen>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/semestre/{idSemestre}")
	public List<Examen> getExamensSemestre (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idSemestre") Long idSemestre) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Semestre semestre = semestreService.rechercher(etablissement, idSemestre) ;
			List<Examen> liste = examenService.rechercher(etablissement, semestre) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Examen>() ;
		}
	}
	
	
	
}
