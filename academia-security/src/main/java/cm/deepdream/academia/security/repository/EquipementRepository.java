package cm.deepdream.academia.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.security.data.Equipement;
import cm.deepdream.academia.security.data.Utilisateur;
@Repository
public interface EquipementRepository extends CrudRepository<Equipement, Long>{
	public List<Equipement> findByUtilisateur (Utilisateur utilisateur) throws Exception ;
}
