package cm.deepdream.academia.programmation.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Examen;
@Repository
public interface ExamenRepository extends CrudRepository<Examen, Long>{
	public Examen findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<Examen> findByEtablissementAndSemestre (Etablissement etablissement, Semestre semestre) ;
	public List<Examen> findByEtablissementAndTrimestre (Etablissement etablissement, Trimestre trimestre) ;
	public List<Examen> findByEtablissementAndAnneeScolaire (Etablissement etablissement, AnneeScolaire anneeScolaire) ;
}
