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

import cm.deepdream.academia.souscription.model.Localite;
import cm.deepdream.academia.souscription.service.LocaliteService;

@RestController
@RequestMapping("/ws/localite")
public class LocaliteWS {
	private LocaliteService localiteService ;
	
	
	public LocaliteWS(LocaliteService localiteService) {
		this.localiteService = localiteService;
	}

	
	@PostMapping("/ajout")
	public Localite ajouter (@RequestBody  Localite localite) {
		return localiteService.creer(localite) ;
	}
	
	
	@PutMapping("/modification")
	public Localite modifier (@RequestBody Localite localite) {
		return localiteService.modifier(localite) ;
	}
	
	
	@DeleteMapping("/suppression")
	public void suppr (@RequestBody Localite localite) {
		localiteService.supprimer(localite) ;
	}
	
	
	@GetMapping("/id/{id}")
	public Localite getById (@PathVariable("id") Long id) {
		return localiteService.rechercher(id) ;
	}
	
	
	@GetMapping("/all")
	public List<Localite> getAll () {
		return localiteService.rechercherTout(new Localite()) ;
	}
	
}
