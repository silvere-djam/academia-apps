package cm.deepdream.academia.viescolaire.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Semestre;
@Repository
public interface SemestreRepository extends CrudRepository<Semestre, Long>{
	public Boolean existsByEtablissementAndLibelle (Etablissement etablissement, String libelle) ;
	public Semestre findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<Semestre> findByEtablissementAndAnneeScolaire (Etablissement etablissement, AnneeScolaire anneeScolaire) ;
}
