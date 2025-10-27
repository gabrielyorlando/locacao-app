package com.gabrielyorlando.locacao.models.dtos.locacao;

import com.gabrielyorlando.locacao.models.enums.TipoLocacao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LocacaoResponseDto {
	private Long id;
	private String nome;
	private TipoLocacao tipo;
	private String descricao;
	private BigDecimal valorHora;
	private Integer tempoMinimoHoras;
	private Integer tempoMaximoHoras;
	private LocalDateTime dataCriacao;
}
