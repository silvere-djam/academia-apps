package cm.deepdream.academia.programmation.webservice;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cm.deepdream.academia.programmation.service.UEService;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.service.ClasseService;
import cm.deepdream.academia.programmation.service.EtablissementService;
@RestController
@RequestMapping("/ws/ue")
public class UEWS {
	private Logger logger = Logger.getLogger(UEWS.class.getName()) ;
	@Autowired
	private UEService ueService ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public UE ajout (@RequestBody  UE ue) {
		try {
			UE ueCree = ueService.creer(ue) ;
			return ueCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/modification")
	public UE maj (@RequestBody UE ue) {
		try {
			UE ueMaj = ueService.modifier(ue) ;
			return ueMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	public int suppr (@RequestBody UE ue) {
		try {
			ueService.supprimer(ue) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}")
	public List<UE> getUEs (@PathVariable("idEtablissement") Long idEtablissement,
			@PathVariable("idClasse") Long idClasse) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, idClasse) ;
			List<UE> liste = ueService.rechercher(classe) ;
			return liste  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<UE>() ; 
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public UE getUE (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			UE ue = ueService.rechercher(etablissement, id) ;
			return ue ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}

	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<UE> getUEs (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<UE> liste = ueService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<UE>() ;
		}
	}
	
}
