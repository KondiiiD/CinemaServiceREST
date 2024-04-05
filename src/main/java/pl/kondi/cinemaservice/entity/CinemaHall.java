package pl.kondi.cinemaservice.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CinemaHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_rows")
    private Integer rows;
    @Column(name = "room_columns")
    private Integer columns;
    @OneToMany(mappedBy = "cinemaHall", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Seat> seats = new ArrayList<>();
    @OneToMany(mappedBy = "cinemaHall", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Ticket> soldTickets = new ArrayList<>();

    public CinemaHall() {
    }

    public CinemaHall(Integer rows, Integer columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Ticket> getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(List<Ticket> soldTickets) {
        this.soldTickets = soldTickets;
    }
}
