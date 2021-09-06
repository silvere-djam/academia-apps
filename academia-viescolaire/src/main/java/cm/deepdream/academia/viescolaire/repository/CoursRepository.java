package cm.deepdream.academia.viescolaire.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.viescolaire.data.Cours;
@Repository
public interface CoursRepository extends CrudRepository<Cours, Long>{
	public List<Cours> findByEtablissement(Etablissement etablissement) ;
	public Cours findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<Cours> findByEtablissementAndClasseAndTrimestre(Etablissement etablissement, Classe classe, Trimestre trimestre) ;
	public List<Cours> findByEtablissementAndClasseAndAnneeScolaire(Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) ;
	public List<Cours> findByEtablissementAndUeAndTrimestre(Etablissement etablissement, UE ue, Trimestre trimestre) ;
}
