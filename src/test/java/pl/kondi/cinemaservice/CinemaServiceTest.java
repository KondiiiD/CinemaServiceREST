package pl.kondi.cinemaservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kondi.cinemaservice.dto.CinemaHallDto;
import pl.kondi.cinemaservice.dto.seat.SeatDto;
import pl.kondi.cinemaservice.dto.seat.SeatToBuyDto;
import pl.kondi.cinemaservice.dto.ticket.ReturnTicketDto;
import pl.kondi.cinemaservice.dto.ticket.TicketDto;
import pl.kondi.cinemaservice.dto.ticket.TicketToReturnDto;
import pl.kondi.cinemaservice.entity.CinemaHall;
import pl.kondi.cinemaservice.entity.Seat;
import pl.kondi.cinemaservice.entity.Ticket;
import pl.kondi.cinemaservice.exceptions.NoExistingSeatException;
import pl.kondi.cinemaservice.exceptions.SeatAlreadyTakenException;
import pl.kondi.cinemaservice.repositories.CinemaHallRepository;
import pl.kondi.cinemaservice.repositories.SeatRepository;
import pl.kondi.cinemaservice.repositories.TicketRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

    @Mock
    private CinemaHallRepository cinemaHallRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private CinemaService cinemaService;


    @Test
    void getSeatInfo_ValidId_ReturnsSeatDto() {
        Long id = 1L;
        Seat seat = new Seat();
        when(seatRepository.findById(id)).thenReturn(Optional.of(seat));

        Optional<SeatDto> result = cinemaService.getSeatInfo(id);

        assertTrue(result.isPresent());
    }

    @Test
    void getSeatInfo_InvalidId_ReturnsEmptyOptional() {
        Long id = 1L;
        when(seatRepository.findById(id)).thenReturn(Optional.empty());

        Optional<SeatDto> result = cinemaService.getSeatInfo(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRoomSeats_ValidId_ReturnsCinemaHallDto() {
        Long roomId = 1L;
        CinemaHall cinemaHall = new CinemaHall();
        when(cinemaHallRepository.findById(roomId)).thenReturn(Optional.of(cinemaHall));

        Optional<CinemaHallDto> result = cinemaService.getRoomSeats(roomId);

        assertTrue(result.isPresent());
    }

    @Test
    void getRoomSeats_InvalidId_ReturnsEmptyOptional() {
        Long roomId = 1L;
        when(cinemaHallRepository.findById(roomId)).thenReturn(Optional.empty());

        Optional<CinemaHallDto> result = cinemaService.getRoomSeats(roomId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRoomAvailableSeats_ValidRoomId_ReturnsListOfAvailableSeats() {
        Long roomId = 1L;
        Seat seat = new Seat();
        seat.setSeatTaken(false);
        when(seatRepository.findAllByCinemaHallId(roomId)).thenReturn(Collections.singletonList(seat));

        List<SeatDto> result = cinemaService.getRoomAvailableSeats(roomId);

        assertEquals(1, result.size());
    }

    @Test
    void getRoomAvailableSeats_NoSeatsAvailable_ReturnsEmptyList() {
        Long roomId = 1L;
        Seat seat = new Seat();
        seat.setSeatTaken(true); // All seats are taken
        when(seatRepository.findAllByCinemaHallId(roomId)).thenReturn(Collections.singletonList(seat));

        List<SeatDto> result = cinemaService.getRoomAvailableSeats(roomId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRoomAvailableSeats_AllSeatsAvailable_ReturnsListOfSeats() {
        Long roomId = 1L;
        Seat seat1 = new Seat();
        Seat seat2 = new Seat();
        seat1.setSeatTaken(false);
        seat2.setSeatTaken(false);
        when(seatRepository.findAllByCinemaHallId(roomId)).thenReturn(List.of(seat1, seat2));

        List<SeatDto> result = cinemaService.getRoomAvailableSeats(roomId);

        assertEquals(2, result.size());
    }

    @Test
    void purchaseTicket_ValidRoomIdAndSeatToBuy_ReturnsTicketDto() {
        Long roomId = 1L;
        SeatToBuyDto seatToBuy = new SeatToBuyDto();
        seatToBuy.setRow(1);
        seatToBuy.setColumn(1);
        CinemaHall cinemaHall = new CinemaHall();
        Seat seat = new Seat();
        seat.setRow(1);
        seat.setColumn(1);
        seat.setCinemaHall(cinemaHall);
        when(seatRepository.findAllByCinemaHallId(roomId)).thenReturn(Collections.singletonList(seat));
        when(ticketRepository.save(any())).thenReturn(new Ticket());

        TicketDto result = cinemaService.purchaseTicket(roomId, seatToBuy);

        assertNotNull(result);
    }

    @Test
    void returnTicket_InvalidToken_ReturnsEmptyOptional() {
        TicketToReturnDto ticketToReturn = new TicketToReturnDto();
        when(ticketRepository.findByToken(any())).thenReturn(Optional.empty());

        Optional<ReturnTicketDto> result = cinemaService.returnTicket(ticketToReturn);

        assertTrue(result.isEmpty());
    }

    @Test
    void returnTicket_ValidTicketToReturn_ReturnsReturnTicketDto() {
        TicketToReturnDto ticketToReturn = new TicketToReturnDto();
        Ticket ticket = new Ticket();
        Seat seat = new Seat();
        ticket.setTicketDetails(seat);
        when(ticketRepository.findByToken(any())).thenReturn(Optional.of(ticket));

        Optional<ReturnTicketDto> result = cinemaService.returnTicket(ticketToReturn);

        assertTrue(result.isPresent());
        assertFalse(seat.isSeatTaken());
    }

    @Test
    void purchaseTicket_SeatAlreadyTaken_ThrowsSeatAlreadyTakenException() {
        Long roomId = 1L;
        SeatToBuyDto seatToBuy = new SeatToBuyDto();
        seatToBuy.setRow(1);
        seatToBuy.setColumn(1);

        // Mocking seat with taken status
        Seat seat = new Seat();
        seat.setRow(1);
        seat.setColumn(1);
        seat.setSeatTaken(true);
        when(seatRepository.findAllByCinemaHallId(roomId)).thenReturn(Collections.singletonList(seat));

        assertThrows(SeatAlreadyTakenException.class, () -> cinemaService.purchaseTicket(roomId, seatToBuy));
    }

    @Test
    void purchaseTicket_NoExistingSeat_ThrowsNoExistingSeatException() {
        Long roomId = 1L;
        SeatToBuyDto seatToBuy = new SeatToBuyDto();
        seatToBuy.setRow(1);
        seatToBuy.setColumn(1);

        // Mocking no seat found
        when(seatRepository.findAllByCinemaHallId(roomId)).thenReturn(Collections.emptyList());

        assertThrows(NoExistingSeatException.class, () -> cinemaService.purchaseTicket(roomId, seatToBuy));
    }

}