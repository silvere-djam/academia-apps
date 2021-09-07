package cm.deepdream.academia.integration.programmation;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Inscription;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.programmation.enums.StatutE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;

@RestController
@RequestMapping("/api/inscription")
public class InscriptionAPI {
	private Logger logger = Logger.getLogger(InscriptionAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	
	@PostMapping("/ajout")
	public int ajouter (@RequestParam("idEleve") Long idEleve, @RequestParam("idClasse") Long idClasse, 
			 @RequestParam("dateInscription") String dateInscriptionStr, @RequestParam("statut") String statut, 
			 @RequestParam("idEtablissement") Long idEtablissement, @RequestParam("idAnneeScolaire") Long idAnneeScolaire){
		try {
			logger.info("Ajout d'une inscription d'un élève") ;
			Etablissement etablissement = new Etablissement() ;
			etablissement.setId(idEtablissement);
			
			AnneeScolaire anneeScolaire = new AnneeScolaire() ;
			anneeScolaire.setId(idAnneeScolaire);
			anneeScolaire.setEtablissement(etablissement);
			
			Eleve eleve = new Eleve() ;
			eleve.setId(idEleve);
			eleve.setEtablissement(etablissement);
			
			Classe classe = new Classe() ;
			classe.setId(idClasse);
			classe.setEtablissement(etablissement);
			
			Inscription inscription = new Inscription() ;
			inscription.setDateInscription(LocalDate.parse(dateInscriptionStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			inscription.setEleve(eleve);
			inscription.setClasse(classe);
			inscription.setStatut(statut);
			inscription.setAnneeScolaire(anneeScolaire);
			inscription.setEtablissement(etablissement);

			Inscription inscriptionCree = rest.postForObject("http://academia-programmation/ws/inscription/ajout2", inscription, 
					Inscription.class) ;
			logger.info("Inscription réussie") ;
			return 1 ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return 0 ;
		}
	}
	
	
}
