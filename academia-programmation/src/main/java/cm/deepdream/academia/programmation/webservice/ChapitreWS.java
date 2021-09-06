package cm.deepdream.academia.programmation.webservice;
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
import cm.deepdream.academia.programmation.data.Chapitre;
import cm.deepdream.academia.programmation.data.Programme;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.ProgrammeService;
import cm.deepdream.academia.programmation.service.SemestreService;
import cm.deepdream.academia.programmation.service.TrimestreService;
import cm.deepdream.academia.programmation.service.UEService;
import cm.deepdream.academia.programmation.service.ChapitreService;

@RestController
@RequestMapping("/ws/chapitre")
public class ChapitreWS {
	private Logger logger = Logger.getLogger(ChapitreWS.class.getName()) ;
	@Autowired
	private ChapitreService chapitreService ;
	@Autowired
	private ProgrammeService programmeService ;
	@Autowired
	private UEService ueService ;
	@Autowired
	private TrimestreService trimestreService ;
	@Autowired
	private SemestreService semestreService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public Chapitre ajout (@RequestBody  Chapitre chapitre) {
		try {
			Chapitre chapitreCree = chapitreService.creer(chapitre) ;
			return chapitreCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public Chapitre modifier ( @RequestBody Chapitre chapitre) {
		try {
			Chapitre chapitreMaj = chapitreService.modifier(chapitre) ;
			return chapitreMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/etablissement/{idEtablissement}/id/{id}")
	public int suppr (@PathVariable ("idEtablissement") Long idEtablissement, @PathVariable ("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Chapitre chapitre = chapitreService.rechercher(etablissement, id) ;
			chapitreService.supprimer(chapitre) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Chapitre getById (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Chapitre chapitre = chapitreService.rechercher(etablissement, id) ;
			return chapitre  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Chapitre> getByEcole (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Chapitre> liste = chapitreService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Chapitre>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/programme/{idProgramme}")
	public List<Chapitre> getByChapitres (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idProgramme") Long idProgramme) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Programme programme = programmeService.rechercher(etablissement, idProgramme) ;
			List<Chapitre> liste = chapitreService.rechercher(etablissement, programme) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Chapitre>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/programme/{idProgramme}/trimestre/{idTrimestre}")
	public List<Chapitre> getByChapitresTrimestre (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idProgramme") Long idProgramme, @PathVariable ("idTrimestre") Long idTrimestre) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Programme programme = programmeService.rechercher(etablissement, idProgramme) ;
			Trimestre trimestre = trimestreService.rechercher(etablissement, idTrimestre) ;
			List<Chapitre> liste = chapitreService.rechercher(etablissement, programme, trimestre) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Chapitre>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/programme/{idProgramme}/semestre/{idSemestre}")
	public List<Chapitre> getByChapitresSemestre (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idProgramme") Long idProgramme, @PathVariable ("idSemestre") Long idSemestre) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Programme programme = programmeService.rechercher(etablissement, idProgramme) ;
			Semestre semestre = semestreService.rechercher(etablissement, idSemestre) ;
			List<Chapitre> liste = chapitreService.rechercher(etablissement, programme, semestre) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Chapitre>() ;
		}
	}
	
}
