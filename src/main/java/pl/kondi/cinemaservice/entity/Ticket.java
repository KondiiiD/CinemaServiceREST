package pl.kondi.cinemaservice.entity;

import jakarta.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false, unique = true)
    private Seat ticketDetails;
    @ManyToOne
    @JoinColumn(name = "cinema_hall_id", nullable = false)
    private CinemaHall cinemaHall;

    public Ticket() {
    }

    public Ticket(String token, Seat ticketDetails, CinemaHall cinemaHall) {
        this.token = token;
        this.ticketDetails = ticketDetails;
        this.cinemaHall = cinemaHall;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Seat getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(Seat ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }

    public void setCinemaHall(CinemaHall cinemaHall) {
        this.cinemaHall = cinemaHall;
    }
}
