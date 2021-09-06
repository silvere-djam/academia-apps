package cm.deepdream.academia.programmation.webservice;
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
import cm.deepdream.academia.programmation.service.IndicateurService;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Indicateur;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.ClasseService;
import cm.deepdream.academia.programmation.service.UEService;
import cm.deepdream.academia.programmation.service.EtablissementService;
@RestController
@RequestMapping("/ws/indicateur")
public class IndicateurWS {
	private Logger logger = Logger.getLogger(IndicateurWS.class.getName()) ;
	@Autowired
	private IndicateurService indicateurService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private UEService disciplineService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	

	
	@PostMapping("/ajout")
	public Indicateur ajout (@RequestBody  Indicateur indicateur) {
		try {
			Indicateur indicateurCree = indicateurService.creer(indicateur) ;
			return indicateurCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Indicateur maj ( @RequestBody Indicateur indicateur) {
		try {
			Indicateur indicateurMaj = indicateurService.modifier(indicateur) ;
			return indicateurMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody Indicateur indicateur) {
		try {
			indicateurService.supprimer(indicateur) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Indicateur getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Indicateur indicateur = indicateurService.rechercher(etablissement, id) ;
			return indicateur  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}")
	public List<Indicateur> getByIndicateurs1 (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("idClasse") long idClasse,
			@PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, idClasse) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Indicateur> liste = indicateurService.rechercher(etablissement, classe, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Indicateur>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/discipline/{idDiscipline}/anneescolaire/{idAnneeScolaire}")
	public List<Indicateur> getByIndicateurs2 (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("idDiscipline") long idDiscipline,
			@PathVariable("idAnneeScolaire") long idAnneeScolaire) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			UE ue = disciplineService.rechercher(etablissement, idDiscipline) ;
			AnneeScolaire anneeScolaire = anneeScolaireService.rechercher(etablissement, idAnneeScolaire) ;
			List<Indicateur> liste = indicateurService.rechercher(etablissement, ue, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Indicateur>() ;
		}
	}
	
}
