package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.LocaliteRepository;
import cm.deepdream.academia.souscription.exceptions.LocaliteNotFoundException;
import cm.deepdream.academia.souscription.model.Localite;
@Service
@Transactional
public class LocaliteService {
	private LocaliteRepository localiteRepository ;

	
	public Localite creer (Localite localite)  {
		return localiteRepository.save(localite) ;
	}	
	
	public Localite modifier (Localite localite)  {
		return localiteRepository.save(localite) ;
	}
	

	public void supprimer (Long id)  {
		 localiteRepository.findById(id)
		 				   .ifPresentOrElse(localiteRepository::delete, 
		 						   () -> {
		 							   throw new LocaliteNotFoundException(String.valueOf(id)) ;
		 						   }) ;
	}
	
	
	public void supprimer (Localite localite)  {
		 localiteRepository.findById(localite.getId())
		 				   .ifPresentOrElse(localiteRepository::delete, 
		 						   () -> {
		 							   throw new LocaliteNotFoundException(String.valueOf(localite.getId())) ;
		 						   }) ;
	}
	
	
	
	public Localite rechercher (Long id)  {
		return localiteRepository.findById(id)
				                 .map(Function.identity())
				                 .orElseThrow(() -> new LocaliteNotFoundException(String.valueOf(id))) ;
	}
	
	
	public List<Localite> rechercher(Localite ville) {
		Iterable<Localite> itVilles = localiteRepository.findAll() ;
		List<Localite> listeVilles = new ArrayList() ;
		itVilles.forEach(listeVilles::add);
		return listeVilles ;
	}
	
	
	public List<Localite> rechercherTout (Localite ville) {
		Iterable<Localite> itVilles = localiteRepository.findAll() ;
		List<Localite> listeVilles = new ArrayList() ;
		itVilles.forEach(listeVilles::add);
		return listeVilles ;
	}
}
