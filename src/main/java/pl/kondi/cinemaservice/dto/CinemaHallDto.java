package pl.kondi.cinemaservice.dto;

import pl.kondi.cinemaservice.dto.seat.SeatDto;

import java.util.List;

public class CinemaHallDto {
    private Integer rows;
    private Integer columns;
    private List<SeatDto> seats;

    public CinemaHallDto(Integer rows, Integer columns, List<SeatDto> seats) {
        this.rows = rows;
        this.columns = columns;
        this.seats = seats;
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

    public List<SeatDto> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDto> seats) {
        this.seats = seats;
    }
}
