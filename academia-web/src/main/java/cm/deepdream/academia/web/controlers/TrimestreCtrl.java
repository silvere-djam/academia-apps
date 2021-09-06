package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante"})
public class TrimestreCtrl implements Serializable{
	private Logger logger = Logger.getLogger(TrimestreCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/trimestres")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			 @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		 ResponseEntity<Trimestre[]> response = rest.getForEntity("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Trimestre[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Trimestre> listeTrimestrex = Arrays.asList(response.getBody());
		 model.addAttribute("listeTrimestres", listeTrimestrex) ;
		 Trimestre trimestre = new Trimestre () ;
		 trimestre.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("trimestre", trimestre) ;
		 model.addAttribute("anneeScolaire", anneeScolaire) ;
		 initDependencies (model, etablissement) ;
		 return "programmation/trimestres" ;
	}
	
	@PostMapping ("/programmation/trimestre/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			  Trimestre trimestre) throws Exception {
		AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", 
				 AnneeScolaire.class, etablissement.getId(), trimestre.getAnneeScolaire().getId()) ;
		
		 ResponseEntity<Trimestre[]> response = rest.getForEntity("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Trimestre[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Trimestre> listeTrimestrex = Arrays.asList(response.getBody());
		 model.addAttribute("listeTrimestres", listeTrimestrex) ;
		 model.addAttribute("anneeScolaire", anneeScolaire) ;
		 trimestre = new Trimestre () ;
		 trimestre.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("trimestre", trimestre) ;
		 initDependencies (model, etablissement) ;
		 return "programmation/trimestres" ;
	}
	
	@GetMapping ("/programmation/ajout-trimestre")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Trimestre trimestre = new Trimestre() ;
		trimestre.setEtablissement(etablissement);
		model.addAttribute("trimestre", trimestre) ;
		return "programmation/ajout-trimestre" ;
	}
	
	@PostMapping ("/programmation/trimestre/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissementCourant,  
			 @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			 Trimestre trimestre) throws Exception {
		try {
			trimestre.setCreateur(utilisateurCourant.getEmail());
			trimestre.setModificateur(utilisateurCourant.getEmail());
			trimestre.setAnneeScolaire(anneeScolaire);
			trimestre.setEtablissement(etablissementCourant) ;
			rest.postForEntity("http://academia-programmation/ws/trimestre/ajout", trimestre, Trimestre.class);			
			return "redirect:/programmation/trimestres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("trimestre", trimestre) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-trimestre" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-trimestre/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Trimestre trimestre = rest.getForObject("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/id/{id}", 
				Trimestre.class, etablissement.getId(),  id) ;
		model.addAttribute("trimestreExistant", trimestre) ;
		return "programmation/modification-trimestre" ;
	}
	
	@PostMapping ("/programmation/trimestre/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			Trimestre trimestreExistant) throws Exception {
		try {
			Trimestre trimestre = rest.getForObject("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/id/{id}", 
					Trimestre.class, etablissement.getId(), trimestreExistant.getId()) ;
			trimestre.setModificateur(utilisateurCourant.getEmail()) ;
			trimestre.setLibelle(trimestreExistant.getLibelle()) ;
			trimestre.setNumero(trimestreExistant.getNumero()) ;
			trimestre.setDateDebut(trimestreExistant.getDateDebut());
			trimestre.setDateFin(trimestreExistant.getDateFin()) ;
			rest.put("http://academia-programmation/ws/trimestre/modification", trimestre, Trimestre.class);
			return "redirect:/programmation/trimestres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("trimestreExistant", trimestreExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-trimestre" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-trimestre/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Trimestre trimestre = rest.getForObject("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/id/{id}", 
				Trimestre.class, etablissement.getId(),  id) ;
		model.addAttribute("trimestreExistant", trimestre) ;
		return "programmation/details-trimestre" ;
	}
	
	
	@PostMapping ("/programmation/trimestre/definition-defaut")
	public String definirDefaut (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, Trimestre trimestreExistant) throws Exception {
		try {
			Trimestre trimestre = rest.getForObject("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/id/{id}", 
					Trimestre.class, etablissement.getId(), trimestreExistant.getId()) ;
			rest.put("http://academia-programmation/ws/trimestre/definition-defaut", trimestre, Trimestre.class);
			return "redirect:/programmation/trimestres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("trimestreExistant", trimestreExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return null ;
		}
	}
	
	
	@PostMapping ("/programmation/trimestre/suppr")
	public String supprimer (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, Trimestre trimestreExistant) throws Exception {
		try {
			trimestreExistant = rest.getForObject("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/id/{id}", 
					Trimestre.class, etablissement.getId(), trimestreExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", trimestreExistant.getId()) ;
			uriVariables.put("idEtablissement", etablissement.getId()) ;
			rest.delete("http://academia-programmation/ws/trimestre/suppr/etablissement/{idEtablissement}/id/{id}", 
					uriVariables);
			return "redirect:/programmation/trimestres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("trimestreExistant", trimestreExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/details-trimestre" ;
		}
	}
	
	
	private void initDependencies (Model model, Etablissement etablissement) {
		ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				AnneeScolaire[].class, etablissement.getId()) ;
		List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
	}
		
}
