package com.gabrielyorlando.locacao.repositories;

import com.gabrielyorlando.locacao.models.entities.Reserva;
import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.locacao.id = :locacaoId " +
            "AND r.situacao = :situacao " + "AND (r.dataInicio < :dataFim AND r.dataFim > :dataInicio)")
    boolean existsConflict(@Param("locacaoId") Long locacaoId, @Param("dataInicio") LocalDateTime dataInicio,
                           @Param("dataFim") LocalDateTime dataFim, @Param("situacao") SituacaoReserva situacao);

    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.locacao.id = :locacaoId " +
            "AND r.id <> :ignoreId " +
            "AND r.situacao = :situacao " + "AND (r.dataInicio < :dataFim AND r.dataFim > :dataInicio)")
    boolean existsConflictExcludingSelf(@Param("locacaoId") Long locacaoId,
                                        @Param("dataInicio") LocalDateTime dataInicio,
                                        @Param("dataFim") LocalDateTime dataFim,
                                        @Param("situacao") SituacaoReserva situacao,
                                        @Param("ignoreId") Long ignoreId);


    boolean existsByClienteId(Long clienteId);

    boolean existsByLocacaoId(Long locacaoId);

    @Modifying
    @Query("UPDATE Reserva r SET r.situacao = :situacao WHERE r.id = :id")
    void updateSituacao(@Param("situacao") SituacaoReserva situacao, @Param("id") Long id);
}
