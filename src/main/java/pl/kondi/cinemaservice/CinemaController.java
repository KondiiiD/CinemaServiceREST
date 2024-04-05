package pl.kondi.cinemaservice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kondi.cinemaservice.dto.*;
import pl.kondi.cinemaservice.dto.seat.SeatDto;
import pl.kondi.cinemaservice.dto.seat.SeatToBuyDto;
import pl.kondi.cinemaservice.dto.ticket.TicketDto;
import pl.kondi.cinemaservice.dto.ticket.TicketToReturnDto;
import pl.kondi.cinemaservice.exceptions.IncorrectPasswordException;
import pl.kondi.cinemaservice.exceptions.NoExistingSeatException;
import pl.kondi.cinemaservice.exceptions.SeatAlreadyTakenException;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats/{id}")
    ResponseEntity<SeatDto> getSeatInfo(@PathVariable Long id) {
        return cinemaService.getSeatInfo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{roomId}")
    ResponseEntity<CinemaHallDto> getRoomSeats(@PathVariable Long roomId) {
        return cinemaService.getRoomSeats(roomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{roomId}/seats")
    ResponseEntity<List<SeatDto>> getRoomAvailableSeats(@PathVariable Long roomId) {
        if (cinemaService.getRoomAvailableSeats(roomId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cinemaService.getRoomAvailableSeats(roomId));
    }

    @PostMapping("/{roomId}/purchase")
    ResponseEntity<?> purchaseTicket(@PathVariable Long roomId, @RequestBody SeatToBuyDto seatToBuy) {
        try {
            TicketDto ticket = cinemaService.purchaseTicket(roomId, seatToBuy);
            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/seats/{token}")
                    .buildAndExpand(ticket.getToken())
                    .toUri();
            return ResponseEntity.created(uri).body(ticket);
        } catch (SeatAlreadyTakenException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Error", "This seat has been already reserved!")
                    .build();
        } catch (NoExistingSeatException | NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/return")
    ResponseEntity<?> returnTicket(@RequestBody TicketToReturnDto token) {
        return cinemaService.returnTicket(token)
                .map(t -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{roomId}/stats")
    ResponseEntity<?> managersStatistics(@PathVariable Long roomId,
                                         @RequestParam(required = false, defaultValue = "") String password) {
        try {
            return cinemaService.getStatisticsForRoom(roomId, password)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Error", "The password is wrong!")
                    .build();
        }
    }
}
