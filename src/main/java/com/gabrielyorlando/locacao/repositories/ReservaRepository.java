package com.gabrielyorlando.locacao.repositories;

import com.gabrielyorlando.locacao.models.entities.Reserva;
import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

	@Query("SELECT r FROM Reserva r WHERE r.locacao.id = :locacaoId " +
			"AND r.situacao = :situacao " + "AND (r.dataInicio < :dataFim AND r.dataFim > :dataInicio)")
	List<Reserva> findReservaConflicts(@Param("locacaoId") Long locacaoId, @Param("dataInicio") LocalDateTime dataInicio,
	                                   @Param("dataFim") LocalDateTime dataFim, @Param("situacao") SituacaoReserva situacao);
	//TROCAR PRA UM EXISTS
}
