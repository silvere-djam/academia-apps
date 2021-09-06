package cm.deepdream.academia.programmation.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.service.ClasseService;
import cm.deepdream.academia.programmation.service.EleveService;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.NiveauService;
@RestController
@RequestMapping("/ws/eleve")
public class EleveWS {
	private Logger logger = Logger.getLogger(EleveWS.class.getName()) ;
	@Autowired
	private EleveService eleveService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private NiveauService niveauService ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	

	
	@PostMapping("/ajout")
	public ResponseEntity<Eleve> ajout (@RequestBody  Eleve eleve) {
		try {
			Eleve eleveCree = eleveService.creer(eleve) ;
			return ResponseEntity.ok(eleveCree) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Eleve>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@PostMapping("/modification")
	public ResponseEntity<Eleve> modifier ( @RequestBody Eleve eleve) {
		try {
			Eleve eleveModifie = eleveService.modifier(eleve) ;
			return ResponseEntity.ok(eleveModifie) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Eleve>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody Eleve eleve) {
		try {
			eleveService.supprimer(eleve) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Eleve getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Eleve eleve = eleveService.rechercher(etablissement, id) ;
			return eleve  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Eleve> getByEtablissement (@PathVariable("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Eleve> liste = eleveService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Eleve>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}")
	public List<Eleve> getByEleve (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("idClasse") Long idClasse) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, idClasse) ;
			List<Eleve> liste = eleveService.rechercher(etablissement, classe) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Eleve>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/niveau/{idNiveau}")
	public List<Eleve> getByElevesNiveau (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idNiveau") Long idNiveau) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Niveau niveau = niveauService.rechercher(etablissement, idNiveau) ;
			List<Eleve> liste = eleveService.rechercher(etablissement, niveau) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Eleve>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/situation/{situation}")
	public List<Eleve> getByEleves (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("situation") String situation) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Eleve> liste = eleveService.rechercher(situation, etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Eleve>() ;
		}
	}
	
	
	@GetMapping("/photo/etablissement/{idEtablissement}/id/{id}")
	public Photo photo (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Eleve eleve = eleveService.rechercher(etablissement, id) ;
			Photo photo = eleve.getPhoto() ;
			byte[] allBytes = eleveService.download(photo) ;
			photo.setBytesStr(Base64.encodeBase64String(allBytes));
			return photo  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
}
