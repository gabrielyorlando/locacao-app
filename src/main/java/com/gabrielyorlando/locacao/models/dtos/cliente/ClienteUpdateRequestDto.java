package com.gabrielyorlando.locacao.models.dtos.cliente;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class ClienteUpdateRequestDto {
    @CPF
    private String cpf;
    private String nome;
    private String telefone;
    @Email
    private String email;
}
