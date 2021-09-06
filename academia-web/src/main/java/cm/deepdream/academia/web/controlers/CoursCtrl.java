package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.viescolaire.data.Absence;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.UE;

@Controller
@SessionAttributes({"utilisateurCourant", "anneeScolaireCourante", "etablissementCourant", "classeCourante", 
	"listeAnneesScolaires", "listeClasses"})
public class CoursCtrl implements Serializable{
	private Logger logger = Logger.getLogger(CoursCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/viescolaire/cours/classe/{idClasse}")
	public String index (Model model, @PathVariable("idClasse") Long idClasse,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		ResponseEntity<Classe[]> response0 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		List<Classe> listeClasses = Arrays.asList(response0.getBody());
		model.addAttribute("listeClasses", listeClasses) ;
		
		Classe classe = null ;
		
		if(idClasse == 0L) {
			classe = listeClasses.size() == 0 ? new Classe() : listeClasses.get(0) ;
		} else {
			classe = rest.getForObject("http://academia-viescolaire/ws/classe/etablissement/{idEtablissement}/id/{id}", 
					Classe.class, etablissement.getId(), idClasse) ;
		}
		
		Cours cours = new Cours() ;
		cours.setEtablissement(etablissement);
		cours.setClasse(classe);
		cours.setAnneeScolaire(anneeScolaire);
		
		model.addAttribute("cours", cours) ;
		
		model.addAttribute("classeCourante", classe) ;
		
		ResponseEntity<AnneeScolaire[]> response1 = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", AnneeScolaire[].class, etablissement.getId()) ;
		List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response1.getBody());
		model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 
		ResponseEntity<Cours[]> response2 = rest.getForEntity("http://academia-viescolaire/ws/cours/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}",  
				Cours[].class, etablissement.getId(), classe == null || classe.getId() == null ? 0L:classe.getId() , anneeScolaire.getId()) ;
		List<Cours> listeCours = Arrays.asList(response2.getBody());
		model.addAttribute("listeCours", listeCours) ;
		return "viescolaire/cours" ;
	}
	
	
	
	@PostMapping ("/viescolaire/recherche-cours-classe")
	public String rechercher (Model model, Cours cours, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute("listeClasses") List<Classe> listeClasses,
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute("listeAnneesScolaires")  List<AnneeScolaire> listeAnneesScolaires) throws Exception {

		model.addAttribute("listeClasses", listeClasses) ;
		
		Classe classe = rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/id/{id}", 
				Classe.class, etablissement.getId(), cours.getClasse().getId()) ;
		model.addAttribute("classeCourante", classe) ;
		model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;

		cours.setEtablissement(etablissement);
		cours.setClasse(classe);
		model.addAttribute("cours", cours) ;
		 
		ResponseEntity<Cours[]> response2 = rest.getForEntity("http://academia-viescolaire/ws/cours/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}",  
				Cours[].class, etablissement.getId(), cours.getClasse().getId() , cours.getAnneeScolaire().getId()) ;
		List<Cours> listeCours = Arrays.asList(response2.getBody());
		model.addAttribute("listeCours", listeCours) ;
		return "viescolaire/cours" ;
	}
	
	
	@GetMapping ("/viescolaire/ajout-cours")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			@SessionAttribute ("classeCourante") Classe classe, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateur) throws Exception {
		Cours cours = new Cours() ;
		cours.setAnneeScolaire(anneeScolaire);
		cours.setClasse(classe);
		cours.setDate(LocalDate.now());
		cours.setHeureDebut(LocalTime.now());
		model.addAttribute("cours", cours) ;
		initDependencies (model, etablissement, classe) ;
		return "viescolaire/ajout-cours" ;
	}
	
	@PostMapping ("/viescolaire/cours/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			 @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			 @SessionAttribute ("classeCourante") Classe classe, Cours cours) throws Exception {
		try {
			logger.info("Ajout du cours "+cours);
			cours.setCreateur(utilisateurCourant.getEmail());
			cours.setModificateur(utilisateurCourant.getEmail());
			cours.setEtablissement(etablissement) ;
			cours.setAnneeScolaire(anneeScolaire);
			rest.postForEntity("http://academia-viescolaire/ws/cours/ajout", cours, Cours.class);			
			return "redirect:/viescolaire/cours/classe/"+classe.getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model, etablissement, classe) ;
			model.addAttribute("cours", cours) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "viescolaire/ajout-cours" ;
		}
	}
	
	
	@GetMapping ("/viescolaire/modification-cours/{id}")
	public String initMaj (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("classeCourante") Classe classe,
			@PathVariable("id") long id) throws Exception {
		Cours cours = rest.getForObject("http://academia-viescolaire/ws/cours/etablissement/{idEtablissement}/id/{id}", Cours.class, etablissement.getId(), id) ;
		model.addAttribute("coursExistant", cours) ;
		initDependencies (model, etablissement, classe) ;
		return "viescolaire/modification-cours" ;
	}
	
	@PostMapping ("/viescolaire/cours/modification")
	public String maj (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,  Cours coursExistant) throws Exception {
		try {
			Cours cours = rest.getForObject("http://academia-viescolaire/ws/cours/etablissement/{idEtablissement}/id/{id}", 
					Cours.class, etablissement.getId(), coursExistant.getId()) ;
			cours.setModificateur(utilisateurCourant.getEmail()) ;
			cours.setUe(coursExistant.getUe());
			cours.setEnseignant(coursExistant.getEnseignant());
			cours.setClasse(coursExistant.getClasse());
			cours.setDate(coursExistant.getDate());
			cours.setHeureDebut(coursExistant.getHeureDebut());
			cours.setHeureFin(coursExistant.getHeureFin());
			rest.postForObject("http://academia-viescolaire/ws/cours/modification", cours, Cours.class);
			return "redirect:/viescolaire/cours/classe/"+coursExistant.getClasse().getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model, etablissement, coursExistant.getClasse()) ;
			model.addAttribute("coursExistant", coursExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "viescolaire/modification-cours" ;
		}
	}
	
	
	@GetMapping ("/viescolaire/details-cours/{idCours}")
	public String initDetails (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("classeCourante") Classe classe, @PathVariable("idCours") Long idCours) throws Exception {
		 Cours cours = rest.getForObject("http://academia-viescolaire/ws/cours/etablissement/{idEtablissement}/id/{id}", 
				Cours.class, etablissement.getId(), idCours) ;
		 model.addAttribute("coursExistant", cours) ;
		
		 ResponseEntity<Absence[]> response0 = rest.getForEntity("http://academia-viescolaire/ws/absence/etablissement/{idEtablissement}/cours/{id}", 
				 Absence[].class, etablissement.getId(), idCours) ;
		 List<Absence> listeAbsences = Arrays.asList(response0.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		
		 return "viescolaire/details-cours" ;
	}
	
	
	
	private void initDependencies (Model model, Etablissement etablissement, Classe classe) {
		 ResponseEntity<Enseignant[]> response2 = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Enseignant[].class, etablissement.getId(), classe.getId()) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response2.getBody());
		 model.addAttribute("listeEnseignants", listeEnseignants) ;
		 
		 ResponseEntity<UE[]> response1 = rest.getForEntity("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/classe/{idClasse}", 
				 UE[].class, etablissement.getId(), classe.getId()) ;
		 List<UE> listeUEs = Arrays.asList(response1.getBody());
		 model.addAttribute("listeUEs", listeUEs) ;
		
	}
	
}
