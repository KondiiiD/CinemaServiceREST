package pl.kondi.cinemaservice.entity;

import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "seat_row")
    private Integer row;
    @Column(name = "seat_column")
    private Integer column;
    private boolean seatTaken;
    private Integer price;
    @ManyToOne
    @JoinColumn(name = "cinema_hall_id", nullable = false)
    private CinemaHall cinemaHall;

    public Seat() {
    }

    public Seat(Integer row, Integer column, boolean seatTaken, Integer price) {
            this.row = row;
            this.column = column;
            this.seatTaken = seatTaken;
            this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public boolean isSeatTaken() {
        return seatTaken;
    }

    public void setSeatTaken(boolean seatTaken) {
        this.seatTaken = seatTaken;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }

    public void setCinemaHall(CinemaHall cinemaHall) {
        this.cinemaHall = cinemaHall;
    }
}
