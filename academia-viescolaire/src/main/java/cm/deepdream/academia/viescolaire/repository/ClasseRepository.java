package cm.deepdream.academia.viescolaire.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface ClasseRepository extends CrudRepository<Classe, Long>{
	public List<Classe> findByEtablissement (Etablissement etablissement) ;
	public Classe findByIdAndEtablissement(Long id, Etablissement etablissement) ;
}
