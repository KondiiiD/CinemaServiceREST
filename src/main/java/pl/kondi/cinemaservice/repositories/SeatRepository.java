package pl.kondi.cinemaservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.kondi.cinemaservice.entity.Seat;

import java.util.List;

@Repository
public interface SeatRepository extends CrudRepository<Seat, Long> {
    List<Seat> findAllByCinemaHallId(Long roomId);
}
