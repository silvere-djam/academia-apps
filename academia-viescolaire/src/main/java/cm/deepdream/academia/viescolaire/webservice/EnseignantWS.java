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
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.service.ClasseService;
import cm.deepdream.academia.viescolaire.service.DomaineService;
import cm.deepdream.academia.viescolaire.service.EnseignantService;
import cm.deepdream.academia.viescolaire.service.EtablissementService;

@RestController
@RequestMapping("/ws/enseignant")
public class EnseignantWS {
	private Logger logger = Logger.getLogger(EnseignantWS.class.getName()) ;
	@Autowired
	private EnseignantService enseignantService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private DomaineService domaineService ;
	@Autowired 
	private ClasseService classeService ;
	

	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Enseignant getEnseignants (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Enseignant enseignant = enseignantService.rechercher(etablissement, id) ;
			return enseignant ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Enseignant getEnseignant (@PathVariable("id") long id) {
		try {
			Enseignant enseignant = enseignantService.rechercher(id) ;
			return enseignant  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Enseignant> getEnseignants (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Enseignant> liste = enseignantService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Enseignant>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/domaine/{idDomaine}")
	public List<Enseignant> getEnseignants2 (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("idDomaine") long idDomaine) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Domaine domaine = domaineService.rechercher(idDomaine) ;
			List<Enseignant> liste = enseignantService.rechercher(etablissement, domaine) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Enseignant>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}")
	public List<Enseignant> getEnseignantsClasse (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("idClasse") long idClasse) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, idClasse) ;
			List<Enseignant> listeEnseignants = enseignantService.rechercher(etablissement, classe) ;
			return listeEnseignants ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Enseignant>() ;
		}
	}
	
}
