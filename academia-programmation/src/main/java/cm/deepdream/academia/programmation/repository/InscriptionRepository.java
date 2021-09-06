package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Inscription;
import cm.deepdream.academia.programmation.data.Niveau;
@Repository
public interface InscriptionRepository extends CrudRepository<Inscription, Long>{
	public Inscription findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	public List<Inscription> findByEtablissementAndAnneeScolaire (Etablissement etablissement, AnneeScolaire anneeScolaire) ;
	public List<Inscription> findByEtablissementAndClasseAndAnneeScolaire (Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) ;
	public List<Inscription> findByEtablissementAndEleveAndAnneeScolaire (Etablissement etablissement, Eleve eleve, AnneeScolaire anneeScolaire) ;
	@Query("select count(i) from Inscription i where i.etablissement=:etablissement and i.anneeScolaire=:anneeScolaire and i.classe.niveau=:niveau")
	public Integer countNumbreInscriptions (@Param("etablissement") Etablissement etablissement, @Param("anneeScolaire") AnneeScolaire anneeScolaire, 
			@Param("niveau") Niveau niveau) ;
}
