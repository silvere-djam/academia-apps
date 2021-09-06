package cm.deepdream.academia.security.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.data.Action;

@Repository
public interface ActionRepository extends CrudRepository<Action, Long>{
	public Action findByEtablissementAndId (Etablissement etablissement, Long id) ;
	public List<Action> findByEtablissement (Etablissement etablissement) ;
}
