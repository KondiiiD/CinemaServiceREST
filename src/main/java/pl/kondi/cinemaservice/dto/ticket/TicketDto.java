package pl.kondi.cinemaservice.dto.ticket;


import pl.kondi.cinemaservice.dto.seat.SeatDto;

public class TicketDto {
    private String token;
    private SeatDto ticket;
    private Long roomId;

    public TicketDto(String token, SeatDto ticket, Long cinemaHallId) {
        this.token = token;
        this.ticket = ticket;
        this.roomId = cinemaHallId;
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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
