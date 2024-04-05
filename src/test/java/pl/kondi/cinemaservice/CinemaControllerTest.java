package pl.kondi.cinemaservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kondi.cinemaservice.dto.CinemaHallDto;
import pl.kondi.cinemaservice.dto.StatisticsDto;
import pl.kondi.cinemaservice.dto.seat.SeatDto;
import pl.kondi.cinemaservice.dto.seat.SeatToBuyDto;
import pl.kondi.cinemaservice.dto.ticket.TicketDto;
import pl.kondi.cinemaservice.exceptions.IncorrectPasswordException;
import pl.kondi.cinemaservice.exceptions.NoExistingSeatException;
import pl.kondi.cinemaservice.exceptions.SeatAlreadyTakenException;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CinemaController.class)
class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CinemaService cinemaService;

    @Test
    public void testGetTicketInfo() throws Exception {
        when(cinemaService.getSeatInfo(1L)).thenReturn(Optional.of(new SeatDto(1,1,8)));

        mockMvc.perform(get("/api/seats/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetRoomSeats() throws Exception {
        SeatDto seat = new SeatDto(1, 1, 8);
        when(cinemaService.getRoomSeats(1L)).thenReturn(Optional.of(new CinemaHallDto(8, 8, Collections.singletonList(seat))));

        mockMvc.perform(get("/api/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetRoomAvailableSeats() throws Exception {
        when(cinemaService.getRoomAvailableSeats(1L)).thenReturn(Collections.singletonList(new SeatDto(1,1,8)));

        mockMvc.perform(get("/api/1/seats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testPurchaseTicket() throws Exception {
        TicketDto ticketDto = new TicketDto("asd", new SeatDto(1,1,8),1L);
        when(cinemaService.purchaseTicket(any(Long.class), any(SeatToBuyDto.class))).thenReturn(ticketDto);

        mockMvc.perform(post("/api/1/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void testManagersStatistics() throws Exception {
        when(cinemaService.getStatisticsForRoom(1L, "password")).thenReturn(Optional.of(new StatisticsDto(1,1,1)));

        mockMvc.perform(get("/api/1/stats")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void testIncorrectPasswordForManagersStatistics() throws Exception {
        when(cinemaService.getStatisticsForRoom(1L, "wrongPassword")).thenThrow(IncorrectPasswordException.class);

        mockMvc.perform(get("/api/1/stats")
                        .param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("Error", "The password is wrong!"));
    }

    @Test
    public void testManagersStatisticsWithValidPassword() throws Exception {
        StatisticsDto statisticsDto = new StatisticsDto(1000, 50, 20);
        when(cinemaService.getStatisticsForRoom(1L, "password")).thenReturn(Optional.of(statisticsDto));

        mockMvc.perform(get("/api/1/stats")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.income").value(1000))
                .andExpect(jsonPath("$.available").value(50))
                .andExpect(jsonPath("$.purchased").value(20));
    }

    @Test
    public void testManagersStatisticsWithInvalidPassword() throws Exception {
        when(cinemaService.getStatisticsForRoom(1L, "wrongPassword")).thenThrow(IncorrectPasswordException.class);

        mockMvc.perform(get("/api/1/stats")
                        .param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("Error", "The password is wrong!"));
    }

    @Test
    public void testManagersStatisticsWithMissingPassword() throws Exception {
        when(cinemaService.getStatisticsForRoom(1L, "")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/1/stats"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testManagersStatisticsWithNullPassword() throws Exception {
        when(cinemaService.getStatisticsForRoom(1L, null)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/1/stats"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testManagersStatisticsWithValidPasswordAndNoStatistics() throws Exception {
        when(cinemaService.getStatisticsForRoom(1L, "password")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/1/stats")
                        .param("password", "password"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testPurchaseTicketSeatAlreadyTaken() throws Exception {
        when(cinemaService.purchaseTicket(any(Long.class), any(SeatToBuyDto.class)))
                .thenThrow(new SeatAlreadyTakenException());

        mockMvc.perform(post("/api/1/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"row\": 1, \"column\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Error", "This seat has been already reserved!"));
    }

    @Test
    public void testPurchaseTicketNoExistingSeat() throws Exception {
        when(cinemaService.purchaseTicket(any(Long.class), any(SeatToBuyDto.class)))
                .thenThrow(new NoExistingSeatException());

        mockMvc.perform(post("/api/1/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"row\": 1, \"column\": 1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPurchaseTicketInvalidRequestBody() throws Exception {
        mockMvc.perform(post("/api/1/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"row\": \"A\", \"column\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPurchaseTicketNoRequestBody() throws Exception {
        mockMvc.perform(post("/api/1/purchase")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}