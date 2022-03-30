package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.LocaliteRepository;
import cm.deepdream.academia.souscription.transfert.LocaliteDTO;
import cm.deepdream.academia.souscription.transfert.RegionDTO;
import cm.deepdream.academia.souscription.exceptions.LocaliteDuplicationException;
import cm.deepdream.academia.souscription.exceptions.LocaliteNotFoundException;
import cm.deepdream.academia.souscription.model.Localite;
import cm.deepdream.academia.souscription.model.Region;
@Service
@Transactional
public class LocaliteService {
	private LocaliteRepository localiteRepository ;
	
	
	public LocaliteService(LocaliteRepository localiteRepository) {
		this.localiteRepository = localiteRepository;
	}


	public LocaliteDTO creer (LocaliteDTO localiteDTO)  {
		if(localiteRepository.existsByLibelle(localiteDTO.getLibelle())) {
			throw new LocaliteDuplicationException(String.valueOf(localiteDTO)) ;
		}
		Localite localiteEntry = this.transformer(localiteDTO) ;
		Localite localiteReturn = localiteRepository.save(localiteEntry) ;
		return this.transformer(localiteReturn) ;
	}	
	
	
	public LocaliteDTO modifier (LocaliteDTO localiteDTO)  {
		Localite localiteEntry = this.transformer(localiteDTO) ;
		Localite localiteExistante = localiteRepository.findById(localiteDTO.getId())
				                                       .map(Function.identity())
				                                       .orElseThrow(() -> new LocaliteNotFoundException(String.valueOf(localiteDTO.getId()))) ;
		localiteExistante.setLibelle(localiteEntry.getLibelle()) ;
		localiteExistante.setRegion(localiteEntry.getRegion()) ;
		Localite localiteReturn = localiteRepository.save(localiteExistante) ;
		return this.transformer(localiteReturn) ;
	}
	

	public void supprimer (Long id)  {
		 localiteRepository.findById(id)
		 				   .ifPresentOrElse(localiteRepository::delete, 
		 						   () -> {
		 							   throw new LocaliteNotFoundException(String.valueOf(id)) ;
		 						   }) ;
	}
	
	
	public void supprimer (LocaliteDTO localiteDTO)  {
		 localiteRepository.findById(localiteDTO.getId())
		 				   .ifPresentOrElse(localiteRepository::delete, 
		 						   () -> {
		 							   throw new LocaliteNotFoundException(String.valueOf(localiteDTO.getId())) ;
		 						   }) ;
	}
	
	
	public LocaliteDTO rechercher (Long id)  {
		return localiteRepository.findById(id)
				                 .map(Function.identity())
				                 .map(this::transformer)
				                 .orElseThrow(() -> new LocaliteNotFoundException(String.valueOf(id))) ;
	}
	
	
	public List<LocaliteDTO> rechercher(RegionDTO regionDTO) {
		Region region = Region.builder().id(regionDTO.getId()).build() ;
		List<Localite> listeLocalites = localiteRepository.findByRegion(region) ;
		return listeLocalites.stream()
				             .map(this::transformer)
				             .collect(Collectors.toList()) ;
	}
	
	
	public List<LocaliteDTO> rechercherTout () {
		Iterable<Localite> itLocalites = localiteRepository.findAll() ;
		List<LocaliteDTO> listeLocalites = new ArrayList<>() ;
		itLocalites.forEach(localite -> listeLocalites.add(this.transformer(localite))) ;
		return listeLocalites ;
	}
	
	
	private LocaliteDTO transformer (Localite localite) {
		return LocaliteDTO.builder()
						  .id(localite.getId())
						  .libelle(localite.getLibelle())
						  .idRegion(localite == null ? null : localite.getRegion().getId())
						  .libelleRegion(localite == null ? null : localite.getRegion().getLibelle())
					      .build() ;
	}
	
	
	private Localite transformer (LocaliteDTO localiteDTO) {
		 Region region = localiteDTO.getIdRegion() == null ? null : Region.builder().id(localiteDTO.getIdRegion()).libelle(localiteDTO.getLibelleRegion()).build() ;
		 return Localite.builder()
				 		.id(localiteDTO.getId())
				 		.libelle(localiteDTO.getLibelle())
				 		.region(region)
				 		.build() ;
	}
}
