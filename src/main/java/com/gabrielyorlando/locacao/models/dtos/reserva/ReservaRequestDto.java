package com.gabrielyorlando.locacao.models.dtos.reserva;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaRequestDto {
	@NotNull(message="A data de início é obrigatória")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime dataInicio;

	@NotNull(message="A data de fim é obrigatória")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime dataFim;

	@NotNull(message="O ID do cliente é obrigatório")
	private Long clienteId;

	@NotNull(message="O ID da locação é obrigatório")
	private Long locacaoId;
}
