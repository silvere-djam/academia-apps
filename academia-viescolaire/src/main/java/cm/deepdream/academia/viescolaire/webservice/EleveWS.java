package cm.deepdream.academia.viescolaire.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.service.AnneeScolaireService;
import cm.deepdream.academia.viescolaire.service.ClasseService;
import cm.deepdream.academia.viescolaire.service.EleveService;
import cm.deepdream.academia.viescolaire.service.EtablissementService;
@RestController
@RequestMapping("/ws/eleve")
public class EleveWS {
	private Logger logger = Logger.getLogger(EleveWS.class.getName()) ;
	@Autowired
	private EleveService eleveService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	

	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Eleve getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Eleve eleve = eleveService.rechercher(etablissement, id) ;
			return eleve  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Eleve> getByEtablissement (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Eleve> liste = eleveService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Eleve>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}")
	public List<Eleve> getByEleve (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idClasse") Long idClasse) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, idClasse) ;
			List<Eleve> liste = eleveService.rechercher(etablissement, classe) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Eleve>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/situation/{situation}")
	public List<Eleve> getByEleves (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("situation") String situation) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Eleve> liste = eleveService.rechercher(situation, etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Eleve>() ;
		}
	}
	
}
