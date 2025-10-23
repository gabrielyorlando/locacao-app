package com.gabrielyorlando.locacao.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteResponseDto {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDateTime dataCriacao;
}
