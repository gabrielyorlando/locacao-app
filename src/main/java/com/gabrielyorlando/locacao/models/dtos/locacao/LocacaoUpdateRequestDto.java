package com.gabrielyorlando.locacao.models.dtos.locacao;

import com.gabrielyorlando.locacao.models.enums.TipoLocacao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocacaoUpdateRequestDto {
    private String nome;
    private TipoLocacao tipo;
    private String descricao;
    private BigDecimal valorHora;
    private Integer tempoMinimoHoras;
    private Integer tempoMaximoHoras;
}
