package com.gabrielyorlando.locacao.models.entities;

import com.gabrielyorlando.locacao.models.enums.TipoLocacao;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tipo_locacao")
public class Locacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(nullable = false)
    private String nome;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoLocacao tipo;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "valor_hora", nullable = false)
    private BigDecimal valorHora;
    @Column(name = "tempo_minimo", nullable = false)
    private Integer tempoMinimo;
    @Column(name = "tempo_maximo", nullable = false)
    private Integer tempoMaximo;
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dataCriacao;
}
