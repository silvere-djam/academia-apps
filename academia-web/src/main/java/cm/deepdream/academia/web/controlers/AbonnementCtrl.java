package cm.deepdream.academia.web.controlers;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import cm.deepdream.academia.souscription.data.Abonnement;
import cm.deepdream.academia.souscription.data.Contact;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Logo;
import cm.deepdream.academia.souscription.data.Localite;
import cm.deepdream.academia.web.util.CaptchaFileStrGenerator;

@Controller
@SessionAttributes({"etablissementCourant", "listeEtablissements"})
public class AbonnementCtrl implements Serializable{
	private Logger logger = Logger.getLogger(AbonnementCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	@Autowired
	private CaptchaFileStrGenerator captchaGenerator ;
	
	
	@GetMapping ("/souscription/abonnements")
	public String getAll (Model model) throws Exception {
		 ResponseEntity<Etablissement[]> response0 = rest.getForEntity("http://academia-souscription/ws/etablissement/all", 
				 Etablissement[].class) ;
		 List<Etablissement> listeEtablissements = Arrays.asList(response0.getBody());
		 
		 model.addAttribute("listeEtablissements", listeEtablissements) ;
		 
		 Etablissement etablissement = listeEtablissements.size() == 0 ? new Etablissement():listeEtablissements.get(0) ;
		
		 ResponseEntity<Abonnement[]> response = rest.getForEntity("http://academia-souscription/ws/abonnement/all", 
				  Abonnement[].class) ;
		 List<Abonnement> listeAbonnements = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbonnements", listeAbonnements) ;
		 Abonnement abonnement = new Abonnement() ;
		 abonnement.setEtablissement(etablissement);
		 model.addAttribute("abonnement", abonnement) ;
		 return "souscription/abonnements" ;
	}
	
	@PostMapping ("/souscription/abonnement/recherche")
	public String getByAnnee (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("listeEtablissements") List<Etablissement> listeEtablissements, Abonnement abonnement) throws Exception {
		 ResponseEntity<Abonnement[]> response = rest.getForEntity("http://academia-souscription/ws/abonnement/etablissement/{idEtablissement}", 
				  Abonnement[].class, abonnement.getEtablissement().getId()) ;
		 List<Abonnement> listeAbonnements = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbonnements", listeAbonnements) ;
		 Abonnement abonnement2 = new Abonnement() ;
		 abonnement2.setEtablissement(etablissement);
		 model.addAttribute("abonnement", abonnement2) ;
		 model.addAttribute("listeEtablissements", listeEtablissements) ;
		 return "souscription/abonnements" ;
	}
	
	
	@GetMapping ("/registration")
	public String initSouscription (Model model, HttpSession session) throws Exception {
		initDependencies (model) ;
		
		String captchaStr = captchaGenerator.genererStr(6, session.getId()) ;
		model.addAttribute("captchaCache", captchaStr) ;
		model.addAttribute("idSession", session.getId()) ;
		
		Etablissement etablissement = new Etablissement() ;
		etablissement.setLogo(new Logo());
		etablissement.setContactChef(new Contact());
		etablissement.setContactInformaticien(new Contact());
		remplirModel (model, etablissement, false) ;
		return "public/ajout-etablissement" ;
	}
	
	
	@GetMapping ("/conditions-dutilisation")
	public String afficherConditions (Model model, HttpSession session) throws Exception {
	
		
		return "public/conditions-dutilisation" ;
	}
	
	
	@PostMapping ("/souscription/abonnement/etablissement/ajout")
	public String ajouter (Model model,  @RequestParam("libelle") String libelle, 
			 @RequestParam("cycle") String cycle, @RequestParam("telephone") String telephone,
			 @RequestParam("email") String email, @RequestParam("boitePostale") String boitePostale,
			 @RequestParam("nbElevesApprox") Integer nbElevesApprox,
			 @RequestParam("idLocalite") Long idLocalite, @RequestParam("nomChef") String nomChef,
			 @RequestParam("telephoneChef") String telephoneChef, @RequestParam("emailChef") String emailChef,
			 @RequestParam("nomInformaticien") String nomInformaticien, 
			 @RequestParam("telephoneInformaticien") String telephoneInformaticien, 
			 @RequestParam("emailInformaticien") String emailInformaticien,
			 @RequestParam("captcha") String captcha, @RequestParam("captchaCache") String captchaCache,
			 HttpSession session, MultipartFile file) throws Exception {
		Etablissement etablissement = new Etablissement() ;
		try {
			logger.info("Ajout de l'établissement "+libelle+" "+idLocalite+ " "+cycle);
			
			etablissement.setLibelle(libelle);
			etablissement.setCycle(cycle);
			etablissement.setTelephone(telephone);
			etablissement.setEmail(email);
			etablissement.setBoitePostale(boitePostale);
			etablissement.setNbElevesApprox(nbElevesApprox);
			
			Contact contactChef = new Contact() ;
			contactChef.setNom(nomChef);
			contactChef.setEmail(emailChef);
			contactChef.setTelephone(telephoneChef);
			etablissement.setContactChef(contactChef);
			
			Contact contactInformaticien = new Contact() ;
			contactInformaticien.setNom(nomInformaticien);
			contactInformaticien.setEmail(emailInformaticien);
			contactInformaticien.setTelephone(telephoneInformaticien);
			etablissement.setContactInformaticien(contactInformaticien);
			
			try {
				etablissement.setLocalite(rest.getForObject("http://academia-souscription/ws/localite/id/{id}", Localite.class, idLocalite));
			}catch(Exception ex) {}
			
			 Logo logo = new Logo() ;
		    if(file.getBytes() != null && file.getBytes().length > 0) {
		    	logo.setContentType(file.getContentType());
		    	logo.setBytes(file.getBytes());
		    	logo.setSize(file.getSize());
		    	etablissement.setLogo(logo);
			}else {
				logger.info("Find the default user image ");
				File defaultFile = ResourceUtils.getFile("classpath:default/logo-minesec.jpg");
				logger.info("Path to abonne logo "+defaultFile.getAbsolutePath());
				byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
		    	logo.setContentType("image/jpg");
		    	logo.setBytes(allBytes);
		    	logo.setSize(allBytes.length*1L);
		    	etablissement.setLogo(logo);
			 }
		    
		    if( ! captchaCache.equals(captcha) ) {
		    	remplirModel (model, etablissement, true) ;
		    	initDependencies (model) ;
		    	model.addAttribute("messageEchec", ! captchaCache.equals(captcha) ? 
		    			"Erreur ! Les caractères ont été mal saisis" : "Erreur ! Vous devez accepter les conditions d'utilisation") ;
		    	String captchaStr = captchaGenerator.genererStr(6, session.getId()) ;
				model.addAttribute("captchaCache", captchaStr) ;
				model.addAttribute("idSession", session.getId()) ;
		    	return "public/ajout-etablissement" ;
			}
			
			ResponseEntity<Etablissement>  response = rest.postForEntity("http://academia-souscription/ws/abonnement/etablissement/ajout", etablissement, Etablissement.class);			
			model.addAttribute("etablissement", etablissement) ;
			return "public/confirmation-ajout-etablissement" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model);
	    	String captchaStr = captchaGenerator.genererStr(6, session.getId()) ;
			model.addAttribute("captcha", captchaStr) ;
			model.addAttribute("idSession", session.getId()) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "public/ajout-etablissement" ;
		}
	}
	
