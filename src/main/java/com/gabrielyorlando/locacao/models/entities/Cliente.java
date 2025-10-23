package com.gabrielyorlando.locacao.models.entities;

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
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, length = 20)
    private String email;
    @Column(nullable = false, length = 20)
    private String telefone;
    @Column(nullable = false, length = 11)
    private String cpf;
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dataCriacao;
}
