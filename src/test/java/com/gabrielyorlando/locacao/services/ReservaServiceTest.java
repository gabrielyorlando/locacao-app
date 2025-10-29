package com.gabrielyorlando.locacao.services;

import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.ReservaMapper;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaRequestDto;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaResponseDto;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaUpdateRequestDto;
import com.gabrielyorlando.locacao.models.entities.Cliente;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import com.gabrielyorlando.locacao.models.entities.Reserva;
import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import com.gabrielyorlando.locacao.repositories.ClienteRepository;
import com.gabrielyorlando.locacao.repositories.LocacaoRepository;
import com.gabrielyorlando.locacao.repositories.ReservaRepository;
import com.gabrielyorlando.locacao.validation.ReservaValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {
    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ReservaValidator validator;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void givenValidRequest_WhenSave_thenReturnReserva() {
        var request = new ReservaRequestDto();
        request.setClienteId(1L);
        request.setLocacaoId(1L);
        request.setDataInicio(LocalDateTime.now());
        request.setDataFim(LocalDateTime.now().plusHours(1));

        var locacao = new Locacao();
        locacao.setTempoMinimo(1);
        locacao.setTempoMaximo(3);
        locacao.setValorHora(BigDecimal.TEN);

        var cliente = new Cliente();
        cliente.setId(1L);

        var entity = new Reserva();
        entity.setId(1L);

        var response = new ReservaResponseDto();
        response.setId(1L);

        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(locacao));
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(reservaMapper.toEntity(any())).thenReturn(entity);
        when(reservaRepository.save(any())).thenReturn(entity);
        when(reservaMapper.toResponseDTO(any())).thenReturn(response);

        var result = reservaService.save(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void givenValidId_WhenFindById_thenReturnReserva() {
        var entity = new Reserva();
        entity.setId(1L);

        var response = new ReservaResponseDto();
        response.setId(1L);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(reservaMapper.toResponseDTO(any())).thenReturn(response);

        var result = reservaService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void whenFindAll_thenReturnList() {
        when(reservaRepository.findAll()).thenReturn(List.of(new Reserva()));
        when(reservaMapper.toResponseDTO(any())).thenReturn(new ReservaResponseDto());

        var result = reservaService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void givenValidRequestAndId_WhenUpdate_thenReturnReserva() {
        var locacao = new Locacao();
        locacao.setTempoMinimo(1);
        locacao.setTempoMaximo(5);
        locacao.setValorHora(BigDecimal.TEN);

        var entity = new Reserva();
        entity.setId(1L);
        entity.setSituacao(SituacaoReserva.CONFIRMADA);
        entity.setDataInicio(LocalDateTime.now());
        entity.setDataFim(LocalDateTime.now().plusHours(2));
        entity.setLocacao(locacao);

        var request = new ReservaUpdateRequestDto();
        request.setDataInicio(entity.getDataInicio().plusHours(1));
        request.setDataFim(entity.getDataFim().plusHours(1));

        var response = new ReservaResponseDto();
        response.setId(1L);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(reservaRepository.save(any())).thenReturn(entity);
        when(reservaMapper.toResponseDTO(any())).thenReturn(response);

        var result = reservaService.update(1L, request);

        assertEquals(1L, result.getId());
    }

    @Test
    void givenConfirmedReservation_WhenDelete_thenCancelAndReturn200() {
        var entity = new Reserva();
        entity.setId(1L);
        entity.setSituacao(SituacaoReserva.CONFIRMADA);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        doNothing().when(reservaRepository).updateSituacao(any(), anyLong());

        var result = reservaService.delete(1L);

        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void givenCanceledReservation_WhenDelete_thenDeleteAndReturn204() {
        var entity = new Reserva();
        entity.setId(1L);
        entity.setSituacao(SituacaoReserva.CANCELADA);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        doNothing().when(reservaRepository).deleteById(anyLong());

        var result = reservaService.delete(1L);

        assertEquals(204, result.getStatusCode().value());
    }

    @Test
    void givenUnknownId_WhenFindById_thenThrowNotFound() {
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservaService.findById(1L));
    }

    @Test
    void givenUnknownId_WhenUpdate_thenThrowNotFound() {
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservaService.update(1L, new ReservaUpdateRequestDto()));
    }

    @Test
    void givenUnknownId_WhenDelete_thenThrowNotFound() {
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reservaService.delete(1L));
    }

    @Test
    void givenCanceledReservation_WhenUpdate_thenThrowBusinessRuleException() {
        var entity = new Reserva();
        entity.setId(1L);
        entity.setSituacao(SituacaoReserva.CANCELADA);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        assertThrows(BusinessRuleException.class, () -> reservaService.update(1L, new ReservaUpdateRequestDto()));
    }
}
