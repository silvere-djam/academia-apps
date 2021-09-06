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
import cm.deepdream.academia.viescolaire.data.Absence;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.service.EtablissementService;
import cm.deepdream.academia.viescolaire.service.AbsenceService;
import cm.deepdream.academia.viescolaire.service.CoursService;

@RestController
@RequestMapping("/ws/absence")
public class AbsenceWS {
	private Logger logger = Logger.getLogger(AbsenceWS.class.getName()) ;
	@Autowired
	private AbsenceService absenceService ;
	@Autowired
	private CoursService coursService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/edition")
	public Absence ajout (@RequestBody  Absence absence) {
		try {
			Absence absenceCree = absenceService.editer(absence) ;
			return absenceCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public Absence maj ( @RequestBody Absence absence) {
		try {
			Absence absenceMaj = absenceService.modifier(absence) ;
			return absenceMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/suppr")
	public void suppr (@RequestBody Absence absence) {
		try {
			absenceService.supprimer(absence) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Absence getById (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Absence absence = absenceService.rechercher(etablissement, id) ;
			return absence  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Absence> getByEcole (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Absence> liste = absenceService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Absence>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/cours/{idCours}")
	public List<Absence> getByEtsEtCours (@PathVariable("idEtablissement") Long idEtablissement,
			@PathVariable("idCours") Long idCours) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Cours cours = coursService.rechercher(etablissement, idCours) ;
			List<Absence> liste = absenceService.rechercher(cours) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Absence>() ;
		}
	}
	
}
