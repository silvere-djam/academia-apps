package cm.deepdream.academia.security.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.security.service.EtablissementService;
import cm.deepdream.academia.security.service.ClasseService;

@RestController
@RequestMapping("/ws/classe")
public class ClasseWS {
	private Logger logger = Logger.getLogger(ClasseWS.class.getName()) ;
	@Autowired
	private ClasseService classeService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Classe getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Classe classe = classeService.rechercher(etablissement, id) ;
			return classe ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Classe> getClasses (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Classe> listeClasses = classeService.rechercher(etablissement) ;
			return listeClasses ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Classe>() ;
		}
	}
	
}
