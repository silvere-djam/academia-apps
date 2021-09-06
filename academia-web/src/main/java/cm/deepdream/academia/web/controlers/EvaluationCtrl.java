package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.UE;

@Controller
@SessionAttributes({"utilisateurCourant", "anneeScolaireCourante", "etablissementCourant", "classeCourante", 
	"listeAnneesScolaires", "listeClasses", "classeCourante"})
public class EvaluationCtrl implements Serializable{
	private Logger logger = Logger.getLogger(EvaluationCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/viescolaire/evaluation/classe/{idClasse}")
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
		
		Evaluation evaluation = new Evaluation() ;
		evaluation.setEtablissement(etablissement);
		evaluation.setClasse(classe);
		evaluation.setAnneeScolaire(anneeScolaire);
		evaluation.setDate(LocalDate.now());
		evaluation.setHeureDebut(LocalTime.now());
		
		model.addAttribute("evaluation", evaluation) ;
		
		model.addAttribute("classeCourante", classe) ;
		
		ResponseEntity<AnneeScolaire[]> response1 = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", AnneeScolaire[].class, etablissement.getId()) ;
		List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response1.getBody());
		model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 
		ResponseEntity<Evaluation[]> response2 = rest.getForEntity("http://academia-viescolaire/ws/evaluation/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}",  
				Evaluation[].class, etablissement.getId(), classe == null || classe.getId() == null ? 0L:classe.getId() , anneeScolaire.getId()) ;
		List<Evaluation> listeEvaluations = Arrays.asList(response2.getBody());
		model.addAttribute("listeEvaluations", listeEvaluations) ;
		return "viescolaire/evaluations" ;
	}
	
	
	@PostMapping ("/viescolaire/recherche-evaluation-classe")
	public String rechercher (Model model, Evaluation evaluation, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute("listeClasses") List<Classe> listeClasses,
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute("listeAnneesScolaires")  List<AnneeScolaire> listeAnneesScolaires) throws Exception {

		model.addAttribute("listeClasses", listeClasses) ;
		
		Classe classe = rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/id/{id}", 
				Classe.class, etablissement.getId(), evaluation.getClasse().getId()) ;
		model.addAttribute("classeCourante", classe) ;
		model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;

		evaluation.setEtablissement(etablissement);
		evaluation.setClasse(classe);
		model.addAttribute("evaluation", evaluation) ;
		 
		ResponseEntity<Evaluation[]> response2 = rest.getForEntity("http://academia-viescolaire/ws/evaluation/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}",  
				Evaluation[].class, etablissement.getId(), evaluation.getClasse().getId() , evaluation.getAnneeScolaire().getId()) ;
		List<Evaluation> listeEvaluation = Arrays.asList(response2.getBody());
		model.addAttribute("listeEvaluations", listeEvaluation) ;
		return "viescolaire/evaluations" ;
	}
	
	
	@GetMapping ("/viescolaire/ajout-evaluation")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			@SessionAttribute ("classeCourante") Classe classe, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateur) throws Exception {
		Evaluation evaluation = new Evaluation() ;
		evaluation.setAnneeScolaire(anneeScolaire);
		evaluation.setClasse(classe);
		evaluation.setDate(LocalDate.now());
		evaluation.setHeureDebut(LocalTime.now());
		model.addAttribute("evaluation", evaluation) ;
		initDependencies (model, etablissement, classe) ;
		return "viescolaire/ajout-evaluation" ;
	}
	
	@PostMapping ("/viescolaire/evaluation/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			 @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			 @SessionAttribute ("classeCourante") Classe classe, Evaluation evaluation) throws Exception {
		try {
			logger.info("Ajout du evaluation "+evaluation);
			evaluation.setCreateur(utilisateurCourant.getEmail());
			evaluation.setModificateur(utilisateurCourant.getEmail());
			evaluation.setEtablissement(etablissement) ;
			evaluation.setAnneeScolaire(anneeScolaire);
			rest.postForEntity("http://academia-viescolaire/ws/evaluation/ajout", evaluation, Evaluation.class);			
			return "redirect:/viescolaire/evaluation/classe/"+classe.getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model, etablissement, classe) ;
			model.addAttribute("evaluation", evaluation) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "viescolaire/ajout-evaluation" ;
		}
	}
	
	
	
	@GetMapping ("/viescolaire/modification-evaluation/{id}")
	public String initMaj (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("classeCourante") Classe classe,
			@PathVariable("id") long id) throws Exception {
		Evaluation evaluation = rest.getForObject("http://academia-viescolaire/ws/evaluation/etablissement/{idEtablissement}/id/{id}", Evaluation.class, etablissement.getId(), id) ;
		model.addAttribute("evaluationExistante", evaluation) ;
		initDependencies (model, etablissement, classe) ;
		return "viescolaire/modification-evaluation" ;
	}
	
	@PostMapping ("/viescolaire/evaluation/modification")
	public String maj (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute ("classeCourante") Classe classe,
			Evaluation evaluationExistante) throws Exception {
		try {
			Evaluation evaluation = rest.getForObject("http://academia-viescolaire/ws/evaluation/etablissement/{idEtablissement}/id/{id}", 
					Evaluation.class, etablissement.getId(), evaluationExistante.getId()) ;
			evaluation.setModificateur(utilisateurCourant.getEmail()) ;
			evaluation.setUe(evaluationExistante.getUe());
			evaluation.setEnseignant(evaluationExistante.getEnseignant());
			evaluation.setClasse(evaluationExistante.getClasse());
			evaluation.setDate(evaluationExistante.getDate());
			evaluation.setHeureDebut(evaluationExistante.getHeureDebut());
			evaluation.setHeureFin(evaluationExistante.getHeureFin());
			rest.put("http://academia-viescolaire/ws/evaluation/modification", evaluation, Evaluation.class);
			return "redirect:/viescolaire/evaluation/classe/"+classe.getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model, etablissement, classe) ;
			model.addAttribute("evaluationExistante", evaluationExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "viescolaire/modification-evaluation" ;
		}
	}
	
	
	@GetMapping ("/viescolaire/details-evaluation/{idEvaluation}")
	public String initDetails (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("classeCourante") Classe classe,
			@PathVariable("idEvaluation") Long idEvaluation) throws Exception {
		 Evaluation evaluation = rest.getForObject("http://academia-viescolaire/ws/evaluation/etablissement/{idEtablissement}/id/{id}", 
				Evaluation.class, etablissement.getId(), idEvaluation) ;
		 model.addAttribute("evaluationExistante", evaluation) ;
		
		 //ResponseEntity<Absence[]> response0 = rest.getForEntity("http://academia-viescolaire/ws/absence/etablissement/{idEtablissement}/evaluation/{id}", 
		 //		 Absence[].class, etablissement.getId(), idEvaluation) ;
		 //List<Absence> listeAbsences = Arrays.asList(response0.getBody());
		 //model.addAttribute("listeAbsences", listeAbsences) ;
		
		 return "viescolaire/details-evaluation" ;
	}
	
	
	
	private void initDependencies (Model model, Etablissement etablissement, Classe classe) {
		 ResponseEntity<Classe[]> response0 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response0.getBody()).stream().distinct().collect(Collectors.toList());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 ResponseEntity<UE[]> response1 = rest.getForEntity("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/classe/{idClasse}", 
				 UE[].class, etablissement.getId(), classe.getId()) ;
		 List<UE> listeUEs = Arrays.asList(response1.getBody());
		 model.addAttribute("listeUEs", listeUEs) ;
		 
		 ResponseEntity<Enseignant[]> response2 = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Enseignant[].class, etablissement.getId(), classe.getId()) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response2.getBody());
		 model.addAttribute("listeEnseignants", listeEnseignants) ;
	}
	
}
