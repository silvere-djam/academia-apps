package cm.deepdream.academia.integration.espaces;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Inscription;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.programmation.enums.StatutE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;

@RestController
@RequestMapping("/api/eleve")
public class EleveAPI {
	private Logger logger = Logger.getLogger(EleveAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	
	@PostMapping("/ajout")
	public Eleve ajouter (@RequestParam("matricule") String matricule,@RequestParam("nom") String nom, 
			 @RequestParam("prenom") String prenom, @RequestParam("sexe") String sexe, @RequestParam("dateNaissance") String dateNaissance,
			 @RequestParam(value = "lieuNaissance", required = false) String lieuNaissance, @RequestParam("dateAdmission") String dateAdmission,
			 @RequestParam("pays") String libellePays, @RequestParam("nomParent") String nomParent, @RequestParam("adresseParent") String adresseParent,
			 @RequestParam(value="adresse", required=false) String adresse, @RequestParam("telephone") String telephone, @RequestParam("email") String email,
			 @RequestParam("idEtablissement") Long idEtablissement, MultipartFile file){
		try {
//			logger.info("matricule="+matricule.substring(1, matricule.length()-1));
//			logger.info("nom="+nom.substring(1, nom.length()-1));
//			logger.info("prenom="+prenom.substring(1, prenom.length()-1));
//			logger.info("sexe="+sexe);
//			logger.info("dateNaissance="+dateNaissance);
//			logger.info("lieuNaissance="+lieuNaissance);
//			logger.info("dateAdmission="+dateAdmission);
//			logger.info("libellePays="+libellePays);
//			logger.info("nomParent="+nomParent);
//			logger.info("adresseParent="+adresseParent);
//			logger.info("adresse="+adresse);
//			logger.info("telephone="+telephone);
//			logger.info("email="+email);
//			logger.info("idEtablissement="+idEtablissement);
//			logger.info("file="+file.getName());
			Eleve eleve = new Eleve() ;
			eleve.setMatricule(matricule.substring(1, matricule.length()-1));
			eleve.setNom(nom.substring(1, nom.length()-1));
			eleve.setPrenom(prenom.substring(1, prenom.length()-1));
			eleve.setSexe(sexe.substring(1, sexe.length()-1));
			eleve.setDateNaissance(LocalDate.parse(dateNaissance.substring(1, dateNaissance.length()-1), 
					DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			eleve.setDateAdmission(LocalDate.parse(dateAdmission.substring(1, dateAdmission.length()-1), 
					DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			eleve.setLieuNaissance(lieuNaissance.substring(1, lieuNaissance.length()-1));
			eleve.setEmail(email.substring(1, email.length()-1));
			eleve.setTelephone(telephone.substring(1, telephone.length()-1));
			eleve.setAdresse(adresse.substring(1, adresse.length()-1));
			
			eleve.setNomParent(nomParent.substring(1, nomParent.length()-1));
			eleve.setAdresseParent(adresseParent.substring(1, adresseParent.length()-1));
			
			try{
				eleve.setPays(rest.getForObject("http://academia-programmation/ws/pays/libelle/{libelle}", 
						Pays.class, libellePays.substring(1, libellePays.length()-1)));
			}catch(Exception ex) {}
			
			try{
				eleve.setEtablissement(rest.getForObject("http://academia-programmation/ws/etablissement/id/{id}", Etablissement.class, idEtablissement));
			}catch(Exception ex) {}
			
			eleve.setStatut(StatutE.Nouveau.getLibelle());
			
			logger.info("etablissement="+eleve.getEtablissement());
			 
			Photo photo =  new Photo();
			if(file.getBytes() != null && file.getBytes().length > 0) {
				 photo.setContentType(file.getContentType());
				 photo.setBytesStr(Base64.encodeBase64String(file.getBytes()));
				 photo.setSize(file.getSize());
			     eleve.setPhoto(photo);
			}else {
				 logger.info("Find the default user image ");
				 File defaultFile = ResourceUtils.getFile("classpath:default/unknow-person.png");
				 logger.info("Path to  logo "+defaultFile.getAbsolutePath());
				 byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
				 photo.setContentType("image/png");
				 photo.setBytesStr(Base64.encodeBase64String(allBytes));
				 photo.setSize(allBytes.length*1L);
				 eleve.setPhoto(photo);
			}
			logger.info("Ajout d'un nouvel élève");
			Eleve eleveCree = rest.postForObject("http://academia-programmation/ws/eleve/ajout", eleve, Eleve.class) ;
			return eleveCree ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null ;
		}
	}
	
	
	@PostMapping("/photo/modification")
	public Integer modifierPhoto (@RequestParam("idEleve") String idEleve, 
			@RequestParam("idEtablissement") String idEtablissement, MultipartFile file){
		try {
			logger.info("idEtablissement="+idEtablissement);
			logger.info("idEleve="+idEleve);
			Eleve eleve = rest.getForObject("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/id/{id}", 
					Eleve.class, idEtablissement, idEleve) ;
			Photo photo =  new Photo();
			photo.setModified(true) ;
			if(file.getBytes() != null && file.getBytes().length > 0) {
				 photo.setContentType(file.getContentType());
				 photo.setBytesStr(Base64.encodeBase64String(file.getBytes()));
				 photo.setSize(file.getSize());
			     eleve.setPhoto(photo);
			}else {
				 logger.info("Find the default user image ");
				 File defaultFile = ResourceUtils.getFile("classpath:default/unknow-person.png");
				 logger.info("Path to  logo "+defaultFile.getAbsolutePath());
				 byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
				 photo.setContentType("image/png");
				 photo.setBytesStr(Base64.encodeBase64String(allBytes));
				 photo.setSize(allBytes.length*1L);
				 eleve.setPhoto(photo);
			}
			
			logger.info("Ajout d'une nouvelle photo d'élève");
			rest.postForObject("http://academia-programmation/ws/eleve/photo/modification", eleve, Eleve.class) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return 0 ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/eleve/{idEleve}")
	public Eleve getTousLesEleves(@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idEleve") Long idEleve){
		 logger.info("Recherche  de l'élève "+idEleve+" de l'établissement "+idEtablissement);
		 Eleve eleve = rest.getForObject("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/eleve/{idEleve}",  Eleve.class, idEtablissement, idEleve) ;
		 return eleve ;
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Eleve> getEleves(@PathVariable("idEtablissement") Long idEtablissement){
		 
		 logger.info("Recherche des élèves de l'établissement '"+idEtablissement+"'");
		 
		 ResponseEntity<Eleve[]> response = rest.getForEntity("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}", 
				 Eleve[].class, idEtablissement) ;
		 
		 List<Eleve> listeEleves = Arrays.asList(response.getBody());

		 return listeEleves ;
	}
	

	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<Eleve> getElevesInscrits(@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idAnneeScolaire") Long idAnneeScolaire){
		 
		 logger.info("Recherche des élèves de l'établissement '"+idEtablissement+"'");
		 
		 ResponseEntity<Inscription[]> response = rest.getForEntity("http://academia-programmation/ws/inscription/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Inscription[].class, idEtablissement, idAnneeScolaire) ;
		 
		 List<Inscription> listeInscriptions = Arrays.asList(response.getBody());
		 
		 List<Eleve> listeEleves = listeInscriptions.stream().map(inscription -> {
			 			Eleve eleve = inscription.getEleve() ;
			 			eleve.setPhoto(null);
			 			return eleve ;
		 			}).distinct().collect(Collectors.toList()) ;

		 return listeEleves ;
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeeScolaire}")
	public List<Eleve> getElevesClasse (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idClasse") Long idClasse, @PathVariable("idAnneeeScolaire") Long idAnneeeScolaire){
		 
		 logger.info("Recherche des élèves de l'établissement '"+idEtablissement+"' et de la classe '"+idClasse+"'");
		 
		 ResponseEntity<Inscription[]> response = rest.getForEntity("http://academia-programmation/ws/inscription/etablissement/{idEtablissement}/classe/{idClasse}/anneescolaire/{idAnneeScolaire}", 
				 Inscription[].class, idEtablissement, idClasse, idAnneeeScolaire) ;
		 
		 List<Inscription> listeInscriptions = Arrays.asList(response.getBody());
		 List<Eleve> listeEleves = listeInscriptions.stream().map(i -> {
			 Eleve eleve = i.getEleve() ;
			 eleve.setPhoto(null);
			 return eleve ;
		 }).collect(Collectors.toList()) ;

		 return listeEleves ;
	}
	
	
	@GetMapping("/photo/etablissement/{idEtablissement}/id/{id}")
	public Photo getPhoto(Model model, @PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		logger.info("Photo de l'élève : "+id);
		Photo photo = rest.getForObject("http://academia-programmation/ws/eleve/photo/etablissement/{idEtablissement}/id/{id}", Photo.class, idEtablissement, id) ;
		return photo ;
	}
	
	@GetMapping("/photo2/etablissement/{idEtablissement}/id/{id}")
	public Photo getPhoto2(Model model, @PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		logger.info("Photo de l'élève : "+id);
		Photo photo = rest.getForObject("http://academia-programmation/ws/eleve/photo/etablissement/{idEtablissement}/id/{id}", Photo.class, idEtablissement, id) ;
		if(photo == null) {
			return null ;
		}

		return photo ;
	}

}
