package pl.kondi.cinemaservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.kondi.cinemaservice.entity.Ticket;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Optional<Ticket> findByToken(String token);
    List<Ticket> getAllByCinemaHallId(Long id);
}
