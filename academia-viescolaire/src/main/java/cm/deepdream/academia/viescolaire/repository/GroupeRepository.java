package cm.deepdream.academia.viescolaire.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.Groupe;
@Repository
public interface GroupeRepository extends CrudRepository<Groupe, Long>{

}
