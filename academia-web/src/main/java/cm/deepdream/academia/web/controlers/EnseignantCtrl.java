package cm.deepdream.academia.web.controlers;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Enseignant;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class EnseignantCtrl implements Serializable{
	private Logger logger = Logger.getLogger(EnseignantCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/espaces/enseignants")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Enseignant[]> response = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}", 
				 Enseignant[].class, etablissement.getId()) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response.getBody());
		 model.addAttribute("listeEnseignants", listeEnseignants) ;
		return "espaces/enseignants" ;
	}
	
	@GetMapping ("/espaces/enseignants-classe")
	public String rechercherClasse0 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Classe[]> response1 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response1.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 model.addAttribute("idClasse", listeClasses.size() == 0 ? 0L:listeClasses.get(0).getId()) ;
		
		 ResponseEntity<Enseignant[]> response2 = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Enseignant[].class, etablissement.getId(), listeClasses.size() == 0 ? 0L:listeClasses.get(0).getId()) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response2.getBody());
		 model.addAttribute("listeEnseignants", listeEnseignants) ;
		 return "espaces/enseignants-classe" ;
	}
	
	@PostMapping ("/espaces/enseignants-classe")
	public String rechercherClasse1 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@RequestParam("idClasse") Long idClasse) throws Exception {
		 ResponseEntity<Classe[]> response1 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response1.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 model.addAttribute("idClasse", idClasse) ;
		
		 ResponseEntity<Enseignant[]> response2 = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Enseignant[].class, etablissement.getId(), idClasse) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response2.getBody());
		 model.addAttribute("listeEnseignants", listeEnseignants) ;
		 return "espaces/enseignants-classe" ;
	}
	
	@GetMapping ("/espaces/enseignants-domaine")
	public String rechercherDomaine0 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Domaine[]> response1 = rest.getForEntity("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}", 
				 Domaine[].class, etablissement.getId()) ;
		 List<Domaine> listeDomaines = Arrays.asList(response1.getBody());
		 model.addAttribute("listeDomaines", listeDomaines) ;
		 
		 Enseignant enseignant = new Enseignant() ;
		 enseignant.setDomaine(listeDomaines.size() == 0 ? new Domaine() : listeDomaines.get(0));
		 model.addAttribute("enseignant", enseignant) ;
		
		 ResponseEntity<Enseignant[]> response2 = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/domaine/{idDomaine}", 
				 Enseignant[].class, etablissement.getId(), listeDomaines.size() == 0 ? 0L : listeDomaines.get(0).getId()) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response2.getBody());
		 model.addAttribute("listeEnseignants", listeEnseignants) ;
		 return "espaces/enseignants-domaine" ;
	}
	
	
	@PostMapping ("/espaces/enseignants-domaine")
	public String rechercherDomaine1 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			Enseignant enseignant) throws Exception {
		 ResponseEntity<Domaine[]> response1 = rest.getForEntity("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}", 
				 Domaine[].class, etablissement.getId()) ;
		 List<Domaine> listeDomaines = Arrays.asList(response1.getBody());
		 model.addAttribute("listeDomaines", listeDomaines) ;
		 
		 model.addAttribute("enseignant", enseignant) ;
		
		 ResponseEntity<Enseignant[]> response2 = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/domaine/{idDomaine}", 
				 Enseignant[].class, etablissement.getId(), enseignant.getDomaine().getId()) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response2.getBody());
		 model.addAttribute("listeEnseignants", listeEnseignants) ;
		 return "espaces/enseignants-domaine" ;
	}
	
	@GetMapping ("/espaces/ajout-enseignant")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Enseignant enseignant = new Enseignant() ;
		enseignant.setDatePriseService(LocalDate.now());
		remplirForm(model, enseignant);
		initDependencies(model, etablissement) ;
		return "espaces/ajout-enseignant" ;
	}
	
	@PostMapping ("/espaces/enseignant/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  @RequestParam("matricule") String matricule,
			 @RequestParam("civilite") String civilite,
			 @RequestParam("nom") String nom, @RequestParam("prenom") String prenom, @RequestParam("sexe") String sexe,
			 @RequestParam("dateNaissance") String dateNaissance, @RequestParam("lieuNaissance") String lieuNaissance,
			 @RequestParam("datePriseService") String datePriseService, @RequestParam("idDomaine") Long idDomaine,
			 @RequestParam("idFonction") Long idFonction,  @RequestParam("telephone") String telephone, @RequestParam("email") String email,
			 MultipartFile file) throws Exception {
		Enseignant enseignant = new Enseignant() ;
		try {
			enseignant.setCreateur(utilisateurCourant.getEmail());
			enseignant.setModificateur(utilisateurCourant.getEmail());
			enseignant.setEtablissement(etablissement) ;
			
			enseignant.setCivilite(civilite);
			enseignant.setMatricule(matricule);
			enseignant.setNom(nom);
			enseignant.setPrenom(prenom);
			enseignant.setSexe(sexe);
			try {
				enseignant.setDateNaissance(LocalDate.parse(dateNaissance, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}catch(Exception e) {}
			enseignant.setLieuNaissance(lieuNaissance);
			try {
				enseignant.setDatePriseService(LocalDate.parse(datePriseService, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}catch(Exception e) {}
			try {
				enseignant.setFonction(rest.getForObject("http://academia-programmation/ws/fonction/id/{id}", Fonction.class, idFonction));
			}catch(Exception e) {}
			try {
				enseignant.setDomaine(rest.getForObject("http://academia-programmation/ws/domaine/id/{id}", Domaine.class, idDomaine));
			}catch(Exception e) {}
			enseignant.setTelephone(telephone);
			enseignant.setEmail(email);
			
			Photo photo = enseignant.getPhoto() ;
			if(file.getBytes() != null && file.getBytes().length > 0) {
				 photo.setContentType(file.getContentType());
				 photo.setBytesStr(Base64.encodeBase64String(file.getBytes()));
				 photo.setSize(file.getSize());
				 enseignant.setPhoto(photo);
			}else {
				 logger.info("Find the default user image ");
				 File defaultFile = ResourceUtils.getFile("classpath:default/unknow-person.png");
				 logger.info("Path to abonne logo "+defaultFile.getAbsolutePath());
				 byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
				 photo.setContentType("image/png");
				 photo.setBytesStr(Base64.encodeBase64String(allBytes));
				 photo.setSize(allBytes.length*1L);
				 enseignant.setPhoto(photo);
			}
			rest.postForEntity("http://academia-programmation/ws/enseignant/ajout", enseignant, Enseignant.class);			
			return "redirect:/espaces/enseignants" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement) ;
			remplirForm(model, enseignant);
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "espaces/ajout-enseignant" ;
		}
	}
	
	
	@GetMapping ("/espaces/modification-enseignant/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Enseignant enseignant = rest.getForObject("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/id/{id}", 
				Enseignant.class, etablissement.getId(), id) ;
		remplirForm(model, enseignant);
		initDependencies(model, etablissement) ;
		return "espaces/modification-enseignant" ;
	}
	
	@PostMapping ("/espaces/enseignant/modification")
	public String modifier (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			 @RequestParam("id") Long id,  @RequestParam("civilite") String civilite, @RequestParam("matricule") String matricule, 
			 @RequestParam("nom") String nom, @RequestParam("prenom") String prenom, @RequestParam("sexe") String sexe,
			 @RequestParam("dateNaissance") String dateNaissance, @RequestParam("lieuNaissance") String lieuNaissance,
			 @RequestParam("datePriseService") String datePriseService, @RequestParam("idDomaine") Long idDomaine,
			 @RequestParam("idFonction") Long idFonction,  @RequestParam("telephone") String telephone, @RequestParam("email") String email,
			 MultipartFile file) throws Exception {
		Enseignant enseignant = null ;
		try {
			enseignant = rest.getForObject("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/id/{id}", 
					Enseignant.class, etablissement.getId(), id) ;
			enseignant.setCreateur(utilisateurCourant.getEmail());
			enseignant.setModificateur(utilisateurCourant.getEmail());
			enseignant.setEtablissement(etablissement) ;
			
			enseignant.setCivilite(civilite);
			enseignant.setMatricule(matricule);
			enseignant.setNom(nom);
			enseignant.setPrenom(prenom);
			enseignant.setSexe(sexe);
			try {
				enseignant.setDateNaissance(LocalDate.parse(dateNaissance, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}catch(Exception e) {}
			enseignant.setLieuNaissance(lieuNaissance);
			try {
				enseignant.setDatePriseService(LocalDate.parse(datePriseService, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}catch(Exception e) {}
			try {
				enseignant.setFonction(rest.getForObject("http://academia-programmation/ws/fonction/id/{id}", Fonction.class, idFonction));
			}catch(Exception e) {}
			try {
				enseignant.setDomaine(rest.getForObject("http://academia-programmation/ws/domaine/id/{id}", Domaine.class, idDomaine));
			}catch(Exception e) {}
			enseignant.setTelephone(telephone);
			enseignant.setEmail(email);
			Photo photo = enseignant.getPhoto() == null ? new Photo() : enseignant.getPhoto() ;
			photo.setModified(false);
			if(file.getBytes() != null && file.getBytes().length > 0) {
				 photo.setContentType(file.getContentType());
				 photo.setBytesStr(Base64.encodeBase64String(file.getBytes()));
				 photo.setSize(file.getSize());
				 photo.setModified(true);
				 enseignant.setPhoto(photo);
			} 
			rest.postForObject("http://academia-programmation/ws/enseignant/modification", enseignant, Enseignant.class);
			return "redirect:/espaces/enseignants" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement) ;
			remplirForm(model, enseignant);
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "espaces/modification-enseignant" ;
		}
	}
	
	
	@GetMapping ("/espaces/details-enseignant/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Enseignant enseignant = rest.getForObject("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/id/{id}", Enseignant.class, etablissement.getId(), id) ;
		model.addAttribute("enseignantExistant", enseignant) ;
		return "espaces/details-enseignant" ;
	}
	
	private void remplirForm(Model model, Enseignant enseignant) {
		model.addAttribute("id", enseignant.getId()) ;
		model.addAttribute("matricule", enseignant.getMatricule()) ;
		model.addAttribute("nom", enseignant.getNom()) ;
		model.addAttribute("prenom", enseignant.getPrenom()) ;
		model.addAttribute("sexe", enseignant.getSexe()) ;
		model.addAttribute("dateNaissance", enseignant.getDateNaissance()) ;
		model.addAttribute("lieuNaissance", enseignant.getLieuNaissance()) ;
		model.addAttribute("datePriseService", enseignant.getDatePriseService()) ;
		model.addAttribute("idFonction", enseignant.getFonction() == null ?0L:enseignant.getFonction().getId()) ;
		model.addAttribute("idDomaine", enseignant.getDomaine() == null ?0L:enseignant.getDomaine().getId()) ;
		model.addAttribute("telephone", enseignant.getTelephone()) ;
		model.addAttribute("email", enseignant.getEmail()) ;
	}
	
	
	private void initDependencies (Model model, Etablissement etablissement) {
		 ResponseEntity<Domaine[]> response = rest.getForEntity("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}", 
				 Domaine[].class, etablissement.getId()) ;
		 List<Domaine> listeDomaines = Arrays.asList(response.getBody());
		 model.addAttribute("listeDomaines", listeDomaines) ;
		 
		 ResponseEntity<Fonction[]> response2 = rest.getForEntity("http://academia-programmation/ws/fonction/etablissement/{idEtablissement}", 
				 Fonction[].class, etablissement.getId()) ;
		 List<Fonction> listeFonctions = Arrays.asList(response2.getBody());
		 model.addAttribute("listeFonctions", listeFonctions) ;
	}
}
