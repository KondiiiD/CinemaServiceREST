package pl.kondi.cinemaservice.dtoMappers;

import pl.kondi.cinemaservice.dto.seat.SeatDto;
import pl.kondi.cinemaservice.dto.ticket.TicketDto;
import pl.kondi.cinemaservice.entity.Ticket;

public class TicketDtoMapper {

    public static TicketDto map(Ticket ticket) {
        SeatDto seatDto = SeatDtoMapper.map(ticket.getTicketDetails());
        return new TicketDto(ticket.getToken(), seatDto, ticket.getCinemaHall().getId());
    }
}
