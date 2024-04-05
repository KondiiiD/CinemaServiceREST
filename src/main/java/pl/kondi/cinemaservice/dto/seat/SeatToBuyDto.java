package pl.kondi.cinemaservice.dto.seat;

public class SeatToBuyDto {
    private Integer row;
    private Integer column;

    public SeatToBuyDto() {
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
}
