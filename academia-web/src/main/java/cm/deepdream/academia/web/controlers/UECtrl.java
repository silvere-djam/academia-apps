package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.Programme;
import cm.deepdream.academia.programmation.data.Trimestre;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante", "listeClasses", "classeCourante"})
public class UECtrl implements Serializable{
	private Logger logger = Logger.getLogger(UECtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/ues")
	public String indexUE (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 
		 ResponseEntity<Classe[]> response3 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response3.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 Classe classe = null ;
		 
		 if(listeClasses.size() > 0) {
			 classe = listeClasses.get(0) ;
		 } else {
			 classe = new Classe() ;
		 }
		 
		 model.addAttribute("classeCourante",  classe) ;
		 
		 ResponseEntity<UE[]> response = rest.getForEntity("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/classe/{idClasse}", 
				 UE[].class, etablissement.getId(), classe.getId() == null ? 0L:classe.getId()) ;
		 List<UE> listeUEs = Arrays.asList(response.getBody());
		 model.addAttribute("listeUEs", listeUEs) ;
		 		 
		 UE ue = new UE () ;
		 ue.setClasse(classe);
		 model.addAttribute("ue", ue) ;
		 return "programmation/ues" ;
	}
	
	
	@GetMapping ("/programmation/ues/classe/{idClasse}")
	public String indexUE (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("listeClasses") List<Classe> listeClasses, @PathVariable("idClasse") Long idClasse) throws Exception {
		 
		model.addAttribute("listeClasses", listeClasses) ;
		 
		 Classe classe = rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/id/{id}", 
				 Classe.class, etablissement.getId(), idClasse);
		 
		 model.addAttribute("classeCourante",  classe) ;
		 
		 ResponseEntity<UE[]> response = rest.getForEntity("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/classe/{idClasse}", 
				 UE[].class, etablissement.getId(), classe.getId() == null ? 0L:classe.getId()) ;
		 List<UE> listeUEs = Arrays.asList(response.getBody());
		 model.addAttribute("listeUEs", listeUEs) ;
		 		 
		 UE ue = new UE () ;
		 ue.setClasse(classe);
		 model.addAttribute("ue", ue) ;
		 return "programmation/ues" ;
	}
	
	@PostMapping ("/programmation/ue/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			UE ue) throws Exception {
		 
		 ResponseEntity<Classe[]> response3 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response3.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 Classe classe = ue.getClasse() ;
		 model.addAttribute("classeCourante",  classe) ;
		 
		 ResponseEntity<UE[]> response = rest.getForEntity("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/classe/{idClasse}", 
				 UE[].class, etablissement.getId(), classe == null ? null :classe.getId()) ;
		 List<UE> listeUEs = Arrays.asList(response.getBody());
		 model.addAttribute("listeUEs", listeUEs) ;
		 
		 ue = ue != null ? ue : new UE () ;
		 model.addAttribute("ue", ue) ;
		 return "programmation/ues" ;
	}
	
	@GetMapping ("/programmation/ajout-ue")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("classeCourante") Classe classe) throws Exception {
		UE ue = new UE() ;
		ue.setEtablissement(etablissement);
		ue.setCoefficient(1.0f);
		ue.setCredits(1.0f);
		ue.setClasse(classe);
		model.addAttribute("ue", ue) ;
		initDependencies(model, etablissement);
		return "programmation/ajout-ue" ;
	}
	
	@PostMapping ("/programmation/ue/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			 @SessionAttribute("classeCourante") Classe classe,  UE ue) throws Exception {
		try {
			ue.setCreateur(utilisateurCourant.getEmail());
			ue.setModificateur(utilisateurCourant.getEmail());
			ue.setEtablissement(etablissement) ;
			rest.postForEntity("http://academia-programmation/ws/ue/ajout", ue, UE.class);			
			return "redirect:/programmation/ues/classe/"+classe.getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement);
			model.addAttribute("ue", ue) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-ue" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-ue/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		UE ue = rest.getForObject("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/id/{id}", 
				UE.class, etablissement.getId(),  id) ;
		model.addAttribute("ueExistant", ue) ;
		initDependencies(model, etablissement);
		return "programmation/modification-ue" ;
	}
	
	@PostMapping ("/programmation/ue/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("etablissementCourant") Etablissement etablissement, 
			 @SessionAttribute("classeCourante") Classe classe, UE ueExistant) throws Exception {
		try {
			UE ue = rest.getForObject("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/id/{id}", 
					UE.class, etablissement.getId(), ueExistant.getId()) ;
			ue.setModificateur(utilisateurCourant.getEmail()) ;
			ue.setCode(ueExistant.getCode());
			ue.setDomaine(ueExistant.getDomaine()) ;
			ue.setClasse(ueExistant.getClasse());
			ue.setCoefficient(ueExistant.getCoefficient());
			ue.setCredits(ueExistant.getCredits());
			ue.setNumero(ueExistant.getNumero());
			ue.setGroupe(ueExistant.getGroupe());
			rest.put("http://academia-programmation/ws/ue/modification", ue, UE.class);
			return "redirect:/programmation/ues/classe/"+classe.getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement);
			model.addAttribute("ueExistant", ueExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-ue" ;
		}
	}
	
	
	@GetMapping ("/programmation/edition-programme/{idUE}")
	public String initEditionProg (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@PathVariable("idUE") long idUE) throws Exception {
		UE ue = rest.getForObject("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/id/{id}", 
				UE.class, etablissement.getId(),  idUE) ;
		
		Programme programme = new Programme() ;
		programme.setUe(ue);
		programme.setDureeTheorique(LocalTime.of(0, 0)) ;
		model.addAttribute("programme", programme) ;
		model.addAttribute("anneeScolaire", anneeScolaire) ;
		return "programmation/edition-programme" ;
	}
	
	
	@PostMapping ("/programmation/ue/edition-programme")
	public String editionProgramme (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateur, 
			@SessionAttribute("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			Programme programme) throws Exception {
		try {
			programme.setEtablissement(etablissement);
			programme.setModificateur(utilisateur.getEmail());
			programme.setAnneeScolaire(anneeScolaire);
			programme.setEtablissement(etablissement);
			rest.postForEntity("http://academia-programmation/ws/programme/ajout", programme, Programme.class);
			model.addAttribute("programme", programme) ;
			return "redirect:/programmation/programmes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("programme", programme) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/edition-programme" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-ue/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		UE ue = rest.getForObject("http://academia-programmation/ws/ue/etablissement/{idEtablissement}/id/{id}", 
				UE.class, etablissement.getId(),  id) ;
		model.addAttribute("ueExistante", ue) ;
		return "programmation/details-ue" ;
	}
	
	private void initDependencies (Model model, Etablissement etablissement) {
		ResponseEntity<Domaine[]> response = rest.getForEntity("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}", 
				 Domaine[].class, etablissement.getId()) ;
		 List<Domaine> listeDomaines = Arrays.asList(response.getBody());
		 model.addAttribute("listeDomaines", listeDomaines) ;
		 
		 ResponseEntity<Classe[]> response3 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response3.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 ResponseEntity<Groupe[]> response4 = rest.getForEntity("http://academia-programmation/ws/groupe/etablissement/{idEtablissement}", 
				 Groupe[].class, etablissement.getId()) ;
		 List<Groupe> listeGroupes = Arrays.asList(response4.getBody());
		 model.addAttribute("listeGroupes", listeGroupes) ;
	}
}
