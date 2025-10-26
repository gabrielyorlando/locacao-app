package com.gabrielyorlando.locacao.services;

import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.ReservaMapper;
import com.gabrielyorlando.locacao.models.dtos.ReservaRequestDto;
import com.gabrielyorlando.locacao.models.dtos.ReservaResponseDto;
import com.gabrielyorlando.locacao.models.dtos.ReservaUpdateRequestDto;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import com.gabrielyorlando.locacao.models.entities.Reserva;
import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import com.gabrielyorlando.locacao.repositories.ClienteRepository;
import com.gabrielyorlando.locacao.repositories.LocacaoRepository;
import com.gabrielyorlando.locacao.repositories.ReservaRepository;
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

    public ReservaResponseDto save(ReservaRequestDto requestDto) {
        validateDateRange(requestDto.getDataInicio(), requestDto.getDataFim());

        long minutes = calculateMinutes(requestDto.getDataInicio(), requestDto.getDataFim());
        Locacao locacao = validateNewReservation(requestDto, minutes);

        Reserva saved = saveReserva(requestDto, locacao, minutes);
        return reservaMapper.toResponseDTO(saved);
    }

    private Reserva saveReserva(ReservaRequestDto request, Locacao locacao, long minutes) {
        Reserva reserva = reservaMapper.toEntity(request);
        reserva.setSituacao(SituacaoReserva.CONFIRMADA);
        BigDecimal total = returnTotal(locacao, minutes);
        reserva.setValorFinal(total);
        return reservaRepository.save(reserva);
    }

    private BigDecimal returnTotal(Locacao locacao, long minutes) {
        // Cálculo proporcional: meu request recebe horas porque é mais prático mas faço o calculo por minuto para evitar perda de valor em reservas parciais

        return locacao.getValorHora()
                .multiply(BigDecimal.valueOf(minutes))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public ReservaResponseDto findById(Long id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));
        return reservaMapper.toResponseDTO(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDto> findAll() {
        return reservaRepository.findAll().stream().map(reservaMapper::toResponseDTO).toList();
    }

    private void validateLocacaoAvailable(Locacao locacao,
                                          LocalDateTime inicio,
                                          LocalDateTime fim,
                                          Long ignoreReservaId) {

        boolean existConflict = reservaRepository.existsConflictExcludingSelf(locacao.getId(), inicio, fim, SituacaoReserva.CONFIRMADA, ignoreReservaId);

        if (existConflict) {
            throw new BusinessRuleException("Já existe outra reserva neste período para essa locação");
        }
    }

    public ReservaResponseDto update(Long id, ReservaUpdateRequestDto requestDto) {
        Reserva existing = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));

        validateDateRange(requestDto.getDataInicio(), requestDto.getDataFim());

        long minutes = calculateMinutes(requestDto.getDataInicio(), requestDto.getDataFim());
        Locacao locacao = validateReservationUpdate(existing, requestDto, minutes);

        existing.setDataInicio(requestDto.getDataInicio());
        existing.setDataFim(requestDto.getDataFim());

        BigDecimal total = returnTotal(locacao, minutes);
        existing.setValorFinal(total);

        Reserva updated = reservaRepository.save(existing);
        return reservaMapper.toResponseDTO(updated);
    }

    @Transactional
    public ResponseEntity<?> delete(Long id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));

        if (reserva.getSituacao() == SituacaoReserva.CONFIRMADA) {
            reservaRepository.updateSituacao(SituacaoReserva.CANCELADA, id);
            return ResponseEntity.ok("Reserva cancelada com sucesso");
        }

        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Locacao findLocacao(Long id) {
        return locacaoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Locação não encontrada"));
    }

    private void validateClient(Long id) {
        clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    private void validateDuration(long minutes, int minHours, int maxHours) {
        long minMinutes = minHours * 60L;
        long maxMinutes = maxHours * 60L;

        if (minutes < minMinutes) {
            throw new BusinessRuleException(String.format("A duração mínima da reserva é de %d horas.", minHours));
        }

        if (minutes > maxMinutes) {
            throw new BusinessRuleException(String.format("A duração máxima da reserva é de %d horas.", maxHours));
        }
    }

    private void validateLocacaoAvailable(Locacao locacao, ReservaRequestDto requestDto) {
        boolean existConflict = reservaRepository.existsConflict(locacao.getId(), requestDto.getDataInicio(), requestDto.getDataFim(), SituacaoReserva.CONFIRMADA);

        if (existConflict) {
            throw new BusinessRuleException("Já existe uma reserva confirmada para este período");
        }
    }

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new BusinessRuleException("A data/hora final deve ser após a data inicial");
        }
    }

    private long calculateMinutes(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }

    private Locacao validateNewReservation(ReservaRequestDto request, long minutes) {
        Locacao locacao = findLocacao(request.getLocacaoId());
        validateClient(request.getClienteId());
        validateDuration(minutes, locacao.getTempoMinimo(), locacao.getTempoMaximo());
        validateLocacaoAvailable(locacao, request);
        return locacao;
    }

    private Locacao validateReservationUpdate(Reserva existing, ReservaUpdateRequestDto request, long minutes) {
        Locacao locacao = existing.getLocacao();
        validateDuration(minutes, locacao.getTempoMinimo(), locacao.getTempoMaximo());
        validateLocacaoAvailable(locacao, request.getDataInicio(), request.getDataFim(), existing.getId());
        return locacao;
    }
}
