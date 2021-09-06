package cm.deepdream.academia.programmation.webservice;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.service.DetailEmploiTempsService;
import cm.deepdream.academia.programmation.service.EtablissementService;

@RestController
@RequestMapping("/ws/detailet")
public class DetailEmploiTempsWS {
	private Logger logger = Logger.getLogger(DetailEmploiTempsWS.class.getName()) ;
	@Autowired
	private DetailEmploiTempsService emploiTempsService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public DetailEmploiTemps ajout (@RequestBody  DetailEmploiTemps emploiTemps) {
		try {
			DetailEmploiTemps emploiTempsCree = emploiTempsService.creer(emploiTemps) ;
			return emploiTempsCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	public DetailEmploiTemps maj (@RequestBody DetailEmploiTemps emploiTemps) {
		try {
			DetailEmploiTemps emploiTempsMaj = emploiTempsService.modifier(emploiTemps) ;
			return emploiTempsMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{idEtablissement}/id/{id}")
	public int suppr (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			DetailEmploiTemps emploiTemps = emploiTempsService.rechercher(etablissement, id) ;
			emploiTempsService.supprimer(emploiTemps) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public DetailEmploiTemps getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			DetailEmploiTemps emploiTemps = emploiTempsService.rechercher(etablissement, id) ;
			return emploiTemps  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
}
