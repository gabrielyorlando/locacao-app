package com.gabrielyorlando.locacao.models.entities;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(nullable = false, length = 150)
    private String nome;
    @Column(nullable = false, length = 150)
    private String email;
    @Column(nullable = false, length = 20)
    private String telefone;
    @Column(nullable = false, length = 11)
    private String cpf;
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dataCriacao;

    public void setCpf(String cpf) {
        if (StringUtils.isNotBlank(cpf)) {
            this.cpf = cpf.replaceAll("\\D", "");
        } else {
            this.cpf = null;
        }
    }
}
