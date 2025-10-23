package com.gabrielyorlando.locacao.models.entities;

import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(name = "data_inicio", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime dataInicio;
    @Column(name = "data_fim", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime dataFim;
    @Column(name = "valor_final", nullable = false)
    private BigDecimal valorFinal;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoReserva situacao;
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dataCriacao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locacao_id", nullable = false)
    private Locacao locacao;
}
