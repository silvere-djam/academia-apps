package cm.deepdream.academia.viescolaire.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.viescolaire.data.Note;
@Repository
public interface NoteRepository extends CrudRepository<Note, Long>{

}
