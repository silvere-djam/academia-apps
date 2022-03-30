package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;

import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.PaysRepository;
import cm.deepdream.academia.souscription.transfert.PaysDTO;
import cm.deepdream.academia.souscription.exceptions.PaysDuplicationException;
import cm.deepdream.academia.souscription.exceptions.PaysNotFoundException;
import cm.deepdream.academia.souscription.model.Pays;

@Service
@Transactional
public class PaysService {
	private PaysRepository paysRepository ;
	
	
	public PaysService(PaysRepository paysRepository) {
		this.paysRepository = paysRepository;
	}


	public PaysDTO creer (PaysDTO paysDTO) {
		if(paysRepository.existsByLibelle(paysDTO.getLibelle())) {
			throw new PaysDuplicationException(String.valueOf(paysDTO)) ;
		}
		Pays paysEntry = this.transformer(paysDTO) ;
		Pays paysReturn = paysRepository.save(paysEntry) ;
		return this.transformer(paysReturn) ;
	}	
	
	
	public PaysDTO modifier (PaysDTO paysDTO) {
		Pays paysEntry = this.transformer(paysDTO) ;
		Pays paysExistant = paysRepository.findById(paysDTO.getId())
				                          .map(Function.identity())
				                          .orElseThrow(() -> new PaysNotFoundException(String.valueOf(paysEntry.getId()))) ;
		paysExistant.setCode(paysEntry.getCode()) ;
		paysExistant.setCodeTel(paysDTO.getCodeTel()) ;
		paysExistant.setLibelle(paysDTO.getLibelle()) ;
		paysExistant.setMonnaie(paysDTO.getMonnaie()) ;
		Pays paysReturn =  paysRepository.save(paysExistant) ;
		return this.transformer(paysReturn) ;
	}
	
	
	public void supprimer (PaysDTO paysDTO) {
		Pays paysEntry = this.transformer(paysDTO) ;
		paysRepository.findById(paysDTO.getId())
		              .ifPresentOrElse(paysRepository::delete, 
		            		  () -> {
		            			  throw new PaysNotFoundException(String.valueOf(paysEntry.getId()));
		            		  }) ;
	}
	
	
	public void supprimer (Long id) {
		paysRepository.findById(id)
        .ifPresentOrElse(paysRepository::delete, 
      		  () -> {
      			  throw new PaysNotFoundException(String.valueOf(id));
      		  }) ;
	}
	
	
	public PaysDTO rechercher (Long id) {
		return paysRepository.findById(id)
                .map(Function.identity())
                .map(this::transformer)
                .orElseThrow(() -> new PaysNotFoundException(String.valueOf(id))) ;
	}
	

	public List<PaysDTO> rechercherTout () {
		Iterable<Pays> itPays = paysRepository.findAll() ;
		List<PaysDTO> listePays = new ArrayList() ;
		itPays.forEach((p) -> listePays.add(this.transformer(p)));
		return listePays ;
	}
	
	
	public PaysDTO transformer (Pays pays) {
		return PaysDTO.builder().id(pays.getId()).code(pays.getCode()).libelle(pays.getLibelle())
					.codeTel(pays.getCodeTel()).monnaie(pays.getMonnaie()).build() ;
	}
	
	public Pays transformer (PaysDTO paysDTO) {
		return Pays.builder().id(paysDTO.getId()).code(paysDTO.getCode()).libelle(paysDTO.getLibelle())
					.codeTel(paysDTO.getCodeTel()).monnaie(paysDTO.getMonnaie()).build() ;
	}
}
