package pl.kondi.cinemaservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.kondi.cinemaservice.entity.CinemaHall;


@Repository
public interface CinemaHallRepository extends CrudRepository<CinemaHall, Long> {
}
