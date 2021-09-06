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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Inscription;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.programmation.data.Profession;
import cm.deepdream.academia.programmation.enums.StatutE;

@Controller
@SessionAttributes({"utilisateurCourant", "anneeScolaireCourante", "etablissementCourant"})
public class InscriptionCtrl implements Serializable{
	private Logger logger = Logger.getLogger(InscriptionCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/admission/inscriptions")
	public String index (Model model, @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, 
			@SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Inscription[]> response = rest.getForEntity("http://academia-programmation/ws/inscription/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Inscription[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Inscription> listeInscriptions = Arrays.asList(response.getBody());
		 model.addAttribute("listeInscriptions", listeInscriptions) ;
		 
		 Inscription inscription = new Inscription() ;
		 model.addAttribute("inscription", inscription) ;
		 initDependencies0 (model,  etablissement) ;
		 
		 model.addAttribute("anneeScolaireCourante", anneeScolaire) ;
		 return "admission/inscriptions" ;
	}
	
	
	@GetMapping ("/admission/inscriptions-classe")
	public String rechercherClasse (Model model, @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, 
			@SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 
		 ResponseEntity<AnneeScolaire[]> response0 = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", AnneeScolaire[].class, etablissement.getId()) ;
		 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response0.getBody());
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 
		 ResponseEntity<Classe[]> response1 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response1.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 Classe classe = listeClasses.size() == 0 ? null : listeClasses.get(0) ; 
		 
		 ResponseEntity<Inscription[]> response2 = rest.getForEntity("http://academia-programmation/ws/inscription/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}", 
				 Inscription[].class, etablissement.getId(), classe == null ? 0: classe.getId(), anneeScolaire.getId()) ;
		 List<Inscription> listeInscriptions = Arrays.asList(response2.getBody());
		 model.addAttribute("listeInscriptions", listeInscriptions) ;
		 
		 Inscription inscription = new Inscription() ;
		 inscription.setClasse(classe);
		 inscription.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("inscription", inscription) ;
		 
		 model.addAttribute("anneeScolaireCourante", anneeScolaire) ;
		 return "admission/inscriptions-classe" ;
	}
	
	
	@PostMapping ("/admission/recherche-inscriptions")
	public String rechercher (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, Inscription inscription) throws Exception {
		 ResponseEntity<Inscription[]> response = rest.getForEntity("http://academia-programmation/ws/inscription/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}", 
				 Inscription[].class, etablissement.getId(),  inscription.getClasse().getId(), inscription.getAnneeScolaire().getId()) ;
		 List<Inscription> listeInscriptions = Arrays.asList(response.getBody());
		 model.addAttribute("listeInscriptions", listeInscriptions) ;
		 
		 model.addAttribute("inscription", inscription) ;
		 initDependencies0 ( model,  etablissement) ;
		 
		 model.addAttribute("anneeScolaireCourante", anneeScolaire) ;
		 return "admission/inscriptions" ;
	}
	
	
	@PostMapping ("/admission/recherche-inscriptions-classe")
	public String rechercherClasse (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, Inscription inscription) throws Exception {
		 ResponseEntity<Inscription[]> response = rest.getForEntity("http://academia-programmation/ws/inscription/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}", 
				 Inscription[].class, etablissement.getId(),  inscription.getClasse().getId(), inscription.getAnneeScolaire().getId()) ;
		 List<Inscription> listeInscriptions = Arrays.asList(response.getBody());
		 model.addAttribute("listeInscriptions", listeInscriptions) ;
		 
		 model.addAttribute("inscription", inscription) ;
		 initDependencies0 ( model,  etablissement) ;
		 model.addAttribute("anneeScolaireCourante", anneeScolaire) ;
		 return "admission/inscriptions-classe" ;
	}
	
	
	@GetMapping ("/admission/ajout-inscription-1")
	public String initAjout1 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Inscription inscription = new Inscription() ;
		model.addAttribute("inscription", inscription) ;
		inscription.setDateInscription(LocalDate.now());
		inscription.setEleve(new Eleve());
		inscription.setClasse(new Classe());
		inscription.setAnneeScolaire(anneeScolaire);
		initDependencies (model,  etablissement) ;
		return "admission/ajout-inscription-1" ;
	}
	
	
	@GetMapping ("/admission/ajout-inscription-2")
	public String initAjout2 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Inscription inscription = new Inscription() ;
		model.addAttribute("inscription", inscription) ;
		inscription.setDateInscription(LocalDate.now());
		inscription.setEleve(new Eleve());
		inscription.setClasse(new Classe());
		inscription.setAnneeScolaire(anneeScolaire);
		initDependencies (model,  etablissement) ;
		return "admission/ajout-inscription-2" ;
	}
	
	@PostMapping ("/admission/inscription/ajout1")
	public String ajouter1 (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@RequestParam("matricule") String matricule, @RequestParam("nom") String nom, @RequestParam("prenom") String prenom, 
			@RequestParam("sexe") String sexe, @RequestParam("dateNaissance") String dateNaissance, @RequestParam("lieuNaissance") String lieuNaissance,
			@RequestParam("idPays") Long idPays, @RequestParam("nomParent") String nomParent,@RequestParam("dateInscription") String dateInscription,
			@RequestParam("idProfession") Long idProfession, @RequestParam("idClasse") Long idClasse, @RequestParam("adresse") String adresse,
			@RequestParam("telephone") String telephone, @RequestParam("email") String email,
			MultipartFile file) throws Exception {		
		Inscription inscription = new Inscription() ;
		try {
			Eleve eleve = new Eleve() ;
			
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
			
			eleve.setPays(rest.getForObject("http://academia-programmation/ws/pays/id/{id}", Pays.class, idPays));
			
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
			
			inscription.setEleve(eleve);
			
			inscription.setCreateur(utilisateurCourant.getEmail());
			inscription.setModificateur(utilisateurCourant.getEmail());
			inscription.setEtablissement(etablissement) ;
			inscription.setAnneeScolaire(anneeScolaire);
			inscription.setDateInscription(LocalDate.parse(dateInscription, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			inscription.setClasse(rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/id/{id}", 
					Classe.class, etablissement.getId(), idClasse));
			inscription.setStatut(StatutE.Nouveau.getLibelle());
			
			rest.postForEntity("http://academia-programmation/ws/inscription/ajout1", inscription, Inscription.class);			
			return "redirect:/admission/inscriptions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model,  etablissement) ;
			model.addAttribute("inscription", inscription) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admission/ajout-inscription-1" ;
		}
	}
	
	
	@PostMapping ("/admission/inscription/ajout2")
	public String ajouter2 (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute("etablissementCourant") Etablissement etablissement,
			Inscription inscription) throws Exception {
		try {
			logger.log(Level.INFO, "Ajout d'une inscription "+inscription);
			inscription.setModificateur(utilisateurCourant.getEmail());
			inscription.setEtablissement(etablissement);
			inscription.setAnneeScolaire(anneeScolaire);
			rest.postForObject("http://academia-programmation/ws/inscription/ajout2", inscription, Inscription.class);
			return "redirect:/admission/inscriptions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model,  etablissement) ;
			model.addAttribute("inscription", inscription) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admission/ajout-inscription-2" ;
		}
	}
	
	
	@GetMapping ("/admission/modification-inscription/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Inscription inscription = rest.getForObject("http://academia-programmation/ws/inscription/etablissement/{idEtablissement}/id/{id}", 
				Inscription.class, etablissement.getId(), id) ;
		model.addAttribute("inscriptionExistante", inscription) ;
		initDependencies (model,  etablissement) ;
		return "admission/modification-inscription" ;
	}
	
	
	
	@PostMapping ("/admission/inscription/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Inscription inscriptionExistante, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		try {
			Inscription inscription = rest.getForObject("http://academia-programmation/ws/inscription//etablissement/{idEtablissement}/id/{id}", 
					Inscription.class, etablissement.getId(), inscriptionExistante.getId()) ;
			inscription.setModificateur(utilisateurCourant.getEmail()) ;
			inscription.setEleve(inscriptionExistante.getEleve());
			inscription.setClasse(inscriptionExistante.getClasse());
			inscription.setDateInscription(inscriptionExistante.getDateInscription());
			inscription.setStatut(inscriptionExistante.getStatut());
			rest.postForObject("http://academia-programmation/ws/inscription/modification", inscription, Inscription.class);
			return "redirect:/admission/inscriptions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model,  etablissement) ;
			model.addAttribute("inscriptionExistante", inscriptionExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admission/modification-inscription" ;
		}
	}
	
	
	@GetMapping ("/admission/details-inscription/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Inscription inscription = rest.getForObject("http://academia-programmation/ws/inscription/etablissement/{idEtablissement}/id/{id}", 
				Inscription.class, etablissement.getId(), id) ;
		model.addAttribute("inscriptionExistante", inscription) ;
		model.addAttribute("eleveExistant", inscription.getEleve()) ;
		return "admission/details-inscription" ;
	}
	
	
	private void initDependencies0 (Model model, Etablissement etablissement) {
		 ResponseEntity<Classe[]> response = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 ResponseEntity<AnneeScolaire[]> response2 = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", AnneeScolaire[].class, etablissement.getId()) ;
		 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response2.getBody());
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
	}
	
	
	private void initDependencies (Model model, Etablissement etablissement) {
		 ResponseEntity<Eleve[]> response0 = rest.getForEntity("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}", Eleve[].class, etablissement.getId()) ;
		 List<Eleve> listeEleves = Arrays.asList(response0.getBody());
		 model.addAttribute("listeEleves", listeEleves) ;
		 
		 ResponseEntity<Classe[]> response = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 ResponseEntity<Pays[]> response2 = rest.getForEntity("http://academia-programmation/ws/pays/all", Pays[].class, etablissement.getId()) ;
		 List<Pays> listePays = Arrays.asList(response2.getBody());
		 model.addAttribute("listePays", listePays) ;
		 
	}
}
