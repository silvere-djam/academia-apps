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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.viescolaire.service.AnneeScolaireService;
import cm.deepdream.academia.viescolaire.service.ClasseService;
import cm.deepdream.academia.viescolaire.service.CoursService;
import cm.deepdream.academia.viescolaire.service.UEService;
import cm.deepdream.academia.viescolaire.service.EtablissementService;
import cm.deepdream.academia.viescolaire.service.TrimestreService;
@RestController
@RequestMapping("/ws/cours")
public class CoursWS {
	private Logger logger = Logger.getLogger(CoursWS.class.getName()) ;
	@Autowired
	private CoursService coursService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private UEService uEService ;
	@Autowired
	private TrimestreService trimestreService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;

	
	@PostMapping("/ajout")
	public Cours ajouter (@RequestBody  Cours cours) {
		try {
			Cours coursCree = coursService.creer(cours) ;
			return coursCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/modification")
	@ResponseStatus(code =  HttpStatus.OK)
	public Cours modifier (@RequestBody Cours cours) {
		try {
			Cours coursMaj = coursService.modifier(cours) ;
			return coursMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@RequestBody Cours cours) {
		try {
			coursService.supprimer(cours) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Cours getById (@PathVariable("id") long id) {
		try {
			Cours cours = coursService.rechercher(id) ;
			return cours  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Cours getById (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Cours cours = coursService.rechercher(etablissement, id) ;
			return cours  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Cours> getByEcole (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Cours> liste = coursService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Cours>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}")
	public List<Cours> getCours1 (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idClasse") Long idClasse,
			@PathVariable("idAnneeScolaire") Long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, idClasse) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Cours> liste = coursService.rechercher(etablissement, classe, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Cours>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/ue/{idUe}/anneescolaire/{idAnneeScolaire}")
	public List<Cours> getCours2 (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idUe") Long idUe,
			@PathVariable("idTrimestre") Long idTrimestre) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			UE ue = uEService.rechercher(etablissement, idUe) ;
			Trimestre trimestre = trimestreService.rechercher(idTrimestre) ;
			List<Cours> liste = coursService.rechercher(etablissement, ue, trimestre) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Cours>() ;
		}
	}
	
}
