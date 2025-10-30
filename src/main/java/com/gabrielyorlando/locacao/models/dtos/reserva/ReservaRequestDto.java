package com.gabrielyorlando.locacao.models.dtos.reserva;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaRequestDto {
	@Schema(type = "string", pattern = "yyyy-MM-dd HH:mm", example = "2025-10-30 08:00")
	@NotNull(message="A data de início é obrigatória")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime dataInicio;

	@Schema(type = "string", pattern = "yyyy-MM-dd HH:mm", example = "2025-10-30 16:00")
	@NotNull(message="A data de fim é obrigatória")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime dataFim;

	@NotNull(message="O ID do cliente é obrigatório")
	private Long clienteId;

	@NotNull(message="O ID da locação é obrigatório")
	private Long locacaoId;
}
