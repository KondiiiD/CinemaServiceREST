package pl.kondi.cinemaservice.dtoMappers;


import pl.kondi.cinemaservice.dto.CinemaHallDto;
import pl.kondi.cinemaservice.dto.seat.SeatDto;
import pl.kondi.cinemaservice.entity.CinemaHall;

import java.util.List;

public class CinemaHallDtoMapper {

    public static CinemaHallDto map(CinemaHall cinema) {
        List<SeatDto> cinemaList = cinema.getSeats().stream().map(SeatDtoMapper::map).toList();
        return new CinemaHallDto(cinema.getRows(), cinema.getColumns(), cinemaList);
    }
}
