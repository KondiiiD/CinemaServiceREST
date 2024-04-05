package pl.kondi.cinemaservice;

import org.springframework.transaction.annotation.Transactional;
import pl.kondi.cinemaservice.dto.StatisticsDto;
import pl.kondi.cinemaservice.dto.seat.SeatToBuyDto;
import pl.kondi.cinemaservice.dto.ticket.ReturnTicketDto;
import pl.kondi.cinemaservice.dto.ticket.TicketToReturnDto;
import pl.kondi.cinemaservice.dtoMappers.TicketDtoMapper;
import pl.kondi.cinemaservice.entity.CinemaHall;
import pl.kondi.cinemaservice.entity.Seat;
import pl.kondi.cinemaservice.entity.Ticket;
import pl.kondi.cinemaservice.exceptions.*;
import org.springframework.stereotype.Service;
import pl.kondi.cinemaservice.dto.CinemaHallDto;
import pl.kondi.cinemaservice.dto.seat.SeatDto;
import pl.kondi.cinemaservice.dto.ticket.TicketDto;
import pl.kondi.cinemaservice.dtoMappers.CinemaHallDtoMapper;
import pl.kondi.cinemaservice.dtoMappers.SeatDtoMapper;
import pl.kondi.cinemaservice.repositories.CinemaHallRepository;
import pl.kondi.cinemaservice.repositories.SeatRepository;
import pl.kondi.cinemaservice.repositories.TicketRepository;

import java.util.*;

@Service
public class CinemaService {

    private final CinemaHallRepository cinemaHallRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final static String PASSWORD = "admin";

    public CinemaService(CinemaHallRepository cinemaHallRepository, SeatRepository seatRepository, TicketRepository ticketRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
    }

    public Optional<SeatDto> getSeatInfo(Long id) {
        return seatRepository.findById(id)
                .map(SeatDtoMapper::map);
    }

    public Optional<CinemaHallDto> getRoomSeats(Long roomId) {
        return cinemaHallRepository.findById(roomId)
                .map(CinemaHallDtoMapper::map);
    }


    public List<SeatDto> getRoomAvailableSeats(Long roomId) {
        return seatRepository.findAllByCinemaHallId(roomId)
                .stream()
                .filter(seat -> !seat.isSeatTaken())
                .map(SeatDtoMapper::map)
                .toList();
    }

    @Transactional
    public TicketDto purchaseTicket(Long roomId, SeatToBuyDto seatToBuy) {
        Seat seat = seatRepository.findAllByCinemaHallId(roomId)
                .stream()
                .filter(s -> seatToBuy.getRow().equals(s.getRow()) &&
                        seatToBuy.getColumn().equals(s.getColumn()))
                .findAny()
                .orElseThrow(NoExistingSeatException::new);

        if (seat.isSeatTaken()) {
            throw new SeatAlreadyTakenException();
        }
        seat.setSeatTaken(true);
        Ticket ticketToSave = generateTicket(seat, seat.getCinemaHall());
        ticketRepository.save(ticketToSave);
        return TicketDtoMapper.map(ticketToSave);
    }

    @Transactional
    public Optional<ReturnTicketDto> returnTicket(TicketToReturnDto ticketToReturn) {
        Optional<Ticket> ticketOptional = ticketRepository.findByToken(ticketToReturn.getToken());
        if (ticketOptional.isEmpty()) {
            return Optional.empty();
        }
        Ticket ticket = ticketOptional.get();
        removeReservationFromSeat(ticket);
        removeTicket(ticket);
        SeatDto seatDto = SeatDtoMapper.map(ticket.getTicketDetails());
        return Optional.of(new ReturnTicketDto(ticket.getToken(), seatDto));
    }


    public Optional<StatisticsDto> getStatisticsForRoom(Long roomId, String password) {
        if (!password.equals(PASSWORD)) {
            throw new IncorrectPasswordException();
        }
        return calculateStatisticsForRoom(roomId);
    }

    private Optional<StatisticsDto> calculateStatisticsForRoom(Long roomId) {
        List<Ticket> soldTickets = ticketRepository.getAllByCinemaHallId(roomId);
        if (soldTickets.isEmpty()) {
            return Optional.empty();
        }
        int amountOfTicketsSold = soldTickets.size();
        int income = calculateIncome(soldTickets);
        int availableSeats = calculateAvailableSeats(roomId);
        return Optional.of(new StatisticsDto(income, availableSeats, amountOfTicketsSold));
    }

    private int calculateAvailableSeats(Long roomId) {
        int seats = seatRepository.findAllByCinemaHallId(roomId).size();
        int tickets = ticketRepository.getAllByCinemaHallId(roomId).size();
        return seats - tickets;
    }

    private int calculateIncome(List<Ticket> soldTickets) {
        return soldTickets.stream()
                .map(Ticket::getTicketDetails)
                .mapToInt(Seat::getPrice)
                .sum();
    }

    private void removeReservationFromSeat(Ticket ticket) {
        ticket.getTicketDetails().setSeatTaken(false);
    }

    private void removeTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    private Ticket generateTicket(Seat seat, CinemaHall cinemaHall) {
        String token = UUID.randomUUID().toString();
        return new Ticket(token, seat, cinemaHall);
    }

}