	private void initDependencies (Model model) {
		 ResponseEntity<Localite[]> response0 = rest.getForEntity("http://academia-souscription/ws/localite/all", 
				 Localite[].class) ;
		 List<Localite> listeLocalites = Arrays.asList(response0.getBody());
		 model.addAttribute("listeLocalites", listeLocalites) ;
	}
	
	private void remplirModel (Model model, Etablissement etablissement, boolean conditionsUtilisation) {
		model.addAttribute("libelle", etablissement.getLibelle()) ;
		model.addAttribute("cycle", etablissement.getCycle()) ;
		model.addAttribute("telephone", etablissement.getTelephone()) ;
		model.addAttribute("email", etablissement.getEmail()) ;
		model.addAttribute("boitePostale", etablissement.getBoitePostale()) ;
		model.addAttribute("idLocalite", etablissement.getLocalite() == null ? null:etablissement.getLocalite().getId()) ;
		model.addAttribute("nbElevesApprox", etablissement.getNbElevesApprox()) ;
		
		Contact contactChef = etablissement.getContactChef() ;
		model.addAttribute("nomChef", contactChef.getNom()) ;
		model.addAttribute("telephoneChef", contactChef.getTelephone()) ;
		model.addAttribute("emailChef", contactChef.getEmail()) ;
		
		Contact contactInformaticien = etablissement.getContactInformaticien() ;
		model.addAttribute("nomInformaticien", contactInformaticien.getNom()) ;
		model.addAttribute("telephoneInformaticien", contactInformaticien.getTelephone()) ;
		model.addAttribute("emailInformaticien", contactInformaticien.getEmail()) ;
		model.addAttribute("conditions",  conditionsUtilisation) ;
	}
	
	private File getCaptchaFolder() {
		File parent = new File("captcha") ;
		if(parent.exists()) {
			parent.mkdir() ;
		}
		return parent ;
	}
}
