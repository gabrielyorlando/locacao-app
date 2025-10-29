package com.gabrielyorlando.locacao.models.dtos.reserva;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaUpdateRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dataFim;
}
