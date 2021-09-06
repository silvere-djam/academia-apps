package cm.deepdream.academia.viescolaire.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.viescolaire.service.UEService;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.service.ClasseService;
import cm.deepdream.academia.viescolaire.service.EtablissementService;
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
