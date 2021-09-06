package cm.deepdream.academia.security.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface AnneeScolaireRepository extends CrudRepository<AnneeScolaire, Long>{
	public Boolean existsByEtablissementAndLibelle(Etablissement etablissement, String libelle) ;

}
