package com.gabrielyorlando.locacao.validation;

import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import com.gabrielyorlando.locacao.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservaValidator {
    private final ReservaRepository reservaRepository;

    public void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new BusinessRuleException("A data/hora final deve ser após a data inicial");
        }
    }

    public void validateDuration(long minutes, int minHours, int maxHours) {
        long minMinutes = minHours * 60L;
        long maxMinutes = maxHours * 60L;

        if (minutes < minMinutes) {
            throw new BusinessRuleException(
                    String.format("A duração mínima da reserva é de %d horas.", minHours));
        }
        if (minutes > maxMinutes) {
            throw new BusinessRuleException(
                    String.format("A duração máxima da reserva é de %d horas.", maxHours));
        }
    }

    public void validateAvailabilityForNew(Locacao locacao, LocalDateTime inicio, LocalDateTime fim) {
        boolean exists = reservaRepository.existsConflict(locacao.getId(), inicio, fim, SituacaoReserva.CONFIRMADA);

        if (exists) {
            throw new BusinessRuleException("Já existe uma reserva confirmada para este período");
        }
    }

    public void validateAvailabilityForUpdate(Locacao locacao, LocalDateTime inicio, LocalDateTime fim, Long id) {
        boolean exists = reservaRepository.existsConflictExcludingSelf(locacao.getId(), inicio, fim, SituacaoReserva.CONFIRMADA, id);

        if (exists) {
            throw new BusinessRuleException("Já existe outra reserva neste período para essa locação");
        }
    }
}
