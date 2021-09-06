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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.security.data.DetailPerimetre;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "listeUtilisateurs", "listeClasses"})
public class PerimetreCtrl implements Serializable{
	private Logger logger = Logger.getLogger(PerimetreCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/administration/perimetre")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		ResponseEntity<Utilisateur[]> response0 = rest.getForEntity("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}", 
				 Utilisateur[].class, etablissement.getId()) ;
		 List<Utilisateur> listeUtilisateurs = Arrays.asList(response0.getBody());
		 
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ; 
		 
		 Utilisateur utilisateur = listeUtilisateurs.size() == 0 ? new Utilisateur():listeUtilisateurs.get(0) ;
		 
		 ResponseEntity<DetailPerimetre[]> response1 = rest.getForEntity("http://academia-security/ws/perimetre/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}", 
				 DetailPerimetre[].class, etablissement.getId(), utilisateur.getId()==null ? 0L:utilisateur.getId()) ;
		 
		 List<DetailPerimetre> listeDetailsPerimetre = Arrays.asList(response1.getBody());
		 
		 model.addAttribute("listeDetailsPerimetre", listeDetailsPerimetre) ;
		 
		 DetailPerimetre detailPerimetre = new DetailPerimetre() ;
		 detailPerimetre.setUtilisateur(utilisateur);
		 model.addAttribute("detailPerimetre", detailPerimetre) ;
		 initDependencies(model, etablissement);
		 return "administration/perimetre" ;
	}
	
	
	@PostMapping ("/administration/perimetre/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("listeClasses") List<Classe> listeClasses, 
			@SessionAttribute ("listeUtilisateurs") List<Utilisateur> listeUtilisateurs,
		 DetailPerimetre detailPerimetre) throws Exception {

		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ; 
		 model.addAttribute("listeClasses", listeClasses) ; 
		 
		 Utilisateur utilisateur = detailPerimetre.getUtilisateur() ;
		 
		 ResponseEntity<DetailPerimetre[]> response1 = rest.getForEntity("http://academia-security/ws/perimetre/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}", 
				 DetailPerimetre[].class, etablissement.getId(), utilisateur.getId()==null ? 0L:utilisateur.getId()) ;
		 
		 List<DetailPerimetre> listeDetailsPerimetre = Arrays.asList(response1.getBody());
		 
		 model.addAttribute("listeDetailsPerimetre", listeDetailsPerimetre) ;
		 
		 detailPerimetre.setUtilisateur(utilisateur);
		 model.addAttribute("detailPerimetre", detailPerimetre) ;
		 return "administration/perimetre" ;
	}
	
	
	
	@GetMapping ("/administration/ajout-detailperimetre")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute ("listeClasses") List<Classe> listeClasses, 
			@SessionAttribute ("listeUtilisateurs") List<Utilisateur> listeUtilisateurs) throws Exception {
		DetailPerimetre detailPerimetre = new DetailPerimetre() ;
		detailPerimetre.setEtablissement(etablissement);
		model.addAttribute("detailPerimetre", detailPerimetre) ;
		model.addAttribute("listeUtilisateurs", listeUtilisateurs) ; 
		model.addAttribute("listeClasses", listeClasses) ; 
		return "administration/ajout-detailperimetre" ;
	}
	
	@PostMapping ("/administration/perimetre/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissementCourant,  DetailPerimetre detailPerimetre) throws Exception {
		try {
			detailPerimetre.setCreateur(utilisateurCourant.getEmail());
			detailPerimetre.setModificateur(utilisateurCourant.getEmail());
			detailPerimetre.setEtablissement(etablissementCourant) ;
			rest.postForEntity("http://academia-security/ws/perimetre/ajout", detailPerimetre, DetailPerimetre.class);			
			return "redirect:/administration/perimetre" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("detailPerimetre", detailPerimetre) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "administration/ajout-detailperimetre" ;
		}
	}
	
	
	@GetMapping ("/administration/maj-detailperimetre/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, @SessionAttribute("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("listeClasses") List<Classe> listeClasses, 
			@SessionAttribute ("listeUtilisateurs") List<Utilisateur> listeUtilisateurs) throws Exception {
		DetailPerimetre detailPerimetre = rest.getForObject("http://academia-security/ws/perimetre/etablissement/{idEtablissement}/id/{id}", 
				DetailPerimetre.class, etablissement.getId(),  id) ;
		model.addAttribute("detailPerimetreExistant", detailPerimetre) ;
		model.addAttribute("listeUtilisateurs", listeUtilisateurs) ; 
		model.addAttribute("listeClasses", listeClasses) ; 
		return "administration/maj-detailperimetre" ;
	}
	
	
	
	@PostMapping ("/administration/perimetre/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("etablissementCourant") Etablissement etablissement, 
			DetailPerimetre detailPerimetreExistant) throws Exception {
		try {
			DetailPerimetre detailPerimetre = rest.getForObject("http://academia-security/ws/perimetre/etablissement/{idEtablissement}/id/{id}", 
					DetailPerimetre.class, etablissement.getId(), detailPerimetreExistant.getId()) ;
			detailPerimetre.setModificateur(utilisateurCourant.getEmail()) ;
			detailPerimetre.setClasse(detailPerimetreExistant.getClasse());
			detailPerimetre.setUtilisateur(detailPerimetreExistant.getUtilisateur());
			rest.put("http://academia-security/ws/perimetre/maj", detailPerimetre, DetailPerimetre.class);
			return "redirect:/administration/perimetre" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("detailPerimetreExistant", detailPerimetreExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "administration/maj-detailperimetre" ;
		}
	}
	
	
	@GetMapping ("/administration/details-detailperimetre/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		DetailPerimetre detailPerimetre = rest.getForObject("http://academia-security/ws/perimetre/etablissement/{idEtablissement}/id/{id}", 
				DetailPerimetre.class, etablissement.getId(),  id) ;
		model.addAttribute("detailPerimetreExistant", detailPerimetre) ;
		return "administration/details-detailperimetre" ;
	}
	
	
	private void initDependencies (Model model, Etablissement etablissement) {
		 ResponseEntity<Classe[]> response0 = rest.getForEntity("http://academia-security/ws/classe/etablissement/{idEtablissement}",  
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response0.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 ResponseEntity<Utilisateur[]> response1 = rest.getForEntity("http://academia-security/ws/utilisateur//etablissement/{idEtablissement}", 
				  Utilisateur[].class, etablissement.getId()) ;
		 List<Utilisateur> listeUtilisateurs = Arrays.asList(response1.getBody());
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
	}
	
		
}
