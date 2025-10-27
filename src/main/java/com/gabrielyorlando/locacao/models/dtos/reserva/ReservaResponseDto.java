package com.gabrielyorlando.locacao.models.dtos.reserva;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaResponseDto {
	private Long id;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime dataInicio;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime dataFim;
	private BigDecimal valorFinal;
	private SituacaoReserva situacao;
	private LocalDateTime dataCriacao;
	private Long clienteId;
	private Long locacaoId;
}
