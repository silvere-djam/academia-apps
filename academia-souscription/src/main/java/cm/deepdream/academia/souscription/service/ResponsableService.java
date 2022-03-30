package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.ResponsableRepository;
import cm.deepdream.academia.souscription.transfert.ResponsableDTO;
import cm.deepdream.academia.souscription.exceptions.ResponsableDuplicationException;
import cm.deepdream.academia.souscription.exceptions.ResponsableNotFoundException;
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Responsable;

@Service
@Transactional
public class ResponsableService {
	private ResponsableRepository responsableRepository ;
	
	
	public ResponsableService(ResponsableRepository responsableRepository) {
		this.responsableRepository = responsableRepository;
	}


	public ResponsableDTO creer (ResponsableDTO responsableDTO) {
		if(responsableRepository.existsByMatricule(responsableDTO.getMatricule())) {
			throw new ResponsableDuplicationException(String.valueOf(responsableDTO)) ;
		}
		Responsable responsableEntry = this.transformer(responsableDTO) ;
		Responsable responsableReturn = responsableRepository.save(responsableEntry) ;
		return this.transformer(responsableReturn) ;
	}	
	
	
	public ResponsableDTO modifier (ResponsableDTO responsableDTO) {
		Responsable responsableEntry = this.transformer(responsableDTO) ;
		Responsable responsableExistant = responsableRepository.findById(responsableDTO.getId())
				                          .map(Function.identity())
				                          .orElseThrow(() -> new ResponsableNotFoundException(String.valueOf(responsableEntry.getId()))) ;
		responsableExistant.setMatricule(responsableEntry.getMatricule()) ;
		responsableExistant.setNom(responsableEntry.getNom()) ;
		responsableExistant.setPrenom(responsableEntry.getPrenom()) ;
		responsableExistant.setSexe(responsableEntry.getSexe()) ;
		responsableExistant.setEmail(responsableEntry.getEmail()) ;
		responsableExistant.setTelephone(responsableEntry.getTelephone()) ;
		responsableExistant.setDateNaissance(responsableEntry.getDateNaissance()) ;
		responsableExistant.setLieuNaissance(responsableEntry.getLieuNaissance()) ;
		responsableExistant.setDatePriseService(responsableEntry.getDatePriseService()) ;
		responsableExistant.setEtablissement(responsableEntry.getEtablissement()) ;
		Responsable responsableReturn = responsableRepository.save(responsableExistant) ;
		return this.transformer(responsableReturn) ;
	}
	
	
	
	public void supprimer (ResponsableDTO responsableDTO) {
		responsableRepository.findById(responsableDTO.getId())
		              .ifPresentOrElse(responsableRepository::delete, 
		            		  () -> {
		            			  throw new ResponsableNotFoundException(String.valueOf(responsableDTO.getId()));
		            		  }) ;
	}
	
	
	public void supprimer (Long id) {
		responsableRepository.findById(id)
        		.ifPresentOrElse(responsableRepository::delete, 
        				() -> {
        					throw new ResponsableNotFoundException(String.valueOf(id));
        				}) ;
	}
	
	
	
	public ResponsableDTO rechercher (Long id) {
		return responsableRepository.findById(id)
                .map(Function.identity())
                .map(this::transformer)
                .orElseThrow(() -> new ResponsableNotFoundException(String.valueOf(id))) ;
	}
	
	
	public List<ResponsableDTO> rechercherTout () {
		Iterable<Responsable> itResponsables = responsableRepository.findAll() ;
		List<ResponsableDTO> listeResponsablesDTO = new ArrayList<>() ;
		itResponsables.forEach(responsable -> listeResponsablesDTO.add(this.transformer(responsable))) ;
		return listeResponsablesDTO ;
	}
	
	
	private ResponsableDTO transformer(Responsable responsable) {
		return ResponsableDTO.builder().id(responsable.getId()).matricule(responsable.getMatricule())
					.nom(responsable.getNom()).prenom(responsable.getPrenom()).sexe(responsable.getSexe())
					.dateNaissance(responsable.getDateNaissance()).lieuNaissance(responsable.getLieuNaissance())
					.datePriseService(responsable.getDatePriseService()).telephone(responsable.getTelephone())
					.email(responsable.getEmail())
					.idEtablissement(responsable == null ? null : responsable.getEtablissement().getId())
					.libelleEtablissement(responsable == null ? null : responsable.getEtablissement().getLibelle()).build() ;
					
	}
	
	
	private Responsable transformer(ResponsableDTO responsableDTO) {
		Etablissement etablissement = responsableDTO.getIdEtablissement() == null ? null : 
				Etablissement.builder().id(responsableDTO.getIdEtablissement()).libelle(responsableDTO.getLibelleEtablissement()).build() ;
		return Responsable.builder().id(responsableDTO.getId()).matricule(responsableDTO.getMatricule())
					.nom(responsableDTO.getNom()).prenom(responsableDTO.getPrenom()).sexe(responsableDTO.getSexe())
					.dateNaissance(responsableDTO.getDateNaissance()).lieuNaissance(responsableDTO.getLieuNaissance())
					.datePriseService(responsableDTO.getDatePriseService()).telephone(responsableDTO.getTelephone())
					.email(responsableDTO.getEmail()).etablissement(etablissement).build() ;
					
	}
}
