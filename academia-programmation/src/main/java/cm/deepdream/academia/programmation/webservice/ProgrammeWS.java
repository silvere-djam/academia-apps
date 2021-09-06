package cm.deepdream.academia.programmation.webservice;
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
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Programme;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.UEService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.ProgrammeService;
@RestController
@RequestMapping("/ws/programme")
public class ProgrammeWS {
	private Logger logger = Logger.getLogger(ProgrammeWS.class.getName()) ;
	@Autowired
	private ProgrammeService programmeService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private UEService disciplineService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	
	@PostMapping("/ajout")
	public Programme ajout (@RequestBody  Programme programme) {
		try {
			Programme programmeCree = programmeService.creer(programme) ;
			return programmeCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public Programme maj ( @RequestBody Programme programme) {
		try {
			Programme programmeMaj = programmeService.modifier(programme) ;
			return programmeMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/etablissement/{idEtablissement}/id/{id}")
	public int suppr (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Programme programme = programmeService.rechercher(etablissement, id) ;
			programmeService.supprimer(programme) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Programme getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Programme programme = programmeService.rechercher(etablissement, id) ;
			return programme  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<Programme> getByEtsAndAnnee (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Programme> liste = programmeService.rechercher(etablissement, anneeScolaire) ;
			return liste  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Programme>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}/ue/{idUE}")
	public Programme getProgramme (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idAnneeScolaire") Long idAnneeScolaire, @PathVariable("idUE") Long idUE) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			UE discipline = disciplineService.rechercher(etablissement, idUE) ;
			Programme programme = programmeService.rechercher(etablissement, anneeScolaire, discipline) ;
			return programme  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Programme> getByEts (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Programme> liste = programmeService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Programme>() ;
		}
	}
	
}
