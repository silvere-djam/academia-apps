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
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Filiere;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.Trimestre;
@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class ClasseCtrl implements Serializable{
	private Logger logger = Logger.getLogger(ClasseCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/classes")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Classe[]> response = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}",  Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 return "programmation/classes" ;
	}
	
	@GetMapping ("/programmation/ajout-classe")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Classe classe = new Classe() ;
		model.addAttribute("classe", classe) ;
		classe.setNiveau(new Niveau());
		initDependencies (model, etablissement) ;
		return "programmation/ajout-classe" ;
	}
	
	@PostMapping ("/programmation/classe/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  Classe classe) throws Exception {
		try {
			classe.setCreateur(utilisateurCourant.getEmail());
			classe.setModificateur(utilisateurCourant.getEmail());
			classe.setEtablissement(etablissement) ;
			rest.postForEntity("http://academia-programmation/ws/classe/ajout", classe, Classe.class);			
			return "redirect:/programmation/classes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model, etablissement) ;
			model.addAttribute("classe", classe) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-classe" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-classe/{id}")
	public String initMaj (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		Classe classe = rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/id/{id}", Classe.class, etablissement.getId(), id) ;
		model.addAttribute("classeExistante", classe) ;
		initDependencies (model, etablissement) ;
		return "programmation/modification-classe" ;
	}
	
	@PostMapping ("/programmation/classe/modification")
	public String maj (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Classe classeExistante) throws Exception {
		try {
			Classe classe = rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/id/{id}", 
					Classe.class, etablissement.getId(), classeExistante.getId()) ;
			classe.setModificateur(utilisateurCourant.getEmail()) ;
			classe.setLibelle(classeExistante.getLibelle()) ;
			classe.setAbreviation(classeExistante.getAbreviation()) ;
			classe.setFiliere(classeExistante.getFiliere());
			classe.setNiveau(classeExistante.getNiveau());
			rest.put("http://academia-programmation/ws/classe/modification", classe, Classe.class);
			return "redirect:/programmation/classes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model, etablissement) ;
			model.addAttribute("classeExistante", classeExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-classe" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-classe/{id}")
	public String initDetails (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		Classe classe = rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/id/{id}", 
				Classe.class, etablissement.getId(), id) ;
		model.addAttribute("classeExistante", classe) ;
		initDependencies (model, etablissement) ;
		return "programmation/details-classe" ;
	}
	
	
	@PostMapping ("/programmation/classe/suppr")
	public String supprimer (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, Classe classeExistante) throws Exception {
		try {
			classeExistante = rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/id/{id}", 
					Classe.class, etablissement.getId(), classeExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", classeExistante.getId()) ;
			uriVariables.put("idEtablissement", etablissement.getId()) ;
			rest.delete("http://academia-programmation/ws/classe/suppr/etablissement/{idEtablissement}/id/{id}", 
					uriVariables);
			return "redirect:/programmation/classes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("classeExistante", classeExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/details-classe" ;
		}
	}
	
	
	private void initDependencies (Model model, Etablissement etablissement) {
		ResponseEntity<Niveau[]> response = rest.getForEntity("http://academia-programmation/ws/niveau/etablissement/{idEtablissement}", 
				 Niveau[].class, etablissement.getId()) ;
		 List<Niveau> listeNiveaux = Arrays.asList(response.getBody());
		 model.addAttribute("listeNiveaux", listeNiveaux) ;
		 ResponseEntity<Filiere[]> response2 = rest.getForEntity("http://academia-programmation/ws/filiere/etablissement/{idEtablissement}", 
				 Filiere[].class, etablissement.getId()) ;
		 List<Filiere> listeFilierex = Arrays.asList(response2.getBody());
		 model.addAttribute("listeFilieres", listeFilierex) ;
	}
	
}
