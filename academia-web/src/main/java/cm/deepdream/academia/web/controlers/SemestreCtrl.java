package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
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
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante"})
public class SemestreCtrl implements Serializable{
	private Logger logger = Logger.getLogger(SemestreCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/semestres")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			 @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		 ResponseEntity<Semestre[]> response = rest.getForEntity("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Semestre[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Semestre> listeSemestres = Arrays.asList(response.getBody());
		 model.addAttribute("listeSemestres", listeSemestres) ;
		 Semestre semestre = new Semestre () ;
		 semestre.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("semestre", semestre) ;
		 model.addAttribute("anneeScolaire", anneeScolaire) ;
		 initDependencies (model, etablissement) ;
		 return "programmation/semestres" ;
	}
	
	@PostMapping ("/programmation/semestre/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			  Semestre semestre) throws Exception {
		AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", 
				 AnneeScolaire.class, etablissement.getId(), semestre.getAnneeScolaire().getId()) ;
		
		 ResponseEntity<Semestre[]> response = rest.getForEntity("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Semestre[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Semestre> listeSemestrex = Arrays.asList(response.getBody());
		 model.addAttribute("listeSemestres", listeSemestrex) ;
		 model.addAttribute("anneeScolaire", anneeScolaire) ;
		 semestre = new Semestre () ;
		 semestre.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("semestre", semestre) ;
		 initDependencies (model, etablissement) ;
		 return "programmation/semestres" ;
	}
	
	@GetMapping ("/programmation/ajout-semestre")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Semestre semestre = new Semestre() ;
		semestre.setEtablissement(etablissement);
		model.addAttribute("semestre", semestre) ;
		return "programmation/ajout-semestre" ;
	}
	
	@PostMapping ("/programmation/semestre/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissementCourant,  
			 @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			 Semestre semestre) throws Exception {
		try {
			semestre.setCreateur(utilisateurCourant.getEmail());
			semestre.setModificateur(utilisateurCourant.getEmail());
			semestre.setAnneeScolaire(anneeScolaire);
			semestre.setEtablissement(etablissementCourant) ;
			rest.postForEntity("http://academia-programmation/ws/semestre/ajout", semestre, Semestre.class);			
			return "redirect:/programmation/semestres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("semestre", semestre) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-semestre" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-semestre/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Semestre semestre = rest.getForObject("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/id/{id}", 
				Semestre.class, etablissement.getId(),  id) ;
		model.addAttribute("semestreExistant", semestre) ;
		return "programmation/modification-semestre" ;
	}
	
	@PostMapping ("/programmation/semestre/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			Semestre semestreExistant) throws Exception {
		try {
			Semestre semestre = rest.getForObject("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/id/{id}", 
					Semestre.class, etablissement.getId(), semestreExistant.getId()) ;
			semestre.setModificateur(utilisateurCourant.getEmail()) ;
			semestre.setLibelle(semestreExistant.getLibelle()) ;
			semestre.setNumero(semestreExistant.getNumero()) ;
			semestre.setDateDebut(semestreExistant.getDateDebut());
			semestre.setDateFin(semestreExistant.getDateFin()) ;
			rest.put("http://academia-programmation/ws/semestre/modification", semestre, Semestre.class);
			return "redirect:/programmation/semestres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("semestreExistant", semestreExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-semestre" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-semestre/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Semestre semestre = rest.getForObject("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/id/{id}", 
				Semestre.class, etablissement.getId(),  id) ;
		model.addAttribute("semestreExistant", semestre) ;
		return "programmation/details-semestre" ;
	}
	
	private void initDependencies (Model model, Etablissement etablissement) {
		ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				AnneeScolaire[].class, etablissement.getId()) ;
		List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
	}
		
}
