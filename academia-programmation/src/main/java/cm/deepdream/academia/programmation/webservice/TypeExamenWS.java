package cm.deepdream.academia.programmation.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.TypeExamen;
import cm.deepdream.academia.programmation.service.EtablissementService;
import cm.deepdream.academia.programmation.service.NiveauService;
import cm.deepdream.academia.programmation.service.TypeExamenService;
import cm.deepdream.academia.souscription.data.Etablissement;

@RestController
@RequestMapping("/ws/typeexamen")
public class TypeExamenWS {
	private Logger logger = Logger.getLogger(TypeExamenWS.class.getName()) ;
	@Autowired
	private TypeExamenService typeExamenService ;
	@Autowired
	private NiveauService niveauService ;
	@Autowired
	private EtablissementService etablissementService ;
	

	
	@PostMapping("/ajout")
	public ResponseEntity<TypeExamen> ajouter (@RequestBody  TypeExamen typeExamen) {
		try {
			TypeExamen typeExamenCree = typeExamenService.creer(typeExamen) ;
			return ResponseEntity.ok(typeExamenCree) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<TypeExamen>(HttpStatus.INTERNAL_SERVER_ERROR)  ;
		}
	}
	
	@PostMapping("/modification")
	public ResponseEntity<TypeExamen> modifier (@RequestBody TypeExamen typeExamen) {
		try {
			TypeExamen typeExamenModifie = typeExamenService.modifier(typeExamen) ;
			return ResponseEntity.ok(typeExamenModifie) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<TypeExamen>(HttpStatus.INTERNAL_SERVER_ERROR)  ;
		}
	}
	
	@DeleteMapping("/suppr")
	public ResponseEntity<Integer> suppr (@RequestBody TypeExamen typeExamen) {
		try {
			typeExamenService.supprimer(typeExamen) ;
			return ResponseEntity.ok(1) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public TypeExamen getById (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			TypeExamen typeExamen = typeExamenService.rechercher(etablissement, id) ;
			return typeExamen  ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/niveau/{idNiveau}")
	public List<TypeExamen> getTypesExamen_0 (@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idNiveau") Long idNiveau) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Niveau niveau = niveauService.rechercher(etablissement, idNiveau) ;
			List<TypeExamen> liste = typeExamenService.rechercher(etablissement, niveau) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<TypeExamen>() ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<TypeExamen> getTypesExamen_1 (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<TypeExamen> liste = typeExamenService.rechercher(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<TypeExamen>() ;
		}
	}
	
}
