package pl.kondi.cinemaservice.dto.ticket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.kondi.cinemaservice.dto.seat.SeatDto;

public class ReturnTicketDto {
    @JsonIgnore
    private String token;
    private SeatDto ticket;

    public ReturnTicketDto(String token, SeatDto ticket) {
        this.token = token;
        this.ticket = ticket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SeatDto getTicket() {
        return ticket;
    }

    public void setTicket(SeatDto ticket) {
        this.ticket = ticket;
    }
}
