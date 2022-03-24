package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.EtablissementRepository;
import cm.deepdream.academia.souscription.exceptions.AbonnementNotFoundException;
import cm.deepdream.academia.souscription.model.Etablissement;

@Transactional
@Service
public class EtablissementService {
	private EtablissementRepository etablissementRepository ;
	
	
	public EtablissementService(EtablissementRepository etablissementRepository) {
		this.etablissementRepository = etablissementRepository;
	}


	public Etablissement creer (Etablissement etablissement) {
		return etablissementRepository.save(etablissement) ;
	}
	
	
	public Etablissement modifier (Etablissement etablissement) {
		return etablissementRepository.save(etablissement) ;
	}
	
	
	public void supprimer (Long id) throws Exception {
		etablissementRepository.findById(id)
                			   .ifPresentOrElse(etablissementRepository::delete, 
                					 () -> {
                						 	throw new AbonnementNotFoundException(String.format("%s", id));
                					 }) ;
	}
	
	
	public Etablissement rechercher (Long id)  {
		return etablissementRepository.findById(id)
				                      .map(Function.identity())
				                      .orElseThrow(() ->new AbonnementNotFoundException(String.format("%s", id))) ;
	}
	
	
	public void supprimer (Etablissement etablissement) {
		etablissementRepository.delete(etablissement) ;
	}
	
	
	public List<Etablissement> rechercher (Etablissement etablissement) {
		Iterable<Etablissement> source = etablissementRepository.findAll() ;
		List<Etablissement> listeEtablissements = new ArrayList<Etablissement>();
		source.forEach(listeEtablissements::add) ;
		return listeEtablissements ;
	}
	
}
