package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.EtablissementRepository;
import cm.deepdream.academia.souscription.transfert.EtablissementDTO;
import cm.deepdream.academia.souscription.exceptions.EtablissementDuplicationException;
import cm.deepdream.academia.souscription.exceptions.EtablissementNotFoundException;
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Localite;

@Transactional
@Service
public class EtablissementService {
	private EtablissementRepository etablissementRepository ;
	
	
	public EtablissementService(EtablissementRepository etablissementRepository) {
		this.etablissementRepository = etablissementRepository;
	}


	public EtablissementDTO creer (EtablissementDTO etablissementDTO) {
		if(etablissementRepository.existsByLibelle(etablissementDTO.getLibelle())) {
			throw new EtablissementDuplicationException(String.valueOf(etablissementDTO)) ;
		}
		
		Etablissement etablissementEntry = this.transformer(etablissementDTO) ;
		Etablissement etablissementReturn = etablissementRepository.save(etablissementEntry) ;
		return this.transformer(etablissementReturn) ;
	}
	
	
	public EtablissementDTO modifier (EtablissementDTO etablissementDTO) {
		Etablissement etablissementEntry = this.transformer(etablissementDTO) ;
		Etablissement etablissementExistant = etablissementRepository.findById(etablissementDTO.getId())
				                                     .map(Function.identity())
				                                     .orElseThrow(() -> new EtablissementNotFoundException(String.valueOf(etablissementDTO.getId()))) ;
		etablissementExistant.setBoitePostale(etablissementEntry.getBoitePostale()) ;
		etablissementExistant.setCycle(etablissementEntry.getCycle()) ;
		etablissementExistant.setEmail(etablissementEntry.getEmail()) ;
		etablissementExistant.setLibelle(etablissementEntry.getLibelle()) ;
		etablissementExistant.setLocalite(etablissementExistant.getLocalite()) ;
		etablissementExistant.setNbElevesApprox(etablissementEntry.getNbElevesApprox()) ;
		etablissementExistant.setTelephone(etablissementEntry.getTelephone()) ;
		
		Etablissement etablissementReturn = etablissementRepository.save(etablissementEntry) ;
		return this.transformer(etablissementReturn) ;
	}
	
	
	public void supprimer (Long id)  {
		etablissementRepository.findById(id)
                			   .ifPresentOrElse(etablissementRepository::delete, 
                					 () -> {
                						 	throw new EtablissementNotFoundException(String.valueOf(id));
                					 }) ;
	}
	

	public void supprimer (EtablissementDTO etablissementDTO) {
		etablissementRepository.findById(etablissementDTO.getId())
		   .ifPresentOrElse(etablissementRepository::delete, 
				 () -> {
					 	throw new EtablissementNotFoundException(String.valueOf(etablissementDTO.getId()));
				 }) ;
	}
	
	
	public EtablissementDTO rechercher (Long id)  {
		return etablissementRepository.findById(id)
				                      .map(Function.identity())
				                      .map(this::transformer)
				                      .orElseThrow(() ->new EtablissementNotFoundException(String.valueOf(id))) ;
	}
	
	
	
	public List<EtablissementDTO> rechercherTout () {
		Iterable<Etablissement> itEtablissements = etablissementRepository.findAll() ;
		List<EtablissementDTO> listeEtablissementsDTO = new ArrayList<>();
		itEtablissements.forEach(etablissement ->  listeEtablissementsDTO.add(this.transformer(etablissement))) ;
		return listeEtablissementsDTO ;
	}
	
	
	private EtablissementDTO transformer (Etablissement etablissement) {
		Localite localite = etablissement.getLocalite() ;
		return EtablissementDTO.builder().id(etablissement.getId()).libelle(etablissement.getLibelle())
				               .cycle(etablissement.getCycle()).email(etablissement.getEmail())
				               .nbElevesApprox(etablissement.getNbElevesApprox())
				               .telephone(etablissement.getTelephone())
				               .boitePostale(etablissement.getBoitePostale())
				               .idLocalite(localite == null ? null : localite.getId())
				               .libelleLocalite(localite == null ? null : localite.getLibelle())
							   .build() ;
	}
	
	
	private Etablissement transformer (EtablissementDTO etablissementDTO) {
		Localite localite = etablissementDTO.getIdLocalite() == null ? null : Localite.builder().id(etablissementDTO.getIdLocalite()).build() ;
		return Etablissement.builder().id(etablissementDTO.getId()).libelle(etablissementDTO.getLibelle())
				               .cycle(etablissementDTO.getCycle()).email(etablissementDTO.getEmail())
				               .nbElevesApprox(etablissementDTO.getNbElevesApprox())
				               .telephone(etablissementDTO.getTelephone())
				               .boitePostale(etablissementDTO.getBoitePostale())
				               .localite(localite)
							   .build() ;
	}
}
