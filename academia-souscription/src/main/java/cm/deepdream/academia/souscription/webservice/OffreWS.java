package cm.deepdream.academia.souscription.webservice;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.deepdream.academia.souscription.model.Offre;
import cm.deepdream.academia.souscription.service.OffreService;
import cm.deepdream.academia.souscription.transfert.OffreDTO;

@RestController
@RequestMapping("/ws/offre")
public class OffreWS {
	private OffreService offreService ;
	
	
	public OffreWS(OffreService offreService) {
		this.offreService = offreService;
	}


	@PostMapping("/ajout")
	public OffreDTO ajouter (@RequestBody  OffreDTO offre) {
		return offreService.creer(offre) ;
	}
	
	
	@PutMapping("/modification")
	public OffreDTO modifier (@RequestBody OffreDTO offre) {
		return offreService.modifier(offre) ;
	}
	
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody OffreDTO offre) {
		offreService.supprimer(offre) ;
	}
	
	
	
	@GetMapping("/id/{id}")
	public OffreDTO getById (@PathVariable("id") Long id) {
		return offreService.rechercher(id) ;
	}
	
	
	@GetMapping("/all")
	public List<OffreDTO> getAll () {
		return  offreService.rechercherTout() ;
	}
	
	
	@GetMapping("/count/{nbEleves}")
	public BigDecimal getPrice (@PathVariable("nbEleves") Integer nbEleves) {
		return  offreService.rechercherCout(nbEleves) ;
	}
	
	
	@GetMapping("/utilisateurs/{nbEleves}")
	public Long getUtilisateurs(@PathVariable("nbEleves") Integer nbEleves) {
		return 0L ;
	}
}
