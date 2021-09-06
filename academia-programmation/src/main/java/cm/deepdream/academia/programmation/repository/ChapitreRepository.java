package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Chapitre;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Programme;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
@Repository
public interface ChapitreRepository extends CrudRepository<Chapitre, Long>{
	public List<Chapitre> findByEtablissementAndProgramme (Etablissement etablissement, Programme programme) ;
	public List<Chapitre> findByEtablissementAndProgrammeAndTrimestre (Etablissement etablissement, Programme programme, 
			Trimestre trimestre) ;
	public List<Chapitre> findByEtablissementAndProgrammeAndSemestre (Etablissement etablissement, Programme programme, 
			Semestre semestre) ;
	public List<Chapitre> findByEtablissement (Etablissement etablissement) ;
	public Chapitre findByIdAndEtablissement (Long id, Etablissement etablissement) ;
}
