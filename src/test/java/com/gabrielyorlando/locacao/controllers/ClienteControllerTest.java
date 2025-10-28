package com.gabrielyorlando.locacao.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteRequestDto;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteResponseDto;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteUpdateRequestDto;
import com.gabrielyorlando.locacao.services.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClienteService clienteService;

    @Test
    void givenValidRequest_WhenCreate_thenReturn201() throws Exception {
        var request = new ClienteRequestDto();
        request.setNome("Gaby");
        request.setCpf("51982794801");
        request.setTelefone("14981499375");
        request.setEmail("teste1234@gmail.com");

        var response = new ClienteResponseDto();
        response.setId(1L);

        when(clienteService.save(any(ClienteRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    void givenId_WhenGetById_thenReturn200() throws Exception {
        var response = new ClienteResponseDto();
        response.setId(1L);

        when(clienteService.findById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/clientes/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void whenGetAll_thenReturn200() throws Exception {
        List<ClienteResponseDto> response = new ArrayList<>();

        when(clienteService.findAll()).thenReturn(response);

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidRequestAndId_WhenUpdate_thenReturn200() throws Exception {
        var request = new ClienteUpdateRequestDto();
        request.setNome("Gaby atualizado");

        var response = new ClienteResponseDto();
        response.setId(1L);
        response.setNome("Gaby atualizado");

        when(clienteService.update(anyLong(), any(ClienteUpdateRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/clientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Gaby atualizado"));
    }

    @Test
    void givenId_WhenDelete_thenReturn204() throws Exception {
        doNothing().when(clienteService).delete(anyLong());

        mockMvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenInvalidCpf_WhenCreate_thenReturn400() throws Exception {
        var request = new ClienteRequestDto();
        request.setNome("Teste");
        request.setCpf("999");
        request.setTelefone("14981499375");
        request.setEmail("teste@teste.com");

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidId_WhenGetById_thenReturn404() throws Exception {
        when(clienteService.findById(anyLong())).thenThrow(new EntityNotFoundException("Cliente não encontrado"));

        mockMvc.perform(get("/clientes/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenInvalidCpf_WhenUpdate_thenReturn400() throws Exception {
        var request = new ClienteUpdateRequestDto();
        request.setCpf("123");

        mockMvc.perform(put("/clientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void givenInvalidId_WhenDelete_thenReturn404() throws Exception {
        doThrow(new EntityNotFoundException("Cliente não encontrado")).when(clienteService).delete(anyLong());

        mockMvc.perform(delete("/clientes/{id}", 999L))
                .andExpect(status().isNotFound());
    }

}