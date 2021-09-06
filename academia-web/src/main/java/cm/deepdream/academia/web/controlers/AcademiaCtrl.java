package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Session;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.security.enums.StatutU;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Trimestre;
@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante", "trimestreCourant",
	"semestreCourant"})
public class AcademiaCtrl implements Serializable{
	private Logger logger = Logger.getLogger(AcademiaCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;

	
	 @GetMapping ("/")
	 public String pageLogin (Model model) {
	       return "login" ;
	 }
	 
	 @GetMapping ("/login")
	 public String pageLogin2 (Model model) {
	       return "login" ;
	 }
	 
	 @GetMapping ("/my-account")
	 public String monCompte (Model model) {
	       return "administration/mon-compte" ;
	 }
	 
	 
	 @GetMapping ("/login-echec")
	 public String pageLogin3 (Model model) {
		 model.addAttribute("messageEchec", model.getAttribute("messageEchec") == null ? "Echec de l'opération" : model.getAttribute("messageEchec")) ;
	     return "login" ;
	 }
	 
	 
	 @GetMapping ("/forgot-password")
	 public String forgotPwd (Model model) {
	       return "forgot-password" ;
	 }
	 
	 @GetMapping ("/change-password")
	 public String changePwd (Model model) {
	       return "change-password" ;
	 }
	 
	 @GetMapping ("/disconnected")
	 public String disconnected (Model model) {
	       return "login" ;
	 }
	 
	 @GetMapping ("/error")
	 public String pageErreur (Model model) {
	       return "extra-404" ;
	 }
	 
	 @GetMapping ("/dashboard")
	 public String dashboard (Model model, HttpServletRequest request) throws Exception{
		 try {
			 Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
			 String telephone = authentication.getName() ;
			 Utilisateur utilisateur = rest.getForObject("http://academia-security/ws/utilisateur/telephone/{telephone}", Utilisateur.class, telephone) ;
			 
			 if (utilisateur == null) {
				 throw new SecurityException ("Echec ! Login ou mot de passe incorrect") ;
			 }
			 if (Duration.between(LocalDateTime.now(), utilisateur.getDateExp()).isNegative()) {
				 throw new SecurityException ("Echec ! Mot de passe expiré") ;
			 }
			 if (Duration.between(LocalDateTime.now(), utilisateur.getDateExpMdp()).isNegative() ) {
				 throw new SecurityException ("Echec ! Votre compte a expiré") ;
			 }
			 
			 model.addAttribute("utilisateurCourant", utilisateur) ;			
			 
			 Etablissement etablissement = rest.getForObject("http://academia-programmation/ws/etablissement/id/{id}",
					 Etablissement.class, utilisateur.getEtablissement().getId()) ;			 
			 model.addAttribute("etablissementCourant", etablissement) ;
			 
			 utilisateur.setDateDernConn(LocalDateTime.now()) ;
			 rest.postForObject("http://academia-security/ws/utilisateur/modification", utilisateur, Utilisateur.class) ;
			 
			 Session session = new Session() ;
			 session.setUtilisateur(utilisateur) ;
			 session.setDateDebut(LocalDateTime.now()) ;
			 session.setAdresseIP(request.getRemoteAddr());
			 session.setEtablissement(etablissement);
			 rest.postForEntity("http://academia-security/ws/session/ajout", session, Session.class);
			 
			 AnneeScolaire anneeScolaire = null ;
			 
			 try {
				anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/courante", 
					 AnneeScolaire.class, etablissement.getId()) ;
			 	model.addAttribute("anneeScolaireCourante", anneeScolaire) ;
			 	if(anneeScolaire == null) {
			 		ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
							 AnneeScolaire[].class, etablissement.getId()) ;
					 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
					 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
			 		 return "definition-anneeacademique" ;
			 	} 
			 }catch(Exception ex) {
				 model.addAttribute("messageEchec", "Erreur ! Année académique invalide") ;
				 logger.log(Level.SEVERE, ex.getMessage(), ex) ;
				 return "login";
			 }
			 
			 try {
				 Trimestre trimestre = rest.getForObject("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}/courant", 
					 Trimestre.class, etablissement.getId(), anneeScolaire == null ?0L:anneeScolaire.getId()) ;
			 	model.addAttribute("trimestreCourant", trimestre) ;
			 }catch(Exception e) {
				 model.addAttribute("trimestreCourant", null) ;
			 }
			 
			 try {
				 Semestre semestre = rest.getForObject("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}/courant", 
						 Semestre.class, etablissement.getId(), anneeScolaire == null ?0L:anneeScolaire.getId()) ;
			 	 model.addAttribute("semestreCourant", semestre) ;
			 }catch(Exception e) {
				 model.addAttribute("semestreCourant", null) ;
			 }
		     return "dashboard" ;
		 }catch(SecurityException sex) {
			 model.addAttribute("messageEchec", sex.getMessage()) ;
			 logger.log(Level.SEVERE, sex.getMessage(), sex) ;
			 return "login";
		 }catch(Exception ex) {
			 model.addAttribute("messageEchec", "Echec de l'opération") ;
			 logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			 return "login";
		 }
	 }
	 
	 
	 @GetMapping ("/info-profil")
	 public String infoProfil (Model model, HttpServletRequest request)  throws Exception{
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		 String login = authentication.getName() ;
		 Utilisateur utilisateur = rest.getForObject("http://academia-security/utilisateur/{login}", Utilisateur.class, login) ;
		 model.addAttribute("utilisateurCourant", utilisateur) ;
		
	     return "administration/info-profil" ;
	 }
	 
	 @GetMapping ("/modification-motdepasse")
	 public String initMajMdp (Model model, HttpServletRequest request)  throws Exception{
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		 String login = authentication.getName() ;
		 Utilisateur utilisateur = rest.getForObject("http://academia-security/utilisateur/login/{login}", Utilisateur.class, login) ;
		 model.addAttribute("currentUser", utilisateur) ;
	     return "modification-motdepasse" ;
	 }
	 
	 @PostMapping ("/utilisateur/motdepasse/modification")
	 public String majMdp (Model model, Utilisateur utilisateurCourant) throws Exception{
		// String mdp1 = utilisateurCourant.getMdp1() ;
		// String mdp2 = utilisateurCourant.getMdp2() ;
		// String mdp3 = utilisateurCourant.getMdp3() ;
		 
		 /*  if(! new BCryptPasswordEncoder().encode(mdp1).equals(utilisateurCourant.getMotDePasse())) {
			 model.addAttribute("messageEchec", "Echec ! Ancien mot de passe incorrect") ;
			 return "maj-mot-de-passe" ;
		 }
		 
		if(! mdp2.equals(mdp3)) {
			 model.addAttribute("messageEchec", "Confirmation de mot de passe incorrecte") ;
			 return "maj-mot-de-passe" ;
		 }
		 
		 utilisateurCourant.setMotDePasse(new BCryptPasswordEncoder().encode(mdp2)) ;*/
		 
		 rest.put("http://academia-security/utilisateur/id/{id}", utilisateurCourant, utilisateurCourant.getId()) ;
		 
		 model.addAttribute("messageSuccess", "Mot de passe mis jour") ;
	     return "administration/modification-motdepasse" ;
	 }
	 
}
