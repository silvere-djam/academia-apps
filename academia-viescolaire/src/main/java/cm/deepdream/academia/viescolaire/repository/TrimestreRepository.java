package cm.deepdream.academia.viescolaire.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Trimestre;
@Repository
public interface TrimestreRepository extends CrudRepository<Trimestre, Long>{
	public Boolean existsByEtablissementAndLibelle (Etablissement etablissement, String libelle) ;
	public Trimestre findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<Trimestre> findByEtablissementAndAnneeScolaire (Etablissement etablissement, AnneeScolaire anneeScolaire) ;
}
