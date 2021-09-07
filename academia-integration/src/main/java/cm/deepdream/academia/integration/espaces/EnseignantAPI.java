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
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.programmation.enums.StatutE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;

@RestController
@RequestMapping("/api/enseignant")
public class EnseignantAPI {
	private Logger logger = Logger.getLogger(EnseignantAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@PostMapping("/ajout")
	public int ajouter (@RequestParam("civilite") String civilite, @RequestParam("matricule") String matricule, @RequestParam("nom") String nom, 
			 @RequestParam("prenom") String prenom, @RequestParam("sexe") String sexe, @RequestParam("dateNaissance") String dateNaissance,
			 @RequestParam(value = "lieuNaissance", required = false) String lieuNaissance, @RequestParam("dateAdmission") String dateAdmission,
			 @RequestParam("pays") String libellePays, @RequestParam("nomParent") String nomParent, @RequestParam("adresseParent") String adresseParent,
			 @RequestParam(value="situation") String situation, @RequestParam("telephone") String telephone, @RequestParam("email") String email,
			 @RequestParam("idEtablissement") Long idEtablissement, MultipartFile file){
		try {
			Enseignant enseignant = new Enseignant() ;
			enseignant.setCivilite(civilite);
			enseignant.setMatricule(matricule.substring(1, matricule.length()-1));
			enseignant.setNom(nom.substring(1, nom.length()-1));
			enseignant.setPrenom(prenom.substring(1, prenom.length()-1));
			enseignant.setSexe(sexe.substring(1, sexe.length()-1));
			enseignant.setDateNaissance(LocalDate.parse(dateNaissance.substring(1, dateNaissance.length()-1), 
					DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			enseignant.setDatePriseService(LocalDate.parse(dateAdmission.substring(1, dateAdmission.length()-1), 
					DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			enseignant.setLieuNaissance(lieuNaissance.substring(1, lieuNaissance.length()-1));
			enseignant.setEmail(email.substring(1, email.length()-1));
			enseignant.setTelephone(telephone.substring(1, telephone.length()-1));
			enseignant.setSituation(situation.substring(1, situation.length()-1));
			
		
			
			try{
				enseignant.setEtablissement(rest.getForObject("http://academia-programmation/ws/etablissement/id/{id}", Etablissement.class, idEtablissement));
			}catch(Exception ex) {}

			 
			Photo photo =  new Photo();
			if(file.getBytes() != null && file.getBytes().length > 0) {
				 photo.setContentType(file.getContentType());
				 photo.setBytesStr(Base64.encodeBase64String(file.getBytes()));
				 photo.setSize(file.getSize());
				 enseignant.setPhoto(photo);
			}else {
				 logger.info("Find the default user image ");
				 File defaultFile = ResourceUtils.getFile("classpath:default/unknow-person.png");
				 logger.info("Path to  logo "+defaultFile.getAbsolutePath());
				 byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
				 photo.setContentType("image/png");
				 photo.setBytesStr(Base64.encodeBase64String(allBytes));
				 photo.setSize(allBytes.length*1L);
				 enseignant.setPhoto(photo);
			}
			logger.info("Ajout d'un nouvel élève");
			Enseignant enseignantCree = rest.postForObject("http://academia-programmation/ws/enseignant/ajout", enseignant, Enseignant.class) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/situation/{situation}")
	public List<Enseignant> getById(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("situation") String situation){
		 logger.info("Recherche  des enseignants en situation "+situation+" de l'établissement "+idEtablissement);
		 ResponseEntity<Enseignant[]> response = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}/situation/{situation}",  
				 Enseignant[].class, idEtablissement, situation) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response.getBody());
		 return listeEnseignants ;
	}
	
	@GetMapping("/photo/etablissement/{idEtablissement}/id/{id}")
	public Photo getPhoto(Model model, @PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		logger.info("Photo de l'enseignant : "+id);
		Photo photo = rest.getForObject("http://academia-programmation/ws/enseignant/photo/etablissement/{idEtablissement}/id/{id}", Photo.class, idEtablissement, id) ;
		return photo ;
	}
	
	
}
