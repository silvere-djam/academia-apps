package cm.deepdream.academia.security.webservice;
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
import org.springframework.web.bind.annotation.RestController;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Candidat;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.security.service.EtablissementService;
import cm.deepdream.academia.security.service.UtilisateurService;

@RestController
@RequestMapping("/ws/utilisateur")
public class UtilisateurWS {
	private Logger logger = Logger.getLogger(UtilisateurWS.class.getName()) ;
	@Autowired
	private UtilisateurService utilisateurService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public ResponseEntity<Utilisateur> ajout (@RequestBody Utilisateur utilisateur) {
		try {
			Utilisateur utilisateurCree = utilisateurService.creer(utilisateur) ;
			return ResponseEntity.ok(utilisateurCree) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Utilisateur>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@PostMapping("/modification")
	public ResponseEntity<Utilisateur> modifier (@RequestBody Utilisateur utilisateur) {
		try {
			Utilisateur utilisateurModifie = utilisateurService.modifier(utilisateur) ;
			return ResponseEntity.ok(utilisateurModifie) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Utilisateur>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@PostMapping("/motdepasse/definition")
	public ResponseEntity<Utilisateur> definierMotDePasse (@RequestBody Utilisateur utilisateur) {
		try {
			Utilisateur utilisateurModifie = utilisateurService.definirMotDePasse(utilisateur) ;
			return ResponseEntity.ok(utilisateurModifie) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Utilisateur>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@PostMapping("/motdepasse/modification")
	public ResponseEntity<Utilisateur> modifierMotDePasse (@RequestBody Utilisateur utilisateur) {
		try {
			Utilisateur utilisateurModifie = utilisateurService.modifierMotDePasse(utilisateur) ;
			return ResponseEntity.ok(utilisateurModifie) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return  new ResponseEntity<Utilisateur>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	@DeleteMapping("/suppr/etablissement/{idEtablissement}/{id}")
	public ResponseEntity<Integer> supprimer (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable ("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Utilisateur utilisateur = utilisateurService.rechercher(etablissement, id) ;
			utilisateurService.supprimer(utilisateur) ;
			return ResponseEntity.ok(1) ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Utilisateur getById (@PathVariable("idEtablissement") Long idEtablissement, @PathVariable("id") Long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Utilisateur utilisateur = utilisateurService.rechercher(etablissement, id) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Utilisateur getById2 (@PathVariable("id") Long id) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercher(id) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/telephone/{telephone}")
	public Utilisateur getByLogin (@PathVariable("telephone") String telephone) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercherTelephone(telephone) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Utilisateur> getUtilisateurs (@PathVariable("idEtablissement") Long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Utilisateur> listeUtilisateurs = utilisateurService.rechercher(etablissement) ;
			return listeUtilisateurs ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Utilisateur>() ;
		}
	}
	
	
	@GetMapping("/id/{id}/code/{codeActivation}")
	public Utilisateur getUtilisateur (
	@PathVariable("id") Long id, @PathVariable("codeActivation") String codeActivation) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercher(id, codeActivation) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/activate/etablissement/{idEtablissement}/id/{id}")
	public Utilisateur activate (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Utilisateur utilisateur = utilisateurService.rechercher(etablissement, id) ;
			Utilisateur utilisateurActive = utilisateurService.activer(utilisateur) ;
			return utilisateurActive ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/desactivate/etablissement/{idEtablissement}/id/{id}")
	public Utilisateur desactivate (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Utilisateur utilisateur = utilisateurService.rechercher(etablissement, id) ;
			Utilisateur utilisateurDesactive = utilisateurService.suspendre(utilisateur) ;
			return utilisateurDesactive ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
}
