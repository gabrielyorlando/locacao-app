package com.gabrielyorlando.locacao.services;

import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.ReservaMapper;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaRequestDto;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaResponseDto;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaUpdateRequestDto;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import com.gabrielyorlando.locacao.models.entities.Reserva;
import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import com.gabrielyorlando.locacao.repositories.ClienteRepository;
import com.gabrielyorlando.locacao.repositories.LocacaoRepository;
import com.gabrielyorlando.locacao.repositories.ReservaRepository;
import com.gabrielyorlando.locacao.validation.ReservaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    private final LocacaoRepository locacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ReservaValidator validator;

    public ReservaResponseDto save(ReservaRequestDto requestDto) {
        validator.validateDateRange(requestDto.getDataInicio(), requestDto.getDataFim());

        long minutes = calculateMinutes(requestDto.getDataInicio(), requestDto.getDataFim());
        Locacao locacao = validateNewReservation(requestDto, minutes);

        Reserva reserva = createReserva(requestDto, locacao, minutes);
        return reservaMapper.toResponseDTO(reserva);
    }

    private Reserva createReserva(ReservaRequestDto request, Locacao locacao, long minutes) {
        Reserva reserva = reservaMapper.toEntity(request);
        reserva.setSituacao(SituacaoReserva.CONFIRMADA);
        reserva.setValorFinal(calculateTotal(locacao, minutes));
        return reservaRepository.save(reserva);
    }

    public ReservaResponseDto update(Long id, ReservaUpdateRequestDto requestDto) {
        Reserva reservaExistente = getReserva(id);

        if (reservaExistente.getSituacao() == SituacaoReserva.CANCELADA) {
            throw new BusinessRuleException("Não é possível alterar uma reserva cancelada");
        }

        if (requestDto.getDataInicio() == null && requestDto.getDataFim() == null) {
            return reservaMapper.toResponseDTO(reservaExistente);
        }

        LocalDateTime novoInicio = requestDto.getDataInicio() != null ? requestDto.getDataInicio() : reservaExistente.getDataInicio();
        LocalDateTime novoFim = requestDto.getDataFim() != null ? requestDto.getDataFim() : reservaExistente.getDataFim();

        validator.validateDateRange(novoInicio, novoFim);

        long minutes = calculateMinutes(novoInicio, novoFim);
        Locacao locacao = validateReservationUpdate(reservaExistente, requestDto, minutes);

        reservaExistente.setDataInicio(novoInicio);
        reservaExistente.setDataFim(novoFim);
        reservaExistente.setValorFinal(calculateTotal(locacao, minutes));

        return reservaMapper.toResponseDTO(reservaRepository.save(reservaExistente));
    }

    @Transactional(readOnly = true)
    public ReservaResponseDto findById(Long id) {
        return reservaMapper.toResponseDTO(getReserva(id));
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDto> findAll() {
        return reservaRepository.findAll().stream().map(reservaMapper::toResponseDTO).toList();
    }

    @Transactional
    public ResponseEntity<?> delete(Long id) {
        Reserva reserva = getReserva(id);

        if (reserva.getSituacao() == SituacaoReserva.CONFIRMADA) {
            reservaRepository.updateSituacao(SituacaoReserva.CANCELADA, id);
            return ResponseEntity.ok("Reserva cancelada com sucesso");
        }

        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Reserva getReserva(Long id) {
        return reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));
    }

    private Locacao validateNewReservation(ReservaRequestDto request, long minutes) {
        Locacao locacao = getLocacao(request.getLocacaoId());
        validateClient(request.getClienteId());
        validator.validateDuration(minutes, locacao.getTempoMinimo(), locacao.getTempoMaximo());
        validator.validateAvailabilityForNew(locacao, request.getDataInicio(), request.getDataFim());
        return locacao;
    }

    private Locacao validateReservationUpdate(Reserva existing, ReservaUpdateRequestDto request, long minutes) {
        Locacao locacao = existing.getLocacao();
        validator.validateDuration(minutes, locacao.getTempoMinimo(), locacao.getTempoMaximo());
        validator.validateAvailabilityForUpdate(locacao, request.getDataInicio(), request.getDataFim(), existing.getId());
        return locacao;
    }

    private Locacao getLocacao(Long id) {
        return locacaoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Locação não encontrada"));
    }

    private void validateClient(Long id) {
        clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    private long calculateMinutes(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }

    // estou calculando em minutos para evitar perda em reservas parciais
    private BigDecimal calculateTotal(Locacao locacao, long minutes) {
        return locacao.getValorHora()
                .multiply(BigDecimal.valueOf(minutes))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }
}
