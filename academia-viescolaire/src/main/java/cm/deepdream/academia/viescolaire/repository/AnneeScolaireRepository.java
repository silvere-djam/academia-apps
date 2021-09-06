package cm.deepdream.academia.viescolaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface AnneeScolaireRepository extends CrudRepository<AnneeScolaire, Long>{
	public Boolean existsByEtablissementAndLibelle(Etablissement etablissement, String libelle) ;
	public List<AnneeScolaire> findByEtablissement(Etablissement etablissement) ;
	public AnneeScolaire findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	@Query("select a from AnneeScolaire a where a.etablissement=:etablissement and a.courante=1")
	public AnneeScolaire rechercherCourant (@Param("etablissement") Etablissement etablissement) ;
}
