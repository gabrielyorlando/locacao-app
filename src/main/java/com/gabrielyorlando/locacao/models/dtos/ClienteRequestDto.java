package com.gabrielyorlando.locacao.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class ClienteRequestDto {
    @NotBlank
    @Size(max = 150)
    private String nome;

    @NotBlank
    @Email(message = "Email inválido")
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String telefone;

    @NotBlank
    @CPF(message = "CPF inválido")
    private String cpf;
}
