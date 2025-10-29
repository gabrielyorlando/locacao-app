package com.gabrielyorlando.locacao.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaRequestDto;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaResponseDto;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaUpdateRequestDto;
import com.gabrielyorlando.locacao.services.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ReservaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservaService reservaService;

    @Test
    void givenValidRequest_WhenCreate_thenReturn201() throws Exception {
        var request = new ReservaRequestDto();
        request.setClienteId(1L);
        request.setLocacaoId(1L);
        request.setDataInicio(LocalDateTime.now());
        request.setDataFim(LocalDateTime.now().plusHours(1));

        var response = new ReservaResponseDto();
        response.setId(1L);

        when(reservaService.save(any())).thenReturn(response);

        mockMvc.perform(post("/reservas").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void givenId_WhenGetById_thenReturn200() throws Exception {
        var response = new ReservaResponseDto();
        response.setId(1L);

        when(reservaService.findById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/reservas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void whenGetAll_thenReturn200() throws Exception {
        when(reservaService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidRequestAndId_WhenUpdate_thenReturn200() throws Exception {
        var request = new ReservaUpdateRequestDto();
        request.setDataInicio(LocalDateTime.now());
        request.setDataFim(LocalDateTime.now().plusHours(2));

        var response = new ReservaResponseDto();
        response.setId(1L);

        when(reservaService.update(anyLong(), any())).thenReturn(response);

        mockMvc.perform(put("/reservas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void givenConfirmedReservation_WhenDelete_thenReturn200() throws Exception {
        when(reservaService.delete(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/reservas/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void givenCanceledReservation_WhenDelete_thenReturn204() throws Exception {
        when(reservaService.delete(anyLong()))
                .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/reservas/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenInvalidRequest_WhenCreate_thenReturn400() throws Exception {
        var request = new ReservaRequestDto();

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUnknownId_WhenGetById_thenReturn404() throws Exception {
        when(reservaService.findById(anyLong()))
                .thenThrow(new EntityNotFoundException("Reserva não encontrada"));

        mockMvc.perform(get("/reservas/{id}", 999L)).andExpect(status().isNotFound());
    }

    @Test
    void givenCanceledReservation_WhenUpdate_thenReturn422() throws Exception {
        var request = new ReservaUpdateRequestDto();
        request.setDataInicio(LocalDateTime.now());
        request.setDataFim(LocalDateTime.now().plusHours(1));

        when(reservaService.update(anyLong(), any())).thenThrow(new BusinessRuleException("Não é possível alterar uma reserva cancelada"));

        mockMvc.perform(put("/reservas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenUnknownId_WhenDelete_thenReturn404() throws Exception {
        when(reservaService.delete(anyLong()))
                .thenThrow(new EntityNotFoundException("Reserva não encontrada"));

        mockMvc.perform(delete("/reservas/{id}", 999L)).andExpect(status().isNotFound());
    }
}
