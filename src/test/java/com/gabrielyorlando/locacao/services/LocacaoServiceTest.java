package com.gabrielyorlando.locacao.services;
import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.LocacaoMapper;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoRequestDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoResponseDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoUpdateRequestDto;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import com.gabrielyorlando.locacao.models.enums.TipoLocacao;
import com.gabrielyorlando.locacao.repositories.LocacaoRepository;
import com.gabrielyorlando.locacao.repositories.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class LocacaoServiceTest {
    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private LocacaoMapper locacaoMapper;

    @InjectMocks
    private LocacaoService locacaoService;

    @Test
    void givenValidRequest_WhenSave_thenReturnLocacao() {
        var request = new LocacaoRequestDto();
        request.setNome("Evento");
        request.setTipo(TipoLocacao.QUADRA_EVENTOS);
        request.setValorHora(BigDecimal.TEN);
        request.setTempoMinimoHoras(1);
        request.setTempoMaximoHoras(3);

        var entity = new Locacao();
        entity.setId(1L);

        var response = new LocacaoResponseDto();
        response.setId(1L);

        when(locacaoMapper.toEntity(any())).thenReturn(entity);
        when(locacaoRepository.save(any())).thenReturn(entity);
        when(locacaoMapper.toResponseDTO(any())).thenReturn(response);

        var result = locacaoService.save(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void givenValidId_WhenFindById_thenReturnLocacao() {
        var entity = new Locacao();
        entity.setId(1L);

        var response = new LocacaoResponseDto();
        response.setId(1L);

        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(locacaoMapper.toResponseDTO(any())).thenReturn(response);

        var result = locacaoService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void whenFindAll_thenReturnLocacaoList() {
        when(locacaoRepository.findAll()).thenReturn(List.of(new Locacao()));
        when(locacaoMapper.toResponseDTO(any())).thenReturn(new LocacaoResponseDto());

        var result = locacaoService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void givenValidDates_WhenFindAvailable_thenReturnPage() {
        Page<Locacao> pageMock = mock(Page.class);
        when(pageMock.map(any())).thenReturn(Page.empty());

        when(locacaoRepository.findAvailableByDateRangeAndSituacaoReservaConfirmada(any(), any(), any())).thenReturn(pageMock);
        var result = locacaoService.findAvailableBetweenDates(LocalDateTime.now(), LocalDateTime.now().plusHours(1), Pageable.unpaged());

        assertNotNull(result);
    }

    @Test
    void givenValidRequestAndId_WhenUpdate_thenReturnLocacao() {
        var entity = new Locacao();
        entity.setId(1L);

        var response = new LocacaoResponseDto();
        response.setId(1L);

        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(locacaoRepository.save(any())).thenReturn(entity);
        when(locacaoMapper.toResponseDTO(any())).thenReturn(response);

        var result = locacaoService.update(1L, new LocacaoUpdateRequestDto());

        assertEquals(1L, result.getId());
    }

    @Test
    void givenExistingId_WhenDelete_thenDoNothing() {
        when(locacaoRepository.existsById(anyLong())).thenReturn(true);
        when(reservaRepository.existsByLocacaoId(anyLong())).thenReturn(false);

        assertDoesNotThrow(() -> locacaoService.delete(1L));
        verify(locacaoRepository, times(1)).deleteById(1L);
    }

    // ---------- FALHAS ----------

    @Test
    void givenUnknownId_WhenFindById_thenThrowNotFound() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> locacaoService.findById(1L));
    }

    @Test
    void givenUnknownId_WhenUpdate_thenThrowNotFound() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> locacaoService.update(1L, new LocacaoUpdateRequestDto()));
    }

    @Test
    void givenUnknownId_WhenDelete_thenThrowNotFound() {
        when(locacaoRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> locacaoService.delete(1L));
    }

    @Test
    void givenExistingReservations_WhenDelete_thenThrowBusinessRuleException() {
        when(locacaoRepository.existsById(anyLong())).thenReturn(true);
        when(reservaRepository.existsByLocacaoId(anyLong())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> locacaoService.delete(1L));
    }
}
