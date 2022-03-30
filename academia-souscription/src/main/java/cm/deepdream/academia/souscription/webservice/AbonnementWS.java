package cm.deepdream.academia.souscription.webservice;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.deepdream.academia.souscription.service.AbonnementService;
import cm.deepdream.academia.souscription.transfert.AbonnementDTO;
import cm.deepdream.academia.souscription.transfert.EtablissementDTO;
@RestController
@RequestMapping("/ws/abonnement")
public class AbonnementWS {
	@Autowired
	private AbonnementService abonnementService ;

	
	
	@PostMapping("/ajout")
	public AbonnementDTO ajouter (@RequestBody  AbonnementDTO abonnement) {
		return abonnementService.creer(abonnement) ;
	}
	
	
	@PutMapping("/modification")
	public AbonnementDTO modifier (@RequestBody AbonnementDTO abonnement) {
		return abonnementService.modifier(abonnement) ;
	}
	
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody AbonnementDTO abonnement) {
		abonnementService.supprimer(abonnement) ;
	}
	
	
	@GetMapping("/id/{id}")
	public AbonnementDTO getById (@PathVariable("id") Long id) {
		return abonnementService.rechercher(id) ;
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<AbonnementDTO> getByEtablissement (@PathVariable("idEtablissement") Long idEtablissement) {
		EtablissementDTO etablissementDTO = EtablissementDTO.builder().id(idEtablissement).build() ;
		return abonnementService.rechercher(etablissementDTO) ;
	}
	
	
	@GetMapping("/annee/{annee}")
	public List<AbonnementDTO> getByYear (@PathVariable("annee") Integer annee) {
		LocalDate dateDebut = LocalDate.of(annee, Month.JANUARY, 1) ;
		LocalDate dateFin = LocalDate.of(annee, Month.DECEMBER, 31) ;
		return abonnementService.rechercher(dateDebut, dateFin) ;
	}
	
	
	@GetMapping("/statut/{statut}")
	public List<AbonnementDTO> getByStatut (@PathVariable("statut") String statut) {
		return abonnementService.rechercher(statut) ;
	}
	
	
	@GetMapping("/all")
	public List<AbonnementDTO> getAll () {
		return abonnementService.rechercherTout() ;
	}
	
}
