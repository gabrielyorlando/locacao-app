package com.gabrielyorlando.locacao.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoRequestDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoResponseDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoUpdateRequestDto;
import com.gabrielyorlando.locacao.models.enums.TipoLocacao;
import com.gabrielyorlando.locacao.services.LocacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
public class LocacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private LocacaoService locacaoService;

    @Test
    void givenValidRequest_WhenCreate_thenReturn201() throws Exception {
        var request = new LocacaoRequestDto();
        request.setNome("Quadra de teste");
        request.setTipo(TipoLocacao.QUADRA_ESPORTIVA);
        request.setDescricao("TESTE");
        request.setValorHora(BigDecimal.valueOf(150));
        request.setTempoMinimoHoras(1);
        request.setTempoMaximoHoras(5);

        var response = new LocacaoResponseDto();
        response.setId(1L);

        when(locacaoService.save(any(LocacaoRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void givenId_WhenGetById_thenReturn200() throws Exception {
        var response = new LocacaoResponseDto();
        response.setId(1L);

        when(locacaoService.findById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/locacoes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void whenGetAll_thenReturn200() throws Exception {
        when(locacaoService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/locacoes"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAllAvailables_thenReturn200() throws Exception {
        when(locacaoService.findAvailableBetweenDates(any(), any(), any())).thenReturn(null);

        mockMvc.perform(get("/locacoes/availables")
                        .param("start", LocalDateTime.now().toString())
                        .param("end", LocalDateTime.now().plusHours(2).toString()))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidRequestAndId_WhenUpdate_thenReturn200() throws Exception {
        var request = new LocacaoUpdateRequestDto();
        request.setNome("Quadra Atualizada");

        var response = new LocacaoResponseDto();
        response.setId(1L);
        response.setNome("Quadra Atualizada");

        when(locacaoService.update(anyLong(), any(LocacaoUpdateRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/locacoes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Quadra Atualizada"));
    }

    @Test
    void givenId_WhenDelete_thenReturn204() throws Exception {
        doNothing().when(locacaoService).delete(anyLong());

        mockMvc.perform(delete("/locacoes/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenInvalidRequest_WhenCreate_thenReturn400() throws Exception {
        var request = new LocacaoRequestDto();

        mockMvc.perform(post("/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidId_WhenGetById_thenReturn404() throws Exception {
        when(locacaoService.findById(anyLong())).thenThrow(new EntityNotFoundException("Locação não encontrada"));

        mockMvc.perform(get("/locacoes/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenInvalidId_WhenUpdate_thenReturn404() throws Exception {
        var request = new LocacaoUpdateRequestDto();
        request.setNome("Teste update");

        doThrow(new EntityNotFoundException("Locação não encontrada"))
                .when(locacaoService).update(anyLong(), any(LocacaoUpdateRequestDto.class));

        mockMvc.perform(put("/locacoes/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void givenInvalidId_WhenDelete_thenReturn404() throws Exception {
        doThrow(new EntityNotFoundException("Locação não encontrada")).when(locacaoService).delete(anyLong());

        mockMvc.perform(delete("/locacoes/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
