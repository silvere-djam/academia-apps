package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Profession;
@Repository
public interface ProfessionRepository extends CrudRepository<Profession, Long>{
	public Profession findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	public List<Profession> findByEtablissement(Etablissement etablissement) ;
}
