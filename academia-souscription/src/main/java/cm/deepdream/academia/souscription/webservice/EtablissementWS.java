package cm.deepdream.academia.souscription.webservice;
import java.util.List;
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

import cm.deepdream.academia.souscription.model.Logo;
import cm.deepdream.academia.souscription.service.EtablissementService;
import cm.deepdream.academia.souscription.transfert.EtablissementDTO;

@RestController
@RequestMapping("/ws/etablissement")
public class EtablissementWS {
	private Logger logger = Logger.getLogger(EtablissementWS.class.getName()) ;
	@Autowired
	private EtablissementService etablissementService ;

	
	
	@PostMapping("/ajout")
	public EtablissementDTO ajouter (@RequestBody  EtablissementDTO etablissement) {
		return etablissementService.creer(etablissement) ; 
	}
	
	@PutMapping("/modification")
	@ResponseStatus(code =  HttpStatus.OK)
	public EtablissementDTO modifier (@RequestBody EtablissementDTO etablissement) {
		return etablissementService.modifier(etablissement) ;
	}
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody EtablissementDTO etablissement) {
		etablissementService.supprimer(etablissement) ;
	}
	
	@GetMapping("/id/{id}")
	public EtablissementDTO getById (@PathVariable("id") Long id) {
		return etablissementService.rechercher(id) ;
	}
	
	@GetMapping("/logo/id/{id}")
	public Logo getLogo (@PathVariable("id") Long id) {
		return null ;
	}
	
	
	@GetMapping("/all")
	public List<EtablissementDTO> getAll () {
		return etablissementService.rechercherTout() ; 
	}
	
}
