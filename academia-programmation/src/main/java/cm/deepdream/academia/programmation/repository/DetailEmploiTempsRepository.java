package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface DetailEmploiTempsRepository extends CrudRepository<DetailEmploiTemps, Long>{
	public List<DetailEmploiTemps> findByEtablissementAndEmploiTemps (Etablissement etablissement, EmploiTemps emploiTemps) ;
	public DetailEmploiTemps findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	@Query("select d from DetailEmploiTemps d where d.etablissement=:etablissement and d.emploiTemps.classe=:classe")
	public List<DetailEmploiTemps> rechercher (@Param("etablissement") Etablissement etablissement, @Param("classe") Classe classe) ;
}
