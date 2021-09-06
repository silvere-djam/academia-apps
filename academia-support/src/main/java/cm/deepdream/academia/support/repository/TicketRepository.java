package cm.deepdream.academia.support.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.support.data.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long>{

}
