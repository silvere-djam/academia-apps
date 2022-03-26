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

import cm.deepdream.academia.souscription.service.ResponsableService;
import cm.deepdream.academia.souscription.transfert.ResponsableDTO;

@RestController
@RequestMapping("/ws/responsable")
public class ResponsableWS {
	private ResponsableService responsableService ;
	
	
	public ResponsableWS(ResponsableService responsableService) {
		this.responsableService = responsableService;
	}

	
	@PostMapping("/ajout")
	public ResponsableDTO ajouter (@RequestBody  ResponsableDTO responsableDTO) {
		return responsableService.creer(responsableDTO) ;
	}
	
	
	@PutMapping("/modification")
	public ResponsableDTO modifier (@RequestBody ResponsableDTO responsableDTO) {
		return responsableService.modifier(responsableDTO) ;
	}
	
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody ResponsableDTO responsableDTO) {
		responsableService.supprimer(responsableDTO) ;
	}
	
	
	@GetMapping("/id/{id}")
	public ResponsableDTO getById (@PathVariable("id") Long id) {
		return responsableService.rechercher(id) ;
	}
	
	
	@GetMapping("/all")
	public List<ResponsableDTO> getAll () {
		return responsableService.rechercherTout() ;
	}
	
}
