package pl.kondi.cinemaservice.dto.seat;

public class SeatDto {
    private Integer row;
    private Integer column;
    private Integer price;


    public SeatDto(Integer row, Integer column, Integer price) {
        this.row = row;
        this.column = column;
        this.price = price;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
