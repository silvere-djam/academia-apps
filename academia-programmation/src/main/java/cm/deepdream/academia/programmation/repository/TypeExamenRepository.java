package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.TypeExamen;
@Repository
public interface TypeExamenRepository extends CrudRepository<TypeExamen, Long>{
	public TypeExamen findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<TypeExamen> findByEtablissement (Etablissement etablissement) ;
	public List<TypeExamen> findByEtablissementAndNiveau (Etablissement etablissement, Niveau niveau) ;
}
