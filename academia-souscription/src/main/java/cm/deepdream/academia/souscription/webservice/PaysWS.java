package cm.deepdream.academia.souscription.webservice;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.souscription.model.Pays;
import cm.deepdream.academia.souscription.service.PaysService;
import cm.deepdream.academia.souscription.transfert.PaysDTO;

@RestController
@RequestMapping("/ws/pays")
public class PaysWS {
	private PaysService paysService ;
	
	
	public PaysWS(PaysService paysService) {
		this.paysService = paysService;
	}

	
	@PostMapping("/ajout")
	public PaysDTO ajouter (@RequestBody  PaysDTO pays) {
		return paysService.creer(pays) ;
	}
	
	
	@PutMapping("/modification")
	public PaysDTO modifier (@RequestBody PaysDTO pays) {
		return paysService.modifier(pays) ;
	}
	
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody PaysDTO pays) {
		paysService.supprimer(pays) ;
	}
	
	
	@GetMapping("/id/{id}")
	public PaysDTO getById (@PathVariable("id") Long id) {
		return paysService.rechercher(id) ;
	}
	
	
	@GetMapping("/all")
	public List<PaysDTO> getAll () {
		return paysService.rechercherTout() ;
	}
	
}
