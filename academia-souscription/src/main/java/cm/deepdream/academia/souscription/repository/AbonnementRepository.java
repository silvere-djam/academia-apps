package cm.deepdream.academia.souscription.repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Abonnement;
@Repository
public interface AbonnementRepository extends CrudRepository<Abonnement, Long>{
	public List<Abonnement> findByEtablissement (Etablissement etablissement) ;
	public List<Abonnement> findByDateDebutBetween (LocalDate date1, LocalDate date2) ;
	public List<Abonnement> findByStatut (String statut) ;
}
