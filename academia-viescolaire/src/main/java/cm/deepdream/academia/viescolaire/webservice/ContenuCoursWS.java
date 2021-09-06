package cm.deepdream.academia.viescolaire.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.data.ContenuCours;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.service.EtablissementService;
import cm.deepdream.academia.viescolaire.service.ContenuCoursService;
import cm.deepdream.academia.viescolaire.service.CoursService;

@RestController
@RequestMapping("/ws/contenucours")
public class ContenuCoursWS {
	private Logger logger = Logger.getLogger(ContenuCoursWS.class.getName()) ;
	@Autowired
	private ContenuCoursService contenuCoursService ;
	@Autowired
	private CoursService coursService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/edition")
	public ContenuCours ajouter (@RequestBody  ContenuCours contenuCours) {
		try {
			ContenuCours contenuCoursCree = contenuCoursService.editer(contenuCours) ;
			return contenuCoursCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public ContenuCours modifier ( @RequestBody ContenuCours contenuCours) {
		try {
			ContenuCours contenuCoursMaj = contenuCoursService.modifier(contenuCours) ;
			return contenuCoursMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/suppr")
	public void suppr (@RequestBody ContenuCours contenuCours) {
		try {
			contenuCoursService.supprimer(contenuCours) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public ContenuCours getById (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			ContenuCours contenuCours = contenuCoursService.rechercher(etablissement, id) ;
			return contenuCours  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<ContenuCours> getByEcole (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<ContenuCours> liste = contenuCoursService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<ContenuCours>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/cours/{idCours}")
	public List<ContenuCours> getByEtsEtCours (@PathVariable("idEtablissement") Long idEtablissement,
			@PathVariable("idCours") Long idCours) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Cours cours = coursService.rechercher(etablissement, idCours) ;
			List<ContenuCours> liste = contenuCoursService.rechercher(cours) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<ContenuCours>() ;
		}
	}
	
}
