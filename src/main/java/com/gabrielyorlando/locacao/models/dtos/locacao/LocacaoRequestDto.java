package com.gabrielyorlando.locacao.models.dtos.locacao;

import com.gabrielyorlando.locacao.models.enums.TipoLocacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocacaoRequestDto {
	@NotBlank()
	private String nome;

	@NotNull(message="Tipo de locação é obrigatório")
	private TipoLocacao tipo;

	private String descricao;

	@NotNull(message="O valor por hora é obrigatório")
	private BigDecimal valorHora;

	@NotNull(message="O tempo mínimo é obrigatório (em horas)")
	private Integer tempoMinimo;

	@NotNull(message="O tempo máximo é obrigatório (em horas)")
	private Integer tempoMaximo;
}
