package cm.deepdream.academia.web.controlers;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "listeClasses", "classeCourante", "pageCourante", 
	"nbPages", "listeEleves"})
public class EleveCtrl implements Serializable{
	private Logger logger = Logger.getLogger(EleveCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	private final Integer NB_ELEVES_PAGE = 20 ;
	
	
	@GetMapping ("/espaces/eleves")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Eleve[]> response = rest.getForEntity("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}", 
				 Eleve[].class, etablissement.getId()) ;
		 List<Eleve> listeEleves = Arrays.asList(response.getBody());
		 model.addAttribute("listeEleves", listeEleves) ;
		 return "espaces/eleves" ;
	}
	
	
	@GetMapping ("/espaces/eleves-classe-1")
	public String indexClasseListe (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 
		 ResponseEntity<Classe[]> response0 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response0.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 Eleve eleve = new Eleve() ;
		 eleve.setClasse(listeClasses.size() == 0 ? null : listeClasses.get(0));
		 
		 ResponseEntity<Eleve[]> response1 = rest.getForEntity("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Eleve[].class, etablissement.getId(), listeClasses.size() == 0 ? 0L : listeClasses.get(0).getId()) ;
		 List<Eleve> listeEleves = Arrays.asList(response1.getBody());
		 model.addAttribute("listeEleves", listeEleves) ;
		 
		 model.addAttribute("eleve", eleve) ;
		return "espaces/eleves-classe-1" ;
	}
	
	@GetMapping ("/espaces/eleves-classe-2")
	public String indexClasseCatalogue (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 
		 ResponseEntity<Classe[]> response0 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response0.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 Eleve eleve = new Eleve() ;
		 eleve.setClasse(listeClasses.size() == 0 ? null : listeClasses.get(0));
		 
		 ResponseEntity<Eleve[]> response1 = rest.getForEntity("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Eleve[].class, etablissement.getId(), listeClasses.size() == 0 ? 0L : listeClasses.get(0).getId()) ;
		 List<Eleve> listeEleves = Arrays.asList(response1.getBody());
		 model.addAttribute("listeEleves", listeEleves) ;
		 
		 model.addAttribute("classeCourante", listeClasses.size() == 0 ? null : listeClasses.get(0)) ;
		 model.addAttribute("pageCourante", 1) ;
		 model.addAttribute("nbPages", listeEleves.size()/NB_ELEVES_PAGE + 
				 (Math.floorMod(listeEleves.size(), NB_ELEVES_PAGE) == 0 ? 0:1)) ;
		 model.addAttribute("nbEleves", listeEleves.size()) ;
		 model.addAttribute("listeEleves_Page", 
				 listeEleves.size() == 0 ? new ArrayList<Eleve>():listeEleves.subList(0, Math.min(NB_ELEVES_PAGE, listeEleves.size()))) ;
		 
		 model.addAttribute("eleve", eleve) ;
		return "espaces/eleves-classe-2" ;
	}
	
	
	@PostMapping ("/espaces/eleves-classe-1")
	public String rechercher1 (Model model, Eleve eleve, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("listeClasses") List<Classe> listeClasses) throws Exception {
		
		 ResponseEntity<Eleve[]> response1 = rest.getForEntity("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Eleve[].class, etablissement.getId(),  eleve.getClasse().getId()) ;
		 List<Eleve> listeEleves = Arrays.asList(response1.getBody());
		 model.addAttribute("listeEleves", listeEleves) ;
		 
		 model.addAttribute("eleve", eleve) ;
		 return "espaces/eleves-classe-1" ;
	}
	
	@PostMapping ("/espaces/eleves-classe-2")
	public String rechercher2 (Model model, Eleve eleve, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("listeClasses") List<Classe> listeClasses) throws Exception {
		
		 ResponseEntity<Eleve[]> response1 = rest.getForEntity("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Eleve[].class, etablissement.getId(),  eleve.getClasse().getId()) ;
		 List<Eleve> listeEleves = Arrays.asList(response1.getBody());
		 model.addAttribute("listeEleves", listeEleves) ;
		 
		 model.addAttribute("nbLivres", listeEleves.size()) ;
		 model.addAttribute("classeCourante", eleve.getClasse()) ;
		 model.addAttribute("pageCourante", 1) ;
		 model.addAttribute("nbPages", listeEleves.size()/NB_ELEVES_PAGE + 
				 (Math.floorMod(listeEleves.size(), NB_ELEVES_PAGE) == 0 ? 0:1)) ;
		 model.addAttribute("listeEleves", listeEleves) ;
		 List<Eleve> listeEleves_Page = listeEleves.subList(0, Math.min(listeEleves.size() , NB_ELEVES_PAGE)) ;
		 model.addAttribute("listeEleves_Page", listeEleves_Page) ;
		 
		 model.addAttribute("eleve", eleve) ;
		 return "espaces/eleves-classe-2" ;
	}
	
	
	@GetMapping ("/page/{numPage}")
	 public String dashboardAuteur_Page (Model model, @PathVariable("numPage")Integer numPage, 
			 @SessionAttribute("listeEleves") List<Eleve> listeEleves,
			 @SessionAttribute("nbPages") Integer nbPages,
			 @SessionAttribute("pageCourante") Integer pageCourante,
			 @SessionAttribute("classeCourante") Classe classe) {
		
		if(numPage > nbPages )
			numPage = nbPages ;
		if(numPage <= 0 )
			numPage = 1 ;
		model.addAttribute("listeEleves", listeEleves) ;
		model.addAttribute("nbEleves", listeEleves.size()) ;
		model.addAttribute("classeCourante", classe) ;
		model.addAttribute("pageCourante", numPage) ;
		model.addAttribute("nbPages", nbPages) ;
		model.addAttribute("nbEleves", listeEleves.size()) ;
		List<Eleve> listeEleves_Page = listeEleves.subList((numPage-1)*NB_ELEVES_PAGE, 
				Math.min(listeEleves.size() , numPage*NB_ELEVES_PAGE)) ;
		
		model.addAttribute("listeEleves_Page", listeEleves_Page) ;
		 
	    return "espaces/eleves-classe-2" ;
	 }
	
	@GetMapping ("/espaces/ajout-eleve")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Eleve eleve = new Eleve() ;
		eleve.setPays(new Pays());
		eleve.setPhoto(new Photo());
		model.addAttribute("eleve", eleve) ;
		initDependencies(model, etablissement) ;
		return "espaces/ajout-eleve" ;
	}
	
	@PostMapping ("/espaces/eleve/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  @RequestParam("matricule") String matricule,
			 @RequestParam("nom") String nom, @RequestParam("prenom") String prenom, @RequestParam("sexe") String sexe,
			 @RequestParam("dateNaissance") String dateNaissance, @RequestParam("lieuNaissance") String lieuNaissance,
			 @RequestParam("idPays") Long idPays, @RequestParam("nomParent") String nomParent,
			 @RequestParam("adresse") String adresse, @RequestParam("telephone") String telephone, @RequestParam("email") String email,
			 MultipartFile file) throws Exception {
		Eleve eleve = new Eleve() ;
		try {
			logger.info("Ajout de l'élève "+nom+" "+idPays+ " ");
			
			eleve.setCreateur(utilisateurCourant.getEmail());
			eleve.setModificateur(utilisateurCourant.getEmail());
			eleve.setEtablissement(etablissement) ;
			
			eleve.setMatricule(matricule);
			eleve.setNom(nom);
			eleve.setPrenom(prenom);
			eleve.setSexe(sexe);
			eleve.setDateNaissance(LocalDate.parse(dateNaissance, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			eleve.setLieuNaissance(lieuNaissance);
			eleve.setEmail(email);
			eleve.setTelephone(telephone);
			eleve.setAdresse(adresse);
			
			eleve.setNomParent(nomParent);
			
			try{
				eleve.setPays(rest.getForObject("http://academia-programmation/ws/pays/id/{id}", Pays.class, idPays));
			}catch(Exception ex) {}
			 
			Photo photo = eleve.getPhoto() ;
			if(file.getBytes() != null && file.getBytes().length > 0) {
				 photo.setContentType(file.getContentType());
				 photo.setBytesStr(Base64.encodeBase64String(file.getBytes()));
				 photo.setSize(file.getSize());
			     eleve.setPhoto(photo);
			}else {
				 logger.info("Find the default user image ");
				 File defaultFile = ResourceUtils.getFile("classpath:default/unknow-person.png");
				 logger.info("Path to abonne logo "+defaultFile.getAbsolutePath());
				 byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
				 photo.setContentType("image/png");
				 photo.setBytesStr(Base64.encodeBase64String(allBytes));
				 photo.setSize(allBytes.length*1L);
				 eleve.setPhoto(photo);
			}
			
			rest.postForEntity("http://academia-programmation/ws/eleve/ajout", eleve, Eleve.class);			
			return "redirect:/espaces/eleves" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initialiserForm(model, eleve);
			initDependencies(model, etablissement) ;
			model.addAttribute("eleve", eleve) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "espaces/ajout-eleve" ;
		}
	}
	
	
	@GetMapping ("/espaces/modification-eleve/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Eleve eleve = rest.getForObject("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/id/{id}", 
				Eleve.class, etablissement.getId(), id) ;
		model.addAttribute("eleveExistant", eleve) ;
		initDependencies(model, etablissement) ;
		return "espaces/modification-eleve" ;
	}
	
	@PostMapping ("/espaces/eleve/modification")
	public String modifier (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,  
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			 @RequestParam("id") Long id, @RequestParam("matricule") String matricule,
			 @RequestParam("nom") String nom, @RequestParam("prenom") String prenom, @RequestParam("sexe") String sexe,
			 @RequestParam("dateNaissance") String dateNaissance, @RequestParam("lieuNaissance") String lieuNaissance,
			 @RequestParam("idPays") Long idPays, @RequestParam("nomParent") String nomParent,
			 @RequestParam("adresse") String adresse, @RequestParam("telephone") String telephone, @RequestParam("email") String email,
			 MultipartFile file) throws Exception {
		Eleve eleve = null ;
		try {
			logger.info("Modification de l'élève "+id+ " "+nom+" "+idPays+ " ");
			
			eleve = rest.getForObject("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/id/{id}", Eleve.class, etablissement.getId(), id) ;
			eleve.setModificateur(utilisateurCourant.getEmail()) ;
			eleve.setMatricule(matricule) ;
			eleve.setNom(nom) ;
			eleve.setPrenom(prenom) ;
			eleve.setSexe(sexe);
			eleve.setDateNaissance(LocalDate.parse(dateNaissance, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			eleve.setLieuNaissance(lieuNaissance);
			eleve.setNomParent(nomParent);
			eleve.setAdresse(adresse);
			eleve.setTelephone(telephone);
			eleve.setEmail(email);
			
			eleve.setNomParent(nomParent);
			try {
				eleve.setPays(rest.getForObject("http://academia-programmation/ws/pays/id/{id}", Pays.class, idPays));
			}catch(Exception ex) {}
			
			Photo photo = eleve.getPhoto() == null ? new Photo() : eleve.getPhoto() ;
			photo.setModified(false);
			if(file.getBytes() != null && file.getBytes().length > 0) {
				 photo.setContentType(file.getContentType());
				 photo.setBytesStr(Base64.encodeBase64String(file.getBytes()));
				 photo.setSize(file.getSize());
				 photo.setModified(true);
			     eleve.setPhoto(photo);
			}
			rest.postForObject("http://academia-programmation/ws/eleve/modification", eleve, Eleve.class);
			return "redirect:/espaces/eleves" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement) ;
			model.addAttribute("eleveExistant", eleve) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "espaces/modification-eleve" ;
		}
	}
	
	
	@GetMapping ("/espaces/details-eleve/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Eleve eleve = rest.getForObject("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/id/{id}", Eleve.class, etablissement.getId(), id) ;
		model.addAttribute("eleveExistant", eleve) ;
		return "espaces/details-eleve" ;
	}
	
	
	private void initialiserForm(Model model, Eleve eleve) {
		model.addAttribute("matricule", eleve.getMatricule()) ;
		model.addAttribute("nom", eleve.getNom()) ;
		model.addAttribute("prenom", eleve.getPrenom()) ;
		model.addAttribute("sexe", eleve.getSexe()) ;
		model.addAttribute("telephone", eleve.getTelephone()) ;
		model.addAttribute("email", eleve.getEmail()) ;
		model.addAttribute("adresse", eleve.getAdresse()) ;
		model.addAttribute("nomParent", eleve.getNomParent()) ;
	}
	
	
	private void initDependencies (Model model, Etablissement etablissement) { 
		 ResponseEntity<Pays[]> response2 = rest.getForEntity("http://academia-programmation/ws/pays/all", 
				 Pays[].class, etablissement.getId()) ;
		 List<Pays> listePays = Arrays.asList(response2.getBody());
		 model.addAttribute("listePays", listePays) ;
	}
}
