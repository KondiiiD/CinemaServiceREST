package pl.kondi.cinemaservice.dtoMappers;


import pl.kondi.cinemaservice.dto.seat.SeatDto;
import pl.kondi.cinemaservice.entity.Seat;

public class SeatDtoMapper {

    public static SeatDto map(Seat seat) {
        return new SeatDto(seat.getRow(), seat.getColumn(), seat.getPrice());
    }
}
