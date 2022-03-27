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

import cm.deepdream.academia.souscription.service.LocaliteService;
import cm.deepdream.academia.souscription.transfert.LocaliteDTO;
import cm.deepdream.academia.souscription.transfert.RegionDTO;

@RestController
@RequestMapping("/ws/localite")
public class LocaliteWS {
	private LocaliteService localiteService ;
	
	
	public LocaliteWS(LocaliteService localiteService) {
		this.localiteService = localiteService;
	}

	
	@PostMapping("/ajout")
	public LocaliteDTO ajouter (@RequestBody  LocaliteDTO localite) {
		return localiteService.creer(localite) ;
	}
	
	
	@PutMapping("/modification")
	public LocaliteDTO modifier (@RequestBody LocaliteDTO localite) {
		return localiteService.modifier(localite) ;
	}
	
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody LocaliteDTO localite) {
		localiteService.supprimer(localite) ;
	}
	
	
	@GetMapping("/id/{id}")
	public LocaliteDTO getById (@PathVariable("id") Long id) {
		return localiteService.rechercher(id) ;
	}
	
	
	@GetMapping("/region/{idRegion}")
	public List<LocaliteDTO> getLocalitesRegion (@PathVariable ("idRegion") Long idRegion) {
		RegionDTO regionDTO = RegionDTO.builder().id(idRegion).build() ;
		return localiteService.rechercher(regionDTO) ;
	}
	
	
	@GetMapping("/all")
	public List<LocaliteDTO> getAll () {
		return localiteService.rechercherTout() ;
	}
	
}
