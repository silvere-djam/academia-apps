package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class UtilisateurCtrl implements Serializable{
	private Logger logger = Logger.getLogger(UtilisateurCtrl.class.getName()) ;
	@Autowired
	private AuthenticationManager authenticationManager ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	@Autowired
	private PasswordEncoder passwordEncoder ;
	
	@GetMapping ("/administration/utilisateurs")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Utilisateur[]> response = rest.getForEntity("http://academia-security/ws/utilisateur//etablissement/{idEtablissement}", 
				  Utilisateur[].class, etablissement.getId()) ;
		 List<Utilisateur> listeUtilisateurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
		 return "administration/utilisateurs" ;
	}
	

	@GetMapping ("/administration/ajout-utilisateur")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Utilisateur utilisateur = new Utilisateur() ;
		utilisateur.setDateCreation(LocalDateTime.now()) ;
		utilisateur.setDateDernMaj(LocalDateTime.now()) ;
		utilisateur.setCreateur(utilisateurCourant.getEmail());
		utilisateur.setModificateur(utilisateurCourant.getEmail());
		utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(env.getProperty("app.security.default_password"))) ;
		utilisateur.setDateExpMdp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.password_validity_period")))) ;
		utilisateur.setDateExp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.account_validity_period")))) ;
		//utilisateur.setStatut(StatutU.Suspendu.toString()) ;
		model.addAttribute("utilisateur", utilisateur) ;
		model.addAttribute("etablissementCourant", etablissement) ;
		initDependencies(model, etablissement) ;
		return "administration/ajout-utilisateur" ;
	}
	
	
	
	@PostMapping ("/administration/utilisateur/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissementCourant,  Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setCreateur(utilisateurCourant.getEmail());
			utilisateur.setModificateur(utilisateurCourant.getEmail());
			utilisateur.setEtablissement(etablissementCourant) ;
			
			ResponseEntity<Utilisateur> utilisateurCree = rest.postForEntity("http://academia-security/ws/utilisateur/ajout", utilisateur, Utilisateur.class);			
			return "redirect:/administration/utilisateurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissementCourant) ;
			model.addAttribute("utilisateur", utilisateur) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "administration/ajout-utilisateur" ;
		}
	}
	
	
	
	@GetMapping ("/administration/modification-utilisateur/{id}")
	public String initMaj (Model model, @SessionAttribute("etablissementCourant") Etablissement etablissement,
			@PathVariable("id") long id) throws Exception {
		Utilisateur utilisateur = rest.getForObject("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}/id/{id}", 
				Utilisateur.class, etablissement.getId(),  id) ;
		model.addAttribute("utilisateurExistant", utilisateur) ;
		initDependencies(model, etablissement) ;
		return "administration/modification-utilisateur" ;
	}
	
	
	
	@PostMapping ("/administration/utilisateur/modification")
	public String maj (Model model, @SessionAttribute("etablissementCourant") Etablissement etablissement, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Utilisateur utilisateurExistant) throws Exception {
		try {
			Utilisateur utilisateur = rest.getForObject("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}/id/{id}", 
					Utilisateur.class, etablissement.getId(), utilisateurExistant.getId()) ;
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setModificateur(utilisateurCourant.getEmail()) ;
			utilisateur.setNom(utilisateurExistant.getNom()) ;
			utilisateur.setTelephone(utilisateurExistant.getTelephone()) ;
			utilisateur.setEmail(utilisateurExistant.getEmail()) ;
			rest.put("http://academia-security/ws/utilisateur/modification", utilisateur, Utilisateur.class);
			return "redirect:/administration/utilisateurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "administration/modification-utilisateur" ;
		}
	}
	
	
	@GetMapping ("/administration/details-utilisateur/{id}")
	public String initDetails (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("etablissementCourant") Etablissement etablissement, @PathVariable("id") Long id) throws Exception {
		Utilisateur utilisateur = rest.getForObject("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}/id/{id}", 
				Utilisateur.class, etablissement.getId(), id) ;
		model.addAttribute("utilisateurExistant", utilisateur) ;
		model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		return "administration/details-utilisateur" ;
	}
	
	
	@PostMapping ("/administration/utilisateur/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Utilisateur utilisateurExistant) throws Exception {
		try {
			Utilisateur utilisateur = rest.getForObject("http://academia-security/ws/utilisateur/id/{idUtilisateur}", 
					Utilisateur.class, utilisateurExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("idUtilisateur", utilisateur.getId()) ;
			rest.delete("http://academia-security/ws/utilisateur/suppr/{idUtilisateur}", uriVariables);
			return "redirect:/administration/utilisateurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "administration/details-utilisateur" ;
		}
	}
	
	
	@GetMapping ("/administration/validation-email-utilisateur/{id}/{codeActivation}")
	public String initialiserActivation (Model model, @PathVariable ("id") Long id, @PathVariable ("codeActivation") String codeActivation) throws Exception {
		Utilisateur utilisateur = rest.getForObject("http://academia-security/ws/utilisateur/id/{id}/code/{codeActivation}", 
				Utilisateur.class, id, codeActivation) ;
		if(utilisateur == null) {
			return "redirect:/login" ;
		}
		
		model.addAttribute("id", utilisateur.getId()) ;
		model.addAttribute("nom", utilisateur.getNom()) ;
		model.addAttribute("telephone", utilisateur.getTelephone()) ;
		model.addAttribute("motDePasse", "") ;
		model.addAttribute("motDePasseConfirme", "") ;
		return "public/validation-utilisateur" ;
	}
	
	
	@PostMapping ("/administration/motdepasse/definition")
	public String definirMotDePasse (Model model, HttpServletRequest request, 
			@RequestParam("id") Long id, @RequestParam("motDePasse") String motDePasse,  
			@RequestParam("motDePasseConfirme") String motDePasseConfirme) throws Exception {
		Utilisateur utilisateur = null ;
		try {
			utilisateur = rest.getForObject("http://academia-security/ws/utilisateur/id/{id}", Utilisateur.class, id) ;
			if(! motDePasse.equals(motDePasseConfirme)) {
				throw new SecurityException ("Erreur ! Mots de passe non identiques") ;
			}
			
			utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(motDePasse)) ;
			ResponseEntity<Utilisateur> response = rest.postForEntity("http://academia-security/ws/utilisateur/motdepasse/definition", utilisateur, Utilisateur.class) ;
			
			if(response.getStatusCode() == HttpStatus.OK) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(utilisateur.getTelephone(), 
						motDePasse);
			    authToken.setDetails(new WebAuthenticationDetails(request));
			    
			    Authentication authentication = authenticationManager.authenticate(authToken);
			    
			    SecurityContextHolder.getContext().setAuthentication(authentication);
			    return "redirect:/dashboard" ;
			}
			remplirModel(model, utilisateur) ;
			return "public/validation-utilisateur" ;
		}catch(SecurityException sex) {
			logger.log(Level.SEVERE, sex.getMessage(), sex) ;
			remplirModel(model, utilisateur) ;
			model.addAttribute("messageEchec", sex.getMessage()) ;
			return "public/validation-utilisateur" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			remplirModel(model, utilisateur);
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "public/validation-utilisateur" ;
		}
	}
	
	
	@PostMapping ("/administration/motdepasse/modification")
	public String modifierMotDePasse (Model model, @SessionAttribute("etablissementCourant") Etablissement etablissement, 
			@RequestParam("id") Long id, @RequestParam("motDePasseActuel") String motDePasseActuel, 
			@RequestParam("nouveauMotDePasse") String nouveauMotDePasse, 
			@RequestParam("motDePasseConfirme") String motDePasseConfirme) throws Exception {
		Utilisateur utilisateur = null ;
		try {
			utilisateur = rest.getForObject("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}/id/{id}", 
					 Utilisateur.class, etablissement.getId(), id) ;
			logger.info("Mot de passe actuel : "+motDePasseActuel);
			logger.info("Mot de passe actuel chiffré : "+passwordEncoder.encode(motDePasseActuel));
			logger.info("Mot de passe de la bd chiffre : "+utilisateur.getMotDePasse());
			if(! passwordEncoder.encode(motDePasseActuel).equals(utilisateur.getMotDePasse())) {
				throw new SecurityException ("Erreur ! Mot de passe actuel invalide") ;
			}
			
			if(! nouveauMotDePasse.equals(motDePasseConfirme)) {
				throw new SecurityException ("Erreur ! Mots de passe non identiques") ;
			}
			
			utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse)) ;
			
			ResponseEntity<Utilisateur> response = rest.postForEntity("http://academia-security/ws/utilisateur/motdepasse/modification", utilisateur, Utilisateur.class) ;
			
			if(response.getStatusCode() == HttpStatus.OK) {
				model.addAttribute("messageSucces", "Succès ! Mot de passe modifié") ;
			} else {
				model.addAttribute("messageEchec", "Echec de l'opération") ;
			}
			remplirModel(model, utilisateur) ;
			return "administration/modification-motdepasse" ;
		}catch(SecurityException sex) {
			logger.log(Level.SEVERE, sex.getMessage(), sex) ;
			remplirModel(model, utilisateur) ;
			model.addAttribute("messageEchec", sex.getMessage()) ;
			return "administration/modification-motdepasse" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			remplirModel(model, utilisateur);
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "administration/modification-motdepasse" ;
		}
	}
	
	
	@GetMapping ("/administration/modification-motdepasse")
	public String initMajMdp (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateur) throws Exception {
		remplirModel (model, utilisateur) ;
		return "administration/modification-motdepasse" ;
	}
	
	
	@GetMapping ("/administration/utilisateur/activate/{id}")
	public String activer (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		try {
			Utilisateur utilisateurExistant = rest.getForObject("http://academia-security/ws/utilisateur/activate/etablissement/{idEtablissement}/id/{id}", 
					Utilisateur.class,  etablissement.getId(), id) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			return "administration/details-utilisateur" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			Utilisateur utilisateurExistant = rest.getForObject("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}/id/{id}", 
					Utilisateur.class, etablissement.getId(), id) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "administration/details-utilisateur" ;
		}
	}
	
	
	@GetMapping ("/administration/utilisateur/desactivate/etablissement/{idEtablissement}/id/{id}")
	public String desactiver (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		try {
			Utilisateur utilisateurExistant = rest.getForObject("http://academia-security/ws/utilisateur/desactivate/etablissement/{idEtablissement}/id/{id}", 
					Utilisateur.class, etablissement.getId(), id) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			return "administration/details-utilisateur" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			Utilisateur utilisateurExistant = rest.getForObject("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}/id/{id}", 
					Utilisateur.class, etablissement.getId(), id) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "administration/details-utilisateur" ;
		}
	}
	
		
		
	private void initDependencies (Model model, Etablissement etablissement) {

	}
	
	
	private void remplirModel (Model model, Utilisateur utilisateur) {
		model.addAttribute("id", utilisateur.getId()) ;
		model.addAttribute("telephone", utilisateur.getTelephone()) ;
		model.addAttribute("nom", utilisateur.getNom()) ;
	}
}
