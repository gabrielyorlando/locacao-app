package com.gabrielyorlando.locacao.services;

import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.ClienteMapper;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteRequestDto;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteResponseDto;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteUpdateRequestDto;
import com.gabrielyorlando.locacao.models.entities.Cliente;
import com.gabrielyorlando.locacao.repositories.ClienteRepository;
import com.gabrielyorlando.locacao.repositories.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void givenValidRequest_WhenSave_thenReturnCliente() {
        var request = new ClienteRequestDto();
        request.setNome("Gaby");
        request.setCpf("51982794801");

        var entity = new Cliente();
        entity.setId(1L);

        var response = new ClienteResponseDto();
        response.setId(1L);

        when(clienteMapper.toEntity(any())).thenReturn(entity);
        when(clienteRepository.save(any())).thenReturn(entity);
        when(clienteMapper.toResponseDTO(any())).thenReturn(response);

        var result = clienteService.save(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void givenValidId_WhenFindById_thenReturnCliente() {
        var entity = new Cliente();
        entity.setId(1L);

        var response = new ClienteResponseDto();
        response.setId(1L);

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(clienteMapper.toResponseDTO(any())).thenReturn(response);

        var result = clienteService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void whenFindAll_thenReturnClienteList() {
        when(clienteRepository.findAll()).thenReturn(List.of(new Cliente()));
        when(clienteMapper.toResponseDTO(any())).thenReturn(new ClienteResponseDto());

        var result = clienteService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void givenValidRequestAndId_WhenUpdate_thenReturnCliente() {
        var entity = new Cliente();
        entity.setId(1L);

        var response = new ClienteResponseDto();
        response.setId(1L);

        var request = new ClienteUpdateRequestDto();
        request.setNome("Atualizado");

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(clienteRepository.save(any())).thenReturn(entity);
        when(clienteMapper.toResponseDTO(any())).thenReturn(response);

        var result = clienteService.update(1L, request);

        assertEquals(1L, result.getId());
    }

    @Test
    void givenExistingId_WhenDelete_thenDoNothing() {
        when(clienteRepository.existsById(anyLong())).thenReturn(true);
        when(reservaRepository.existsByClienteId(anyLong())).thenReturn(false);

        assertDoesNotThrow(() -> clienteService.delete(1L));
        verify(clienteRepository, times(1)).deleteById(1L);
    }


    @Test
    void givenUnknownId_WhenFindById_thenThrowNotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteService.findById(1L));
    }

    @Test
    void givenUnknownId_WhenUpdate_thenThrowNotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteService.update(1L, new ClienteUpdateRequestDto()));
    }

    @Test
    void givenUnknownId_WhenDelete_thenThrowNotFound() {
        when(clienteRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> clienteService.delete(1L));
    }

    @Test
    void givenExistingReservations_WhenDelete_thenThrowBusinessRuleException() {
        when(clienteRepository.existsById(anyLong())).thenReturn(true);
        when(reservaRepository.existsByClienteId(anyLong())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> clienteService.delete(1L));
    }
}
